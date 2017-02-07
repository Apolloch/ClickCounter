package com.example.bacquet.myapplication.clickCounting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by DIENG on 09/12/2016.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionEnd {
    String name();
    boolean success() default true;
    //boolean repeatable() default false;
}
