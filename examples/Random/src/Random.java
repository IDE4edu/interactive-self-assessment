/* 
 * some comment here
 */
public class Random {

	// This method should return a random integer between one and ten.
	public int randomOneToTen() {
		// Assume we have a method getRandom() which returns a double with range
		// [0,1).
		// Fill in the textbox below to change that into a random integer with
		// range [1,10].
		return (int) (getRandom() * 5);

	}

	private double getRandom() {
		// Now let's fill in the getRandom() method. Create a method in the
		// textarea below to return a random double with value [0,1).
		return (-2.0);
	}

	// This is some code the student should not see.

	public boolean invisible() {
		return true;
	}

	// End

}
