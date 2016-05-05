package com.walmart.lab.ticketService;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walmart.lab.ticketService.booking.Seat;
import com.walmart.lab.ticketService.booking.SeatHold;
import com.walmart.lab.ticketService.constants.TicketServiceConstants;
import com.walmart.lab.ticketService.exception.WalmartException;

/**
 * This service implements an application that provide 
 * functionality related to Ticket booking system.It is designed
 * to receive the inputs with the help of Command Line.
 * 
 * @author ANUP SINGH
 *
 */
public class Service implements TicketServiceConstants {

	private static Service service;
	private TicketService ticketService;

	public TicketService getTicketService() {
		return ticketService;
	}

	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	public static void getInstance() {
		ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		service = (Service) factory.getBean(SERVICE);
	}

	public static void main(String arguments[]) {

		String method = null;
		String parameter = null;
		String emailId = null;
		int venueLevel = -1;
		int seatCount = -1;
		int minVenueLevel = -1;
		int maxVenueLevel = -1;
		int seatHoldId = -1;

		/*
		 * arguments=new String[5]; arguments[0]="holdSeat";
		 * arguments[1]="-count"; arguments[2]="1500";
		 * //arguments[3]="-minLevel"; //arguments[4]="1";
		 * //arguments[5]="-maxLevel"; //arguments[6]="4";
		 * arguments[3]="-emailId"; arguments[4]="anup.singh@marsh.com";
		 */

		// arguments=new String[1];
		// arguments[0]="availableSeatCount";
		/*
		 * arguments[1]="-level"; arguments[2]="1";
		 */

		/*
		 * arguments=new String[5]; arguments[0]="bookSeat";
		 * arguments[1]="-bookingId"; arguments[2]="3"; arguments[3]="-emailId";
		 * arguments[4]="anup.singh@marsh.com";
		 */

		System.out.println("Work in Progress... ");

		try {
			// Reading the command line arguments
			for (String value : arguments) {
				switch (value.replace("-", "")) {
				case CHECK_AVAILABLE_SEAT_COUNT:
					method = CHECK_AVAILABLE_SEAT_COUNT;
					break;
				case FIND_AND_HOLD_SEAT:
					method = FIND_AND_HOLD_SEAT;
					break;
				case RESERVE_SEATS:
					method = RESERVE_SEATS;
					break;
				case VENUE_LEVEL:
					parameter = VENUE_LEVEL;
					break;
				case NUMBER_OF_SEATS:
					parameter = NUMBER_OF_SEATS;
					break;
				case MIN_VENUE_LEVEL:
					parameter = MIN_VENUE_LEVEL;
					break;
				case MAX_VENUE_LEVEL:
					parameter = MAX_VENUE_LEVEL;
					break;
				case CUSTOMER_EMAIL_ADDRESS:
					parameter = CUSTOMER_EMAIL_ADDRESS;
					break;
				case SEAT_HOLD_ID:
					parameter = SEAT_HOLD_ID;
					break;
				default:
					if (parameter != null) {
						switch (parameter) {
						case VENUE_LEVEL:
							try {
								venueLevel = Integer.parseInt(value);
								break;
							} catch (NumberFormatException exception) {
								throw new WalmartException("Invalid Venue Level.Please enter valid number.");
							}
						case NUMBER_OF_SEATS:
							try {
								seatCount = Integer.parseInt(value);
								break;
							} catch (NumberFormatException exception) {
								throw new WalmartException("Invalid Seat Count.Please enter valid number.");
							}
						case MIN_VENUE_LEVEL:
							try {
								minVenueLevel = Integer.parseInt(value);
								break;
							} catch (NumberFormatException exception) {
								throw new WalmartException("Invalid Minimum venue level.Please enter valid number.");
							}
						case MAX_VENUE_LEVEL:
							try {
								maxVenueLevel = Integer.parseInt(value);
								break;
							} catch (NumberFormatException exception) {
								throw new WalmartException("Invalid Maximum venue level.Please enter valid number.");
							}
						case CUSTOMER_EMAIL_ADDRESS:
							emailId = value;
							Pattern pattern = Pattern.compile(EMAIL_PATTERN);
							Matcher matcher = pattern.matcher(value);
							if (!matcher.matches()) {
								throw new WalmartException(
										"Invalid Customer Email Address.Please enter valid email address.");
							}
							break;
						case SEAT_HOLD_ID:
							try {
								seatHoldId = Integer.parseInt(value);
								break;
							} catch (NumberFormatException exception) {
								throw new WalmartException(
										"Invalid Seat Hold ID.Please enter valid seat hold ID obtained during find & hold seat.");
							}
						default:
							throw new WalmartException("Invalid Function/Parameter passed.");
						}
					} // End Inner Switch
				}// End Outer Switch
			} // End For Loop

			// Check mandatory parameters
			checkMandatoryParameters(method, venueLevel, seatCount, minVenueLevel, maxVenueLevel, seatHoldId, emailId);

			// Calling the service methods for further processing
			if (service == null) {
				Service.getInstance();
			}

			service.processRequest(method, venueLevel, seatCount, minVenueLevel, maxVenueLevel, seatHoldId, emailId);

		} // Try block End
		catch (WalmartException exception) {
			System.out.println("Exception Occured in Ticket Service.Description --> " + exception.getMessage());
		} catch (Exception exception) {
			System.out.println("Exception Occured in Ticket Service.Description --> " + exception.getMessage());
		}

		System.out.println("Ticket Service Ends...");

	}

