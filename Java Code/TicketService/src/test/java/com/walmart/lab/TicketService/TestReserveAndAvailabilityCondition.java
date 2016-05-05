package com.walmart.lab.TicketService;

import java.util.Optional;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walmart.lab.ticketService.Service;
import com.walmart.lab.ticketService.TicketService;
import com.walmart.lab.ticketService.TicketServiceMaintenance;
import com.walmart.lab.ticketService.booking.SeatHold;
import com.walmart.lab.ticketService.constants.TicketServiceConstants;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class TestReserveAndAvailabilityCondition extends TestCase implements TicketServiceConstants
{
	TicketService ticketService=null;
	TicketServiceMaintenance ticketServiceMaintenance=null;
	 protected void setUp(){
		 ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		 ticketService = (TicketService) factory.getBean("ticketService");
		 ticketServiceMaintenance=(TicketServiceMaintenance) factory.getBean("ticketServiceMaintenance");
	 }

	   
     public void testReserveAndAvailabilityCondition(){
      //Just to make make sure we have seats available
      ticketServiceMaintenance.revokeAllReservation();
      //Check all the available seats
      int seatCount=ticketService.numSeatsAvailable(Optional.ofNullable(null));
      //Hold all the available seats 
      SeatHold seatHold=ticketService.findAndHoldSeats(seatCount, Optional.ofNullable(1), Optional.ofNullable(4), "ticketService@test.com");
      //Reserve all the hold seat
      String confirmationCode=ticketService.reserveSeats((int)seatHold.getSeatHoldId(), "ticketService@test.com");
      //Check again the seat availability by explicitly each level 
      int seatCountAgain=ticketService.numSeatsAvailable(Optional.ofNullable(1))+ticketService.numSeatsAvailable(Optional.ofNullable(2))+ticketService.numSeatsAvailable(Optional.ofNullable(3))+ticketService.numSeatsAvailable(Optional.ofNullable(4));
      if(seatCountAgain==0){
    	  assertTrue(true);
      }
      else{
    	  assertTrue(false);
      }
       
    }
}
