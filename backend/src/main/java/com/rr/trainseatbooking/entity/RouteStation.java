package com.rr.trainseatbooking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "route_stations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

    @Column(nullable = false)
    private Integer stopOrder;

    @Column(nullable = false)
    private Double distanceFromOrigin;

    @Column(nullable = false)
    private Boolean scheduledStop = true;
}