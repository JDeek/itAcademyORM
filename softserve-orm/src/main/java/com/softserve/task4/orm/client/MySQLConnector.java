package com.softserve.task4.orm.client;

import com.softserve.task4.orm.processes.CreateTable;
import com.softserve.task4.orm.processes.InsertColumns;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class MySQLConnector {

    private String user;
    private String password;
    private String db;
    private Connection connection;
    private Statement statement;

    public MySQLConnector(String db,String user, String password) {
        this.db = db;
        this.user = user;
        this.password = password;
    }

    public Connection connect() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();

        connection = java.sql.DriverManager.getConnection(db,user,password);

        statement = connection.createStatement();
        return connection;
    }

    public void disconnect() throws SQLException{
        statement.close();
        connection.close();
    }

    public void createTable(Class clss) throws Exception {
        new CreateTable(connection,clss).toPrepared().execute();
    }

    public void insertValue(Object object) throws  Exception{
        new InsertColumns(connection,object).toPreparedStatement().executeUpdate();
    }
}
