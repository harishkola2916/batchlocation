package com.rxsavings.location.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.rxsavings.location.exception.LocationException;
import com.rxsavings.location.model.Pharmacy;
import com.rxsavings.location.model.Result;

/**
 * 
 * Service class which takes user location(latitude and longitude) as input and
 * returns closest pharmacy
 * 
 * @author Harish
 *
 */
@Service
public class LocationService {

	@Autowired
	private Result result;

	/**
	 * Calculates the distance between user location and each pharmacy and returns
	 * {@link Result} object which has closest pharmacy address and distance from
	 * user location to it
	 * 
	 * 
	 * @param inputLatitude
	 * @param inputLongitude
	 * @return
	 * @throws IOException
	 * @throws LocationException
	 */
	public Result getClosestDistance(double inputLatitude, double inputLongitude)
			throws IOException, LocationException {

		List<Pharmacy> pharmacyList = new ArrayList<Pharmacy>();
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("pharmacies.csv");
		try (CSVReader br = new CSVReader(new FileReader(resource.getFile()))) {
			br.readNext();
			String[] line;
			while ((line = br.readNext()) != null) {
				pharmacyList.add(new Pharmacy(line[0], line[1], line[2], line[3], Integer.parseInt(line[4]),
						Double.valueOf(line[5]), Double.valueOf(line[6])));

			}

		} catch (FileNotFoundException e) {
			throw new LocationException("Could not find csv file", e);
		} catch (IOException e) {
			throw new LocationException("Unable to read the content in csv file", e);
		}

		double minDistance = Double.MAX_VALUE;
		double earthRadius = 3958.75;
		for (Pharmacy pharmacy : pharmacyList) {

			double csvLatitude = pharmacy.getLatitude();
			double csvLongitude = pharmacy.getLongitude();
			double deltaLat = Math.toRadians(csvLatitude - inputLatitude);
			double deltaLng = Math.toRadians(csvLongitude - inputLongitude);
			double sindLat = Math.sin(deltaLat / 2);
			double sindLng = Math.sin(deltaLng / 2);
			double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(inputLatitude))
					* Math.cos(Math.toRadians(csvLatitude));
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			double dist = earthRadius * c;
			if (dist < minDistance) {
				minDistance = dist;
				result.setMinDistance(minDistance);
				result.setPharmObject(pharmacy);
			}

		}
		return result;
	}

}
