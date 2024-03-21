package com.zuci.taskScheduler.service;

import com.zuci.taskScheduler.exception.TaskIdNotFoundException;
import com.zuci.taskScheduler.model.SignUp;
import com.zuci.taskScheduler.model.TaskDetail;
import com.zuci.taskScheduler.repository.SignUpRepository;
import com.zuci.taskScheduler.repository.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@EnableScheduling
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private JwtUtilityService jwtUtilityService;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SignUpRepository signUpRepository;
    private String getEmailFromUsername(String username) {
        SignUp signUp = signUpRepository.findUserByUsername(username);
        if (signUp != null) {
            return signUp.getEmailId();
        }
        return null;
    }

    private String getUsernameFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtilityService.extractUsername(token);
        }
        return username;
    }
    @Override
    public TaskDetail createTask(TaskDetail taskDetail, HttpServletRequest request) {
        List<TaskDetail> existingTasks = findByDateForSchedule(taskDetail.getDateForSchedule(), request);
        for (TaskDetail existingTask : existingTasks) {
            if (existingTask.getStartTime().isBefore(taskDetail.getEndTime()) &&
                    existingTask.getEndTime().isAfter(taskDetail.getStartTime())) {
                return null;
            }
        }
        taskDetail.setUsername(getUsernameFromToken(request));
        // No overlap
        return taskRepository.save(taskDetail);
    }

    @Override
    public List<TaskDetail> findByDateForSchedule(Date date, HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        return taskRepository.findByDateForScheduleAndUsername(date, username);
    }

    @Override
    public List<TaskDetail> getTaskDetailsByDateAndPriority(Date date, String priority, HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        return taskRepository.findByDateForScheduleAndPriorityAndUsername(date, priority, username);
    }

    @Override
    public void deleteTaskById(int taskId) {
        if(taskRepository.findById(taskId).isPresent()){
            taskRepository.deleteById(taskId);
        }
        else{
            throw new TaskIdNotFoundException();
        }
    }

    @Override
    public TaskDetail getTaskDetailByTasKId(int taskId) {
        if(taskRepository.findById(taskId).isPresent()) {
            return taskRepository.findById(taskId).get();
        }
        else {
            throw new TaskIdNotFoundException();
        }
    }

    @Override
    public TaskDetail updateTaskByTaskId(TaskDetail taskDetail) {
        if (taskRepository.existsById(taskDetail.getTaskId())) {
            TaskDetail existingTask = taskRepository.findById(taskDetail.getTaskId()).get();
                taskDetail.setUsername(existingTask.getUsername());
                return taskRepository.save(taskDetail);

        }
        else{
                throw new TaskIdNotFoundException();
            }
        }

    @Override
    public List<TaskDetail> getAllTaskByUserName(HttpServletRequest request) {
        return taskRepository.getAllTaskByusername(getUsernameFromToken(request));
    }

    @Scheduled(fixedRate = 60000) // runs every minute
    public void sendTaskNotifications() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate currentDate = currentDateTime.toLocalDate();
        LocalTime notificationTime = currentDateTime.plusMinutes(10).toLocalTime().withSecond(0).withNano(0);
        List<TaskDetail> tasks = taskRepository.findByDateForScheduleAndStartTime(currentDate, notificationTime);
        log.info(String.valueOf(currentDate));
        log.info(String.valueOf(notificationTime));


        // Group tasks by username
        Map<String, List<TaskDetail>> tasksByUsername = tasks.stream()
                .collect(Collectors.groupingBy(TaskDetail::getUsername));

        // get email ID for each username
        Map<String, List<String>> emailIdsByUsername = tasksByUsername.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(task -> getEmailFromUsername(task.getUsername()))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())));

        // Send notification
        emailIdsByUsername.forEach((username, emailIds) -> {
            sendNotificationEmail(emailIds, tasksByUsername.get(username));
        });
    }

    private void sendNotificationEmail(List<String> emailIds, List<TaskDetail> tasks) {
        for (String email : emailIds) {
            log.info(email);
            for (TaskDetail task : tasks) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject("Task Reminder");
                message.setText("Reminder:Your task \"" + task.getTaskName() + "\"  will be starting in 10 minutes. Prepare accordingly.");

                emailSender.send(message);
            }
        }
    }

}



