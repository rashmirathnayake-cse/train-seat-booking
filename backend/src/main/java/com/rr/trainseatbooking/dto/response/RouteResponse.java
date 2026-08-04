package com.rr.trainseatbooking.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteResponse {

    private Long id;

    private String name;

    private String description;

    private Boolean active;

}
