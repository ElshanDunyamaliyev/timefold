package com.example.timefold.school.service;

import com.example.timefold.school.model.Lesson;
import com.example.timefold.school.model.Room;
import com.example.timefold.school.model.Timeslot;
import com.example.timefold.school.model.Timetable;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class schoolService {

    public static void main(String[] args) {
        Timetable timetable = generate();
        LocalDateTime before = LocalDateTime.now();
        optimizeWithGreedy(timetable);
        LocalDateTime after = LocalDateTime.now();
        Duration duration = Duration.between(before, after);
        System.out.println(duration.toMillis());
        System.out.println(optimizeWithBruteForce(timetable));
    }

    public static long calculateScore(Timetable timeTable) {
        long hardScore = 0;
        for (Lesson lesson1 : timeTable.getLessons()) {
            for (Lesson lesson2 : timeTable.getLessons()) {
                // Room conflict
                if (Objects.equals(lesson1.getRoom(), lesson2.getRoom()) && Objects.equals(lesson1.getTimeslot(), lesson2.getTimeslot())) {
                    hardScore--;
                }
                // Teacher conflict
                if (Objects.equals(lesson1.getTeacher(), lesson2.getTeacher()) && Objects.equals(lesson1.getTimeslot(), lesson2.getTimeslot())) {
                    hardScore--;
                }
                // Student group conflict
                if (Objects.equals(lesson1.getStudentGroup(), lesson2.getStudentGroup()) && Objects.equals(lesson1.getTimeslot(), lesson2.getTimeslot())) {
                    hardScore--;
                }
            }
        }
        return hardScore;
    }

    public static void optimizeWithGreedy(Timetable timeTable) {
        for (Lesson lesson : timeTable.getLessons()) {
            long bestScore = Long.MIN_VALUE;
            Room bestRoom = null;
            Timeslot bestTimeslot = null;

            for (Timeslot timeslot : timeTable.getTimeslots()) {
                lesson.setTimeslot(timeslot);

                for (Room room : timeTable.getRooms()) {
                    lesson.setRoom(room);
                    long score = calculateScore(timeTable);

                    if (score > bestScore) {
                        bestScore = score;
                        bestRoom = room;
                        bestTimeslot = timeslot;
                    }
                }
            }

            lesson.setTimeslot(bestTimeslot);
            lesson.setRoom(bestRoom);
        }
    }

    public static long solveWithBruteForce(Timetable timeTable) {
        long bestScore = Long.MIN_VALUE;
        Timetable bestSolution = new Timetable();

        // Arrays to track room and timeslot for each lesson
        List<Lesson> lessons = timeTable.getLessons();
//        System.out.println(timeTable);
        List<Timeslot> timeslots = timeTable.getTimeslots();
        List<Room> rooms = timeTable.getRooms();

        // Create indices for brute-force looping
        int totalCombinations = (int) Math.pow(timeslots.size() * rooms.size(),lessons.size());

        // Try all combinations of rooms and timeslots for all lessons
        for (int i = 0; i < totalCombinations; i++) {
            // Set room and timeslot for each lesson
            int combinationIndex = i;
            for (Lesson lesson : lessons) {
                // Assign timeslot and room for this lesson
                Timeslot timeslot = timeslots.get(combinationIndex % timeslots.size());
                Room room = rooms.get((combinationIndex / timeslots.size()) % rooms.size());

                lesson.setTimeslot(timeslot);
                lesson.setRoom(room);

                combinationIndex /= timeslots.size() * rooms.size();
            }

            // Calculate score after assigning rooms and timeslots for all lessons
            long score = calculateScore(timeTable);
            if (score > bestScore) {
                bestScore = score;
//                bestSolution = (Timetable) timeTable.clone();
            }
        }

        System.out.println(timeTable);
        System.out.println("salam");
        System.out.println(bestSolution);
        return bestScore;
    }

    public static Timetable optimizeWithBruteForce(Timetable timeTable) {
        long bestScore = Long.MIN_VALUE;
        Timetable bestSolution = null;

        for (var timeslot: timeTable.getTimeslots()){
            for (var lesson : timeTable.getLessons()){
                for (var room : timeTable.getRooms()){
                    lesson.setRoom(room);
                    lesson.setTimeslot(timeslot);
                }
            }
            long score = calculateScore(timeTable);
            if (score > bestScore) {
                bestScore = score;
            }

        }

//        // Nested loops to try all possible combinations of timeslots and rooms for lessons
//        for (Timeslot timeslotLesson1 : timeTable.getTimeslots()) {
//            Lesson lesson1 = timeTable.getLesson(0); // Assuming lesson 1 is at index 0
//            for (Room roomLesson1 : timeTable.getRooms()) {
//                lesson1.setTimeslot(timeslotLesson1);
//                lesson1.setRoom(roomLesson1);
//
//                for (Timeslot timeslotLesson2 : timeTable.getTimeslots()) {
//                    Lesson lesson2 = timeTable.getLesson(1); // Assuming lesson 2 is at index 1
//                    for (Room roomLesson2 : timeTable.getRooms()) {
//                        lesson2.setTimeslot(timeslotLesson2);
//                        lesson2.setRoom(roomLesson2);
//
//                        // Continue for additional lessons (up to N lessons)
//                        // For simplicity, we'll assume N lessons are indexed from 0 to N-1
//                        for (Timeslot timeslotLessonN : timeTable.getTimeslots()) {
//                            Lesson lessonN = timeTable.getLesson(N); // For Nth lesson
//                            for (Room roomLessonN : timeTable.getRooms()) {
//                                lessonN.setTimeslot(timeslotLessonN);
//                                lessonN.setRoom(roomLessonN);
//
//                                // After setting timeslots and rooms for all lessons, calculate the score
//                                long score = calculateScore(timeTable);
//
//                                // If the score is better than the current best score, update the best score and solution
//                                if (score > bestScore) {
//                                    bestScore = score;
//                                    bestSolution = timeTable.clone(); // Assuming clone() deep copies the timetable
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }

        return bestSolution;
    }




    public static Timetable generate() {
        List<Timeslot> timeslots = new ArrayList<>(10);
        long nextTimeslotId = 0L;
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.MONDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.MONDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.MONDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));

        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.TUESDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.TUESDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.TUESDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.TUESDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.TUESDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));

        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.WEDNESDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.WEDNESDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.WEDNESDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.WEDNESDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.WEDNESDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.THURSDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.THURSDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.THURSDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.THURSDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.THURSDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.FRIDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.FRIDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.FRIDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.FRIDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
        timeslots.add(new Timeslot(Long.toString(nextTimeslotId++), DayOfWeek.FRIDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));


        List<Room> rooms = new ArrayList<>(3);
        long nextRoomId = 0L;
        rooms.add(new Room(Long.toString(nextRoomId++), "Room A"));
        rooms.add(new Room(Long.toString(nextRoomId++), "Room B"));
        rooms.add(new Room(Long.toString(nextRoomId++), "Room C"));
        rooms.add(new Room(Long.toString(nextRoomId++), "Room D"));
        rooms.add(new Room(Long.toString(nextRoomId++), "Room E"));
        rooms.add(new Room(Long.toString(nextRoomId++), "Room F"));


        List<Lesson> lessons = new ArrayList<>();
        long nextLessonId = 0L;
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physics", "M. Curie", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Chemistry", "M. Curie", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Biology", "C. Darwin", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "History", "I. Jones", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "I. Jones", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "I. Jones", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Spanish", "P. Cruz", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Spanish", "P. Cruz", "9th grade"));

        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "ICT", "A. Turing", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physics", "M. Curie", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Geography", "C. Darwin", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Geology", "C. Darwin", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "History", "I. Jones", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "I. Jones", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Drama", "I. Jones", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Art", "S. Dali", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Art", "S. Dali", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "9th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "9th grade"));


        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physics", "M. Curie", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Chemistry", "M. Curie", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "French", "M. Curie", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Geography", "C. Darwin", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "History", "I. Jones", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "P. Cruz", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Spanish", "P. Cruz", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "ICT", "A. Turing", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physics", "M. Curie", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Biology", "C. Darwin", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Geology", "C. Darwin", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "History", "I. Jones", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "P. Cruz", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "P. Cruz", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Drama", "I. Jones", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Art", "S. Dali", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Art", "S. Dali", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "10th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "10th grade"));

        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "ICT", "A. Turing", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physics", "M. Curie", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Chemistry", "M. Curie", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "French", "M. Curie", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physics", "M. Curie", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Geography", "C. Darwin", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Biology", "C. Darwin", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Geology", "C. Darwin", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "History", "I. Jones", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "History", "I. Jones", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "P. Cruz", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "P. Cruz", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "P. Cruz", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Spanish", "P. Cruz", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Drama", "P. Cruz", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Art", "S. Dali", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Art", "S. Dali", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "11th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "11th grade"));

        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Math", "A. Turing", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "ICT", "A. Turing", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physics", "M. Curie", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Chemistry", "M. Curie", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "French", "M. Curie", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physics", "M. Curie", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Geography", "C. Darwin", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Biology", "C. Darwin", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Geology", "C. Darwin", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "History", "I. Jones", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "History", "I. Jones", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "P. Cruz", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "P. Cruz", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "English", "P. Cruz", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Spanish", "P. Cruz", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Drama", "P. Cruz", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Art", "S. Dali", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Art", "S. Dali", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "12th grade"));
        lessons.add(new Lesson(Long.toString(nextLessonId++), "Physical education", "C. Lewis", "12th grade"));

        return new Timetable("first", timeslots, rooms, lessons);
    }
}

