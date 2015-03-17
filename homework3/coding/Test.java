
import static org.junit.Assert.*;

import java.util.*;

public class Test {

	@org.junit.Test
	public void depPresBasictest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t2.add(new Attribute("b"));
		
		fds.add(new FunctionalDependency(t1,new Attribute("a")));

		// tables
		// a
		// b
		// fds
		// a -> a
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		
		
		fds.add(new FunctionalDependency(t1, new Attribute("b")));
		// tables
		// a
		// b
		// fds
		// a -> a
		// a -> b
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));
	}

	@org.junit.Test
	public void losslessBasictest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t2.add(new Attribute("b"));
		
		// tables
		// a
		// b
		// fds
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		t1.add(new Attribute("b"));
		// tables
		// a b
		// b
		// fds
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
	}

	@org.junit.Test
	public void depPresFDtest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t2.add(new Attribute("b"));
		
		fds.add(new FunctionalDependency(t1,new Attribute("b")));

		// tables
		// a b
		// b
		// fds
		// ab -> b
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		
		
		fds.add(new FunctionalDependency(t2, new Attribute("a")));
		// tables
		// a b
		// b
		// fds
		// ab -> b
		// b -> a
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
	}

	@org.junit.Test
	public void losslesstest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t2.add(new Attribute("b"));
		t2.add(new Attribute("c"));
		t2.add(new Attribute("d"));
		
		AttributeSet temp = new AttributeSet();
		temp.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp,new Attribute("c")));
		// tables
		// a b
		// b c d
		// fds
		// b -> c
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		fds.add(new FunctionalDependency(temp, new Attribute("d")));
		// tables
		// a b
		// b c d
		// fds
		// b -> c
		// c -> d
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
	}
	
	@org.junit.Test
	public void extralossLessTests(){
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		AttributeSet temp1 = new AttributeSet();
		AttributeSet temp2 = new AttributeSet();
		AttributeSet temp3 = new AttributeSet();
		AttributeSet temp4 = new AttributeSet();
		AttributeSet temp5 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		// tables
		// b c d
		// a c e
		// fds
		// ab -> c
		// c -> e
		// b -> d
		// e -> a
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		t1.add(new Attribute("d"));
		t2.add(new Attribute("a"));
		t2.add(new Attribute("c"));
		t2.add(new Attribute("e"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1, new Attribute("c")));
		
		temp2.add(new Attribute("c"));
		fds.add(new FunctionalDependency(temp2, new Attribute("e")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3, new Attribute("d")));
		
		temp4.add(new Attribute("e"));
		fds.add(new FunctionalDependency(temp4, new Attribute("a")));
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		
		t1.clear();
		t2.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		temp4.clear();
		fds.clear();

		// tables
		// a b
		// a c
		// fds
		// a -> b
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t2.add(new Attribute("a"));
		t2.add(new Attribute("c"));
		
		temp1.add(new Attribute("a"));
		fds.add(new FunctionalDependency(temp1, new Attribute("b")));
		
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		
		t1.clear();
		t2.clear();
		temp1.clear();
		fds.clear();

		// tables
		// a b
		// b c
		// fds
		// a -> b
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t2.add(new Attribute("b"));
		t2.add(new Attribute("c"));
		
		temp1.add(new Attribute("a"));
		fds.add(new FunctionalDependency(temp1, new Attribute("b")));
		
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		t1.clear();
		t2.clear();
		temp1.clear();
		fds.clear();

		// tables
		// c z
		// s z
		// fds
		// cs -> z
		// z -> c
		t1.add(new Attribute("c"));
		t1.add(new Attribute("z"));
		t2.add(new Attribute("s"));
		t2.add(new Attribute("z"));
		
		temp1.add(new Attribute("c"));
		temp1.add(new Attribute("s"));
		fds.add(new FunctionalDependency(temp1, new Attribute("z")));
		
		temp2.add(new Attribute("z"));
		fds.add(new FunctionalDependency(temp2, new Attribute("c")));
		
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		
		t1.clear();
		t2.clear();
		temp1.clear();
		temp2.clear();
		fds.clear();

		// tables
		// a b c
		// a d e
		// fds
		// a -> b
		// a -> c
		// cd -> e
		// b -> d
		// e -> a
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		t2.add(new Attribute("a"));
		t2.add(new Attribute("d"));
		t2.add(new Attribute("e"));
		
		temp1.add(new Attribute("a"));
		fds.add(new FunctionalDependency(temp1, new Attribute("b")));
		
		temp2.add(new Attribute("a"));
		fds.add(new FunctionalDependency(temp2, new Attribute("c")));
		
		temp3.add(new Attribute("c"));
		temp3.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp3, new Attribute("e")));
		
		temp4.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp4, new Attribute("d")));
		
		temp5.add(new Attribute("e"));
		fds.add(new FunctionalDependency(temp5, new Attribute("a")));
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		
		t1.clear();
		t2.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		temp4.clear();
		temp5.clear();
		fds.clear();

		// tables
		// a b c
		// c d e
		// fds
		// a -> b
		// a -> c
		// cd -> e
		// b -> d
		// e -> a
		// e -> a
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		t2.add(new Attribute("c"));
		t2.add(new Attribute("d"));
		t2.add(new Attribute("e"));
		
		temp1.add(new Attribute("a"));
		fds.add(new FunctionalDependency(temp1, new Attribute("b")));
		
		temp2.add(new Attribute("a"));
		fds.add(new FunctionalDependency(temp2, new Attribute("c")));
		
		temp3.add(new Attribute("c"));
		temp3.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp3, new Attribute("e")));
		
		temp4.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp4, new Attribute("d")));
		
		temp5.add(new Attribute("e"));
		fds.add(new FunctionalDependency(temp5, new Attribute("a")));
		fds.add(new FunctionalDependency(temp5, new Attribute("a")));
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		t1.clear();
		t2.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		temp4.clear();
		temp5.clear();
		fds.clear();

		// tables
		// a b c
		// c d e
		// fds
		// ab -> c
		// c -> d
		// b -> e
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		t2.add(new Attribute("c"));
		t2.add(new Attribute("d"));
		t2.add(new Attribute("e"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1, new Attribute("c")));
		
		temp2.add(new Attribute("c"));
		fds.add(new FunctionalDependency(temp2, new Attribute("d")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3, new Attribute("e")));
		
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		t1.clear();
		t2.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		fds.clear();

		// tables
		// a b c d
		// a b e
		// fds
		// ab -> c
		// c -> d
		// b -> e
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		t1.add(new Attribute("d"));
		t2.add(new Attribute("a"));
		t2.add(new Attribute("b"));
		t2.add(new Attribute("e"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1, new Attribute("c")));
		
		temp2.add(new Attribute("c"));
		fds.add(new FunctionalDependency(temp2, new Attribute("d")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3, new Attribute("e")));
		
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		
		t1.clear();
		t2.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		fds.clear();

		// tables
		// a b c d
		// b e
		// fds
		// ab -> c
		// c -> d
		// b -> e
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		t1.add(new Attribute("d"));
		t2.add(new Attribute("b"));
		t2.add(new Attribute("e"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1, new Attribute("c")));
		
		temp2.add(new Attribute("c"));
		fds.add(new FunctionalDependency(temp2, new Attribute("d")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3, new Attribute("e")));
		
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		
		t1.clear();
		t2.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		fds.clear();
	}
