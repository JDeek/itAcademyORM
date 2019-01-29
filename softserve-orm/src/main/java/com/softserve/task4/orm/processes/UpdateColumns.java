package com.softserve.task4.orm.processes;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class UpdateColumns {
    private Map<ColumnProcessor, Object> valueMap = new HashMap<>();
    private Map<ColumnProcessor, Integer> indexMap = new HashMap<>();

    private Connection connection;
    private Object object;
    private Class clazz;
    private String _tableName;
    private int initIndex = 0;

    private Set<String> updateColumnNameSet = new HashSet<>();
    private String whereExpr;
    private Object[] whereValues;

    public UpdateColumns(Connection connection, Object object, String[] updateColumnNames, String whereExpr, Object[] whereValues, String tableName) {
        this.connection = connection;
        this.object = object;
        clazz = object.getClass();
        _tableName = tableName;

        for (String name : updateColumnNames) {
            updateColumnNameSet.add(name);
        }

        this.whereExpr = whereExpr;
        this.whereValues = whereValues;
    }

    public UpdateColumns(Connection connection, Object object, String[] updateColumnNames, String whereExpr, Object[] whereValues) {
        this(connection, object, updateColumnNames, whereExpr, whereValues, null);
    }

    private String generateUpdateQuery() throws Exception {
        String tableName = (_tableName == null) || (_tableName.isEmpty()) ? TableProcessor.getTableName(clazz) : _tableName;

        List<String> setValueStringList = new ArrayList<>();
        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if ((Modifier.isPublic(modifiers)) && (!Modifier.isStatic(modifiers)))
            {
                ColumnProcessor column = ColumnProcessor.fromField(field);
                if ((column != null) &&
                        (updateColumnNameSet.contains(column.name))) {
                    Object  value = field.get(object);
                    if ((column.isNotNull) && (value == null)) {
                        throw new Exception();
                    }

                    String setValueString = Const.STRING_BEFORE_QUOTE + column.name + Const.STRING_AFTER_QUOTE +
                            Const.STRING_ASSIGN + (value != null ? Const.STRING_VAR : Const.STRING_NULL);

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


        String header = Const.STRING_UPDATE + Const.STRING_BEFORE_QUOTE + tableName + Const.STRING_AFTER_QUOTE + Const.STRING_SET;

        String result = header + StringUtils.join(setValueStringList, Const.STRING_GLUE) + Const.STRING_WHERE + whereExpr + Const.STRING_QUERY_END;

        System.out.println(result);
        return result;
    }

    public PreparedStatement toPreparedStatement() throws Exception {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(generateUpdateQuery());
            for (ColumnProcessor column : valueMap.keySet()) {
                Object object = valueMap.get(column);
                preparedStatement.setObject(indexMap.get(column), object);
            }
            int whereValIndex = 0;
                for (Object value : whereValues) {
                    if ((value != null) && ((value.getClass() == Character.TYPE) || (value.getClass() == Character.class))) {
                        preparedStatement.setObject(++whereValIndex, String.valueOf(value));
                    } else {
                        preparedStatement.setObject(++whereValIndex, value);
                    }
                }

            return preparedStatement;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    public String toSQLString() throws Exception {
        return toPreparedStatement().toString();
    }
}
