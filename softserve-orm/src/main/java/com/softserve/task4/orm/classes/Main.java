package com.softserve.task4.orm.classes;

import com.softserve.task4.orm.client.MySQLConnector;
import com.softserve.task4.orm.processes.UpdateColumns;

import java.sql.Connection;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        MySQLConnector client = new MySQLConnector();
        Connection connection = client.connect();

        User user = new User();
        user.name = "Alexander";
        user.password = "admin";
        user.email = "admin-shop@mail.ru";
        //client.insertValue(user);
        String query3 = new UpdateColumns(connection, user,
                new String[] { "user_name"},
                "user_id=6", new Object[] {"Bogdan"}).toSQLString();

        client.updateValue( user,
                new String[] {"user_name","user_password","user_email"},
                "user_id=6", new Object[] {"Alexander","adminadmin","jumper-2017@mail.ru"});
        System.out.println(query3);
        client.disconnect();
    }
}
