/**
 * 
 */
package com.rxsavings.location.utility;

import org.springframework.batch.item.ItemProcessor;

import com.rxsavings.location.model.Pharmacy;

/**
 * @author haree
 *
 */
public class PharmacyObjectProcessor
		implements
			ItemProcessor<Pharmacy, Pharmacy> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public Pharmacy process(Pharmacy item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}

}
