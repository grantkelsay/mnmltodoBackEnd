package com.minimal.minimalTodo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;

@Table(name = "Tasks")
@Entity
public class Tasks {
    
    @Id
    private Integer id;

    @Column(name = "backgroundColor")
    private String backgroundColor;

    @Column(name = "columndId")
    private int columnId;

    @Column(name = "content")
    private String content;

    @Column(name = "isNew")
    private Boolean isNew;

    @ManyToOne
    @JoinColumn(name = "userName", referencedColumnName = "userName")
    private Users user;

    public Tasks() {
        this.id = 0;
        this.columnId = 0;
        this.content = "";
        this.backgroundColor = "";
        this.isNew = false;
        this.user = new Users();
    }

    public Tasks(String backgroundColor, int columnId, String content, int id, Boolean isNew, Users user) {
        this.id = id;
        this.backgroundColor = backgroundColor;
        this.columnId = columnId;
        this.content = content;
        this.isNew = isNew;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public int getColumnId() {
        return columnId;
    }

    public String getContent() {
        return content;
    }

    public String getbackgroundColor() {
        return backgroundColor;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setUser(Users user) {
        this.user = user;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }
}
