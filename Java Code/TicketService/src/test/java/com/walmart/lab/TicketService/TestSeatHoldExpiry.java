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
public class TestSeatHoldExpiry extends TestCase implements TicketServiceConstants
{
	TicketService ticketService=null;
	TicketServiceMaintenance ticketServiceMaintenance=null;
	 protected void setUp(){
		 ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		 ticketService = (TicketService) factory.getBean("ticketService");
		 ticketServiceMaintenance=(TicketServiceMaintenance) factory.getBean("ticketServiceMaintenance");
	 }

	   
     public void testSeatHoldExpiry(){
      //Just to make make sure we have seats available
      ticketServiceMaintenance.revokeAllReservation();
      //Check available seats
      int seatCount=ticketService.numSeatsAvailable(Optional.ofNullable(null));
      //Hold all the available seats
      SeatHold seatHold=ticketService.findAndHoldSeats(50, Optional.ofNullable(1), Optional.ofNullable(4), "ticketService@test.com");
      
      //Wait to release the hold seat without performing reservation
      try {
		Thread.sleep(140000);
	   } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
      
      //After specified wait, check the seat availability
      int seatCountagain=ticketService.numSeatsAvailable(Optional.ofNullable(null));
      System.out.println("SeatHoldID::"+seatHold.getSeatHoldId()); 
      if(seatCount==seatCountagain){
       assertTrue(true);
      }
      else{
       assertTrue(false);  
      }
      
    }
}
