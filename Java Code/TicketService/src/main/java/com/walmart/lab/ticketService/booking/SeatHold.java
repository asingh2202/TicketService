package com.walmart.lab.ticketService.booking;

import java.util.List;

/**
 * This is used to represent all the data related to 
 * a seat hold function in ticket Service.
 * 
 * @author ANUP SINGH
 *
 */
public class SeatHold implements SeatHoldIF {

	private long seatHoldId;
	private List<Seat> seats;
	private int seatHoldCount;

	public long getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(long seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public int getSeatHoldCount() {
		return seatHoldCount;
	}

	public void setSeatHoldCount(int seatHoldCount) {
		this.seatHoldCount = seatHoldCount;
	}

}
