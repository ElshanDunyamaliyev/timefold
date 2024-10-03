package com.example.timefold.appointment.controller;

import ai.timefold.solver.core.api.solver.SolverManager;
import com.example.timefold.appointment.model.Appointment;
import com.example.timefold.appointment.model.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class TimeFoldController {

    private final SolverManager<Schedule,String> solverManager;

    @GetMapping("/salam")
    public Schedule getSchedule() throws ExecutionException, InterruptedException {
        var problem = new Schedule(
                List.of(
                        new Appointment("Doctor", Duration.ofHours(1)),
                        new Appointment("Lunch", Duration.ofHours(3)),
                        new Appointment("Barber", Duration.ofMinutes(35))
                ),
                List.of(
                        LocalTime.of(15,0),
                        LocalTime.of(15,30),
                        LocalTime.of(16,0),
                        LocalTime.of(16,30),
                        LocalTime.of(17,0),
                        LocalTime.of(17,30),
                        LocalTime.of(18,0)
                )
        );

        Schedule finalBestSolution = solverManager.solve("salam", problem).getFinalBestSolution();

        return finalBestSolution;
    }
}
