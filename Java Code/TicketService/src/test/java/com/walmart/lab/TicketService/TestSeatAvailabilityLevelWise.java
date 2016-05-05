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
public class TestSeatAvailabilityLevelWise extends TestCase implements TicketServiceConstants
{
	TicketService ticketService=null;
	TicketServiceMaintenance ticketServiceMaintenance=null;
	 protected void setUp(){
		 ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext(SPRING_CONFIG_FILE);
		 ticketService = (TicketService) factory.getBean("ticketService");
		 ticketServiceMaintenance=(TicketServiceMaintenance) factory.getBean("ticketServiceMaintenance");
	 }

	   
     public void testSeatAvailabilityLevelWise(){
      
    	  //Just to make make sure we have seats available
         ticketServiceMaintenance.revokeAllReservation();
    	 //Seat Availability at each individual level
    	 int seatsAtLevelOne=ticketService.numSeatsAvailable(Optional.ofNullable(1));
    	 int seatsAtLevelTwo=ticketService.numSeatsAvailable(Optional.ofNullable(2));
    	 int seatsAtLevelThree=ticketService.numSeatsAvailable(Optional.ofNullable(3));
    	 int seatsAtLevelFour=ticketService.numSeatsAvailable(Optional.ofNullable(4));
    	 int totalLevelSeats=seatsAtLevelOne+seatsAtLevelTwo+seatsAtLevelThree+seatsAtLevelFour;
    	 
    	 
    	 //Seat Availabilty considering all the level
    	 int totalSeats=ticketService.numSeatsAvailable(Optional.ofNullable(null));
    	 
    	 if(totalLevelSeats==totalSeats){
    		 assertTrue(true);
         }
         else{
          assertTrue(false);  
         }
         
    }
}
