package com.softserve.task4.orm.classes;

import com.softserve.task4.orm.client.MySQLConnector;
import com.softserve.task4.orm.queries.SelectColumns;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class Main {
    public static void main(String[] args) throws Exception {
       MySQLConnector client = new MySQLConnector();
     //   MySQLConnector.connect();
     //  MySQLConnector.createTable();
       /* User user = new User();
        user.name = "Jack";
        user.password = "123321";
        user.email = "jumper-2017@mail.ru";*/
        //client.insertValue(user);
   //     client.updateValue(user,new String[]{"user_name"},"user_id=2",new Object[]{"Bogdan"});
        SelectColumns selectColumns = new SelectColumns();
        selectColumns.findByCondition(User.class,"user_name","user_id",1);

    }
}
