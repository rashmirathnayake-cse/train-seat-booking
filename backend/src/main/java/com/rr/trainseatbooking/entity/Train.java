package com.rr.trainseatbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.RouteMatcher;

import java.util.List;


@Entity
@Table(name="trains")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Train extends BaseEntity {


    @Column(nullable=false)
    private String trainNumber;


    @Column(nullable=false)
    private String name;

    private String description;


    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;


    @OneToMany(
            mappedBy = "train",
            cascade = CascadeType.ALL
    )
    private List<Coach> coaches;

}