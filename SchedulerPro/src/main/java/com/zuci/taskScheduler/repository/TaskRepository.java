package com.zuci.taskScheduler.repository;

import com.zuci.taskScheduler.model.TaskDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskDetail,Integer> {

    @Query("SELECT t FROM TaskDetail t WHERE t.dateForSchedule = :date AND t.username = :username ORDER BY t.dateForSchedule ASC, t.startTime ASC")
    public List<TaskDetail> findByDateForScheduleAndUsername(Date date, String username);


    @Query("SELECT t FROM TaskDetail t " +"WHERE t.dateForSchedule = :date " + "AND t.priority = :priority " + "AND t.username = :username " + "ORDER BY t.dateForSchedule ASC, t.startTime ASC")
    public List<TaskDetail> findByDateForScheduleAndPriorityAndUsername(Date date, String priority, String username);


    List<TaskDetail> findByDateForScheduleAndStartTime(LocalDate currentDate, LocalTime notificationTime);

    @Query("SELECT t FROM TaskDetail t WHERE t.username = :username ORDER BY t.dateForSchedule ASC, t.startTime ASC")
    List<TaskDetail> getAllTaskByusername(@Param("username") String username);
}
