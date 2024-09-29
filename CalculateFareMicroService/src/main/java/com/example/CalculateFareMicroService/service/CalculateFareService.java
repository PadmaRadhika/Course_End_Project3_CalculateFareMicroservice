package com.example.CalculateFareMicroService.service;

import java.util.Map;

public interface CalculateFareService {
	public static final String ECONOMY_CABTYPE = "economy";
	public static final String PREMIUM_CABTYPE = "premium";
	public static final String LUXURY_CABTYPE = "luxury";	
	public double calculateEconomyCabFare(double distance);
	public double calculatePremiumCabFare(double distance);
	public double calculateLuxuryCabFare(double distance);
	public Map<String, String> calculateDistance(String fromLocation, String toLocation);
}
