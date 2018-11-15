package com.softserve.task4.orm.classes;

import com.softserve.task4.orm.annotations.*;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */

@Table("users")
public class User {


    @PrimaryKey
    @AutoIncrement
    @Column("user_id")
    public int id;

    @Column("user_name")
    @NotNull
    public String name;

    @Column("user_password")
    @NotNull
    public String password;

    @Column("user_email")
    @NotNull
    public String email;


    public User() {
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
