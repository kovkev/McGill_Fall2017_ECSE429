package org.apache.commons.collections4.bidimap;

import static org.junit.Assert.*;

import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;

import javax.naming.OperationNotSupportedException;
import javax.swing.JApplet;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.bidimap.TreeBidiMap.ViewMapIterator;
import org.apache.commons.collections4.iterators.EmptyOrderedMapIterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TreeBidiMapTest {
	TreeBidiMap<String, Integer> tbm_operation;
	TreeBidiMap<String, Integer> tbm_empty;
	OrderedBidiMap<Integer, String> inverseBDM;
	OrderedBidiMap<Integer, String> inverseEmptyBDM;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}


	@Test
	public void testMapOrder() {
		// This test demonstrates that 
		// Keys where the comparator returns 0 will be overwritten. They can be overwritten even when the new value is smaller.
		// When a Key is set where the Value compares to one other value in the TreeBidiMap with 0, the key is overwritten;
		// The map iterators do iterate in a sorted manner.
		
		class Node implements Comparable<Node> {
			public int value;
			public Node(int value){
				this.value = value;
			}

			@Override
			public int compareTo(Node o) {
				return value - o.value;
			}
		}
		
		Node a = new Node(1);
		Node c1 = new Node(3);
		Node c2 = new Node(3);
		Node y = new Node(25);
		Node z = new Node(26);
		
		TreeBidiMap<Node, Integer> tbm = new TreeBidiMap<Node, Integer>();
		
		tbm.put(y, 0);
		tbm.put(a, 2);
		tbm.put(c2, 4);
		tbm.put(c1, 3);
		tbm.put(z, 0);
		
		MapIterator<Node, Integer> mapiter = tbm.mapIterator();
		Integer count = 0;
		
		while(mapiter.hasNext()){
			Node c = mapiter.next();
			Integer i = mapiter.getValue();
			
			// mapiter.setValue(i + 10);
			
			System.out.println(c.value);
			System.out.println(i);
			
			if(count == 0){
				assert c.value == 1;
				assert i == 2;
			} else if(count == 1){
				assert c.value == 3;
				assert i == 3;
			} else if ( count == 2){
				assert c.value == 26;
				assert i == 0;
			}
			
			count++;
		}
		
		System.out.println("next");
		
		OrderedBidiMap<Integer, Node> ibm = tbm.inverseBidiMap();
		MapIterator<Integer, Node> mi = ibm.mapIterator();
		
		count = 0;
		while (mi.hasNext()) {
			Integer i = mi.next();
			Node c = mi.getValue();
			
			System.out.println(c.value);
			System.out.println(i);
			
			if (count == 0) {
				assert c.value == 26;
				assert i == 0;
			} else if (count == 1){
				assert c.value == 1;
				assert i == 2;
			} else if (count == 2){
				assert c.value == 3;
				assert i == 3;
			}
			
			count++;
		}
		
		System.out.println("AAA");
	}
	
	@Test
	public void testSetValue() {
		
		TreeBidiMap<Character, Integer> tbm =
			new TreeBidiMap<Character, Integer>();
		
		tbm.put('c', 0);
		
		Set<java.util.Map.Entry<Character, Integer>> es =
			tbm.entrySet();
		
		java.util.Map.Entry<Character, Integer> e = 
			es.iterator().next();
		
		try {
			e.setValue(5);
			assert false;
		} catch(UnsupportedOperationException exception) {
			// all good
		}
	}
	
	@Test
	public void testSetUpdate() {
		
		TreeBidiMap<Character, Integer> tbm =
			new TreeBidiMap<Character, Integer>();
		
		tbm.put('c', 0);
		
		Set<java.util.Map.Entry<Character, Integer>> es =
			tbm.entrySet();
		
		Iterator<java.util.Map.Entry<Character, Integer>> iter = es.iterator();
		
		java.util.Map.Entry<Character, Integer> e = 
			iter.next();
		
		assert e.getKey() == 'c';
		assert e.getValue() == 0;
		
		assert !iter.hasNext();
		
		tbm.put('z', 2);
		
		es = tbm.entrySet();
		assert es.size() == 2;
		
		assert !iter.hasNext();
		
		OrderedBidiMap<Integer, Character> ibm = tbm.inverseBidiMap();
		Set<java.util.Map.Entry<Integer, Character>> ies =
				ibm.entrySet();
		
		assert ies.size() == 2;
	}
	
	/* White-Box Testing
	 * (Feel free to add more tests!) 
	 */
	
	@Before
	public void setUp() throws Exception {
		tbm_operation = new TreeBidiMap<String, Integer>();
		tbm_operation.put("One", 1);
		tbm_operation.put("Two", 2);
		tbm_empty = new TreeBidiMap<String, Integer>();
		inverseBDM = tbm_operation.inverseBidiMap();
		inverseEmptyBDM = tbm_empty.inverseBidiMap();
	}
	
