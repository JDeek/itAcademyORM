package com.softserve.task4.orm.processes;

import com.softserve.task4.orm.annotations.DefaultMaxLength;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class TableProcessor {
    private Class clazz;
    private String tableName;

    public TableProcessor(Class clazz) {
        this.clazz = clazz;
    }

    public TableProcessor(Class clazz, String tableName) {
        this.clazz = clazz;
        this.tableName = tableName;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getTableName() {
        return tableName;
    }


    public static int getDefaultMaxLength(Class clazz){
        if (clazz.isAnnotationPresent(DefaultMaxLength.class)){
            DefaultMaxLength defaultMaxLength = (DefaultMaxLength) clazz.getAnnotation(DefaultMaxLength.class);
            return defaultMaxLength.value();
        }
        return 256;
    }

    public static String getTableName(Class clazz){
        String tableName = "";

        if (clazz.isAnnotationPresent(com.softserve.task4.orm.annotations.Table.class)){
            com.softserve.task4.orm.annotations.Table tableNameAnn = (com.softserve.task4.orm.annotations.Table) clazz.getAnnotation(com.softserve.task4.orm.annotations.Table.class);
            tableName = tableNameAnn.value();
        }

        if (tableName.isEmpty()){
            tableName = clazz.getSimpleName();
        }
        return tableName;
    }

}
