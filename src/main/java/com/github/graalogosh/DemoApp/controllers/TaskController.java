package com.github.graalogosh.DemoApp.controllers;

import com.github.graalogosh.DemoApp.models.Task;
import com.github.graalogosh.DemoApp.models.TaskStatus;
import com.github.graalogosh.DemoApp.repositories.TaskRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Anton Mukovozov (graalogosh@gmail.com) on 18.08.2018.
 */
@RestController
@RequestMapping(value = "/task", produces = "application/json")
public class TaskController {
    TaskRepository taskRepository;
    Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Map> createTask() {
        Task task = new Task();
        task = taskRepository.save(task);
        Task finalTask = task;
        new Thread(() -> {
            processTask(finalTask);
        }).start();
        return new ResponseEntity<>(Collections.singletonMap("uuid", task.getUuid()), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Task> getTask(
            @PathVariable(name = "id") String uuid
    ) {
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task task = taskRepository.findByUuid(uuid);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    private void processTask(@NonNull Task task) {
        task.setStatus(TaskStatus.RUNNING);
        taskRepository.save(task);
        logger.info("Task " + task.getId() + " starts processing in " + new Date());

        try {
            Thread.sleep(1000 * 60 * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        task.setStatus(TaskStatus.FINISHED);
        taskRepository.save(task);

        logger.info("Task " + task.getId() + " finish processing in " + new Date());

    }
}
