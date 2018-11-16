package com.softserve.task4.orm.classes;

import com.softserve.task4.orm.client.MySQLConnector;
import com.softserve.task4.orm.processes.CreateTable;
import com.softserve.task4.orm.processes.InsertColumns;

import java.sql.Connection;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        MySQLConnector client = new MySQLConnector();
        Connection connection = client.connect();
        String query = new CreateTable(connection,User.class).setTable("users").toSQLString();
            System.out.println(query);
            client.createTable(User.class);
            User user = new User();
            user.name = "Al";
            user.email = "gm@com.ua";
            user.password = "2122231";
            String query1 = new InsertColumns(connection,User.class).setTableName("users").toSQLString();
            System.out.println(query1);
            client.insertValue(user);
            client.disconnect();
    }
}
