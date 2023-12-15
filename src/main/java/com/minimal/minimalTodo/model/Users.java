package com.minimal.minimalTodo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

@Table(name = "Users")
@Entity
public class Users {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Integer id;

    @Id
    @Column(name = "userName", unique = true, nullable = false)
    private String userName;

    @Column(name = "userPass", nullable = false)
    private String userPass;

    public Users() {
        this.userName = "";
        this.userPass = "";
    }

    public Users(String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;
    }

    // public int getUserId() {
    //     return this.id;
    // }

    public String getUserName() {
        return this.userName;
    }

    public String getUserPass() {
        return this.userPass;
    }

    // public void setUserId(int id) {
    //     this.id = id;
    // }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
