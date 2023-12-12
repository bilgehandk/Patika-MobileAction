package com.project.Patika.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.Patika.Enum.PollutionLevel;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirPollutionData {
    private LocalDate dt;
    private Map<String, PollutionLevel> categories;
}
