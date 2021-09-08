package com.example.assignment_1_study_app.ui.timetable;

public class TimetableSession {
    private Long id;
    private String title;
    private Integer start_hour;
    private Integer start_min;
    private Integer end_hour;
    private Integer end_min;
    private Integer day;

    public TimetableSession (Long id, String title, Integer start_hour, Integer start_min, Integer end_hour, Integer end_min, Integer day) {
        this.id = id;
        this.title = title;
        this.start_hour = start_hour;
        this.start_min = start_min;
        this.end_hour = end_hour;
        this.end_min = end_min;
        this.day = day;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public Integer getStartHour() {
        return this.start_hour;
    }

    public Integer getStartMin() {
        return this.start_min;
    }

    public Integer getEndHour() {
        return this.end_hour;
    }

    public Integer getEndMin() {
        return this.end_min;
    }

    public Integer getDay() {
        return this.day;
    }

    public String getTime() {
        return prependInt(start_hour)+":"+prependInt(start_min)+" - "+prependInt(end_hour)+":"+prependInt(end_min);
    }

    public String prependInt(Integer value) {
        if (value < 10) {
            return "0" + value.toString();
        } else {
            return value.toString();
        }
    }
}
