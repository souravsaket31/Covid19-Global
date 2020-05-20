package com.sourav.coronavirustracker.models.locationstats;

public class LocationStats implements Comparable<Object> {

	private String country;
	private int latestTotalCases;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	public int getLatestToatalCases() {
		return latestTotalCases;
	}

	public void setLatestToatalCases(int latestToatalCases) {
		this.latestTotalCases = latestToatalCases;
	}

	@Override
	public String toString() {
		return "LocationStats [country=" + country + ", latestTotalCases=" + latestTotalCases + "]";
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		int compareCases = ((LocationStats) o).getLatestToatalCases();
		/* For Ascending order */
		return  compareCases - this.latestTotalCases;
	}
	
	

}
