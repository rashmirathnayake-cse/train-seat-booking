package com.rr.trainseatbooking.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationRequest {

    private String name;

    private String code;

    private Integer sequenceNumber;

}