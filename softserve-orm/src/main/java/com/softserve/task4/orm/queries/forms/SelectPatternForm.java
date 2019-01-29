package com.softserve.task4.orm.queries.forms;

import com.softserve.task4.orm.processes.constantsExpressions.Const;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class SelectPatternForm {

    public static String selectFormer(String table,String column, String hallMark, Object hallMarkValue){
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append(Const.STRING_SELECT).
                append(column==null ?Const.STRING_ALL_VALUES : Const.STRING_BEFORE_QUOTE+column+Const.STRING_AFTER_QUOTE).
                append(Const.STRING_FROM).
                append(hallMark == null ? table : table + Const.STRING_WHERE ).
                append(hallMark + Const.STRING_ASSIGN).
                    append(hallMarkValue instanceof Integer ? String.valueOf(hallMarkValue) :
                        " `"+String.valueOf(hallMarkValue)+"`"  );
        builder.append(";");

        return builder.toString();
    }
}
