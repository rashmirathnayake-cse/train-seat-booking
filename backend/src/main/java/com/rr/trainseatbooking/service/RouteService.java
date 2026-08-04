package com.rr.trainseatbooking.service;

import com.rr.trainseatbooking.dto.request.RouteRequest;
import com.rr.trainseatbooking.dto.response.RouteResponse;

import java.util.List;

public interface RouteService {

    RouteResponse create(RouteRequest request);

    List<RouteResponse> getAll();

    RouteResponse getById(Long id);

    RouteResponse update(Long id, RouteRequest request);

    void delete(Long id);

}