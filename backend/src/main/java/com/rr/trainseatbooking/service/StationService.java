package com.rr.trainseatbooking.service;

import com.rr.trainseatbooking.dto.request.StationRequest;
import com.rr.trainseatbooking.entity.Station;

import java.util.List;

public interface StationService {

    Station createStation(StationRequest request);

    List<Station> getAllStations();

    Station getStationById(Long id);

    Station updateStation(Long id, StationRequest request);

    void deleteStation(Long id);

}

