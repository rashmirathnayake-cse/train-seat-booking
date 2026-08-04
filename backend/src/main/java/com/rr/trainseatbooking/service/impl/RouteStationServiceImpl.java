package com.rr.trainseatbooking.service.impl;

import com.rr.trainseatbooking.dto.request.RouteStationRequest;
import com.rr.trainseatbooking.dto.response.RouteStationResponse;
import com.rr.trainseatbooking.entity.Route;
import com.rr.trainseatbooking.entity.RouteStation;
import com.rr.trainseatbooking.entity.Station;
import com.rr.trainseatbooking.exception.DuplicateResourceException;
import com.rr.trainseatbooking.exception.ResourceNotFoundException;
import com.rr.trainseatbooking.repository.RouteRepository;
import com.rr.trainseatbooking.repository.RouteStationRepository;
import com.rr.trainseatbooking.repository.StationRepository;
import com.rr.trainseatbooking.service.RouteStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteStationServiceImpl
        implements RouteStationService {


    private final RouteRepository routeRepository;

    private final StationRepository stationRepository;

    private final RouteStationRepository routeStationRepository;



    @Override
    public RouteStationResponse addStation(
            Long routeId,
            RouteStationRequest request) {


        Route route = routeRepository.findById(routeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Route not found"
                        ));


        Station station = stationRepository.findById(
                        request.getStationId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Station not found"
                        ));



        if(routeStationRepository
                .existsByRouteIdAndStationId(
                        routeId,
                        request.getStationId()
                )) {

            throw new DuplicateResourceException(
                    "Station already exists in route"
            );
        }



        RouteStation routeStation =
                RouteStation.builder()
                        .route(route)
                        .station(station)
                        .stopOrder(request.getStopOrder())
                        .distanceFromOrigin(
                                request.getDistanceFromOrigin()
                        )
                        .scheduledStop(
                                request.getScheduledStop()
                        )
                        .build();


        return map(
                routeStationRepository.save(routeStation)
        );
    }




    @Override
    public List<RouteStationResponse> getRouteStations(
            Long routeId) {


        return routeStationRepository
                .findByRouteIdOrderByStopOrder(routeId)
                .stream()
                .map(this::map)
                .toList();

    }




    @Override
    public RouteStationResponse update(
            Long id,
            RouteStationRequest request) {


        RouteStation routeStation =
                routeStationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Route station not found"
                                ));



        routeStation.setStopOrder(
                request.getStopOrder()
        );

        routeStation.setDistanceFromOrigin(
                request.getDistanceFromOrigin()
        );

        routeStation.setScheduledStop(
                request.getScheduledStop()
        );


        return map(
                routeStationRepository.save(routeStation)
        );

    }




    @Override
    public void delete(Long id) {

        RouteStation routeStation =
                routeStationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Route station not found"
                                ));


        routeStationRepository.delete(routeStation);

    }




    private RouteStationResponse map(
            RouteStation rs) {


        return RouteStationResponse.builder()
                .id(rs.getId())
                .stationId(
                        rs.getStation().getId()
                )
                .stationName(
                        rs.getStation().getName()
                )
                .stopOrder(
                        rs.getStopOrder()
                )
                .distanceFromOrigin(
                        rs.getDistanceFromOrigin()
                )
                .scheduledStop(
                        rs.getScheduledStop()
                )
                .build();

    }
}