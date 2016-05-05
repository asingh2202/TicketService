package com.walmart.lab.ticketService.booking;

/**
 *It represent the setting level involved in the 
 *ticket service application.It contains all the details
 *related to it.
 * 
 * @author ANUP SINGH
 *
 */
public class Venue {

	private int LEVEL_ID;
	private String LEVEL_NAME;
	private float PRICE;
	private int NUMBER_OF_ROW;
	private int SEATS_PER_ROW;

	public int getLEVEL_ID() {
		return LEVEL_ID;
	}

	public void setLEVEL_ID(int lEVEL_ID) {
		LEVEL_ID = lEVEL_ID;
	}

	public String getLEVEL_NAME() {
		return LEVEL_NAME;
	}

	public void setLEVEL_NAME(String lEVEL_NAME) {
		LEVEL_NAME = lEVEL_NAME;
	}

	public float getPRICE() {
		return PRICE;
	}

	public void setPRICE(float pRICE) {
		PRICE = pRICE;
	}

	public int getNUMBER_OF_ROW() {
		return NUMBER_OF_ROW;
	}

	public void setNUMBER_OF_ROW(int nUMBER_OF_ROW) {
		NUMBER_OF_ROW = nUMBER_OF_ROW;
	}

	public int getSEATS_PER_ROW() {
		return SEATS_PER_ROW;
	}

	public void setSEATS_PER_ROW(int sEATS_PER_ROW) {
		SEATS_PER_ROW = sEATS_PER_ROW;
	}

}
