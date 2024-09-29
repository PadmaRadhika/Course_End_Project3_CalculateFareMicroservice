package com.example.CalculateFareMicroService.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
@Service
public class CalculateFareServiceImpl implements CalculateFareService{
	//Since, this data does not change frequently and also, only few values are there,
	//its not needed to save this information in DB as hitting db is a costlier operation
	//for infrequently changed values. Hence, stored them in properties file. Can be changed
	//any time there is a change in fare calculation.
	//Get all the required attributes from application.properties
	//to calculate the fare for each type of cab.
	//There are three types of cabs, i) Economy, ii)Premium, iii)Luxury
	@Value("${economy.basefare}")
	private String economyBaseFare;
	@Value("${economy.costpermile}")
	private String economyCostpermile;
	@Value("${economy.servicecharge}")
	private String economyServicecharge;
	
	@Value("${premium.basefare}")
	private String premiumBaseFare;
	@Value("${premium.costpermile}")
	private String premiumCostpermile;
	@Value("${premium.servicecharge}")
	private String premiumServicecharge;
	
	@Value("${luxury.basefare}")
	private String luxuryBaseFare;
	@Value("${luxury.costpermile}")
	private String luxuryCostpermile;
	@Value("${luxury.servicecharge}")
	private String luxuryServicecharge;
	//get google maps API key from application.properties file
	@Value("${apiKey}")
	private String apiKey;	
	
	/** This is a generic method to calculate cab fare
	 * calculation of fare is: baseFare + serviceCharge + (distance * costPerMile).
	 * @param baseFare
	 * @param serviceCharge
	 * @param costPerMile
	 * @param distance
	 * @return
	 */
	private double calculateCabFare(String baseFare, String serviceCharge, String costPerMile, double distance) {
		return (Double.valueOf(baseFare)+Double.valueOf(serviceCharge)+ (distance *Double.valueOf(costPerMile)));
	}
	
	/**Method to calculate Economy cab fare
	 * It calculates based on the distance calculated between locations using google maps.
	 */
	public double calculateEconomyCabFare(double distance) {		
		return Math.round(calculateCabFare(economyBaseFare,economyServicecharge, economyCostpermile, distance));
	}
	
	/**Method to calculate Premium cab fare
	 * It calculates based on the distance calculated between locations using google maps.
	 *
	 */
	public double calculatePremiumCabFare(double distance) {
		return Math.round(calculateCabFare(premiumBaseFare,premiumServicecharge, premiumCostpermile, distance));
	}
	/**Method to calculate Luxury cab fare
	 * It calculates based on the distance calculated between locations using google maps.
	 */
	public double calculateLuxuryCabFare(double distance) {
		return Math.round(calculateCabFare(luxuryBaseFare,luxuryServicecharge, luxuryCostpermile, distance));
	}
	/**This method returns distance and duration between from and to locations
	 *as a map. This information is obtained from google maps API using generated apiKey.
	 */
	@Override
	public Map<String, String> calculateDistance(String fromLocation, String toLocation) {
		Map<String, String> response = new HashMap<>();
        try {
        	response = getDistance(apiKey, fromLocation, toLocation);
            
        	} catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return response;
    }

    /**This method actually gets the distance and duration information from google maps 
     * and returns to calculateDistance method which in turn sends this information to
     * the calling microservices to get displayed in the html forms and to calculate the 
     * cab fares.
     * @param apiKey
     * @param origin
     * @param destination
     * @return
     * @throws Exception
     */
    private Map<String, String> getDistance(String apiKey, String origin, String destination) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                .origins(origin)
                .destinations(destination)
                .await();
        double kmToMiles = 0.621371;
        DistanceMatrixRow row = matrix.rows[0];
        DistanceMatrixElement element = row.elements[0];
        Map<String, String> response = new HashMap<>();
        //Distance is converted from meters to kms to miles and rounded to the nearest value.
        double distanceInMiles = Math.round((element.distance.inMeters/1000.0)*kmToMiles);
        response.put("distance", String.valueOf(distanceInMiles));
        response.put("duration", element.duration.humanReadable);
        return response;
    }

	
}
