package com.minimal.minimalTodo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.minimal.minimalTodo.model.Users;
import com.minimal.minimalTodo.repository.UsersRepository;

import java.util.Optional;

@RestController
@CrossOrigin
public class UsersController {
    
    private final UsersRepository userRepo;

    @Autowired
    public UsersController(UsersRepository userRepo) {
        this.userRepo = userRepo;
    }

    // Mapping to validate user credentials
    @PostMapping("/validate-user")
    public ResponseEntity<String> validateUser(@RequestBody Users user) {
        Optional<Users> foundUser = userRepo.findByUserName(user.getUserName());
        
        if (foundUser.isPresent()) {
            
            Users existingUser = foundUser.get();
            System.out.println(existingUser.getUserName());
            System.out.println(existingUser.getUserPass());
            System.out.println(user.getUserName());
            System.out.println(user.getUserPass());
            if (existingUser.getUserPass().equals(user.getUserPass())) {
                // Password matches, user is validated
                return ResponseEntity.ok("User validated");
            } else {
                // Password doesn't match
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } else {
            // User not found in the database
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<Users> addUser(@RequestBody Users user) {
        Users userObj = userRepo.save(user);

        return new ResponseEntity<>(userObj, HttpStatus.OK);
    }

}
