package com.softserve.task4.orm.processes;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public enum ColumnType {
    BOOL,
    INT,
    BIGINT,
    FLOAT,
    DOUBLE,
    VARCHAR,
    CHAR,
    TEXT,
    OTHER;

    @Override
    public String toString() {
        switch (this){
            case BOOL:
                return Const.STRING_BOOL;
            case INT:
                return Const.STRING_INT;
            case BIGINT:
                return Const.STRING_BIGINT;
            case FLOAT:
                return Const.STRING_FLOAT;
            case DOUBLE:
                return Const.STRING_DOUBLE;
            case VARCHAR:
                return Const.STRING_VARCHAR;
            case CHAR:
                return Const.STRING_CHAR;
            case TEXT:
                return Const.STRING_TEXT;
        }
        return "OTHER";
    }
}
