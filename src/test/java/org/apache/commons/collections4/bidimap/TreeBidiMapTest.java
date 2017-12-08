package org.apache.commons.collections4.bidimap;

import static org.junit.Assert.*;

import java.awt.List;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Collections;
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
import org.apache.commons.collections4.bidimap.TreeBidiMap.View;
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

	/* Integration Testing here
	 * White-box testing below (look for white-box testing comment)
	 */
	
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
			
			//System.out.println(c.value);
			//System.out.println(i);
			
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
		
		//System.out.println("next");
		
		OrderedBidiMap<Integer, Node> ibm = tbm.inverseBidiMap();
		MapIterator<Integer, Node> mi = ibm.mapIterator();
		
		count = 0;
		while (mi.hasNext()) {
			Integer i = mi.next();
			Node c = mi.getValue();
			
			//System.out.println(c.value);
			//System.out.println(i);
			
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
		
		//System.out.println("AAA");
	}
	
	/* White-Box Testing starts here
	 *  
	 */
	
	@Test
	public void testSetValue() {
		
		// simultaneously testing normal and inverse
		
		TreeBidiMap<Character, Integer> tbm =
			new TreeBidiMap<Character, Integer>();
		OrderedBidiMap<Integer, Character> i_tbm = tbm.inverseBidiMap();
		
		tbm.put('c', 0);
		
		Set<java.util.Map.Entry<Character, Integer>> es =
			tbm.entrySet();
		Set<java.util.Map.Entry<Integer, Character>> i_es = i_tbm.entrySet();
		
		java.util.Map.Entry<Character, Integer> e = 
			es.iterator().next();
		java.util.Map.Entry<Integer, Character> i_e = 
				i_es.iterator().next();
		
		try {
			e.setValue(5);
			assert false;
		} catch(UnsupportedOperationException exception) {
			// all good
		}
		try {
			i_e.setValue('c');
			assert false;
		} catch(UnsupportedOperationException exception) {
			// all good
		}
		
	}
	
	@Test
	public void testSetUpdate() {
		
		// simultaneously testing normal and inverse
		
		TreeBidiMap<Character, Integer> tbm =
			new TreeBidiMap<Character, Integer>();
		OrderedBidiMap<Integer, Character> i_tbm = tbm.inverseBidiMap();
		
		tbm.put('c', 0);
		
		Set<java.util.Map.Entry<Character, Integer>> es =
			tbm.entrySet();
		es = tbm.entrySet(); // for branch coverage
		Set<java.util.Map.Entry<Integer, Character>> i_es = i_tbm.entrySet();
		i_es = i_tbm.entrySet(); // for branch coverage
		
		Iterator<java.util.Map.Entry<Character, Integer>> iter = es.iterator();
		Iterator<java.util.Map.Entry<Integer, Character>> i_iter = i_es.iterator();
		
		java.util.Map.Entry<Character, Integer> e = 
			iter.next();
		java.util.Map.Entry<Integer, Character> i_e = 
				i_iter.next();
		
		assert e.getKey() == 'c';
		assert e.getValue() == 0;
		assert i_e.getKey() == 0;
		assert i_e.getValue() == 'c';
		
		assert !iter.hasNext();
		assert !i_iter.hasNext();
		
		tbm.put('z', 2);
		
		es = tbm.entrySet();
		assert es.size() == 2;
		i_es = i_tbm.entrySet();
		assert i_es.size() == 2;
		
		assert !iter.hasNext();
		assert !i_iter.hasNext();
		
	}
	
	@Before
	public void setUp() throws Exception {
		tbm_operation = new TreeBidiMap<String, Integer>();
		tbm_operation.put("One", 1);
		tbm_operation.put("Two", 2);
		tbm_empty = new TreeBidiMap<String, Integer>();
		inverseBDM = tbm_operation.inverseBidiMap();
		inverseBDM = tbm_operation.inverseBidiMap(); // for branch coverage
		inverseEmptyBDM = tbm_empty.inverseBidiMap();
	}
	
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
		MapIterator<String, Integer> map_iter = tbm_empty.mapIterator(); // for branch coverage
		
		//Non-empty TreeBidiMap
		map_iter = tbm_operation.mapIterator();
		while(map_iter.hasNext()) {
			String key = map_iter.next();
			int value = map_iter.getValue();
			assertEquals(tbm_operation.containsKey(key), true);
			assertEquals(tbm_operation.containsValue(value), true);
		}
		
		// InverseBidiMap
		
		assertEquals(inverseEmptyBDM.containsKey(3), false);
		assertEquals(inverseEmptyBDM.containsValue("Dummy"), false);
		
		try {
			inverseEmptyBDM.containsKey(new Object());
			assert false;
		} catch (ClassCastException e) {}
		try {
			inverseEmptyBDM.containsValue(new Object());
			assert false;
		} catch (ClassCastException e) {}
		
		try {
			inverseEmptyBDM.containsKey(null);
			assert false;
		} catch (NullPointerException e) {}
		try {
			inverseEmptyBDM.containsValue(null);
			assert false;
		} catch (NullPointerException e) {}
		
		OrderedMapIterator<Integer, String> inverse_map_iter = inverseEmptyBDM.mapIterator(); // for branch coverage
		
		
		// Non Empty InverseBidiMap
		inverse_map_iter = inverseBDM.mapIterator();
		while(inverse_map_iter.hasNext()) {
			int key = inverse_map_iter.next();
			String value = inverse_map_iter.getValue();
			assertEquals(inverseBDM.containsKey(key), true);
			assertEquals(inverseBDM.containsValue(value), true);
		}
		
		OrderedBidiMap<String, Integer> inverseOfInverseBDM = inverseBDM.inverseBidiMap();
		assertTrue(inverseOfInverseBDM.containsKey("One"));
		
		
		//System.out.println("testContains() Passed");
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
		
		//Empty InverseTreeBidiMap
		Integer key3 = inverseEmptyBDM.getKey(1);
		assertEquals(key3, null);
		
		//Non-empty TreeBidiMap
		OrderedMapIterator<Integer, String> inverse_map_iter = inverseBDM.mapIterator();
		while(inverse_map_iter.hasNext()) {
			Integer key4 = inverse_map_iter.next();
			String value2 = inverse_map_iter.getValue();
			assertEquals(key4, inverseBDM.getKey(value2));
		}
		
		//System.out.println("testGetKey() Passed");
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
		
		//InverseTreeBidiMap
		try {
			inverseEmptyBDM.firstKey();
			assert false;
		}catch (NoSuchElementException e){
//			System.out.println(e.getMessage());
			assertEquals(e.getMessage(), "Map is empty");
		}
		
		//Non-empty InverseTreeBidiMap
		Integer inverse_first_key = inverseBDM.firstKey();
		MapIterator<Integer, String> inverse_map_iter = inverseBDM.mapIterator();
		int inverse_count=0;
		while(inverse_map_iter.hasNext()) {
			Integer key = inverse_map_iter.next();
			if(inverse_count==0) {
				assertEquals(key,inverse_first_key);
			}
			inverse_count++;
		}
		
		
		//System.out.println("testFirstKey() Passed");
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
		
		//Empty InverseTreeBidiMap
		try {
			inverseEmptyBDM.lastKey();
			fail("Should throw NoSuchElementException");
		}catch (NoSuchElementException e){
//					System.out.println(e.getMessage());
			assertEquals(e.getMessage(), "Map is empty");
		}
		//Non-empty TreeBidiMap
		OrderedMapIterator<Integer, String> inverse_map_iter = inverseBDM.mapIterator();
		Integer last_key2 = inverseBDM.lastKey();
		int count2=0;
		while(inverse_map_iter.hasNext()) {
			Integer key = inverse_map_iter.next();
			if(count2==inverseBDM.size()-1) {
				assertEquals(key,last_key2);
			}
			count2++;
		}
		
		
		//System.out.println("testLastKey() Passed");
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
		
		//Empty InverseTreeBidiMap
		assertEquals(inverseEmptyBDM.nextKey(99), null);
		assertEquals(inverseEmptyBDM.previousKey(99), null);
		
		//Non-empty TreeBidiMap
		OrderedMapIterator<Integer, String> inverse_map_iter = inverseBDM.mapIterator();
		Integer prev_key2;
		Integer current_key2 = inverseBDM.firstKey();
		while(inverse_map_iter.hasNext()) {
			Integer key = inverse_map_iter.next();
			assertEquals(key, current_key2);
			prev_key2=current_key2;
			current_key2=inverseBDM.nextKey(key);
			if(current_key2!=null) {
				assertEquals(prev_key2, inverseBDM.previousKey(current_key2));	
			}
		}
		
		//System.out.println("testNextAndPrevKey() Passed");
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
		
		
		// Inverse BDM
		OrderedBidiMap<Integer, String> inverseDummy = new TreeBidiMap<String, Integer>().inverseBidiMap();
		OrderedBidiMap<Integer, String> inverseDummy2 = new TreeBidiMap<String, Integer>().inverseBidiMap();
		OrderedBidiMap<Integer, String> inverseDummy3 = new TreeBidiMap<String, Integer>().inverseBidiMap();
		OrderedBidiMap<Integer, String> inverseDummy4 = inverseBDM;
		OrderedMapIterator<Integer, String> inverse_map_iter = inverseBDM.mapIterator();
		
		inverseDummy2.put(5, "DUMMY");
		inverseDummy2.put(5, "DUMMY+");
		inverseDummy3.put(1, "One");
		inverseDummy3.put(2, "Two");
		
		//False
		assertEquals(inverseEmptyBDM.equals(inverseBDM), false);
		assertEquals(inverseBDM.equals(inverseEmptyBDM), false);
		assertEquals(inverseBDM.equals(inverse_map_iter), false);
		assertEquals(inverseBDM.equals(inverseDummy2), false);
		
		//True
		assertEquals(inverseEmptyBDM.equals(inverseDummy), true);
		assertEquals(inverseBDM.equals(inverseDummy3), true);
		assertEquals(inverseBDM.equals(inverseDummy4), true);
		
	}
	
	
	@Test
	public void testRemove() {
		//Empty TreeBidiMap
		Integer isNull = tbm_empty.remove("One");
		assertNull(isNull);
		assertEquals(tbm_empty.size(),0);
		
		//Non-empty TreeBidiMap
		int size = tbm_operation.size();
	    Integer key = tbm_operation.remove("Two");
	    assertFalse(tbm_operation.containsKey("Two"));
	    assertFalse(tbm_operation.containsValue(2));
	    assertTrue(key == 2);
		assertEquals(tbm_operation.size(), size-1);
		
		//Empty InverseTreeBidiMap
		String isNull2 = inverseEmptyBDM.remove(1);
		assertNull(isNull2);
		assertEquals(inverseEmptyBDM.size(),0);
		
		//Non-empty TreeBidiMap
		size = inverseBDM.size();
	    String key2 = inverseBDM.remove(1);
	    assertFalse(inverseBDM.containsKey(1));
	    assertFalse(inverseBDM.containsValue("One"));
	    assertEquals(key2, "One");
		assertEquals(inverseBDM.size(), size-1);
		
		
		//System.out.println("testRemove() Passed");
	}
	
	@Test
	public void testRemoveValue() {
		//Empty TreeBidiMap
		String isNull = tbm_empty.removeValue(1);
		assertNull(isNull);
		assertEquals(tbm_empty.size(),0);
		
		//Non-empty TreeBidiMap
		int size = tbm_operation.size();
	    String key = tbm_operation.removeValue(2);
	    assertFalse(tbm_operation.containsKey("Two"));
	    assertFalse(tbm_operation.containsValue(2));
	    assertEquals(key, "Two");
		assertEquals(tbm_operation.size(), size-1);
		
		//Empty InverseTreeBidiMap
		Integer isNull2 = inverseEmptyBDM.removeValue(1);
		assertNull(isNull2);
		assertEquals(inverseEmptyBDM.size(),0);
		
		//Non-empty TreeBidiMap
		size = inverseBDM.size();
	    Integer key2 = inverseBDM.removeValue("One");
	    assertFalse(inverseBDM.containsKey(1));
	    assertFalse(inverseBDM.containsValue("One"));
	    assertTrue(key2 == 1);
		assertEquals(inverseBDM.size(), size-1);
		
		//System.out.println("testRemoveValue() Passed");
	}
	
	@Test
	public void testRebalance() {
		TreeBidiMap<String, Integer> dummy = new TreeBidiMap<String, Integer>();
		for (int i = 0; i < 1000 ; ++i) {
			String key = Integer.toString(i);
			dummy.put(key, i);
		}
		
		for (int i = 0; i < 1000 ; ++i) {
			String key = Integer.toString(i);
			dummy.remove(key);
		}
		
		for (int i = 0; i < 1000 ; ++i) {
			String key = Integer.toString(i);
			dummy.put(key, i);
		}
		
		for (int i = 0; i < 1000 ; ++i) {
			int j = 1000 - 1 - i;
			String key = Integer.toString(j);
			dummy.remove(key);
		}
	}
	
	@Test
	public void testRandomInserts() {
		TreeBidiMap<String, Integer> dummy = new TreeBidiMap<String, Integer>();
		
		ArrayList<Integer> order = new ArrayList<Integer>();
		for (int i = 0; i < 10000; ++i){
			order.add(i);
		}
		Collections.shuffle(order);
		for (int j = 0; j < 10000; ++j) {
			int i = order.get(j);
			
			String key = Integer.toString(j);
			
			dummy.put(key, i);
		}
		
		Collections.shuffle(order);
		
		for (int j = 0; j < 10000; ++j) {
			int i = order.get(j);
			
			String key = Integer.toString(j);
			
			dummy.remove(key);
		}
	}
	
	@Test
	public void testDoToString() {
		TreeBidiMap<String, Integer> dummy = new TreeBidiMap<String, Integer>();
		for (int i = 0; i < 100 ; ++i) {
			String key = Integer.toString(i);
			dummy.put(key, i);
		}
		
		dummy.toString();
	}
	
	@Test
	public void testKeyView() {
		tbm_operation.put("AAA", 0);
		tbm_operation.put("ZZZ", 99);
		
		Set<String> s = tbm_operation.keySet();
		Iterator<String> itr = s.iterator();
		String first = "AAA"; // assert that first is "aaa" and not "One"
		String current = itr.next();
		assertEquals(current, first);
		while (itr.hasNext()) {
			assertTrue(s.contains(itr.next()));
		}
		assertFalse(s.contains("abc"));
		assertFalse(s.remove("abc"));
		
		s.remove("AAA");
		assertTrue(s.size() == 3);
	}
	
	@Test
	public void testValueView() {
		tbm_operation.put("AAA", 0);
		tbm_operation.put("ZZZ", 99);
		
		Set<Integer> s = tbm_operation.values();
		Iterator<Integer> itr = s.iterator();
		Integer first = 0; // assert that first 0 and not 1
		Integer current = itr.next();
		assertEquals(current, first);
		while (itr.hasNext()) {
			assertTrue(s.contains(itr.next()));
		}
		assertFalse(s.contains(4));
		assertFalse(s.remove(4));
		
		s.remove(0);
		assertTrue(s.size() == 3);
	}
	
	@Test
	public void testInverseKeyView() {
		inverseBDM.put(0, "AAA");
		inverseBDM.put(99, "ZZZ");
		
		Set<Integer> s = inverseBDM.keySet();
		Iterator<Integer> itr = s.iterator();
		Integer first = 0; // assert that first 0 and not 1
		Integer current = itr.next();
		assertEquals(current, first);
		while (itr.hasNext()) {
			assertTrue(s.contains(itr.next()));
		}
		assertFalse(s.contains(4));
		assertFalse(s.remove(4));
		
		s.remove(0);
		assertTrue(s.size() == 3);
	}
	
	@Test
	public void testInverseValueView() {
		inverseBDM.put(0, "AAA");
		inverseBDM.put(99, "ZZZ");
		
		Set<String> s = inverseBDM.values();
		Iterator<String> itr = s.iterator();
		String first = "AAA"; // assert that first is "aaa" and not "One"
		String current = itr.next();
		assertEquals(current, first);
		while (itr.hasNext()) {
			assertTrue(s.contains(itr.next()));
		}
		assertFalse(s.contains("abc"));
		assertFalse(s.remove("abc"));
		
		s.remove("AAA");
		assertTrue(s.size() == 3);
	}
	
	@Test
	public void testHashCode() {
		Integer empty = tbm_empty.hashCode();
		Integer h = tbm_operation.hashCode();
		assertNotEquals(h, empty);
	}
	
	@Test
	public void testInverseHashCode() {
		Integer empty = inverseEmptyBDM.hashCode();
		Integer h = inverseBDM.hashCode();
		assertNotEquals(h, empty);
	}
	
	
	
	@After
	public void tearDown() throws Exception {
		tbm_operation.clear();
		tbm_empty.clear();
		inverseBDM.clear();
		inverseEmptyBDM.clear();
	}
	
	
	
	
}

