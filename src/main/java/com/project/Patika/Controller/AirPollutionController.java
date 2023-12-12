package com.project.Patika.Controller;


import com.project.Patika.Repository.AirPollutionRepository;
import com.project.Patika.Service.OpenWeatherMapService;

import com.project.Patika.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pollution")
public class AirPollutionController {

    private final OpenWeatherMapService openWeatherMapService;



    @GetMapping()
    public ResponseEntity<Object> getAirPollutionData(
            @RequestParam(value = "city") String city,
            @RequestParam(value = "startDate", required = false, defaultValue = "#{T(LocalDate).now().minusWeeks(1)}") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "#{T(LocalDate).now()}") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate endDate) {

        if (startDate == null) {
            startDate = LocalDate.now().minusWeeks(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        try {
            ResultDto airPollutionDto = openWeatherMapService.getAirPollutionData(city, startDate, endDate);
            return ResponseEntity.ok(airPollutionDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.toString());

        }

    }



}
