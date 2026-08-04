package com.rr.trainseatbooking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Station extends BaseEntity {

    @Column(nullable = false)
    private String name;


    @Column(unique = true, nullable = false)
    private String code;


    @Column(nullable = false)
    private Integer sequenceNumber;



}