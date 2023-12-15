package com.minimal.minimalTodo.repository;

import org.springframework.stereotype.Repository;
import com.minimal.minimalTodo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>{
    Optional<Users> findByUserName(String username);
    Optional<Users> findById(Integer id);
}
