package com.softserve.task4.orm.processes;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class CreateTable {
    private List<Object> defaults = new ArrayList<>();
    private String table;
    private Connection connection;
    private Class clss;
    private int versionOfDb;

    public CreateTable(Connection connection, Class clss){
        this.connection = connection;
        this.clss = clss;
        table = TableProcessor.getTableName(clss);
    }

    public CreateTable setTable(String name){
        table = name;
        return this;
    }

    public CreateTable setVersionOfDB(int versionOfDb){
        this.versionOfDb = versionOfDb;
        return this;
    }

    private String generateCreateTableQuery() throws Exception {
        String tableName = table == null || table.isEmpty() ? TableProcessor.getTableName(clss) : table;

        List<ColumnProcessor> columnProcessors = new ArrayList<ColumnProcessor>();
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

        List<String> columnStringList = new ArrayList<String>();

        for (ColumnProcessor columnProcessor : columnProcessors){
            if (columnProcessor.isPrimaryKey || columnProcessor.isAutoIncrement){
                primaryKeys ++;
                primaryKey = columnProcessor.name;
            }
            columnStringList.add(columnProcessor.toString());
        }

        if (primaryKeys > 1) throw new Exception();
        String footer = "";
        if (primaryKeys == 1){
            footer = Const.STRING_PRIMARY_KEY +"`" +primaryKey + "`" + "))";
        }else {
            footer = ")";
        }

        return header + StringUtils.join(columnStringList,", \n") +","+ footer + Const.STRING_QUERY_END;
    }

    public PreparedStatement toPrepared() throws Exception {
        try {
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(generateCreateTableQuery());
            int index= 0;

            for (Object defaultValue : defaults){
                preparedStatement.setObject(++index, defaultValue);
            }
            return preparedStatement;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception();
        }
    }

    public String toSQLString() throws Exception{
        return toPrepared().toString();
    }
}
