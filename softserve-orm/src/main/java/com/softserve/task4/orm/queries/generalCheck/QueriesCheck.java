package com.softserve.task4.orm.queries.generalCheck;

import com.softserve.task4.orm.processes.ColumnProcessor;
import com.softserve.task4.orm.processes.constantsExpressions.Const;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */
public class QueriesCheck {

    private  int initIndex = 0;
    public boolean checkNullValue(Object value){
        return value != null;
    }

    public boolean characterOrNot(Object value){
        return !((value.getClass() == Character.TYPE) || (value.getClass() == Character.class));
    }

   public void prepareToGeneratingQuery(Set<String> columnNamesSet, Field field, ColumnProcessor column, Object object,
                                        List<String> valuesString, Map<ColumnProcessor,Object> valuesMap,
                                        Map<ColumnProcessor,Integer> indexMap) throws Exception {
       if ((column != null) &&
               (columnNamesSet.contains(column.name))) {
           Object  value = field.get(object);
           if ((column.isNotNull) && (value == null)) {
               throw new Exception();
           }

           String setValueString = Const.STRING_BEFORE_QUOTE + column.name + Const.STRING_AFTER_QUOTE +
                   Const.STRING_ASSIGN + (value != null ? Const.STRING_VAR : Const.STRING_NULL);

           valuesString.add(setValueString);

           if (checkNullValue(value)) {
               if (!characterOrNot(value)) {
                   valuesMap.put(column, String.valueOf(value));
               } else {
                   valuesMap.put(column, value);
               }
               indexMap.put(column, ++initIndex);
           }
       }
   }
}
