package com.zuci.taskScheduler.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int taskId;
    @Temporal(TemporalType.DATE)
    private Date dateForSchedule;
    private String taskName;
    private String username;
    private String priority;
    @Column(columnDefinition = "TIME")
    @JsonFormat(pattern = "HH:mm", timezone = "UTC")
    private LocalTime startTime;
    @Column(columnDefinition = "TIME")
    @JsonFormat(pattern = "HH:mm", timezone = "UTC")
    private LocalTime endTime;
}
