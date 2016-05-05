package com.walmart.lab.TicketService;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walmart.lab.ticketService.TicketService;
import com.walmart.lab.ticketService.TicketServiceMaintenance;
import com.walmart.lab.ticketService.booking.Seat;
import com.walmart.lab.ticketService.booking.SeatHold;
import com.walmart.lab.ticketService.constants.TicketServiceConstants;

import junit.framework.TestCase;


public class TestSeatHoldAndAvailability extends TestCase implements TicketServiceConstants
{
	TicketService ticketService=null;
	TicketServiceMaintenance ticketServiceMaintenance=null;
	 protected void setUp(){
		 ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		 ticketService = (TicketService) factory.getBean("ticketService");
		 ticketServiceMaintenance=(TicketServiceMaintenance) factory.getBean("ticketServiceMaintenance");
	 }

	   
     public void testSeatHoldAndAvailability(){
     
      //Just to make make sure we have seats available
      ticketServiceMaintenance.revokeAllReservation();
      //get the available seats 
      int seatCount=ticketService.numSeatsAvailable(Optional.ofNullable(null));
      SeatHold seatHold=ticketService.findAndHoldSeats(seatCount, Optional.ofNullable(1), Optional.ofNullable(4), "ticketService@test.com");
      int seatCountAgain=ticketService.numSeatsAvailable(Optional.ofNullable(null));
          
      if(seatCountAgain==0){
          assertTrue(true);
      }
      else{
    	  assertTrue(false);
      }
     
      
      
      
      
       
    }
}
