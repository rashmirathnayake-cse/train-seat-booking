package com.rr.trainseatbooking.repository;

import com.rr.trainseatbooking.entity.RouteStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteStationRepository
        extends JpaRepository<RouteStation, Long> {


    List<RouteStation> findByRouteIdOrderByStopOrder(Long routeId);


    boolean existsByRouteIdAndStationId(
            Long routeId,
            Long stationId
    );

}