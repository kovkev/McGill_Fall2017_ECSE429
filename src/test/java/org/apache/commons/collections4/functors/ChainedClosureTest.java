package org.apache.commons.collections4.functors;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
	
	static ChainedClosure closureThatDoesNothing;
	static ChainedClosure closureNotNull;
	static Closure c;
	static Closure closures[] = new Closure[2];
	static ChainedClosure collection;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		closureThatDoesNothing = new ChainedClosure(ClosureUtils.nopClosure());
		c = new Closure()
		  {
		    public void execute(Object o){
		      assert o != null;
		      System.out.print(o.toString() + " ");
		    }
		  };
			
		closureNotNull = new ChainedClosure(c);
		closures[0] = c;
		closures[1] = ClosureUtils.nopClosure();
		//collection = ChainedClosure.chainedClosure(closures);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// set all to null?
	}

	@Test
	public void testExecute() {
		closureNotNull.execute("Hello");
	}
	
	@Test
	public void testEmptyClosure() {
		assertSame(NOPClosure.nopClosure(), ChainedClosure.chainedClosure());
	}
	
	@Test
	public void testNonEmptyClosure() {
		assertSame(closureNotNull, ChainedClosure.chainedClosure(c)); // TODO: HOW TO GET THIS TO WORK
	}
	
	@Test
	public void testGetClosure() {
		assertSame(c, closureNotNull.getClosures()); // TODO: HOW TO GET THIS TO WORK
		// or THIS assertSame(new ChainedClosure(c), closureNotNull.getClosures());
	}

	@Test
	public void test() {
		// TODO: deal with last method
	}
}