/*	@org.junit.Test
	public void closureTests(){
		AttributeSet t1 = new AttributeSet();
		AttributeSet temp1 = new AttributeSet();
		AttributeSet temp2 = new AttributeSet();
		AttributeSet temp3 = new AttributeSet();
		AttributeSet temp4 = new AttributeSet();
		AttributeSet temp5 = new AttributeSet();
		AttributeSet correct = new AttributeSet();
		AttributeSet test = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		//attrs
		//a e
		//fds
		//a->d
		//ab ->e
		//bi->e
		//cd->i
		//e->c
		//expected
		//acdei
		t1.add(new Attribute("a"));
		t1.add(new Attribute("e"));
		
		temp1.add(new Attribute("a"));
		fds.add(new FunctionalDependency(temp1,new Attribute("d")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp2,new Attribute("e")));
		
		temp3.add(new Attribute("b"));
		temp3.add(new Attribute("i"));
		fds.add(new FunctionalDependency(temp3,new Attribute("e")));
		
		temp4.add(new Attribute("c"));
		temp4.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp4,new Attribute("i")));
		
		temp5.add(new Attribute("e"));
		fds.add(new FunctionalDependency(temp5,new Attribute("c")));
		
		correct.add(new Attribute("a"));
		correct.add(new Attribute("c"));
		correct.add(new Attribute("d"));
		correct.add(new Attribute("e"));
		correct.add(new Attribute("i"));
		
		test = FDChecker.closure(t1, fds);
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		temp4.clear();
		temp5.clear();
		correct.clear();
		fds.clear();
		test.clear();
		
		
		// attrs
		// a g
		// fds
		// a -> b
		// a -> c
		// cg -> h
		// cg -> i
		// b -> h
		// expected
		// a b c g h i
		t1.add(new Attribute("a"));
		t1.add(new Attribute("g"));
		
		
		temp1.add(new Attribute("a"));
		fds.add(new FunctionalDependency(temp1,new Attribute("b")));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("c"));
		temp2.add(new Attribute("g"));
		fds.add(new FunctionalDependency(temp2, new Attribute("h")));
		fds.add(new FunctionalDependency(temp2, new Attribute("i")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3, new Attribute("h")));
		
		correct.add(new Attribute("a"));
		correct.add(new Attribute("b"));
		correct.add(new Attribute("c"));
		correct.add(new Attribute("g"));
		correct.add(new Attribute("h"));
		correct.add(new Attribute("i"));
		
		test = FDChecker.closure(t1, fds);
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();
		
		// attrs
		// a c
		// fds
		// ab -> e
		// ad -> b
		// b -> c
		// c -> d
		// expected
		// a b c d e
		t1.add(new Attribute("a"));
		t1.add(new Attribute("c"));
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("e")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("c")));
		
		temp4.add(new Attribute("c"));
		fds.add(new FunctionalDependency(temp4,new Attribute("d")));
		
		correct.add(new Attribute("a"));
		correct.add(new Attribute("b"));
		correct.add(new Attribute("c"));
		correct.add(new Attribute("d"));
		correct.add(new Attribute("e"));
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		temp4.clear();
		correct.clear();
		fds.clear();
		test.clear();
		// attrs
		// a
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// a
		t1.add(new Attribute("a"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("a"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();

		// attrs
		// b
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// b d
		t1.add(new Attribute("b"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("b"));
		correct.add(new Attribute("d"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();
		// attrs
		// c
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// c
		t1.add(new Attribute("c"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("c"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();
		// attrs
		// d
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// d
		t1.add(new Attribute("d"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("d"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();
		// attrs
		// a b
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// a b c d
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("a"));
		correct.add(new Attribute("b"));
		correct.add(new Attribute("c"));
		correct.add(new Attribute("d"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();
		// attrs
		// a c
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// a c
		t1.add(new Attribute("a"));
		t1.add(new Attribute("c"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("a"));
		correct.add(new Attribute("c"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();
		// attrs
		// a d
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// a b c d
		t1.add(new Attribute("a"));
		t1.add(new Attribute("d"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("a"));
		correct.add(new Attribute("b"));
		correct.add(new Attribute("c"));
		correct.add(new Attribute("d"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();
		// attrs
		// a b c
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// a b c d
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("a"));
		correct.add(new Attribute("b"));
		correct.add(new Attribute("c"));
		correct.add(new Attribute("d"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();
		// attrs
		// a b d
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// a b c d
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t1.add(new Attribute("d"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("a"));
		correct.add(new Attribute("b"));
		correct.add(new Attribute("c"));
		correct.add(new Attribute("d"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();

		// attrs
		// a c d
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// a b c d
		t1.add(new Attribute("a"));
		t1.add(new Attribute("c"));
		t1.add(new Attribute("d"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("a"));
		correct.add(new Attribute("b"));
		correct.add(new Attribute("c"));
		correct.add(new Attribute("d"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();

		// attrs
		// b c d
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// b c d
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		t1.add(new Attribute("d"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("b"));
		correct.add(new Attribute("c"));
		correct.add(new Attribute("d"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();
		// attrs
		// a b c d
		// fds
		// ab -> c
		// ad -> b
		// b -> d
		// expected
		// a b c d
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		t1.add(new Attribute("c"));
		
		temp1.add(new Attribute("a"));
		temp1.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp1,new Attribute("c")));
		
		temp2.add(new Attribute("a"));
		temp2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(temp2,new Attribute("b")));
		
		temp3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp3,new Attribute("d")));
		
		correct.add(new Attribute("a"));
		correct.add(new Attribute("b"));
		correct.add(new Attribute("c"));
		correct.add(new Attribute("d"));
		
		test = FDChecker.closure(t1, fds);
		
		assertEquals(correct, test);
		
		t1.clear();
		temp1.clear();
		temp2.clear();
		temp3.clear();
		correct.clear();
		fds.clear();
		test.clear();
	}
	
	@org.junit.Test
	public void depPresOtherTests() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		AttributeSet t3 = new AttributeSet();
		AttributeSet t4 = new AttributeSet();
		AttributeSet t5 = new AttributeSet();
		
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		
		t2.add(new Attribute("c"));
		t2.add(new Attribute("d"));
		
		t3.add(new Attribute("a"));
		t4.add(new Attribute("b"));
		t5.add(new Attribute("c"));
		
		fds.add(new FunctionalDependency(t3,new Attribute("b")));
		fds.add(new FunctionalDependency(t4,new Attribute("c")));
		fds.add(new FunctionalDependency(t5,new Attribute("d")));
		// tables
		// a b c
		// c d
		// fds
		// a -> b
		// b -> c
		// c -> d
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		
		t1.clear();
		t2.clear();

		t1.add(new Attribute("a"));
		t1.add(new Attribute("c"));
		t1.add(new Attribute("d"));
		
		t2.add(new Attribute("b"));
		t2.add(new Attribute("c"));
		// tables
		// a c d
		// b c
		// fds
		// a -> b
		// b -> c
		// c -> d
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));
	}
	*/
}
