package org.apache.commons.collections4.functors;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.ClosureUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class ChainedClosureTest {
	
	static Integer myInt;
	Closure nopClosure;
	ChainedClosure<Integer> chained;
	static Closure<Integer> multiplyByFour;
	static Closure<Integer> subtractTwo;
	List encryption;
	static String output;
	Closure<? super Integer>[] copy;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		myInt = 4;
		output = "";
		subtractTwo = new Closure<Integer>() {
			public void execute(Integer i){
			      output += " " + (i-2);
			}
		};
		multiplyByFour = new Closure<Integer>() {
			public void execute(Integer i){
			      output += " " + (4*i);
			}
		};
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		myInt = null;
		subtractTwo = null;
		multiplyByFour = null;
	}
	
	@Before
	public void setRegularClosureConstructor() throws Exception {
		output = "" + myInt;
	}

	@Test
	public void testRegularClosureConstructor() {
		chained = new ChainedClosure<Integer>(multiplyByFour);
		chained.execute(myInt);
		assertTrue(output.equals("4 16"));
	}
	
	@After
	public void tearRegularClosureConstructor() throws Exception {
		chained = null;
		output = null;
	}
	
	@Before
	public void setStaticClosureMethod() throws Exception {
		output = "" + myInt;
	}

	@Test
	public void testStaticClosureMethod() {
		nopClosure = ChainedClosure.chainedClosure();
		nopClosure.execute(myInt);
		assertTrue(output.equals("4"));
		
		chained = (ChainedClosure<Integer>) ChainedClosure.chainedClosure(subtractTwo);
		chained.execute(myInt);
		assertTrue(output.equals("4 2"));
	}
	
	@After
	public void tearStaticClosureMethod() throws Exception {
		chained = null;
		output = null;
		nopClosure = null;
	}
	
	@Before
	public void setStaticCollectionConstructorAndGetter() throws Exception {
		output = "" + myInt;
		encryption = new ArrayList(2);
	}

	@Test(expected = NullPointerException.class)
	public void testStaticCollectionConstructor() {
		nopClosure = ChainedClosure.chainedClosure(encryption);
		nopClosure.execute(myInt);
		assertTrue(output.equals("4"));
		
		encryption.add(multiplyByFour);
		encryption.add(subtractTwo);
		chained = (ChainedClosure<Integer>) ChainedClosure.chainedClosure(encryption);
		chained.execute(myInt);
		assertTrue(output.equals("4 16 2"));
		
		encryption = null;
		nopClosure = ChainedClosure.chainedClosure(encryption);
	}
	
	@After
	public void tearStaticCollectionConstructor() throws Exception {
		chained = null;
		output = null;
		encryption = null;
		nopClosure = null;
	}
	
	@Before
	public void setGetClosures() throws Exception {
		encryption = new ArrayList(2);
	}

	@Test
	public void testGetClosures() {
		encryption.add(multiplyByFour);
		encryption.add(subtractTwo);
		chained = (ChainedClosure<Integer>) ChainedClosure.chainedClosure(encryption);
		chained.execute(myInt);
		assertTrue(output.equals("4 16 2"));
		
		output = "" + myInt;
		copy = chained.getClosures();
		chained = (ChainedClosure<Integer>) ChainedClosure.chainedClosure(copy);
		chained.execute(myInt);
		assertTrue(output.equals("4 16 2"));
	}
	
	@After
	public void tearGetClosures() throws Exception {
		chained = null;
		output = null;
		encryption = null;
		copy = null;
	}
}

