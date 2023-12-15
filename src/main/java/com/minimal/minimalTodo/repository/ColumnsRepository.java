package com.minimal.minimalTodo.repository;

import org.springframework.stereotype.Repository;
import com.minimal.minimalTodo.model.Columns;
import org.springframework.data.jpa.repository.JpaRepository;
import com.minimal.minimalTodo.model.Users;
import java.util.List;

@Repository
public interface ColumnsRepository extends JpaRepository<Columns, Integer>{
    List<Columns> findByUser(Users user);
}
