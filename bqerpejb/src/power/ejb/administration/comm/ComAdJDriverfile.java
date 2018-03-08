/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.comm;

/**
 * AdJDriverfile entity.
 * 
 * @author xsTan
 */
public class ComAdJDriverfile implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
	/** 司机编号 */
	private String driverCode;
	/** 姓名 */
	private String driverName;
	// Constructors

	/** default constructor */
	public ComAdJDriverfile() {
	}

	/** full constructor */
	public ComAdJDriverfile(String driverCode, String driverName) {
		this.driverCode = driverCode;
		this.driverName = driverName;
	}

	/**
	 * @return 司机编号
	 */
	public String getDriverCode() {
		return driverCode;
	}

	/**
	 * @param 司机编号
	 */
	public void setDriverCode(String driverCode) {
		this.driverCode = driverCode;
	}

	/**
	 * @return 姓名
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param 姓名
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
}