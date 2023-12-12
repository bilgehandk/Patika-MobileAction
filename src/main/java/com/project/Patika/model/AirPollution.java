package com.project.Patika.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "air_pollution")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirPollution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "city_name")
    private String city;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "co")
    private String co;

    @Column(name = "so2")
    private String so2;

    @Column(name = "o3")
    private String o3;
}
