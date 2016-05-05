package com.walmart.lab.ticketService.exception;

/**
 * It is wrapper encapsulation the exception which is used for exception
 * handling in the ticket service.
 * 
 * @author ANUP SINGH
 *
 */
public class WalmartException extends Exception {

	public WalmartException(String errorMessage) {
		super(errorMessage);
	}

}
