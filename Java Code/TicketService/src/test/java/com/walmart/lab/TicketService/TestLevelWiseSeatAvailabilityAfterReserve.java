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

public class TestLevelWiseSeatAvailabilityAfterReserve extends TestCase implements TicketServiceConstants
{
	TicketService ticketService=null;
	TicketServiceMaintenance ticketServiceMaintenance=null;
	 protected void setUp(){
		 ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		 ticketService = (TicketService) factory.getBean("ticketService");
		 ticketServiceMaintenance=(TicketServiceMaintenance) factory.getBean("ticketServiceMaintenance");
	 }
	 

	   
     public void testLevelWiseSeatAvailabilityAfterReserve(){
    	 
      //Just to make make sure we have seats available
      ticketServiceMaintenance.revokeAllReservation();
      //Checking all the available seats at level#4    
      int seatCount=ticketService.numSeatsAvailable(Optional.ofNullable(4));
      //Booking all the available seats at level#4
      SeatHold seatHold=ticketService.findAndHoldSeats(seatCount, Optional.ofNullable(null), Optional.ofNullable(4), "ticketService@test.com");
      String confirmationCode=ticketService.reserveSeats((int)seatHold.getSeatHoldId(), "ticketService@test.com");
      
      //Testing if reservation is successful
      if(confirmationCode.contains(CONFIRMATION_CODE_FIXED_STRING)){
    	  //Check again if seatHoldPossible
    	  SeatHold seatHoldAgain=ticketService.findAndHoldSeats(50, Optional.ofNullable(null), Optional.ofNullable(4), "ticketService@test.com");
    	  if(seatHoldAgain.getSeatHoldCount()==0)
    	  assertTrue(true);
      }
      else{
    	  assertTrue(false);
      }
       
    }
}
