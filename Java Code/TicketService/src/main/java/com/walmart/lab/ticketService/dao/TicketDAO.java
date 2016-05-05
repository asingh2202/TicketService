package com.walmart.lab.ticketService.dao;

import java.util.List;
import java.util.Optional;

import com.walmart.lab.ticketService.booking.SeatHold;

/**
 * @author ANUP SINGH
 *
 */
public interface TicketDAO {
	
	
	/**
	 * @param venueLevel a numeric venue level identifier to limit the search
	 * @return the number of tickets available on the provided level
	 */
	public int getAvailableSeatCount(int venueLevel);

	/**
	 * @param seatHoldId a numeric booking id obtain as a result of seat hold
	 * @param CustomerEmailId representing customers email id used for seat hold
	 * @return a confirmation a string to be returned in case of successful reservation
	 */
	public String confirmBooking(long seatHoldId, String CustomerEmailId);

	/**
	 * @param numSeats a numeric value representing no of seats to be hold
	 * @param customerEmail represent the customer's email id for which seats are being hold
	 * @param ConfirmationCode a string to be returned in case of successful reservation
	 * @return SeatHold which contains all the details about seat hold
	 */
	public SeatHold checkAndHold(int numSeats, String customerEmail, String ConfirmationCode);

	/**
	 * @return a collection containing all the setting level available within the Venue.
	 */
	public List<Integer> getAvailableLevels();

	/**
	 * @return a numeric value representing count of available seats in the Venue.
	 */
	public int getAvailableSeatCount();

	/**
	 * @param emailId representing customers email id used for seat hold
	 * @param ConfirmationCD a string to be returned in case of successful reservation
	 * @return a positive numeric value representing a valid seat hold id generated after seat hold
	 */
	public long holdSeat(String emailId, String ConfirmationCD);

	/**
	 * @param seat_hold_id a positive numeric value representing a valid seat hold id generated after seat hold
	 * @param level_id represents the venue level on which hold is requested
	 * @param seatCount is the seat count requested in the seat hold operation.
	 */
	public void holdSeatBook(long seat_hold_id, int level_id, int seatCount);

	/**
	 * It is most important function called every time to invalidate or remove all the
	 * expired seat hold details from the database based on the timestamp. 
	 */
	public void deleteExpiredHold();

	/**
	 * @param numSeats is the seat count requested in the seat hold operation.
	 * @param minLevel is the minimum level on which seat hold is requested.
	 * @param maxLevel is the maximum level on which seat hold is requested.
	 * @param customerEmail representing customers email id used for seat hold
	 * @param confirmationCode a string to be returned in case of successful reservation
	 * @return SeatHold which contains all the details about seat hold
	 */
	public SeatHold checkAndHold(int numSeats, int minLevel, int maxLevel, String customerEmail,
			String confirmationCode);

	/**
	 * @param seatHoldId a positive numeric value representing a valid seat hold id generated after seat hold
	 * @param emailId customerEmail representing customers email id used for seat hold
	 * @return boolean confirming whether a valid seat hold id and email Id used for booking
	 */
	public boolean checkValidHold(long seatHoldId, String emailId);

	/**
	 * @param minLevel is the minimum level on which seat hold is requested.
	 * @param maxLevel is the maximum level on which seat hold is requested.
	 * @return a numeric value representing count of available seats in the all the levels between min and max levels .
	 */
	public int getAvailableSeatCount(int minLevel, int maxLevel);

	/**
	 * It is used to remove to all the reservations from the database to re-initialize it.
	 */
	public void revokeReservation();
}
