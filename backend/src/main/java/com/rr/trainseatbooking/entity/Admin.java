package com.rr.trainseatbooking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="admins")
@Getter
@Setter
public class Admin extends BaseEntity {


    @Column(unique=true)
    private String username;


    private String password;


}