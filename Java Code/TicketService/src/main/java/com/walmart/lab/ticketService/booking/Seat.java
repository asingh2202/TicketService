package com.walmart.lab.ticketService.booking;

/**
 * This is used for hold the Seat Detail in the Seat Hold 
 * functionality for Ticket Service.
 * 
 * @author ANUP SINGH
 *
 */
public class Seat implements SeatHoldIF {

	private long seatHoldId;
	private int levelId;
	private int seatnumber;

	public long getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(long seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public int getSeatnumber() {
		return seatnumber;
	}

	public void setSeatnumber(int seatnumber) {
		this.seatnumber = seatnumber;
	}
}
