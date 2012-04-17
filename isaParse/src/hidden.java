public class hidden {
	private String name;
	private String tag;
	
	public hidden (String name, String tag){
		setName(name);
		setTag(tag);
	}

	/**
	 * @return this.name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return this.tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param name set to this.name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param set to this.tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

}
