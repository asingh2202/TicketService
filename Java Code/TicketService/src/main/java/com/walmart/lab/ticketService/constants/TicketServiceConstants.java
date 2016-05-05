package com.walmart.lab.ticketService.constants;

/**
 * This contains all the common constants used across all
 * the implementing class in the ticket service.
 * 
 * @author ANUP SINGH
 *
 */
public interface TicketServiceConstants {

	String CHECK_AVAILABLE_SEAT_COUNT = "availableSeatCount";
	String VENUE_LEVEL = "level";
	String FIND_AND_HOLD_SEAT = "holdSeat";
	String NUMBER_OF_SEATS = "count";
	String MIN_VENUE_LEVEL = "minLevel";
	String MAX_VENUE_LEVEL = "maxLevel";
	String CUSTOMER_EMAIL_ADDRESS = "emailId";
	String RESERVE_SEATS = "bookSeat";
	String SEAT_HOLD_ID = "bookingId";

	String SERVICE = "service";
	String SPRING_CONFIG_FILE = "applicationContext.xml";

	String SEAT_HOLD_TIME_IN_SECONDS = "120";
	String CONFIRMATION_CODE_FIXED_STRING = "Success";
	int RANDOM_STRING_LENGTH = 5;

	String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

}
