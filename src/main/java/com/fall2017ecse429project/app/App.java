package com.fall2017ecse429project.app;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.bag.CollectionBag;
import org.apache.commons.collections4.bag.CollectionSortedBag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.PredicatedSortedBag;
import org.apache.commons.collections4.bag.TransformedBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
import org.apache.commons.collections4.map.HashedMap;


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

        test_map_iterator();
        test_transformer();
        test_Bag_Group();
    }
    
    public static void test_Bag_Group() {
    	test_Bag();
    	test_SortedBag();
    	test_BagUtils();
    }
    
    // Since a Bag instance cannot be instantiated, the HashBag was used
    // to test the Bag class
    public static void test_Bag() {
    	Bag bag = new HashBag();
    	
    	// Add an element to the bag
    	bag.add("apple", 3);
    	System.out.println("*");
    	assert bag.getCount("apple") == 3;
    	
    	// Remove only one unit of the element 
    	// Two elements should be remaining
    	bag.remove("apple", 1);
    	assert bag.getCount("apple") == 2;
    	
    	// Remove all elements
    	bag.remove("apple");
    	assert bag.getCount("apple") == 0; 
    }
    
    // In order to test the SortedBag class and its inheritance from the class Bag
    // The inherited methods were tested in the following tests
    public static void test_SortedBag() {
    	
    	Bag bag = BagUtils.synchronizedBag(new HashBag());
    	// This will unsure that no null entries are added to the bag
    	SortedBag sortedBag = PredicatedSortedBag.predicatedSortedBag(new TreeBag(), NotNullPredicate.INSTANCE);
    	 
    	// Add an element to the bag
    	sortedBag.add("banana", 10);
    	assert sortedBag.getCount("banana") == 10;

    	// Remove only one unit of the element 
    	// Two elements should be remaining
    	sortedBag.remove("banana", 1);
    	assert sortedBag.getCount("banana") == 9;

    	// Remove all elements
    	sortedBag.remove("banana");
    	assert sortedBag.getCount("apple") == 0; 
    }
    
    public static void test_BagUtils() {
    	
    }


    public static void test_transformer() {
        test_constant_transformingBag();
        test_constant_transformedBag();
    }

    public static void test_constant_transformingBag() {
        HashBag<Integer> b = new HashBag<Integer>();
        b.add(1);
        b.add(2);
        b.add(3);
        Transformer<Integer, Integer> t = TransformerUtils.constantTransformer(5);
        Bag<Integer> tb = TransformedBag.transformingBag(b, t);

        System.out.println(tb.toString());
        assert t.transform(1) == 5;

        assert tb.getCount(1) == 1;
        assert tb.getCount(2) == 1;
        assert tb.getCount(3) == 1;

        assert tb.getCount(5) == 0;

        tb.add(4);

        assert tb.getCount(4) == 0;
        assert tb.getCount(5) == 1;

        b.add(10);

        assert tb.getCount(10) == 1;
        assert tb.getCount(5) == 1;
    }

    public static void test_constant_transformedBag() {
        HashBag<Integer> b = new HashBag<Integer>();
        b.add(1);
        b.add(2);
        b.add(3);
        Transformer<Integer, Integer> t = TransformerUtils.constantTransformer(5);
        Bag<Integer> tb = TransformedBag.transformedBag(b, t);

        System.out.println(tb.toString());
        assert t.transform(1) == 5;

        assert tb.getCount(1) == 0;
        assert tb.getCount(2) == 0;
        assert tb.getCount(3) == 0;

        assert tb.getCount(5) == 3;

        tb.add(4);

        assert tb.getCount(4) == 0;
        assert tb.getCount(5) == 4;

        b.add(10);

        assert tb.getCount(5) == 4;
        assert tb.getCount(10) == 1;
    }

    public static void test_map_iterator() {
        test_empty_map_iterator();
        test_order_map_iterator();
        test_unmodifiable_decorator_map_iterator();
        test_order_continuous_map_iterator();
    }

    public static void test_order_continuous_map_iterator(){
        IterableMap<String, Integer> map = new HashedMap<String, Integer>();
        map.put("aaa", 3);

        MapIterator it = map.mapIterator();

        int count = 10;
        while (it.hasNext()) {
            Object key = it.next();
            Integer value = (Integer) it.getValue();

            it.setValue(count + 10);

            if (count > 0) {
                map.put("zzz" + Integer.toString(count), count);
            }

            count -= 1;
        }

        // If you add keys while iterating, the iterator won't pick that up.
        assert count == 9;
        assert map.get("zzz1") == null;
    }

    public static void test_unmodifiable_decorator_map_iterator() {
        boolean did_throw = false;

        IterableMap<String, Integer> map = new HashedMap<String, Integer>();
        map.put("aaa", 3);
        map.put("bbb", 2);
        map.put("zzz", 1);

        MapIterator it = map.mapIterator();
        UnmodifiableMapIterator uit = (UnmodifiableMapIterator) UnmodifiableMapIterator.unmodifiableMapIterator(it);

        while (uit.hasNext()) {
            Object key = uit.next();
            Integer value = (Integer) uit.getValue();

            try {
                uit.setValue(1);
            } catch(UnsupportedOperationException e) {
                did_throw = true;
            }
        }

        assert did_throw;
    }

    public static void test_empty_map_iterator() {
        IterableMap map = new HashedMap();

        MapIterator it = map.mapIterator();

        int count = 0;
        while (it.hasNext()) {
            Object key = it.next();
            Object value = it.getValue();

            it.setValue("foo");
            count += 1;
        }

        assert count == 0;
    }

    public static void test_order_map_iterator() {
        IterableMap<String, Integer> map = new HashedMap<String, Integer>();
        map.put("aaa", 3);
        map.put("bbb", 2);
        map.put("zzz", 1);

        MapIterator it = map.mapIterator();

        int count = 0;
        while (it.hasNext()) {
            Object key = it.next();
            Integer value = (Integer) it.getValue();

            it.setValue(count + 10);

            // Follows the order with which the keys were added
            if (count == 0) {
                assert value == 3;
            } else if (count == 1) {
                assert value == 2;
            } else if (count == 2) {
                assert value == 1;
            }

            count += 1;
        }

        assert count == 3;

        assert map.get("aaa") == 10;
        assert map.get("bbb") == 11;
        assert map.get("zzz") == 12;
    }
}
