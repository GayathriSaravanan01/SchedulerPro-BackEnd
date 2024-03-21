package com.zuci.taskScheduler.controller;

import com.zuci.taskScheduler.model.TaskDetail;
import com.zuci.taskScheduler.service.JwtUtilityService;
import com.zuci.taskScheduler.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private JwtUtilityService jwtUtilityService;
    @PostMapping("/task")
    public ResponseEntity<String> createTask(@RequestBody TaskDetail taskDetail, HttpServletRequest request){
        TaskDetail createdTask = taskService.createTask(taskDetail, request);
        if (createdTask != null) {
            return ResponseEntity.ok("Task Added successfully");
        } else {
            return ResponseEntity.badRequest().body("Task cannot be scheduled because it conflicts with an existing task.");
        }
    }
    @GetMapping("/task/{taskId}")
    public TaskDetail getTaskDetailByTasKId(@PathVariable("taskId") int taskId){
        return taskService.getTaskDetailByTasKId(taskId);
    }
    @GetMapping("/task")
    public List<TaskDetail> getAllTaskByUserName(HttpServletRequest request){
        return taskService.getAllTaskByUserName(request);
    }
    @GetMapping("/task/date/{dateForSchedule}")
    public List<TaskDetail> getTaskDetailsByDate(@PathVariable("dateForSchedule") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateForSchedule, HttpServletRequest request){
        return taskService.findByDateForSchedule(dateForSchedule,request);
    }
    @GetMapping("/task/{dateForSchedule}/{priority}")
    public List<TaskDetail> getTaskDetailsByDateAndPriority(@PathVariable("dateForSchedule") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateForSchedule,@PathVariable("priority") String priority,HttpServletRequest request){
        return taskService.getTaskDetailsByDateAndPriority(dateForSchedule,priority,request);
    }
    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<String> deleteTaskById(@PathVariable("taskId") int taskId){
        taskService.deleteTaskById(taskId);
        return ResponseEntity.ok("Task Deleted Successfully");
    }
    @PutMapping("/task")
    public ResponseEntity<?> updateTaskById(@RequestBody TaskDetail taskDetail) {
        TaskDetail updatedTask = taskService.updateTaskByTaskId(taskDetail);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
