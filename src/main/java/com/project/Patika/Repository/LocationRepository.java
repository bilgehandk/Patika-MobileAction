package com.project.Patika.Repository;

import com.project.Patika.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long>
{
    Location findByCityName(String city);
}
