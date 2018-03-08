/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.comm;

/**
 * AdCDutytype entity.
 * 
 * @author xsTan
 */
public class ComAdCDutytype implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
	/** 值别 */
	private String dutyType;
	/** 值别名称 */
	private String dutyTypeName;

	/** default constructor */
	public ComAdCDutytype() {
	}

	/** full constructor */
	public ComAdCDutytype(String dutyType, String dutyTypeName) {
		this.dutyType = dutyType;
		this.dutyTypeName = dutyTypeName;
	}

	/**
	 * @return 值别
	 */
	public String getDutyType() {
		return dutyType;
	}

	/**
	 * @param 值别
	 */
	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	/**
	 * @return 值别名称
	 */
	public String getDutyTypeName() {
		return dutyTypeName;
	}

	/**
	 * @param 值别名称
	 */
	public void setDutyTypeName(String dutyTypeName) {
		this.dutyTypeName = dutyTypeName;
	}
}