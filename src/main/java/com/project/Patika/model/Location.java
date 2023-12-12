package com.project.Patika.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
public class Location {

    @Id
    @Column(name = "city_name")
    private String cityName;
    private double lat;
    private double lon;



    // getters, setters, constructors
}
