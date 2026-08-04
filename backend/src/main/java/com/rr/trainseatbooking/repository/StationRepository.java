package com.rr.trainseatbooking.repository;

import com.rr.trainseatbooking.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

}