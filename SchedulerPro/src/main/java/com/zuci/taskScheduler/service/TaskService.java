package com.zuci.taskScheduler.service;

import com.zuci.taskScheduler.model.TaskDetail;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;

public interface TaskService {
    public TaskDetail createTask(TaskDetail taskDetail, HttpServletRequest request);

    public List<TaskDetail> findByDateForSchedule(Date date, HttpServletRequest request);

    public List<TaskDetail> getTaskDetailsByDateAndPriority(Date date, String priority,HttpServletRequest request);

    public  void deleteTaskById(int taskId);

    public TaskDetail getTaskDetailByTasKId(int taskId);

    public TaskDetail updateTaskByTaskId(TaskDetail taskDetail);

    public List<TaskDetail> getAllTaskByUserName(HttpServletRequest request);
}
