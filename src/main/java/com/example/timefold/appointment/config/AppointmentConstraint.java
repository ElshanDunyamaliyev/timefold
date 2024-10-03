package com.example.timefold.appointment.config;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import com.example.timefold.appointment.model.Appointment;

import java.time.Duration;
import java.time.LocalTime;

import static ai.timefold.solver.core.api.score.stream.Joiners.overlapping;

public class AppointmentConstraint implements ConstraintProvider {


    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
//                overlappingTime(constraintFactory),
//                preferredTimeForCertainAppointments(constraintFactory),
//                breakBetweenAppointments(constraintFactory)
        };
    }

    private Constraint overlappingTime(ConstraintFactory constraintFactory) {

        return constraintFactory
                .forEachUniquePair(Appointment.class,overlapping(Appointment::getStartTime,Appointment::getEndTime))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("overLappingTimeConstraint");
    }

    private Constraint preferredTimeForCertainAppointments(ConstraintFactory constraintFactory) {
        LocalTime preferredStartTime = LocalTime.of(15, 10);
        LocalTime preferredEndTime = LocalTime.of(16, 30);

        return constraintFactory
                .forEach(Appointment.class)
                .filter(appointment -> appointment.getName().equals("Doctor") &&
                        (appointment.getStartTime().isBefore(preferredStartTime) ||
                                appointment.getEndTime().isAfter(preferredEndTime)))
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("preferredTimeForDoctorConstraint");
    }

    private Constraint breakBetweenAppointments(ConstraintFactory constraintFactory) {
        Duration breakDuration = Duration.ofMinutes(25);

        return constraintFactory
                .forEachUniquePair(Appointment.class)
                .filter((appointment1, appointment2) ->
                        appointment1.getEndTime().isAfter(appointment2.getStartTime().minus(breakDuration)) &&
                                appointment1.getStartTime().isBefore(appointment2.getEndTime().plus(breakDuration))
                )
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("breakBetweenAppointmentsConstraint");
    }

}
