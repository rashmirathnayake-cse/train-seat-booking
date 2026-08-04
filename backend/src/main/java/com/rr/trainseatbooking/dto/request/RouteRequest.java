package com.rr.trainseatbooking.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteRequest {

    private String name;

    private String description;

    private Boolean active;

}