//	@Test
//	public void testInverseBidimap() {
//		OrderedBidiMap<Integer, String> inverseBDM = tbm_operation.inverseBidiMap();
//		OrderedBidiMap<Integer, String> inverseEmptyBDM = tbm_empty.inverseBidiMap();
//	}
	
	@Test
	public void testMapIterator() {
		assertEquals(tbm_operation.mapIterator().getClass(), ViewMapIterator.class);
		assertEquals(tbm_empty.mapIterator().getClass(), EmptyOrderedMapIterator.class);
	}
	
	@Test
	public void testContains() {
		//Empty TreeBidiMap
		assertEquals(tbm_empty.containsKey("Dummy"), false);
		assertEquals(tbm_empty.containsValue(1), false);
		
		//Non-empty TreeBidiMap
		MapIterator<String, Integer> map_iter = tbm_operation.mapIterator();
		while(map_iter.hasNext()) {
			String key = map_iter.next();
			int value = map_iter.getValue();
			assertEquals(tbm_operation.containsKey(key), true);
			assertEquals(tbm_operation.containsValue(value), true);
		}
		
		//Inverse TreeBidiMap
		
		
		System.out.println("testContains() Passed");
	}
	
	@Test
	public void testGetKey() {
		//Empty TreeBidiMap
		String key2 = tbm_empty.getKey("Dummy");
		assertEquals(key2, null);
		
		//Non-empty TreeBidiMap
		MapIterator<String, Integer> map_iter = tbm_operation.mapIterator();
		while(map_iter.hasNext()) {
			String key = map_iter.next();
			int value = map_iter.getValue();
			assertEquals(key, tbm_operation.getKey(value));
		}
		
		//Inverse TreeBidiMap
		
		
		
		System.out.println("testGetKey() Passed");
	}
	
	@Test
	public void testFirstKey() {
		//Empty TreeBidiMap
		try {
			tbm_empty.firstKey();
			fail("Should throw NoSuchElementException");
		}catch (NoSuchElementException e){
//			System.out.println(e.getMessage());
			assertEquals(e.getMessage(), "Map is empty");
		}
		//Non-empty TreeBidiMap
		String first_key = tbm_operation.firstKey();
		MapIterator<String, Integer> map_iter = tbm_operation.mapIterator();
		int count=0;
		while(map_iter.hasNext()) {
			String key = map_iter.next();
			if(count==0) {
				assertEquals(key,first_key);
			}
			count++;
		}
		
		//Inverse TreeBidiMap
		
		
		
		System.out.println("testFirstKey() Passed");
	}
	
	@Test
	public void testLastKey() {
		//Empty TreeBidiMap
		try {
			tbm_empty.lastKey();
			fail("Should throw NoSuchElementException");
		}catch (NoSuchElementException e){
//			System.out.println(e.getMessage());
			assertEquals(e.getMessage(), "Map is empty");
		}
		//Non-empty TreeBidiMap
		MapIterator<String, Integer> map_iter = tbm_operation.mapIterator();
		String last_key = tbm_operation.lastKey();
		int count=0;
		while(map_iter.hasNext()) {
			String key = map_iter.next();
			if(count==tbm_operation.size()-1) {
				assertEquals(key,last_key);
			}
			count++;
		}
		
		//Inverse TreeBidiMap
		
		
		
		System.out.println("testLastKey() Passed");
	}
	
	@Test
	public void testNextAndPrevKey() {
		//Empty TreeBidiMap
		assertEquals(tbm_empty.nextKey("Dummy"), null);
		assertEquals(tbm_empty.previousKey("Dummy"), null);
		
		//Non-empty TreeBidiMap
		MapIterator<String, Integer> map_iter = tbm_operation.mapIterator();
		String prev_key;
		String current_key = tbm_operation.firstKey();
		while(map_iter.hasNext()) {
			String key = map_iter.next();
			assertEquals(key, current_key);
			prev_key=current_key;
			current_key=tbm_operation.nextKey(key);
			if(current_key!=null) {
				assertEquals(prev_key, tbm_operation.previousKey(current_key));	
			}
		}
		
		//Inverse TreeBidiMap
		
		
		
		System.out.println("testNextAndPrevKey() Passed");
	}
	
	@Test
	public void testEquals() {
		TreeBidiMap<String, Integer> dummy = new TreeBidiMap<String, Integer>();
		TreeBidiMap<String, Integer> dummy2 = new TreeBidiMap<String, Integer>();
		TreeBidiMap<String, Integer> dummy3 = new TreeBidiMap<String, Integer>();
		TreeBidiMap<String, Integer> dummy4 = tbm_operation;
		MapIterator<String, Integer> map_iter = tbm_operation.mapIterator();
		
		dummy2.put("DUMMY", 5);
		dummy2.put("DUMMY+", 6);
		dummy3.put("One", 1);
		dummy3.put("Two", 2);
		
		//False
		assertEquals(tbm_empty.equals(tbm_operation), false);
		assertEquals(tbm_operation.equals(tbm_empty), false);
		assertEquals(tbm_operation.equals(map_iter), false);
		assertEquals(tbm_operation.equals(dummy2), false);
		
		//True
		assertEquals(tbm_empty.equals(dummy), true);
		assertEquals(tbm_operation.equals(dummy3), true);
		assertEquals(tbm_operation.equals(dummy4), true);
		
		
	}
	
	
	@Test
	public void testRemove() {
		//Empty TreeBidiMap
		tbm_empty.remove("Dummy");
		assertEquals(tbm_empty.size(),0);
		
		//Non-empty TreeBidiMap
		int size = tbm_operation.size();
		tbm_operation.remove("One");
		assertEquals(tbm_operation.size(), size-1);
		
		//Inverse TreeBidiMap

		
		System.out.println("testRemove() Passed");
	}
	
	@Test
	public void testRemoveValue() {
		//Empty TreeBidiMap
		tbm_empty.removeValue(1);
		assertEquals(tbm_empty.size(),0);
		
		//Non-empty TreeBidiMap
		int size = tbm_operation.size();
		tbm_operation.removeValue(2);
		assertEquals(tbm_operation.size(), size-1);
		
		//Inverse TreeBidiMap
		
		
		System.out.println("testRemoveValue() Passed");
	}	
	
	
	@After
	public void tearDown() throws Exception {
		tbm_operation.clear();
		tbm_empty.clear();
		inverseBDM.clear();
		inverseEmptyBDM.clear();
		
	}
	
	
}

