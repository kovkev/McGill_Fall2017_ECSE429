package org.apache.commons.collections4.functors;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.ClosureUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ChainedClosureTest {
	
	static ChainedClosure closureThatDoesNothing;
	static ChainedClosure<String> closureNotNull;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		closureThatDoesNothing = new ChainedClosure(ClosureUtils.nopClosure());
		closureNotNull = new ChainedClosure(new Closure()
		  {
		    public void execute(Object o){
		      assert o != null;
		      System.out.print(o.toString() + " ");
		    }
		  }
		  );
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
	public void test() {
		closureNotNull.execute("Hello");
	}

}
