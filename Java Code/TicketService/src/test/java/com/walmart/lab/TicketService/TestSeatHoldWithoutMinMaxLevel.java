package com.walmart.lab.TicketService;

import java.util.Optional;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walmart.lab.ticketService.TicketService;
import com.walmart.lab.ticketService.TicketServiceMaintenance;
import com.walmart.lab.ticketService.booking.SeatHold;
import com.walmart.lab.ticketService.constants.TicketServiceConstants;

import junit.framework.TestCase;


public class TestSeatHoldWithoutMinMaxLevel extends TestCase implements TicketServiceConstants
{
	TicketService ticketService=null;
	TicketServiceMaintenance ticketServiceMaintenance=null;
	 protected void setUp(){
		 ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		 ticketService = (TicketService) factory.getBean("ticketService");
		 ticketServiceMaintenance=(TicketServiceMaintenance) factory.getBean("ticketServiceMaintenance");
	 }

	   
     public void testSeatHoldWithoutMinMaxLevel(){
     
      //Just to make make sure we have seats available
      ticketServiceMaintenance.revokeAllReservation();
      
      //get the available seats 
      int seatCount=ticketService.numSeatsAvailable(Optional.ofNullable(null));
      //book the seats
      SeatHold seatHold=ticketService.findAndHoldSeats(seatCount, Optional.ofNullable(null), Optional.ofNullable(null), "ticketService@test.com");
      //Try to reserve the seats seatHoldID
      String reservationCode=ticketService.reserveSeats((int)(seatHold.getSeatHoldId()), "ticketService@test.com");
      System.out.println("reservationCode::"+reservationCode);
      
      //Just to make make sure we have seats available
      ticketServiceMaintenance.revokeAllReservation();
      
      
      if(reservationCode.contains(CONFIRMATION_CODE_FIXED_STRING)){     
          assertTrue(true);
      }
      else{
    	  assertTrue(false);
      }
     
    }
}
