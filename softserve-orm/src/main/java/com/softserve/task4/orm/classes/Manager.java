package com.softserve.task4.orm.classes;

import com.softserve.task4.orm.annotations.AutoIncrement;
import com.softserve.task4.orm.annotations.Column;
import com.softserve.task4.orm.annotations.PrimaryKey;
import com.softserve.task4.orm.annotations.Table;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
@Table("manager")
public class Manager {

    @PrimaryKey
    @AutoIncrement
    @Column("manager_id")
    public int manager_id;
    @Column("manager_name")
    public String manager_name;
    @Column("position")
    public String position;
    @Column("workExperience")
    public String workExperience;

    public Manager() {
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }
}
