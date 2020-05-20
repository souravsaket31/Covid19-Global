package com.sourav.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sourav.coronavirustracker.models.locationstats.LocationStats;

@Service
public class CoronaVirusDataService {

	private int casesInIndia;

	public int getCasesInIndia() {
		return casesInIndia;
	}

	public void setCasesInIndia(int casesInIndia) {
		this.casesInIndia = casesInIndia;
	}

	private String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	private List<LocationStats> allStats = new ArrayList<>();

	public String getVIRUS_DATA_URL() {
		return VIRUS_DATA_URL;
	}

	public List<LocationStats> getAllStats() {
		return allStats;
	}

	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchVirusData() throws IOException, InterruptedException {
		List<LocationStats> newStats = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient(); // Create a Client
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
		// Create a Request, which is creating a request using Build pattern
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
		StringReader csvBodyReader = new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		for (CSVRecord record : records) {
			LocationStats locationStat = new LocationStats();
			// String country = record.get("Country/Region");
			locationStat.setCountry(record.get("Country/Region"));
			int latestCases = Integer.parseInt(record.get(record.size() - 1));
			// int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
			locationStat.setLatestToatalCases(latestCases);
			// locationStat.setDiffFromPrevDay(latestCases - prevDayCases);
			newStats.add(locationStat);
			System.out.println(locationStat);

			if (locationStat.getCountry().equals("India")) {
				casesInIndia = locationStat.getLatestToatalCases();

			}

		}
		System.out.println("**************Total cases in India : " + casesInIndia);
		this.allStats = newStats;
		Collections.sort(allStats);
	}

}
