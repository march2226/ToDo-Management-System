package com.example.task.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.task.data.entity.Tasks;

public interface TasksRepository extends JpaRepository<Tasks, Integer> {

}