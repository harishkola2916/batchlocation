package com.rxsavings.location.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rxsavings.location.exception.LocationException;
import com.rxsavings.location.model.Result;
import com.rxsavings.location.utility.LocationService;

/**
 * Rest controller performs http request based on user location and returns http
 * response with a closest pharmacy from user location
 * 
 * @author Harish
 *
 */
@RequestMapping(value = "/api")
@RestController
public class LocationController {

	@Autowired
	private LocationService locationService;

	/**
	 * Returns {@link Result} object with has closest pharmacy from user location
	 * including its address
	 * 
	 * @param latitude  latitude of user location
	 * @param longitude longitude of user location
	 * @return Object with minimum distance and location of pharmacy
	 * @throws IOException
	 * @throws LocationException
	 */
	@RequestMapping(value = "/pharmacy", method = RequestMethod.GET)
	public Result getClosestPharmacy(@RequestParam(name = "latitude") @Valid String latitude,
			@RequestParam(name = "longitude") @Valid String longitude) throws IOException, LocationException {
		double lat = 0.0;
		double lngtd = 0.0;
		try {
			lat = Double.valueOf(latitude);
			lngtd = Double.valueOf(longitude);
		} catch (NumberFormatException e) {
			throw new LocationException("Invalid Input. Please check.");
		}
		if (lat < -90.0 || lat > 90.0) {
			throw new LocationException("Latitude must be between -90.0 to 90.0");
		}

		if (lngtd < -180.0 || lngtd > 180.0) {
			throw new LocationException("Latitude must be between -180.0 to 180.0");
		}
		try {
			return locationService.getClosestPharmacyAndItsDistance(lat, lngtd);
		} catch (LocationException | IOException e) {
			throw new LocationException("Unable to find the closest pharmacy");
		}

	}
}
