public class textArea {
	private String name;
	private String header;
	private int size;
	
	public textArea(String name, String header, int size){
		setName(name);
		setHeader(header);
		setSize(size);
	}
	
	/**
	 * @param name set to this.name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param header set to this.header
	 */
	public void setHeader(String header) {
		this.header = header;
	}
	
	
	/**
	 * @param size set to this.size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return this.name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return this.header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @return this.size
	 */
	public int getSize() {
		return size;
	}

}