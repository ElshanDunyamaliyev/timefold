package com.example.timefold.appointment.model;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalTime;

@PlanningEntity
@NoArgsConstructor
@Getter
@Setter
public class Appointment {

    @PlanningId
    private String name;

    private Duration duration;

    @PlanningVariable
    private LocalTime startTime;

    @JsonProperty("endTime")
    public LocalTime getEndTime(){
        return startTime.plus(duration);
    }

    public Appointment(String name, Duration duration) {
        this.name = name;
        this.duration = duration;
    }
}
