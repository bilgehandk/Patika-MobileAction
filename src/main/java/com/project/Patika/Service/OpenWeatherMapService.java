package com.project.Patika.Service;

import com.project.Patika.Enum.PollutionLevel;
import com.project.Patika.Repository.AirPollutionRepository;
import com.project.Patika.Repository.LocationRepository;
import com.project.Patika.dto.*;
import com.project.Patika.model.AirPollution;
import com.project.Patika.model.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenWeatherMapService {
    private static final String API_KEY = "bbc906206166b001ef5835ec196e56c6";
    private static final String AIR_POLLUTION_API_URL = "http://api.openweathermap.org/data/2.5/air_pollution/history?";
    private static final String GEO_API_URL = "http://api.openweathermap.org/geo/1.0/direct?";

    private final LocationRepository locationRepository;
    private final AirPollutionRepository airPollutionRepository;
    private final RestTemplate restTemplate;

    public LocationDto getGeocoding(String city) {
        String url = GEO_API_URL + "q=" + city + "&limit=1&appid=" + API_KEY;

        ResponseEntity<LocationDto[]> response = restTemplate.getForEntity(url, LocationDto[].class);
        LocationDto[] locationData = response.getBody();

        if (locationData != null && locationData.length > 0) {
            Location location = new Location();
            location.setCityName(locationData[0].getName());
            location.setLat(locationData[0].getLat());
            location.setLon(locationData[0].getLon());

            locationRepository.save(location);
            return locationData[0];
        }

        return null;
    }

    public ResultDto getAirPollutionData(String cityName, LocalDate startDate, LocalDate endDate) {
        Location locationDto = locationRepository.findByCityName(cityName);
        if (locationDto == null) {
            return null;
        }


        long startUnixTimestamp = startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        long endUnixTimestamp = endDate.atTime(23, 59, 59).toEpochSecond(ZoneOffset.UTC);


        if (startUnixTimestamp < 1606483200) {
            throw new IllegalArgumentException("Start date cannot be greater than 27/11/2020");
        }


        if (!isValidCity(cityName)) {
            throw new IllegalArgumentException("Invalid city name. Supported cities: London, Barcelona, Ankara, Tokyo, Mumbai");
        }

        AirPollution saveObj = new AirPollution();
        List<AirPollutionData> airPollutionDataList = new ArrayList<>();
        PollutionApiResultDto apiResultDto = null;
        List<AirPollution> airPollutionSaveList = new ArrayList<>();
        List<AirPollution> list = airPollutionRepository.findAllByCityAndStartDateAndEndDate(cityName, startDate, endDate);

        if(list != null && !list.isEmpty()){
            //if it is not null, it means that the data is already in the database so get data from database
            //convert the data to the resultDto
           List<AirPollution> dbData = airPollutionRepository.findAllByCityAndStartDateAndEndDate(cityName, startDate, endDate);

                for(AirPollution airPollution : dbData){
                    AirPollutionData airPollutionDatadb = new AirPollutionData();
                    airPollutionDatadb.setDt(airPollution.getStartDate());
                    Map<String, PollutionLevel> pollutionLevels = new HashMap<>();
                    pollutionLevels.put("CO", PollutionLevel.valueOf(airPollution.getCo()));
                    pollutionLevels.put("SO2", PollutionLevel.valueOf(airPollution.getSo2()));
                    pollutionLevels.put("O3", PollutionLevel.valueOf(airPollution.getO3()));
                    airPollutionDatadb.setCategories(pollutionLevels);
                    airPollutionDataList.add(airPollutionDatadb);



                }


        }
        else {

            ResponseEntity<PollutionApiResultDto> response = restTemplate.getForEntity(
                    AIR_POLLUTION_API_URL + "lat=" + locationDto.getLat() + "&lon=" + locationDto.getLon() +
                            "&start=" + startUnixTimestamp + "&end=" + endUnixTimestamp + "&appid=" + API_KEY,
                    PollutionApiResultDto.class);

            apiResultDto = response.getBody();
            if (apiResultDto == null) {
                return null;
            }

            Map<LocalDate, List<PollutionValByDateDto>> groupedData = apiResultDto.getList().stream()
                    .collect(Collectors.groupingBy(dataPoint -> Instant.ofEpochSecond(dataPoint.getDt()).atZone(ZoneOffset.UTC).toLocalDate()));

            for (Map.Entry<LocalDate, List<PollutionValByDateDto>> entry : groupedData.entrySet()) {
                LocalDate date = entry.getKey();
                List<PollutionValByDateDto> dataList = entry.getValue();

                double totalCo = 0;
                double totalSo2 = 0;
                double totalO3 = 0;

                for (PollutionValByDateDto pollutionDto : dataList) {
                    totalCo += pollutionDto.getComponents().getCo();
                    totalSo2 += pollutionDto.getComponents().getSo2();
                    totalO3 += pollutionDto.getComponents().getO3();
                }

                double avgCo = totalCo / dataList.size();
                double avgSo2 = totalSo2 / dataList.size();
                double avgO3 = totalO3 / dataList.size();

                AirPollutionData airPollutionData = new AirPollutionData();
                airPollutionData.setDt(date);
                airPollutionData.setCategories(classifyPollution(new PollutionValuesDto(avgCo, avgSo2, avgO3)));
                airPollutionDataList.add(airPollutionData);

                AirPollution airPollution = new AirPollution();
                airPollution.setCity(cityName);
                airPollution.setStartDate(date);
                airPollution.setEndDate(date);
                airPollution.setCo(airPollutionData.getCategories().get("CO").name());
                airPollution.setO3(airPollutionData.getCategories().get("O3").name());
                airPollution.setSo2(airPollutionData.getCategories().get("SO2").name());
                airPollutionSaveList.add(airPollution);
            }
        }



        airPollutionRepository.saveAll(airPollutionSaveList);

        ResultDto result = new ResultDto();
        result.setCity(cityName);
        result.setResults(airPollutionDataList);

        return result;
    }


    private boolean isValidCity(String cityName) {
        List<String> supportedCities = Arrays.asList("London", "Barcelona", "Ankara", "Tokyo", "Mumbai");
        return supportedCities.contains(cityName);
    }


    private Map<String, PollutionLevel> classifyPollution(PollutionValuesDto components) {
        Map<String, PollutionLevel> pollutionLevels = new HashMap<>();

        pollutionLevels.put("CO", classifyLevelCo(components.getCo()));
        pollutionLevels.put("SO2", classifyLevelSo2(components.getSo2()));
        pollutionLevels.put("O3", classifyLevelO3(components.getO3()));

        return pollutionLevels;
    }

    private PollutionLevel classifyLevelCo(double concentration) {
        if (concentration <= 1.0) {
            return PollutionLevel.GOOD;
        } else if (concentration <= 2.0) {
            return PollutionLevel.SATISFACTORY;
        } else if (concentration <= 10.0) {
            return PollutionLevel.MODERATE;
        } else if (concentration <= 17.0) {
            return PollutionLevel.POOR;
        } else if (concentration <= 34.0) {
            return PollutionLevel.SEVERE;
        } else {
            return PollutionLevel.HAZARDOUS;
        }
    }

    private PollutionLevel classifyLevelSo2(double concentration) {
        if (concentration <= 40) {
            return PollutionLevel.GOOD;
        } else if (concentration <= 80) {
            return PollutionLevel.SATISFACTORY;
        } else if (concentration <= 380) {
            return PollutionLevel.MODERATE;
        } else if (concentration <= 800) {
            return PollutionLevel.POOR;
        } else if (concentration <= 1600) {
            return PollutionLevel.SEVERE;
        } else {
            return PollutionLevel.HAZARDOUS;
        }
    }

    private PollutionLevel classifyLevelO3(double concentration) {
        if (concentration <= 50) {
            return PollutionLevel.GOOD;
        } else if (concentration <= 100) {
            return PollutionLevel.SATISFACTORY;
        } else if (concentration <= 200) {
            return PollutionLevel.MODERATE;
        } else if (concentration <= 300) {
            return PollutionLevel.POOR;
        } else if (concentration <= 400) {
            return PollutionLevel.SEVERE;
        } else {
            return PollutionLevel.HAZARDOUS;
        }
    }


}
