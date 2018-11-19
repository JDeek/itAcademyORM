package com.softserve.task4.orm.processes;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class InsertColumns {

    private Map<ColumnProcessor,Object> valueMap = new HashMap<>();
    private Map<ColumnProcessor,Integer> indexMap = new HashMap<>();

    public static int INSERT_DEFAULT = 0;
    public static int INSERT_REPLACE = 1;
    public static int INSERT_IGNORE = 2;
    private Connection connection;
    private Object object;
    private Class clss;
    private int initIndex = 0;

    private int insertType;
    private String table;

    private Map<String,String> functionValueMap = new HashMap<>();

    public InsertColumns(Connection connection, Object object){
        this.connection = connection;
        this.object = object;
        clss = object.getClass();
        this.insertType = INSERT_DEFAULT;
    }

    public InsertColumns setTableName(String name){
        this.table = name;
        return  this;
    }

private String generateInsertQuery() throws Exception{
    String tableName = (table == null) || (table.isEmpty()) ? TableProcessor.getTableName(clss) : table;

    List<String> setValueStringList = new ArrayList<>();
    Field[] fields = clss.getFields();

    for (Field field : fields) {
        int modifiers = field.getModifiers();
        if ((Modifier.isPublic(modifiers)) && (!Modifier.isStatic(modifiers))) {

            ColumnProcessor column = ColumnProcessor.fromField(field);
            if ((column != null) && (!(column.isAutoIncrement && insertType==INSERT_DEFAULT))) {
                if (functionValueMap.containsKey(column.name)) {
                    String setValueString = "`" + column.name + "`" + "=" + functionValueMap.get(column.name);

                    setValueStringList.add(setValueString);
                } else {
                    Object value = field.get(object);

                    if ((value != null) || (!column.hasDefaultValue)) {
                        if ((column.isNotNull) && (value == null)) {
                            throw new Exception();
                        }

                        String setValueString = Const.STRING_BEFORE_QUOTE + column.name + Const.STRING_AFTER_QUOTE
                                + Const.STRING_ASSIGN + (value != null ? Const.STRING_VAR : Const.STRING_NULL);

                        setValueStringList.add(setValueString);

                        if (value != null) {
                            if ((value.getClass() == Character.TYPE) || (value.getClass() == Character.class)) {
                                valueMap.put(column, String.valueOf(value));
                            } else {
                                valueMap.put(column, value);
                            }
                            indexMap.put(column, ++initIndex);
                        }
                    }
                }
            }
        }
    }
    boolean insertReplaceSet = (insertType == INSERT_REPLACE);

    boolean insertIgnoreSet = (insertType == INSERT_IGNORE);

    String header = (insertReplaceSet ? Const.STRING_REPLACE : Const.STRING_INSERT) +
            (insertIgnoreSet ? Const.STRING_IGNORE : "") + Const.STRING_INTO +
            Const.STRING_BEFORE_QUOTE + tableName + Const.STRING_AFTER_QUOTE + Const.STRING_SET;



    String result = header + StringUtils.join(setValueStringList, Const.STRING_GLUE) + Const.STRING_QUERY_END;
    System.out.println(result);
    return result;
}

    public PreparedStatement toPreparedStatement() throws Exception {
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(generateInsertQuery());

            for (ColumnProcessor column : valueMap.keySet()) {
                Object object = valueMap.get(column);
                preparedStatement.setObject(indexMap.get(column), object);
            }
            return preparedStatement;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    public String toSQLString() throws Exception{
        return toPreparedStatement().toString();
    }
}
