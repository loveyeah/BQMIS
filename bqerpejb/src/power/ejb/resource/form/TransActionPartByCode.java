package power.ejb.resource.form;

public class TransActionPartByCode implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 1事务作用表 事务名称*/
	private String transName;
	/** 2事务作用表 事务编码 */
	private String transCode;
	/**
	 * @return the transName
	 */
	public String getTransName() {
		return transName;
	}
	/**
	 * @param transName the transName to set
	 */
	public void setTransName(String transName) {
		this.transName = transName;
	}
	/**
	 * @return the transCode
	 */
	public String getTransCode() {
		return transCode;
	}
	/**
	 * @param transCode the transCode to set
	 */
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
}
