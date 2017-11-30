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

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		ChainedClosure closureThatDoesNothing = new ChainedClosure(ClosureUtils.nopClosure());
		ChainedClosure<String> closureNotNull = new ChainedClosure(new Closure()
		  {
		    public void execute(Object o){
		      assert o != null;
		      System.out.print(o.toString() + " ");
		    }
		  }
		  );
		closureNotNull.execute("Hello");
	}

}
