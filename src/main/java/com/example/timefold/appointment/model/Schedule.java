package com.example.timefold.appointment.model;


import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@PlanningSolution
@Getter
@Setter
@NoArgsConstructor
public class Schedule {

    @PlanningEntityCollectionProperty
    List<Appointment> appointments;

    @ValueRangeProvider
    List<LocalTime> startTimes;

    @PlanningScore
    HardSoftScore score;

    public Schedule(List<Appointment> appointments, List<LocalTime> startTimes) {
        this.appointments = appointments;
        this.startTimes = startTimes;
    }
}
