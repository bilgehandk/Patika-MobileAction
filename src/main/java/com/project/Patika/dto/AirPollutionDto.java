package com.project.Patika.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirPollutionDto {
    private Long id;
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private double co;
    private double o3;
    private double so2;
}
