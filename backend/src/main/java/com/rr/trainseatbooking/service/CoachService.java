package com.rr.trainseatbooking.service;

import com.rr.trainseatbooking.dto.request.CoachRequest;
import com.rr.trainseatbooking.dto.response.CoachResponse;
import com.rr.trainseatbooking.entity.Coach;

import java.util.List;

public interface CoachService {


    CoachResponse createCoach(CoachRequest request);


    List<CoachResponse> getAllCoaches();


    List<CoachResponse> getCoachesByTrain(Long trainId);


    Coach getCoachById(Long id);


    CoachResponse updateCoach(Long id, CoachRequest request);


    void deleteCoach(Long id);

}