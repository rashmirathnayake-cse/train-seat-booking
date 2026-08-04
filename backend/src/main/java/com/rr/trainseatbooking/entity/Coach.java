package com.rr.trainseatbooking.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rr.trainseatbooking.enums.CoachType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "coaches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coach extends BaseEntity {


    @Column(nullable = false)
    private String coachNumber;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoachType type;


    @Column(nullable = false)
    private Integer seatCapacity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "coach",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Seat> seats;

}