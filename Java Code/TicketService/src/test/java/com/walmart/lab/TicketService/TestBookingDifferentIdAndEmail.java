package com.walmart.lab.TicketService;

import java.util.Optional;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walmart.lab.ticketService.TicketService;
import com.walmart.lab.ticketService.TicketServiceMaintenance;
import com.walmart.lab.ticketService.booking.SeatHold;
import com.walmart.lab.ticketService.constants.TicketServiceConstants;

import junit.framework.TestCase;


public class TestBookingDifferentIdAndEmail extends TestCase implements TicketServiceConstants
{
	TicketService ticketService=null;
	TicketServiceMaintenance ticketServiceMaintenance=null;
	 protected void setUp(){
		 ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		 ticketService = (TicketService) factory.getBean("ticketService");
		 ticketServiceMaintenance=(TicketServiceMaintenance) factory.getBean("ticketServiceMaintenance");
	 }

	   
     public void testBookingDifferentIdAndEmail(){
     
      //Just to make make sure we have seats available
      ticketServiceMaintenance.revokeAllReservation(); 
      //get the available seats 
      int seatCount=ticketService.numSeatsAvailable(Optional.ofNullable(null));
      //book the seats
      SeatHold seatHold1=ticketService.findAndHoldSeats(10, Optional.ofNullable(1), Optional.ofNullable(4), "ticketService@test.com");
      SeatHold seatHold2=ticketService.findAndHoldSeats(10, Optional.ofNullable(1), Optional.ofNullable(4), "ticketService@testing.com");
      
      //booking with emailID and seatHoldId of two different seatHold
      String reservationCode=ticketService.reserveSeats((int)(seatHold1.getSeatHoldId()), "ticketService@testing.com");
     
     if(!reservationCode.contains(CONFIRMATION_CODE_FIXED_STRING)){     
          assertTrue(true);
      }
      else{
    	  assertTrue(false);
      }
     
      
      
      
      
       
    }
}
