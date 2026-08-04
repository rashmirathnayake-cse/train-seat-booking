package com.rr.trainseatbooking.service;

import com.rr.trainseatbooking.dto.request.ScheduleStopUpdateRequest;
import com.rr.trainseatbooking.dto.response.ScheduleStopResponse;

import java.util.List;

public interface ScheduleStopService {

    List<ScheduleStopResponse> getStopsBySchedule(Long scheduleId);

    ScheduleStopResponse getById(Long id);

    ScheduleStopResponse update(
            Long id,
            ScheduleStopUpdateRequest request
    );

    List<ScheduleStopResponse> updateAll(
            Long scheduleId,
            List<ScheduleStopUpdateRequest> requests
    );
}
