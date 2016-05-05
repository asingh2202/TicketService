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
public class TestAvailabitySeatHoldAndBook extends TestCase implements TicketServiceConstants
{
	TicketService ticketService=null;
	TicketServiceMaintenance ticketServiceMaintenance=null;
	 protected void setUp(){
		 ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		 ticketService = (TicketService) factory.getBean("ticketService");
		 ticketServiceMaintenance=(TicketServiceMaintenance) factory.getBean("ticketServiceMaintenance");
	 }

	   
     public void testAvailabitySeatHoldAndBook(){
      //Just to make make sure we have seats available
      ticketServiceMaintenance.revokeAllReservation();
      
      int randomSeats=50;
      
      //Check available seats
      int seatCount=ticketService.numSeatsAvailable(Optional.ofNullable(null));
      
      while(seatCount>0 && randomSeats < seatCount){
    	  
    	  //Hold RandomCount of Seats
    	  SeatHold seatHold=ticketService.findAndHoldSeats(randomSeats, Optional.ofNullable(1), Optional.ofNullable(4),"testUser@testing.com");
    	  System.out.println("seatHold::"+seatHold.getSeatHoldId());
    	  String confirmationCD=ticketService.reserveSeats((int)seatHold.getSeatHoldId(), "testUser@testing.com");
    	  System.out.println("confirmationCD::"+confirmationCD);
    	  if(!confirmationCD.contains(CONFIRMATION_CODE_FIXED_STRING)){
    	  assertTrue(false);
    	  }
    	  
    	  seatCount=ticketService.numSeatsAvailable(Optional.ofNullable(null)); 
    	  System.out.println("seatCount::"+seatCount);
    	  
      }
     
      //To Handle remaining seat
      if(seatCount>0 && randomSeats >= seatCount){
    	  
    	  //Hold remaining Seats
    	  SeatHold seatHold=ticketService.findAndHoldSeats(seatCount, Optional.ofNullable(1), Optional.ofNullable(4),"testUser@testing.com");
    	  System.out.println("seatHold::"+seatHold.getSeatHoldId());
    	  String confirmationCD=ticketService.reserveSeats((int)seatHold.getSeatHoldId(), "testUser@testing.com");
    	  System.out.println("confirmationCD::"+confirmationCD);
    	  if(!confirmationCD.contains(CONFIRMATION_CODE_FIXED_STRING)){
    	  assertTrue(false);
    	  }
    	  	
      }
      
      if(ticketService.numSeatsAvailable(Optional.ofNullable(null))==0){
    	  assertTrue(true);
      }
      else{
    	  assertTrue(false);
      }
      
      
      
    }
}
