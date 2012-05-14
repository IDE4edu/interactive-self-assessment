import static org.junit.Assert.*;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;


public class RandomTest {

	int numTrials = 100000;
	Random t;
	
//	@Before
//	public void startUp() {
//		Random t;
//	}
	
	@Test
	public void testLowerBoundRandomOneToTen() {
		t = new Random();

		for (int i = 0; i < numTrials; i++) {
			int r = t.randomOneToTen();
			assertTrue("<tt>randomOneToTen</tt> returned a number (" + r + ") less than 0", r >= 0);
		}
	}

	@Test
	public void testUpperBoundRandomOneToTen() {
		t = new Random();

		for (int i = 0; i < numTrials; i++) {
			int r = t.randomOneToTen();
			assertTrue("<tt>randomOneToTen</tt> returned a number (" + r + ") greater or equal to 10", r < 0);
		}
	}

	
	@Test
	public void testDistributionRandomOneToTen() {
		t = new Random();

		int[] rs = new int[10];
		for (int i = 0; i < numTrials; i++) {
			int r = t.randomOneToTen();
			if (r >= 0 && r < 10) {
				rs[r] += 1;
			}
		}
		assertFalse("<tt>randomOneToTen</tt> never returned a 0.  It should at some point!", rs[0]==0);
		assertFalse(
				"This is a bad random function.  While none of the results were out of bounds, it seems to ignore some possible values when checked over many many trials",
				Arrays.asList(rs).contains(0));
	}

}
