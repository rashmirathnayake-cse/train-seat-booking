package com.rr.trainseatbooking.repository;

import com.rr.trainseatbooking.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {

    boolean existsByName(String name);

}