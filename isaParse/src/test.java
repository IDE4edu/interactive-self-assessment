import java.util.ArrayList;

public class test {
	private String name;
	private ArrayList<jUnit> jUnit;
	private String target;

	public test (String name, ArrayList<jUnit> jUnit, String target){
		setName(name);
		setjUnit(jUnit);
		setTarget(target);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the jUnit
	 */
	public ArrayList<jUnit> getjUnit() {
		return jUnit;
	}

	/**
	 * @param jUnit the jUnit to set
	 */
	public void setjUnit(ArrayList<jUnit> jUnit) {
		this.jUnit = jUnit;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

}
