package ru.homework3.mvc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.homework3.mvc.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {
}
