package com.project.Patika;


import com.project.Patika.Repository.AirPollutionRepository;
import com.project.Patika.Repository.LocationRepository;
import com.project.Patika.Service.OpenWeatherMapService;
import com.project.Patika.dto.PollutionApiResultDto;
import com.project.Patika.dto.ResultDto;
import com.project.Patika.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OpenWeatherMapServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private AirPollutionRepository airPollutionRepository;

    @Mock
    private RestTemplate restTemplate;

    private OpenWeatherMapService openWeatherMapService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        openWeatherMapService = new OpenWeatherMapService(locationRepository, airPollutionRepository, restTemplate);
    }

    @Test
    public void testGetAirPollutionData() {
        // Arrange
        String cityName = "London";
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 10);

        Location mockLocation = new Location();
        mockLocation.setLat(51.5074);
        mockLocation.setLon(-0.1278);
        when(locationRepository.findByCityName(cityName)).thenReturn(mockLocation);

        ResponseEntity<PollutionApiResultDto> mockResponse = ResponseEntity.ok(new PollutionApiResultDto());
        when(restTemplate.getForEntity(anyString(), eq(PollutionApiResultDto.class))).thenReturn(mockResponse);

        when(airPollutionRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        // Act
        ResultDto resultDto = openWeatherMapService.getAirPollutionData(cityName, startDate, endDate);

        // Assert
        assertNotNull(resultDto);
        assertEquals(cityName, resultDto.getCity());
        assertNotNull(resultDto.getResults());
        assertFalse(resultDto.getResults().isEmpty());
    }
}
