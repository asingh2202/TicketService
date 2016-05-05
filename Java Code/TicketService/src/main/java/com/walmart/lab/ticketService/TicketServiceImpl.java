package com.walmart.lab.ticketService;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.transaction.annotation.Transactional;

import com.walmart.lab.ticketService.booking.SeatHold;
import com.walmart.lab.ticketService.constants.TicketServiceConstants;
import com.walmart.lab.ticketService.dao.TicketDAO;
import com.walmart.lab.ticketService.exception.WalmartException;

/**
 * @author ANUP SINGH
 *
 */
public class TicketServiceImpl implements TicketService, TicketServiceConstants {

	private TicketDAO ticketDAO;

	public TicketDAO getTicketDAO() {
		return ticketDAO;
	}

	public void setTicketDAO(TicketDAO ticketDAO) {
		this.ticketDAO = ticketDAO;
	}

	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {

		int seatCount = 0;
		if (!venueLevel.isPresent()) {
			seatCount = ticketDAO.getAvailableSeatCount();
		} else {
			if (ticketDAO.getAvailableLevels().contains(venueLevel.get().intValue())) {
				seatCount = ticketDAO.getAvailableSeatCount(venueLevel.get().intValue());
			} else {
				System.out.println("Invalid Venue Level.Please enter a valid Venue Level.");
			}
		}
		return seatCount;
	}

	@Override
	@Transactional
	public synchronized SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		SeatHold seatHold = null;
		if (numSeats > 0) {
			// No levels specified
			if (!minLevel.isPresent() && !maxLevel.isPresent()) {
				// Check if seats available
				int availableSeatCount = ticketDAO.getAvailableSeatCount();
				if (availableSeatCount >= numSeats) {
					seatHold = ticketDAO.checkAndHold(numSeats, customerEmail,
							CONFIRMATION_CODE_FIXED_STRING + stringRandomizer(RANDOM_STRING_LENGTH));
				} else {
					System.out.println("Required number of seats are not Available.Current Seat Availabilty is ::"
							+ availableSeatCount);
					seatHold = new SeatHold();
					seatHold.setSeatHoldCount(0);
					seatHold.setSeatHoldId(-1);
					seatHold.setSeats(null);
				}
			}
			// Only MaxLevel is Specified
			else if (!minLevel.isPresent() && maxLevel.isPresent()) {
				List<Integer> levelLst = ticketDAO.getAvailableLevels();
				if (levelLst.contains(maxLevel.get())) {

					int avlSeats = ticketDAO.getAvailableSeatCount(levelLst.get(0).intValue(),
							maxLevel.get().intValue());
					if (avlSeats >= numSeats) {
						seatHold = ticketDAO.checkAndHold(numSeats, levelLst.get(0).intValue(),
								maxLevel.get().intValue(), customerEmail,
								CONFIRMATION_CODE_FIXED_STRING + stringRandomizer(RANDOM_STRING_LENGTH));
					} else {
						System.out.println(
								"Entered number of seats are not available in the mentioned Level range.Current Availablity within the range is "
										+ avlSeats);
						seatHold = new SeatHold();
						seatHold.setSeatHoldCount(0);
						seatHold.setSeatHoldId(-1);
						seatHold.setSeats(null);
					}
				} else {
					System.out.println("Entered max Level is not Available.Current max level Available is ::"
							+ levelLst.get(levelLst.size() - 1).intValue());
					seatHold = new SeatHold();
					seatHold.setSeatHoldCount(0);
					seatHold.setSeatHoldId(-1);
					seatHold.setSeats(null);
				}
			}
			// Only minLevel is Specified
			else if (minLevel.isPresent() && !maxLevel.isPresent()) {
				List<Integer> levelLst = ticketDAO.getAvailableLevels();
				if (levelLst.contains(minLevel.get())) {

					int avlSeats = ticketDAO.getAvailableSeatCount(minLevel.get().intValue(),
							levelLst.get(levelLst.size() - 1).intValue());
					if (avlSeats >= numSeats) {
						seatHold = ticketDAO.checkAndHold(numSeats, minLevel.get().intValue(),
								levelLst.get(levelLst.size() - 1).intValue(), customerEmail,
								CONFIRMATION_CODE_FIXED_STRING + stringRandomizer(RANDOM_STRING_LENGTH));
					} else {
						System.out.println(
								"Entered number of seats are not available in the mentioned Level range.Current Availablity within the range is "
										+ avlSeats);
						seatHold = new SeatHold();
						seatHold.setSeatHoldCount(0);
						seatHold.setSeatHoldId(-1);
						seatHold.setSeats(null);
					}
				} else {
					System.out.println("Entered min Level is not Available.Current min level Available is ::"
							+ levelLst.get(0).intValue());
					seatHold = new SeatHold();
					seatHold.setSeatHoldCount(0);
					seatHold.setSeatHoldId(-1);
					seatHold.setSeats(null);
				}
			}
			// Both levels are specified
			else {
				List<Integer> levelLst = ticketDAO.getAvailableLevels();
				if (levelLst.contains(minLevel.get()) && levelLst.contains(maxLevel.get())) {

					int avlSeats = ticketDAO.getAvailableSeatCount(minLevel.get().intValue(),
							maxLevel.get().intValue());
					if (avlSeats >= numSeats) {
						seatHold = ticketDAO.checkAndHold(numSeats, minLevel.get().intValue(),
								maxLevel.get().intValue(), customerEmail,
								CONFIRMATION_CODE_FIXED_STRING + stringRandomizer(RANDOM_STRING_LENGTH));
					} else {
						System.out.println(
								"Entered number of seats are not available in the mentioned Level range.Current Availablity within the range is "
										+ avlSeats);
						seatHold = new SeatHold();
						seatHold.setSeatHoldCount(0);
						seatHold.setSeatHoldId(-1);
						seatHold.setSeats(null);
					}

				} else {
					System.out.println(
							"Entered min/max Level range is not Available.Current min/max level Available is ::"
									+ levelLst.get(0).intValue() + " to "
									+ levelLst.get(levelLst.size() - 1).intValue());
					seatHold = new SeatHold();
					seatHold.setSeatHoldCount(0);
					seatHold.setSeatHoldId(-1);
					seatHold.setSeats(null);
				}
			}
		} else {

			System.out.println("Please enter more than zero for the seat booking.");
			seatHold = new SeatHold();
			seatHold.setSeatHoldCount(0);
			seatHold.setSeatHoldId(-1);
			seatHold.setSeats(null);
		}
		return seatHold;
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		String ConfirmationCode = "NOT_AVAILALBE";

		// check Available Seat Count for deleting the expired SeatHold Id
		ticketDAO.getAvailableSeatCount();
		if (ticketDAO.checkValidHold(seatHoldId, customerEmail)) {
			ConfirmationCode = ticketDAO.confirmBooking(seatHoldId, customerEmail);
		} else {
			System.out.println(
					"Entered Seat Hold ID or Customer Email ID is not matching or Reservation is already Commited.Please check and try again");
			System.out.println("seatHoldId" + seatHoldId);
			System.out.println("customerEmail" + customerEmail);
		}
		return ConfirmationCode;
	}

	private String stringRandomizer(int length) {

		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomLimitedInt = 'a' + (int) (new Random().nextFloat() * ('z' - 'a'));
			buffer.append((char) randomLimitedInt);
		}

		return buffer.toString();

	}

}
