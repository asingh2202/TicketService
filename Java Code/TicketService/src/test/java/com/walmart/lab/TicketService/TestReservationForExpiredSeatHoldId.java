package com.walmart.lab.TicketService;

import java.util.Optional;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walmart.lab.ticketService.TicketService;
import com.walmart.lab.ticketService.TicketServiceMaintenance;
import com.walmart.lab.ticketService.booking.SeatHold;
import com.walmart.lab.ticketService.constants.TicketServiceConstants;

import junit.framework.TestCase;


public class TestReservationForExpiredSeatHoldId extends TestCase implements TicketServiceConstants
{
	TicketService ticketService=null;
	TicketServiceMaintenance ticketServiceMaintenance=null;
	 protected void setUp(){
		 ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		 ticketService = (TicketService) factory.getBean("ticketService");
		 ticketServiceMaintenance=(TicketServiceMaintenance) factory.getBean("ticketServiceMaintenance");
	 }

	   
     public void testReservationForExpiredSeatHoldId(){
     
     //Just to make make sure we have seats available
     ticketServiceMaintenance.revokeAllReservation();
      //get the available seats 
     int seatCount=ticketService.numSeatsAvailable(Optional.ofNullable(null));
      //book the seats
     System.out.println(seatCount);
     SeatHold seatHold=ticketService.findAndHoldSeats(seatCount>10?5:seatCount, Optional.ofNullable(1), Optional.ofNullable(4), "ticketService@test.com");
     
     //Wait to release the hold seat without performing reservation
     try {
		Thread.sleep(130000);
	   } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
     
     String reservationCode=ticketService.reserveSeats((int)(seatHold.getSeatHoldId()), "ticketService@test.com");
     
     System.out.println("Reservation Code::"+reservationCode);
     
     if(!reservationCode.contains(CONFIRMATION_CODE_FIXED_STRING)){     
          assertTrue(true);
      }
      else{
    	  assertTrue(false);
      }
     
    }
}
