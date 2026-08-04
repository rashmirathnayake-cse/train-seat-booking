package com.rr.trainseatbooking.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrainRequest {

    private String trainNumber;

    private String name;

    private String description;

    private Long routeId;

    private Boolean active;

}
