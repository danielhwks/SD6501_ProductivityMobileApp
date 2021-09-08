package com.example.assignment_1_study_app.ui.timetable;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TimetableSessionUnitTest {
    @Test
    public void testSessionGetters() {
        TimetableSession session = new TimetableSession((long) 1, "Test Session", 9, 30, 11, 00, 2);
        assertEquals(session.getTitle(), "Test Session");
        assertEquals((int) session.getStartHour(), 9);
        assertEquals((int) session.getStartMin(), 30);
        assertEquals((int) session.getEndHour(), 11);
        assertEquals((int) session.getEndMin(), 0);
        assertEquals((int) session.getDay(), 2);
    }

    @Test
    public void testSessionTime() {
        TimetableSession session = new TimetableSession((long) 1, "Test Session", 9, 30, 11, 00, 2);
        assertEquals(session.getTime(), "09:30 - 11:00");
    }
}
