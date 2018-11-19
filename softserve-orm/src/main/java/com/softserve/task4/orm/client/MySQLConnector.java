package com.softserve.task4.orm.client;

import com.softserve.task4.orm.processes.CreateTable;
import com.softserve.task4.orm.processes.InsertColumns;
import com.softserve.task4.orm.processes.UpdateColumns;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class MySQLConnector {

    private Connection connection;
    private Statement statement;

    public MySQLConnector() {
    }

    public Connection connect() throws Exception {
        Properties properties = new Properties();
        FileInputStream stream = new FileInputStream("C:\\Gradle\\gradle-4.8.1\\softserve-orm\\src\\main\\resources\\database.properties");
        properties.load(stream);
        stream.close();
        Class.forName(properties.getProperty("jdbc.driver")).getDeclaredConstructor().newInstance();

        connection = java.sql.DriverManager.getConnection(properties.getProperty("jdbc.url"),
                                                          properties.getProperty("jdbc.username"),
                                                          properties.getProperty("jdbc.password"));

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

    public void updateValue(Object object, String[] updateColumnNames, String whereExpr, Object[] whereValues) throws Exception {
        new UpdateColumns(connection,object,updateColumnNames,whereExpr,whereValues).toPreparedStatement().executeUpdate();
    }
}
