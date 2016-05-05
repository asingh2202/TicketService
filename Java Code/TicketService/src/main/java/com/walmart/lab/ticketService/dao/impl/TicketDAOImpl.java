package com.walmart.lab.ticketService.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.walmart.lab.ticketService.booking.Seat;
import com.walmart.lab.ticketService.booking.SeatHold;
import com.walmart.lab.ticketService.booking.Venue;
import com.walmart.lab.ticketService.constants.TicketServiceConstants;
import com.walmart.lab.ticketService.dao.TicketDAO;

/**
 * @author ANUP SINGH
 *
 */
public class TicketDAOImpl implements TicketDAO, TicketServiceConstants {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	TicketDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Override
	public int getAvailableSeatCount(int venueLevel) {
		int availableSeats = 0;
		Integer totalCapacity = 0;
		Integer ReservedSeats = 0;

		// Removing the expired seat_holds
		deleteExpiredHold();

		totalCapacity = jdbcTemplate.queryForObject("select NUMBER_OF_ROW*SEATS_PER_ROW from Venue where level_id=?",
				new Object[] { venueLevel }, Integer.class);
		// System.out.println("totalCapacity("+venueLevel+")::"+totalCapacity.intValue());
		ReservedSeats = jdbcTemplate.queryForObject(
				"select nvl(sum(book.number_of_seats),0) from BOOKING book,SEAT_HOLD hold where hold.seat_hold_id=book.seat_hold_id and book.level_id=?",
				new Object[] { venueLevel }, Integer.class);
		// System.out.println("ReservedSeats("+venueLevel+")::"+ReservedSeats.intValue());
		// System.out.println("Available("+venueLevel+")::"+(totalCapacity.intValue()-ReservedSeats.intValue()));
		availableSeats = totalCapacity.intValue() - ReservedSeats.intValue();
		return availableSeats;
	}

	@Override
	public String confirmBooking(long seatHoldId, String CustomerEmailId) {

		jdbcTemplate.update(
				"update SEAT_HOLD set RESERVATION_TIME=current_timestamp where SEAT_HOLD_ID=? and EMAIL_ID=?",
				new Object[] { seatHoldId, CustomerEmailId });
		String confirmationCD = jdbcTemplate.queryForObject(
				"select RESERVATION_CONFIRMATION_CODE from SEAT_HOLD where SEAT_HOLD_ID=? and EMAIL_ID=?",
				new Object[] { seatHoldId, CustomerEmailId }, String.class);
		return confirmationCD;
	}

	@Override
	public SeatHold checkAndHold(int numSeats, String customerEmail, String ConfirmationCode) {

		SeatHold seatHold = null;
		long seatHoldId = holdSeat(customerEmail, ConfirmationCode);
		// System.out.println("Seat Hold ID::"+seatHoldId);

		int seatCountToBook = numSeats;
		List<Integer> lst = getAvailableLevels();
		for (int venueLevel : lst) {
			int availableSeat = getAvailableSeatCount(venueLevel);
			if (availableSeat >= seatCountToBook) {
				holdSeatBook(seatHoldId, venueLevel, seatCountToBook);
				break;
			} else if (availableSeat > 0) {
				holdSeatBook(seatHoldId, venueLevel, availableSeat);
				seatCountToBook = seatCountToBook - availableSeat;
			}
		}

		List<Seat> rows = jdbcTemplate.query("select * from seat where SEAT_HOLD_ID=?", new Object[] { seatHoldId },
				new SeatRowMapper());
		seatHold = new SeatHold();
		seatHold.setSeatHoldId(seatHoldId);
		seatHold.setSeats(rows);
		return seatHold;
	}

	@Override
	public List<Integer> getAvailableLevels() {
		List<Integer> venueLevels = jdbcTemplate.queryForList("select LEVEL_ID from Venue order by LEVEL_ID asc",
				Integer.class);
		return venueLevels;
	}

	@Override
	public int getAvailableSeatCount() {
		int availableSeats = 0;
		Integer totalCapacity = 0;
		Integer ReservedSeats = 0;

		// Removing the expired seat_holds
		deleteExpiredHold();

		// checking the availabilty
		totalCapacity = jdbcTemplate.queryForObject("select sum(NUMBER_OF_ROW*SEATS_PER_ROW) from Venue",
				Integer.class);
		// System.out.println("totalCapacity::"+totalCapacity.intValue());
		ReservedSeats = jdbcTemplate.queryForObject(
				"select nvl(sum(number_of_seats),0) from BOOKING book where seat_hold_id in(select seat_hold_id from seat_hold)",
				Integer.class);
		// System.out.println("TotalReservedSeats::"+ReservedSeats.intValue());
		// System.out.println("TotalAvailable::"+(totalCapacity.intValue()-ReservedSeats.intValue()));
		availableSeats = totalCapacity.intValue() - ReservedSeats.intValue();

		return availableSeats;
	}

