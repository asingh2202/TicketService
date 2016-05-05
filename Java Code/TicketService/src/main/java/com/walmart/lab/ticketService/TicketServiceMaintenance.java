package com.walmart.lab.ticketService;

/**
 * This defines one of the service interface which provide 
 * some of the maintenance related functionality to ease 
 * smooth functioning of mains service implementation. 
 * 
 * @author ANUP SINGH
 *
 */
public interface TicketServiceMaintenance {

	
	/**
	 * This removes all the reservations from the 
	 * database and initializes database to 
	 * initial stage.
	 */
	void revokeAllReservation();

}
