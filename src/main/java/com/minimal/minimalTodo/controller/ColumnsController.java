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

import com.minimal.minimalTodo.model.Columns;
import com.minimal.minimalTodo.model.Users;
import com.minimal.minimalTodo.repository.ColumnsRepository;
import com.minimal.minimalTodo.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class ColumnsController {
    
    private final ColumnsRepository colsRepo;
    private final UsersRepository usersRepo;

    @Autowired
    public ColumnsController(ColumnsRepository colsRepo, UsersRepository usersRepo) {
        this.colsRepo = colsRepo;
        this.usersRepo = usersRepo;
    }

    // Return the list of columns stored in the H2 database
    @GetMapping("/columns")
    public ResponseEntity<List<Columns>> getAllColumns() {
        try {
            List<Columns> columnList = new ArrayList<>();
            colsRepo.findAll().forEach(columnList::add);

            // If there are no columns in the database
            //  return NO_CONTENT
            if (columnList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            System.out.println(columnList);
            // Else return 200
            return ResponseEntity.ok(columnList);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/getColumnsByName/{username}")
    public ResponseEntity<List<Columns>> getColumnsByName(@PathVariable String username) {
        try {
            // Find the user by username
            Optional<Users> user = usersRepo.findByUserName(username);
    
            if (user.isPresent()) {
                // Retrieve columns associated with the user
                List<Columns> foundColumns = colsRepo.findByUser(user.get());
    
                if (foundColumns.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
    
                return new ResponseEntity<>(foundColumns, HttpStatus.OK);
            } else {
                // User not found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addColumn")
    public ResponseEntity<Columns> addColumn(@RequestBody Columns column) {
        Columns columnObj = colsRepo.save(column);

        return new ResponseEntity<>(columnObj, HttpStatus.OK);
    }

    @PostMapping("/updateColumnById/{id}")
    public ResponseEntity<Columns> updateColumnById(@PathVariable Integer id, @RequestBody Columns newColumnData) {

        // Retrieve the column using its id
        Optional<Columns> oldColumnData = colsRepo.findById(id);

        // If the column is found by its id
        if (oldColumnData.isPresent()) {

            // Create a clone of the column and store the new details inside of the copy
            Columns updatedColumnData = oldColumnData.get();
            updatedColumnData.setTitle(newColumnData.getTitle());
            updatedColumnData.setIsNew(newColumnData.getIsNew());

            // Create a new columns object and save it to the repo
            Columns columnObj = colsRepo.save(updatedColumnData);
            return new ResponseEntity<>(columnObj, HttpStatus.OK);
        }

        // Else return 404
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/updateColumns")
    public ResponseEntity<List<Columns>> updateColumns(@RequestBody Columns[] updatedColumnData) {

        List<Columns> updatedColumns = new ArrayList<>();

        for (Columns col : updatedColumnData) {

            Optional<Columns> existingColumn = colsRepo.findById(col.getId());

            if (existingColumn.isPresent()) {
                // Create a copy of the old column and update it with the
                //  new information
                Columns existing = existingColumn.get();
                existing.setTitle(col.getTitle());
                existing.setIsNew(col.getIsNew());

                // Save to the repo
                updatedColumns.add(colsRepo.save(existing));
            } else {
                // Else return 404
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        // Return 200
        return new ResponseEntity<>(updatedColumns, HttpStatus.OK);

    }

    @DeleteMapping("/deleteColumnById/{id}")
    public ResponseEntity<Columns> deleteColumnById(@PathVariable Integer id) {

        Optional<Columns> column = colsRepo.findById(id);

        if (column.isPresent()) {
            // Fetch tasks associated with the columnId
            // List<Tasks> tasksToDelete = taskRepo.findByColumn(column.getId());
    
            // // Delete associated tasks
            // taskRepo.deleteAll(tasksToDelete);
    
            // Delete the column
            colsRepo.deleteById(id);
    
            return new ResponseEntity<>(HttpStatus.OK);
        }
    
        // If the column is not found, return 404
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
