package org.apache.commons.collections4.bidimap;

import static org.junit.Assert.*;

import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;

import javax.naming.OperationNotSupportedException;
import javax.swing.JApplet;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.OrderedBidiMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TreeBidiMapTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {


	}

	@After
	public void tearDown() throws Exception {
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
}

