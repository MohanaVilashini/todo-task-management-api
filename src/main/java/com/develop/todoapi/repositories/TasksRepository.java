package com.develop.todoapi.repositories;

import com.develop.todoapi.models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, UUID> {

    List<Tasks> findByTaskListUuidAndIsDeleted(UUID taskListUuid, boolean isDeleted);
}
