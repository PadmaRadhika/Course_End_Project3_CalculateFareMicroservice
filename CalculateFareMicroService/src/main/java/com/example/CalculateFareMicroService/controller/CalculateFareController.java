package com.example.CalculateFareMicroService.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.CalculateFareMicroService.service.CalculateFareService;

@RestController
@Configuration
public class CalculateFareController {
	@Autowired
	private CalculateFareService calculateFareService;	

	/**This method calculates the distance from google maps and based on the
	 * selected cabType 'economy'/'premium'/'luxury', it will calculate the
	 * fare and returns a map which contains, distance, duration, and fare information
	 * to be shown in the book-cab.html page.
	 * This method is called by feign service from CabBookingMicroservice
	 * @param fromLocation
	 * @param toLocation
	 * @param cabType
	 * @return
	 */
	@GetMapping("/calculateFare")
    public ResponseEntity<Map<String, String>> calculateFare(@RequestParam String fromLocation,
            @RequestParam String toLocation, String cabType) { 
		Map<String, String> distanceDurationMap = calculateFareService.calculateDistance(fromLocation, toLocation);
		double fare = 0.0d;
		double distance = Double.valueOf(distanceDurationMap.get("distance"));
		if(cabType.equals(calculateFareService.ECONOMY_CABTYPE))
			fare = calculateFareService.calculateEconomyCabFare(distance);
		else if(cabType.equals(calculateFareService.PREMIUM_CABTYPE))
			fare = calculateFareService.calculatePremiumCabFare(distance);
		else
			fare = calculateFareService.calculateLuxuryCabFare(distance);		
		distanceDurationMap.put("fare", String.valueOf(fare));
        // Return the fare in a ResponseEntity
        return new ResponseEntity<>(distanceDurationMap, HttpStatus.OK);
    }
	/**This method calculates cab fares for all the cab types, economy,premium and luxury.
	 * This method is used to fetch all cab type fares to be shown in calculate-fare.html.
	 * This method is also called by feign service from CabBookingMicroservice.
	 * This method returns a map with distance, duration and cab fares for all the cab types.
	 * @param fromLocation
	 * @param toLocation
	 * @return
	 */
	@PostMapping("/displayFare")
    public ResponseEntity<Map<String, String>> displayAllCabsFare(@RequestParam String fromLocation,
            @RequestParam String toLocation) {		
		Map<String, String> distanceDurationMap = calculateFareService.calculateDistance(fromLocation, toLocation);		
		double distance = Double.valueOf(distanceDurationMap.get("distance"));						
		distanceDurationMap.put(CalculateFareService.ECONOMY_CABTYPE, String.valueOf(calculateFareService.calculateEconomyCabFare(distance)));
		distanceDurationMap.put(CalculateFareService.PREMIUM_CABTYPE, String.valueOf(calculateFareService.calculatePremiumCabFare(distance)));
		distanceDurationMap.put(CalculateFareService.LUXURY_CABTYPE, String.valueOf(calculateFareService.calculateLuxuryCabFare(distance)));
		// Return the fare in a ResponseEntity
        return new ResponseEntity<>(distanceDurationMap, HttpStatus.OK);
    }
}
