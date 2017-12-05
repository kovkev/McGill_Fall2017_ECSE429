package org.apache.commons.collections4;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.PredicatedSortedBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BagUtilsTest {
	
	Bag bag;
	Bag modifiedBag;
	SortedBag sortedBag;
	Predicate oneValueOnly;
	Transformer transform;
	static Integer myInt;
	static String output;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		myInt = 0;
		output = "";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		myInt = null;
		
	}

	@Before
	public void setUpSynchronizedBag() throws Exception {
		output = "";
	}
	
	@Test
	public void testSynchronizedBag() {
		bag = BagUtils.synchronizedBag(new HashBag());
		bag.add("apple", 2);
		bag.add("orange");
		
		synchronized(bag) {
			Iterator i = bag.iterator();
			 while (i.hasNext()) {
				 output += i.next() + " ";
			 } 	
		}
		assertTrue(output.equals("orange apple apple "));
	}

	@After
	public void tearDownSynchronizedBag() throws Exception {
		bag = null;
		output = null;
	}
	
	@Before
	public void setUpUnmodifiableBag() throws Exception {
		bag = BagUtils.synchronizedBag(new HashBag());
		bag.add("grape", 2);
		output = "";
	}
	
	@Test
	public void testUnmodifiableBag() {
		modifiedBag = BagUtils.unmodifiableBag(bag);
		
		// Make sure the bag is indeed unmodifiable
		// An exception is thrown if one tries to modify an unmodifiable Bag
		try {
			modifiedBag.add("apple", 2);
		} catch (Exception e) {
			output = "exception catched";
		}
		
		assertTrue(output.equals("exception catched"));
	}

	@After
	public void tearDownUnmodifiableBag() throws Exception {
		bag = null;
		modifiedBag = null;
		output = null;
	}
	
	@Before
	public void setUpPredicatedBag() throws Exception {
		bag = BagUtils.synchronizedBag(new HashBag());
		oneValueOnly = PredicateUtils.identityPredicate("apple");
		output = "";
	}
	
	@Test
	public void testPredicatedBag() {
		modifiedBag = BagUtils.predicatedBag(bag, oneValueOnly);
		
		// No exception is thrown here
		modifiedBag.add("apple");
		
		// Make sure the bag can only accept "apple" elements and nothing else
		// An exception is thrown if one tries to add something other than an apple
		try {
			modifiedBag.add("orange");
		} catch (Exception e) {
			output = "exception catched";
		}
		
		assertTrue(output.equals("exception catched"));
	}

	@After
	public void tearDownPredicatedBag() throws Exception {
		bag = null;
		modifiedBag = null;
		oneValueOnly = null;
		output = null;
	}
	
	@Before
	public void setUpTransformingBag() throws Exception {
		bag = BagUtils.synchronizedBag(new HashBag());
		transform = TransformerUtils.constantTransformer("orange");
		output = "";
	}
	
	@Test
	public void testTransformingBag() {		
		modifiedBag = BagUtils.transformingBag(bag, transform);
		modifiedBag.add("apple");
		output += modifiedBag.iterator().next();
		
		// The transformer used will transform any input to "orange"
		assertTrue(output.equals("orange"));
	}

	@After
	public void tearDownTransformingBag() throws Exception {
		bag = null;
		modifiedBag = null;
		transform = null;
		output = null;
	}
	
	@Before
	public void setUpCollectionBag() throws Exception {
		bag = BagUtils.synchronizedBag(new HashBag());
		transform = TransformerUtils.constantTransformer("orange");
		output = "";
	}
	
	@Test
	public void testCollectionBag() {		
		modifiedBag = BagUtils.collectionBag(bag);
		modifiedBag.add("apple");
		output += modifiedBag.iterator().next();
		
		assertTrue(output.equals("apple"));
	}

	@After
	public void tearDownCollectionBag() throws Exception {
		bag = null;
		modifiedBag = null;
		output = null;
	}
	
	@Before
	public void setUpSynchronizedSortedBag() throws Exception {
    	sortedBag = PredicatedSortedBag.predicatedSortedBag(new TreeBag(), NotNullPredicate.INSTANCE);
		output = "";
	}
	
	@Test
	public void testSynchronizedSortedBag() {
		modifiedBag = BagUtils.synchronizedSortedBag(sortedBag);
		modifiedBag.add("apple");
		modifiedBag.add("orange");
		
		synchronized(modifiedBag) {
			Iterator i = modifiedBag.iterator();
			 while (i.hasNext()) {
				 output += i.next() + " ";
			 } 	
		}
		assertTrue(output.equals("apple orange "));
	}

	@After
	public void tearDownSynchronizedSortedBag() throws Exception {
		sortedBag = null;
		modifiedBag = null;
		output = null;
	}
	
	@Before
	public void setUpUnmodifiableSortedBag() throws Exception {
    	sortedBag = PredicatedSortedBag.predicatedSortedBag(new TreeBag(), NotNullPredicate.INSTANCE);
		output = "";
	}
	
	@Test
	public void testUnmodifiableSortedBag() {
		modifiedBag = BagUtils.unmodifiableSortedBag(sortedBag);
		
		// Make sure the sorted Bag is indeed unmodifiable
		// An exception is thrown if one tries to modify an unmodifiable Bag
		try {
			modifiedBag.add("apple", 2);
		} catch (Exception e) {
			output = "exception catched";
		}
		
		assertTrue(output.equals("exception catched"));
	}

	@After
	public void tearDownUnmodifiableSortedBag() throws Exception {
		sortedBag = null;
		modifiedBag = null;
		output = null;
	}
	
	@Before
	public void setUpPredicatedSortedBag() throws Exception {
    	sortedBag = PredicatedSortedBag.predicatedSortedBag(new TreeBag(), NotNullPredicate.INSTANCE);
		oneValueOnly = PredicateUtils.identityPredicate("apple");
		output = "";
	}
	
	@Test
	public void testPredicatedSortedBag() {
		modifiedBag = BagUtils.predicatedSortedBag(sortedBag, oneValueOnly);
		
		// No exception is thrown here
		modifiedBag.add("apple");
		
		// Make sure the bag can only accept "apple" elements and nothing else
		// An exception is thrown if one tries to add something other than an apple
		try {
			modifiedBag.add("orange");
		} catch (Exception e) {
			output = "exception catched";
		}
		
		assertTrue(output.equals("exception catched"));
	}

	@After
	public void tearDownPredicatedSortedBag() throws Exception {
		sortedBag = null;
		modifiedBag = null;
		oneValueOnly = null;
		output = null;
	}
	
	@Before
	public void setUpTransformingSortedBag() throws Exception {
    	sortedBag = PredicatedSortedBag.predicatedSortedBag(new TreeBag(), NotNullPredicate.INSTANCE);
		transform = TransformerUtils.constantTransformer("orange");
		output = "";
	}
	
	@Test
	public void testTransformingSortedBag() {		
		modifiedBag = BagUtils.transformingSortedBag(sortedBag, transform);
		modifiedBag.add("apple");
		output += modifiedBag.iterator().next();
		
		// The transformer used will transform any input to "orange"
		assertTrue(output.equals("orange"));
	}

	@After
	public void tearDownTransformingSortedBag() throws Exception {
		sortedBag = null;
		modifiedBag = null;
		transform = null;
		output = null;
	}
	
	@Before
	public void setUpEmptyBag() throws Exception {
		bag = BagUtils.synchronizedBag(new HashBag());
		bag.add("apple", 4);
		output = "";
	}
	
	@Test
	public void testEmptyBag() {		
		bag = BagUtils.emptyBag();
		output += bag.size();
		
		// Since the bag is empty, there is 0 element inside
		assertTrue(output.equals("0"));
	}

	@After
	public void tearDownEmptyBag() throws Exception {
		bag = null;
		output = null;
	}
	
	@Before
	public void setUpEmptySortedBag() throws Exception {
    	sortedBag = PredicatedSortedBag.predicatedSortedBag(new TreeBag(), NotNullPredicate.INSTANCE);
		sortedBag.add("apple", 4);
		output = "";
	}
	
	@Test
	public void testEmptySortedBag() {		
		sortedBag = BagUtils.emptySortedBag();
		output += sortedBag.size();
		
		// Since the bag is empty, there is 0 element inside
		assertTrue(output.equals("0"));
	}

	@After
	public void tearDownEmptySortedBag() throws Exception {
		sortedBag = null;
		output = null;
	}

}
