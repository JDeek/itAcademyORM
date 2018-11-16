package com.softserve.task4.orm.processes;

import com.softserve.task4.orm.annotations.*;

import java.lang.reflect.Field;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class ColumnProcessor {

    public String name;
    public ColumnType columnType;
    public boolean isAutoIncrement;
    public boolean isPrimaryKey;
    public boolean isNotNull;
    public boolean hasDefaultValue;
    public String defaultValue;

    public ColumnProcessor() {
    }

    public int maxLength = 256;

    public static ColumnProcessor fromField(Field field){
        return fromField(field,"#");
    }

    public static ColumnProcessor fromField(Field field, String vars){
        ColumnType columnType = checkFieldType(field);

        ColumnProcessor columnProcessor = new ColumnProcessor();
        columnProcessor.columnType = columnType;
        columnProcessor.name = field.getName();
        columnProcessor.isAutoIncrement = field.isAnnotationPresent(AutoIncrement.class);
        columnProcessor.isNotNull = field.isAnnotationPresent(NotNull.class);
        columnProcessor.isPrimaryKey = field.isAnnotationPresent(PrimaryKey.class);
        columnProcessor.hasDefaultValue = field.isAnnotationPresent(Default.class);

        columnProcessor = checkAnnotation(field, columnProcessor);

        return columnProcessor;
    }

    public Object getDefaultValue(){
        if (!hasDefaultValue) return null;
        switch (columnType){
            case BOOL:
                return Boolean.parseBoolean(defaultValue);
            case INT:
                return Integer.parseInt(defaultValue);
            case BIGINT:
                return Long.parseLong(defaultValue);
            case FLOAT:
                return Float.parseFloat(defaultValue);
            case DOUBLE:
                return Double.parseDouble(defaultValue);
            case CHAR:
            case VARCHAR:
            case TEXT:
                return defaultValue;
        }
        return null;
    }

    private static ColumnType checkFieldType(Field field){
        Class fieldType = field.getType();
        ColumnType columnType;

        if (fieldType == Boolean.TYPE || (fieldType == Boolean.class)){
            columnType = ColumnType.BOOL;
        }else if ((fieldType == Integer.TYPE )||(fieldType == Integer.class)){
            columnType = ColumnType.INT;
        }else if (fieldType == Double.TYPE || fieldType == Double.class){
            columnType = ColumnType.DOUBLE;
        }else if (fieldType == Float.TYPE || fieldType == Float.class){
            columnType = ColumnType.FLOAT;
        }else if (fieldType == Character.TYPE || fieldType == Character.class){
            columnType =ColumnType.CHAR;
        }else if (fieldType == String.class){
            columnType = ColumnType.VARCHAR;
        }else {
            return null;
        }
        return  columnType;
    }

    private static ColumnProcessor checkAnnotation(Field field, ColumnProcessor columnProcessor){
        ColumnType columnType = checkFieldType(field);
        if (columnProcessor.hasDefaultValue){
            Default defaultAnnotation = field.getAnnotation(Default.class);
            columnProcessor.defaultValue = defaultAnnotation.value();
        }
        columnProcessor.maxLength = TableProcessor.getDefaultMaxLength(field.getDeclaringClass());
        if (field.isAnnotationPresent(MaxLength.class)){
            MaxLength maxLengthAnnotation = field.getAnnotation(MaxLength.class);
            columnProcessor.maxLength = maxLengthAnnotation.value();
        }
        if (columnType == ColumnType.VARCHAR && columnProcessor.maxLength == Integer.MAX_VALUE){
            columnProcessor.columnType = ColumnType.TEXT;
        }
        if (field.isAnnotationPresent(com.softserve.task4.orm.annotations.Column.class)){
            com.softserve.task4.orm.annotations.Column columnName = field.getAnnotation(com.softserve.task4.orm.annotations.Column.class);
            String name = columnName.value();
            if (!name.trim().isEmpty()){
                columnProcessor.name = name;
            }
        }
        return  columnProcessor;
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("`");
        result.append(name);
        result.append("`");
        result.append(columnType.toString());
        if (columnType == ColumnType.CHAR){
            result.append("(1)");
        }else if (columnType == ColumnType.VARCHAR){
            result.append("(");
            result.append(maxLength);
            result.append(")");
        }
        if (isNotNull){ result.append(Const.STRING_NOT_NULL);
        }
        if (hasDefaultValue){
            result.append(Const.STRING_DEFAULT);
        }
        if (isAutoIncrement){
            result.append(Const.STRING_AUTO_INCREMENT);
        }
        return result.toString();
    }

    @Override
    public int hashCode() {
        return 731 * name.hashCode() + 137 * columnType.hashCode() + 1331 * (defaultValue == null ? 0 : defaultValue.hashCode()) + 16 * (isAutoIncrement ? 1 : 0) + 4 * (isNotNull ? 1 : 0) + 2 * (hasDefaultValue ? 1 : 0) + 1523 * maxLength;
    }

    public boolean equals(Object obj)
    {
        return ((obj instanceof ColumnProcessor)) && (hashCode() == ((ColumnProcessor)obj).hashCode());
    }

    public boolean equalsByName(Object obj) {
        return ((obj instanceof ColumnProcessor)) && (name.equals(name));
    }
}
