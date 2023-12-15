package com.minimal.minimalTodo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.minimal.minimalTodo.model.Tasks;
import com.minimal.minimalTodo.model.Users;
import com.minimal.minimalTodo.repository.TasksRepository;
import com.minimal.minimalTodo.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class TasksController {
    
    private final TasksRepository taskRepo;
    private final UsersRepository usersRepo;

    @Autowired
    public TasksController(TasksRepository taskRepo, UsersRepository usersRepo) {
        this.taskRepo = taskRepo;
        this.usersRepo = usersRepo;
    }

    // Return the list of tasks stored in the H2 database
    @GetMapping("/tasks")
    public ResponseEntity<List<Tasks>> getAllTasks() {
        try {
            List<Tasks> taskList = new ArrayList<>();
            taskRepo.findAll().forEach(taskList::add);

            // If there are no tasks in the database
            //  return NO_CONTENT
            if (taskList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // Else return 200
            return new ResponseEntity<>(taskList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/getTasksByName/{username}")
    public ResponseEntity<List<Tasks>> getTasksByName(@PathVariable String username) {
        try {
            // Find the user by username
            Optional<Users> user = usersRepo.findByUserName(username);
    
            if (user.isPresent()) {
                // Retrieve columns associated with the user
                List<Tasks> foundTasks = taskRepo.findByUser(user.get());
    
                if (foundTasks.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }

                System.out.println(foundTasks);
    
                return new ResponseEntity<>(foundTasks, HttpStatus.OK);
            } else {
                // User not found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addTask")
    public ResponseEntity<Tasks> addTask(@RequestBody Tasks task) {
        Tasks taskObj = taskRepo.save(task);

        return new ResponseEntity<>(taskObj, HttpStatus.OK);
    }

    @PostMapping("/updateTaskById/{id}")
    public ResponseEntity<Tasks> updateTaskById(@PathVariable Integer id, @RequestBody Tasks newTaskData) {

        // Retrieve the column using its id
        Optional<Tasks> oldTaskData = taskRepo.findById(id);

        // If the column is found by its id
        if (oldTaskData.isPresent()) {

            // Create a clone of the column and store the new details inside of the copy
            Tasks updateTaskData = oldTaskData.get();
            updateTaskData.setColumnId(newTaskData.getColumnId());
            updateTaskData.setContent(newTaskData.getContent());
            updateTaskData.setBackgroundColor(newTaskData.getbackgroundColor());
            updateTaskData.setIsNew(newTaskData.getIsNew());

            // Create a new columns object and save it to the repo
            Tasks taskObj = taskRepo.save(updateTaskData);
            return new ResponseEntity<>(taskObj, HttpStatus.OK);
        }

        // Else return 404
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/updateTasks")
    public ResponseEntity<List<Tasks>> updateTasks(@RequestBody Tasks[] updatedTaskData) {

        List<Tasks> updatedTasks = new ArrayList<>();

        for (Tasks tasks : updatedTaskData) {

            Optional<Tasks> existingTasks = taskRepo.findById(tasks.getId());

            if (existingTasks.isPresent()) {
                // Create a copy of the old column and update it with the
                //  new information
                Tasks existing = existingTasks.get();
                existing.setContent(tasks.getContent());
                existing.setColumnId(tasks.getColumnId());
                existing.setBackgroundColor(tasks.getbackgroundColor());
                existing.setIsNew(tasks.getIsNew());

                // Save to the repo
                updatedTasks.add(taskRepo.save(existing));
            } else {
                // Else return 404
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        // Return 200
        return new ResponseEntity<>(updatedTasks, HttpStatus.OK);

    }

    @DeleteMapping("/deleteTaskById/{id}")
    public ResponseEntity<Tasks> deleteTaskById(@PathVariable Integer id) {

        // If the column to be deleted is found by its id
        if (taskRepo.findById(id) != null) {
            taskRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        
        // Else return 404
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }

}
