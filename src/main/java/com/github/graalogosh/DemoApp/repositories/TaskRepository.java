package com.github.graalogosh.DemoApp.repositories;

import com.github.graalogosh.DemoApp.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Anton Mukovozov (graalogosh@gmail.com) on 18.08.2018.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByUuid(String uuid);
}