	@Override
	public long holdSeat(String customerEmail, String ConfirmationCD) {
		long seatHoldId = jdbcTemplate.queryForObject("select nvl(max(seat_hold_id),0) from seat_hold", Long.class);
		String sql = "INSERT INTO SEAT_HOLD(SEAT_HOLD_ID,EMAIL_ID,HOLD_TIME,RESERVATION_CONFIRMATION_CODE) VALUES (?, ?,CURRENT_TIMESTAMP,?)";
		jdbcTemplate.update(sql, new Object[] { seatHoldId + 1, customerEmail, ConfirmationCD });
		return seatHoldId + 1;
	}

	@Override
	public void holdSeatBook(long seat_hold_id, int level_id, int seatCount) {
		String sql = "INSERT INTO BOOKING(SEAT_HOLD_ID,LEVEL_ID,NUMBER_OF_SEATS) VALUES (?,?,?)";
		jdbcTemplate.update(sql, new Object[] { seat_hold_id, level_id, seatCount });

		String getAvlseatSql = "select seat_no from seat where level_id=? and seat_hold_id is null order by seat_no asc";
		List<Integer> venueLevels = jdbcTemplate.queryForList(getAvlseatSql, new Object[] { level_id }, Integer.class);

		StringBuilder str = new StringBuilder("");
		String prefix = "";
		for (int i = 0; i < seatCount; i++) {
			str.append(prefix);
			prefix = ",";
			str.append(venueLevels.get(i).intValue());
		}

		String seatSql = "update seat set seat_hold_id=? where level_id=? and seat_no in (" + str + ")";
		// System.out.println("str::"+seatSql);
		jdbcTemplate.update(seatSql, new Object[] { seat_hold_id, level_id });

	}

	@Override
	public void deleteExpiredHold() {

		String getExpiredSeatHoldIdSql = "select SEAT_HOLD_ID from seat_hold where TIMESTAMPDIFF ( SQL_TSI_SECOND,hold_time,current_timestamp)>? and RESERVATION_TIME is null";
		List<Integer> seatHoldId = jdbcTemplate.queryForList(getExpiredSeatHoldIdSql,
				new Object[] { SEAT_HOLD_TIME_IN_SECONDS }, Integer.class);

		if (!seatHoldId.isEmpty()) {
			StringBuilder ids = new StringBuilder("");
			String prefix = "";
			for (int id : seatHoldId) {
				ids.append(prefix);
				prefix = ",";
				ids.append(id);
			}

			// System.out.println("IDs::"+ids);

			jdbcTemplate.update("update seat set seat_hold_id =null where seat_hold_id in (" + ids + ")");
			jdbcTemplate.update("delete from booking where seat_hold_id in(" + ids + ")");
			jdbcTemplate.update("delete from seat_hold where seat_hold_id in (" + ids + ")");
		}

	}

	@Override
	public SeatHold checkAndHold(int numSeats, int minLevel, int maxLevel, String customerEmail,
			String confirmationCode) {

		SeatHold seatHold = null;

		// Create a seathold id for the user
		long seatHoldId = holdSeat(customerEmail, confirmationCode);
		// System.out.println("Seat Hold ID::"+seatHoldId);

		// Hold the seats in the venue levels
		int seatCountToBook = numSeats;
		for (int venueLevel = minLevel; venueLevel <= maxLevel; venueLevel++) {
			int availableSeat = getAvailableSeatCount(venueLevel);
			if (availableSeat >= seatCountToBook) {
				holdSeatBook(seatHoldId, venueLevel, seatCountToBook);
				break;
			} else if (availableSeat != 0) {
				holdSeatBook(seatHoldId, venueLevel, availableSeat);
				seatCountToBook = seatCountToBook - availableSeat;
			}
		}

		List<Seat> rows = jdbcTemplate.query("select * from seat where SEAT_HOLD_ID=?", new Object[] { seatHoldId },
				new SeatRowMapper());
		seatHold = new SeatHold();
		seatHold.setSeatHoldId(seatHoldId);
		seatHold.setSeats(rows);
		seatHold.setSeatHoldCount(rows.size());
		return seatHold;
	}

	@Override
	public boolean checkValidHold(long seatHoldId, String emailId) {
		int count = jdbcTemplate.queryForObject(
				"select nvl(count(seat_hold_id),0) from seat_hold where SEAT_HOLD_ID=? and EMAIL_ID=? and RESERVATION_TIME is null",
				new Object[] { seatHoldId, emailId }, Integer.class);
		return count == 0 ? false : true;
	}

	@Override
	public int getAvailableSeatCount(int minLevel, int maxLevel) {

		// Removing the expired seat_holds
		deleteExpiredHold();

		int totalSeatAvailable = 0;
		for (int i = minLevel; i <= maxLevel; i++) {
			totalSeatAvailable = totalSeatAvailable + getAvailableSeatCount(i);
		}
		return totalSeatAvailable;
	}

