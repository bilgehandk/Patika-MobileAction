package com.project.Patika.Repository;

import com.project.Patika.dto.PollutionApiResultDto;
import com.project.Patika.model.AirPollution;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AirPollutionRepository extends JpaRepository<AirPollution, Long> {




    //Find all city according to start and end date and city name
    List<AirPollution> findAllByCityAndStartDateAndEndDate(String city, LocalDate startDate, LocalDate endDate);

}
