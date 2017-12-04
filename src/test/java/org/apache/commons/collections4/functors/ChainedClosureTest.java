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
	
	Integer myInt;
	static ChainedClosure<Integer> chained;
	static Closure<Integer> multiplyByFour;
	static Closure<Integer> subtractTwo;
	static List encryption = new ArrayList(2);
	String output;
	
	@SuppressWarnings("unchecked")
	
	@Before
	public void setRegularClosureConstructor() throws Exception {
		myInt = 4;
		multiplyByFour = new Closure<Integer>() {
			public void execute(Integer i){
			      output += " " + (4*i);
			}
		};
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
		myInt = null;
		multiplyByFour = null;
		subtractTwo = null;
		chained = null;
		output = null;
	}
	
	// testStaticConstructor
	// testCollectionConstructor
	// testGetClosures
}

