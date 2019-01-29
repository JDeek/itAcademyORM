package com.softserve.task4.orm.processes;

import org.reflections.Reflections;

import java.util.Set;

/**
 * Created by ${JDEEK} on ${11.11.2018}.
 */

public class CheckAnnotatedClass {

    public static Set<Class<?>> getEntityAnnotatedClasses(String pckg){
        Reflections reflections = new Reflections(pckg);
        return reflections.getTypesAnnotatedWith(com.softserve.task4.orm.annotations.Table.class);
    }
}
