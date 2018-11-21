package com.softserve.task4.orm.queries;

import com.softserve.task4.orm.processes.CheckAnnotatedClass;
import com.softserve.task4.orm.client.MySQLConnector;
import com.softserve.task4.orm.processes.ColumnProcessor;
import com.softserve.task4.orm.processes.TableProcessor;
import com.softserve.task4.orm.processes.constantsExpressions.Const;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class CreateTable {
    private static List<Object> defaults = new ArrayList<>();
    private static String table;
    private static Class<?> clzz;


    public CreateTable() {
    }

    public CreateTable(Class<?> clzz){
        CreateTable.clzz = clzz;
    }

    public static void createTables(){
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("C:\\Gradle\\gradle-4.8.1\\softserve-orm\\src\\main\\resources\\database.properties"));
            String pckg = properties.getProperty("models.package");
            createTablesFromPackage(pckg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Class<?> createTablesFromPackage(String pckg) throws Exception {
        Set<Class<?>> annotatedTableClass = CheckAnnotatedClass.getEntityAnnotatedClasses(pckg);
        //MySQLConnector client= new MySQLConnector();
        Connection connection = MySQLConnector.connect();
        for (Class<?> clss : annotatedTableClass){
            generateCreateTableQuery(clss,connection);
            clzz = clss;
        }
        return clzz;
    }
    private static void generateCreateTableQuery(Class<?> clss, Connection connection) throws Exception {
        String tableName = table == null || table.isEmpty() ? TableProcessor.getTableName(clss) : table;

        List<ColumnProcessor> columnProcessors = new ArrayList<>();
        Field[] fields = clss.getFields();

        for (Field field : fields){
            int modificators = field.getModifiers();
            if (Modifier.isPublic(modificators) && !Modifier.isStatic(modificators)){
                ColumnProcessor columnProcessor = ColumnProcessor.fromField(field);
                if (columnProcessor != null){
                    columnProcessors.add(columnProcessor);
                    if (columnProcessor.hasDefaultValue){
                        defaults.add(columnProcessor.getDefaultValue());
                    }
                }
            }
        }

        String header = Const.STRING_CREATE_TABLE + Const.STRING_BEFORE_QUOTE + tableName + Const.STRING_AFTER_QUOTE+
                "(\n";

        int primaryKeys = 0;
        String primaryKey = "";

        List<String> columnStringList = new ArrayList<>();

        for (ColumnProcessor columnProcessor : columnProcessors){
            if (columnProcessor.isPrimaryKey || columnProcessor.isAutoIncrement){
                primaryKeys ++;
                primaryKey = columnProcessor.name;
            }
            columnStringList.add(columnProcessor.toString());
        }

        if (primaryKeys > 1) throw new Exception();
        String footer;
        if (primaryKeys == 1){
            footer = Const.STRING_PRIMARY_KEY +"`" +primaryKey + "`" + "))";
        }else {
            footer = ")";
        }

        String resultQuery = header + StringUtils.join(columnStringList,", \n") +","+ footer + Const.STRING_QUERY_END;
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(resultQuery);
        int index= 0;

        for (Object defaultValue : defaults){
            preparedStatement.setObject(++index, defaultValue);
        }
        preparedStatement.execute();
        System.out.println(preparedStatement.toString());
/*
        return header + StringUtils.join(columnStringList,", \n") +","+ footer + Const.STRING_QUERY_END;
*/
    }

    /*public PreparedStatement toPrepared() throws Exception {
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(generateCreateTableQuery(clzz));
            int index= 0;

            for (Object defaultValue : defaults){
                preparedStatement.setObject(++index, defaultValue);
            }
            preparedStatement.execute();
            System.out.println(preparedStatement.toString());
            return preparedStatement;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception();
        }
    }*/

 /*   public String toSQLString() throws Exception{
        return toPrepared().toString();
    }*/
}