	public void setupVenue() {

		int TotalTables = jdbcTemplate.queryForObject(
				"SELECT count(TABLE_NAME) FROM INFORMATION_SCHEMA.TABLES  where table_schema='PUBLIC'", Integer.class);
		// System.out.println("TotalTables::"+TotalTables);
		if (TotalTables == 0) {

			System.out.println("Plese wait....Setting up the new database instance...");
			// Create Venue Table
			jdbcTemplate.update(
					"CREATE TABLE VENUE(LEVEL_ID INTEGER NOT NULL,LEVEL_NAME VARCHAR(100) NOT NULL,PRICE DECIMAL NOT NULL,NUMBER_OF_ROW INT,SEATS_PER_ROW INT,PRIMARY KEY (LEVEL_ID))");
			// Insert Venue data
			jdbcTemplate.batchUpdate(new String[] {
					"insert into Venue(LEVEL_ID,LEVEL_NAME,PRICE,NUMBER_OF_ROW,SEATS_PER_ROW) values(1,'Orchestra',100.00,25,50)",
					"insert into Venue(LEVEL_ID,LEVEL_NAME,PRICE,NUMBER_OF_ROW,SEATS_PER_ROW) values(2,'Main',75.00,20,100)",
					"insert into Venue(LEVEL_ID,LEVEL_NAME,PRICE,NUMBER_OF_ROW,SEATS_PER_ROW) values(3,'Balcony 1',50.00,15,100)",
					"insert into Venue(LEVEL_ID,LEVEL_NAME,PRICE,NUMBER_OF_ROW,SEATS_PER_ROW) values(4,'Balcony 2',40.00,15,100)" });
			// Create Seat Table
			jdbcTemplate.update(
					"CREATE TABLE SEAT(SEAT_NO INTEGER NOT NULL,LEVEL_ID INTEGER NOT NULL,SEAT_HOLD_ID INTEGER)");
			// Create Seat_Hold Table
			jdbcTemplate.update(
					"CREATE TABLE SEAT_HOLD (SEAT_HOLD_ID BIGINT  NOT NULL,EMAIL_ID VARCHAR(150) NOT NULL,HOLD_TIME TIMESTAMP DEFAULT NULL,RESERVATION_CONFIRMATION_CODE VARCHAR(300),RESERVATION_TIME TIMESTAMP DEFAULT NULL,PRIMARY KEY(SEAT_HOLD_ID))");
			// Create Booking Table
			jdbcTemplate.update(
					"CREATE TABLE BOOKING(BOOKING_ID BIGINT NOT NULL IDENTITY,SEAT_HOLD_ID BIGINT NOT NULL,LEVEL_ID INT NOT NULL,NUMBER_OF_SEATS INT NOT NULL,CONSTRAINT SEAT_HOLD_ORDER_FK FOREIGN KEY (SEAT_HOLD_ID)REFERENCES SEAT_HOLD(SEAT_HOLD_ID),CONSTRAINT VENUE_ORDER_FK FOREIGN KEY (LEVEL_ID)REFERENCES VENUE(LEVEL_ID))");

		}

		int setup = jdbcTemplate.queryForObject("Select nvl(Count(*),0) from seat", Integer.class);
		// For very first time to setup the seats in case not present.
		if (setup == 0) {
			System.out.println("Inserting the initial data set into table...");
			List<Venue> venue = jdbcTemplate.query("select * from Venue", new VenueRowMapper());
			for (Venue v : venue) {
				int seatNo = 1;
				for (int i = 1; i <= v.getNUMBER_OF_ROW(); i++) {
					for (int j = 1; j <= v.getSEATS_PER_ROW(); j++) {
						jdbcTemplate.update("insert into SEAT(SEAT_NO,LEVEL_ID) values(?,?)",
								new Object[] { seatNo++, v.getLEVEL_ID() });
					}
				}
			}
			System.out.println("Database initialization completed.Thank you for your patience.");
		} // if ends

	}

	public void revokeReservation() {

		// Free Seat Table
		jdbcTemplate.update("update seat set seat_hold_id=null");
		// Truncate Booking Table
		jdbcTemplate.update("truncate table booking");
		// Truncate Seat_Hold Table
		jdbcTemplate.update("truncate table seat_hold");
	}

}

class SeatRowMapper implements RowMapper {
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Seat seat = new Seat();
		seat.setSeatHoldId(rs.getLong("SEAT_HOLD_ID"));
		seat.setLevelId(rs.getInt("LEVEL_ID"));
		seat.setSeatnumber(rs.getInt("SEAT_NO"));
		return seat;
	}

}

class VenueRowMapper implements RowMapper {
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Venue venue = new Venue();
		venue.setLEVEL_ID(rs.getInt("lEVEL_ID"));
		venue.setLEVEL_NAME(rs.getString("lEVEL_NAME"));
		venue.setNUMBER_OF_ROW(rs.getInt("NUMBER_OF_ROW"));
		venue.setPRICE(rs.getFloat("PRICE"));
		venue.setSEATS_PER_ROW(rs.getInt("SEATS_PER_ROW"));
		return venue;
	}

}