	private void processRequest(String method, int venueLevel, int seatCount, int minVenueLevel, int maxVenueLevel,
			int seatHoldId, String emailId) {
		switch (method) {

		case CHECK_AVAILABLE_SEAT_COUNT:
			System.out.println("**********Seat Availabilty Details************");
			int Count = ticketService
					.numSeatsAvailable(venueLevel == -1 ? Optional.ofNullable(null) : Optional.ofNullable(venueLevel));
			System.out.println("Available Seat Count is " + Count);
			System.out.println("**********************************************");
			break;

		case FIND_AND_HOLD_SEAT:
			System.out.println("*****************SeatHold Details*****************");
			SeatHold seatHold = ticketService.findAndHoldSeats(seatCount,
					minVenueLevel == -1 ? Optional.ofNullable(null) : Optional.ofNullable(minVenueLevel),
					maxVenueLevel == -1 ? Optional.ofNullable(null) : Optional.ofNullable(maxVenueLevel), emailId);
			if (seatHold != null) {
				System.out.println("SeatHoldID::" + seatHold.getSeatHoldId());
				for (Seat seat : seatHold.getSeats()) {
					System.out.println("Venue Level::" + seat.getLevelId() + "     Seats No::" + seat.getSeatnumber());
				}
			}
			System.out.println("**************************************************");
			break;

		case RESERVE_SEATS:
			System.out.println("**********Reservation Details********************");
			String confirmationCode = ticketService.reserveSeats(seatHoldId, emailId);
			System.out.println("Reservation Confirmation Code::" + confirmationCode);
			System.out.println("*************************************************");
			break;
		}
	}

	private static void checkMandatoryParameters(String method, int venueLevel, int seatCount, int minVenueLevel,
			int maxVenueLevel, int seatHoldId, String emailId) throws WalmartException {

		if (method != null) {

			if (method.equals(CHECK_AVAILABLE_SEAT_COUNT)) {
				// No Mandatory Check Required
			} else if (method.equals(FIND_AND_HOLD_SEAT)) {
				if (seatCount == -1 || emailId == null) {
					throw new WalmartException("Mandatory parameter '" + NUMBER_OF_SEATS + "' or '"
							+ CUSTOMER_EMAIL_ADDRESS + "'is missing for method " + FIND_AND_HOLD_SEAT
							+ ". Please specify the parameter(s) and try again.");
				}
			} else if (method.equals(RESERVE_SEATS)) {
				if (seatHoldId == -1 || emailId == null) {
					throw new WalmartException("Mandatory parameter(s) " + SEAT_HOLD_ID + " or "
							+ CUSTOMER_EMAIL_ADDRESS + " is missing for method " + RESERVE_SEATS
							+ ". Please specify the parameter(s) and try again.");
				}
			}

		} else {
			throw new WalmartException(
					"Function name missing.Please specify a valid function name and required parameter");
		}
	}

}
