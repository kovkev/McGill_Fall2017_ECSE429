package com.fall2017ecse429project.app;

import org.apache.commons.collections4.CollectionUtils;


import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Integer[] arr = new Integer[]{1, 2};
        ArrayList<Integer> l = new ArrayList<Integer>();
        l.add(5);
        l.add(6);

        CollectionUtils.addAll(l, arr);

        System.out.println( "\""
                + Integer.toString(l.get(2))
                + "\""
                + " should be 1."
        );
        System.out.println( "\""
                + Integer.toString(l.get(0))
                + "\""
                + " should be 5."
        );
    }
}
