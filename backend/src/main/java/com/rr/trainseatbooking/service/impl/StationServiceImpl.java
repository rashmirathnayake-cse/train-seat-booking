package com.rr.trainseatbooking.service.impl;

import com.rr.trainseatbooking.dto.request.StationRequest;
import com.rr.trainseatbooking.entity.Station;
import com.rr.trainseatbooking.repository.StationRepository;
import com.rr.trainseatbooking.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    @Override
    public Station createStation(StationRequest request) {

        Station station = Station.builder()
                .name(request.getName())
                .code(request.getCode())
                .sequenceNumber(request.getSequenceNumber())
                .build();

        return stationRepository.save(station);
    }

    @Override
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    @Override
    public Station getStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found with id: " + id));
    }

    @Override
    public Station updateStation(Long id, StationRequest request) {

        Station station = getStationById(id);

        station.setName(request.getName());
        station.setCode(request.getCode());
        station.setSequenceNumber(request.getSequenceNumber());

        return stationRepository.save(station);
    }

    @Override
    public void deleteStation(Long id) {

        Station station = getStationById(id);

        stationRepository.delete(station);
    }
}