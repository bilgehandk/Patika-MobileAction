package com.project.Patika.Controller;

import com.project.Patika.Service.OpenWeatherMapService;
import com.project.Patika.dto.LocationDto;
import com.project.Patika.model.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/city")
@RequiredArgsConstructor
public class GeoController {

    private final OpenWeatherMapService openWeatherMapService;

    @GetMapping
    public ResponseEntity<LocationDto> getGeocoding(@RequestParam("cityName") String city) {
        LocationDto locationDto = openWeatherMapService.getGeocoding(city);
        return ResponseEntity.ok(locationDto);
    }


}
