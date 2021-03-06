package com.walmart.lab.TicketService;

import java.util.Optional;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walmart.lab.ticketService.TicketService;
import com.walmart.lab.ticketService.TicketServiceMaintenance;
import com.walmart.lab.ticketService.booking.SeatHold;
import com.walmart.lab.ticketService.constants.TicketServiceConstants;

import junit.framework.TestCase;


public class TestHoldMoreThanAvailable extends TestCase implements TicketServiceConstants
{
	TicketService ticketService=null;
	TicketServiceMaintenance ticketServiceMaintenance=null;
	 protected void setUp(){
		 ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		 ticketService = (TicketService) factory.getBean("ticketService");
		 ticketServiceMaintenance=(TicketServiceMaintenance) factory.getBean("ticketServiceMaintenance");
	 }

     public void testHoldMoreThanAvailable(){
      //Just to make make sure we have seats available
      ticketServiceMaintenance.revokeAllReservation();
      //Get available seat Count
      int seatCount=ticketService.numSeatsAvailable(Optional.ofNullable(null));
      //Hold the seats
      SeatHold seatHold=ticketService.findAndHoldSeats(seatCount+1, Optional.ofNullable(1), Optional.ofNullable(4), "ticketService@test.com");
      
      if(seatHold.getSeatHoldId()==-1){
    	  assertTrue(true);
      }
      else{
    	  assertTrue(false);
      }
       
    }
}
