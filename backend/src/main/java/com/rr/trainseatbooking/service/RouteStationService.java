package com.rr.trainseatbooking.service;

import com.rr.trainseatbooking.dto.request.RouteStationRequest;
import com.rr.trainseatbooking.dto.response.RouteStationResponse;

import java.util.List;

public interface RouteStationService {


    RouteStationResponse addStation(
            Long routeId,
            RouteStationRequest request
    );


    List<RouteStationResponse> getRouteStations(
            Long routeId
    );


    RouteStationResponse update(
            Long id,
            RouteStationRequest request
    );


    void delete(Long id);

}