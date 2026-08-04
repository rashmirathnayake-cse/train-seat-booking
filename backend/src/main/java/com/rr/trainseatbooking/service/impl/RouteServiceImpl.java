package com.rr.trainseatbooking.service.impl;

import com.rr.trainseatbooking.dto.request.RouteRequest;
import com.rr.trainseatbooking.dto.response.RouteResponse;
import com.rr.trainseatbooking.entity.Route;
import com.rr.trainseatbooking.exception.DuplicateResourceException;
import com.rr.trainseatbooking.exception.ResourceNotFoundException;
import com.rr.trainseatbooking.repository.RouteRepository;
import com.rr.trainseatbooking.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    @Override
    public RouteResponse create(RouteRequest request) {

        if (routeRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Route already exists.");
        }

        Route route = Route.builder()
                .name(request.getName())
                .description(request.getDescription())
                .active(request.getActive())
                .build();

        return map(routeRepository.save(route));
    }

    @Override
    public List<RouteResponse> getAll() {

        return routeRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public RouteResponse getById(Long id) {

        return map(findRoute(id));
    }

    @Override
    public RouteResponse update(Long id, RouteRequest request) {

        Route route = findRoute(id);

        route.setName(request.getName());
        route.setDescription(request.getDescription());
        route.setActive(request.getActive());

        return map(routeRepository.save(route));
    }

    @Override
    public void delete(Long id) {

        routeRepository.delete(findRoute(id));
    }

    private Route findRoute(Long id) {

        return routeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Route not found."));
    }

    private RouteResponse map(Route route) {

        return RouteResponse.builder()
                .id(route.getId())
                .name(route.getName())
                .description(route.getDescription())
                .active(route.getActive())
                .build();
    }
}
