package com.project.Patika.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.Patika.model.AirPollution;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultDto {

    private String city;
    private List<AirPollutionData> results;

}
