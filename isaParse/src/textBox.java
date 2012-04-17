public class textBox {
	private String name;
	private String header;
	//TODO start and end are ints
	private String start;
	private String end;
	
	public textBox(String name, String header, String start, String end){
		setName(name);
		setHeader(header);
		setStart(start);
		setEnd(end);
		
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
	 * @return this.start
	 */
	public String getStart() {
		return start;
	}
	
	/**
	 * @param start set to this.start
	 */
	public void setStart(String start) {
		this.start = start;
	}
	
	/**
	 * @return this.end
	 */
	public String getEnd() {
		return end;
	}
	
	/**
	 * @param end set to this.end
	 */
	public void setEnd(String end) {
		this.end = end;
	}



}