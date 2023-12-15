package com.minimal.minimalTodo.repository;

import org.springframework.stereotype.Repository;

import com.minimal.minimalTodo.model.Tasks;
import com.minimal.minimalTodo.model.Users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer>{
    List<Tasks> findByUser(Users user);
}
