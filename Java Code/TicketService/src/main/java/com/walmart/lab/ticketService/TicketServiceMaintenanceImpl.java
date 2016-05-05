package com.walmart.lab.ticketService;

import com.walmart.lab.ticketService.constants.TicketServiceConstants;
import com.walmart.lab.ticketService.dao.TicketDAO;

/**
 * @author ANUP SINGH
 *
 */
public class TicketServiceMaintenanceImpl implements TicketServiceMaintenance, TicketServiceConstants {

	private TicketDAO ticketDAO;

	public TicketDAO getTicketDAO() {
		return ticketDAO;
	}

	public void setTicketDAO(TicketDAO ticketDAO) {
		this.ticketDAO = ticketDAO;
	}

	@Override
	public void revokeAllReservation() {
		// TODO Auto-generated method stub
		ticketDAO.revokeReservation();

	}

}
