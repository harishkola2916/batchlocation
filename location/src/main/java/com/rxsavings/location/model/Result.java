package com.rxsavings.location.model;

import org.springframework.stereotype.Component;

/**
 * Result model holds the minimum distance and closest pharmacy attributes
 * 
 * @author Harish
 *
 */
@Component
public class Result {
	private double distance;
	private Pharmacy pharmacy;

	/**
	 * @return the minDistance
	 */
	public double getMinDistance() {
		return distance;
	}

	/**
	 * @param minDistance the minDistance to set
	 */
	public void setMinDistance(double minDistance) {
		this.distance = minDistance;
	}

	/**
	 * @return the pharmObject
	 */
	public Pharmacy getPharmObject() {
		return pharmacy;
	}

	/**
	 * @param pharmObject the pharmObject to set
	 */
	public void setPharmObject(Pharmacy pharmObject) {
		this.pharmacy = pharmObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Result [minDistance=" + distance + ", pharmObject=" + pharmacy + "]";
	}

}
