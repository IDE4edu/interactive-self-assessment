public class assertion {
	private String name;
	private String method;
	private String target;
	private String params;
	private String msg;
	
	public assertion (String name, String method, String target, String params, String msg){
		setName(name);
		setMethod(method);
		setTarget(target);
		setParams(params);
		setMsg(msg);
	}
	
	/**
	 * @return this.name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name set to this.name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return this.method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method set to this.method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	
	/**
	 * @return this.target
	 */
	public String getTarget() {
		return target;
	}

	
	/**
	 * @param target set to this.target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	
	/**
	 * @return this.params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param params set to this.params
	 */
	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * @return this.msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg set to this.msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}




}