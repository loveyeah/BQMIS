/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

/**
 * 组织结构图Bean
 * @author zhujie
 *
 */
public class OrganizationOutputBean implements java.io.Serializable{

	/** 部门ID */
	private String DEPT_ID = "";
	/** 父父部门ID */
	private String PDEPT_ID = "";
	/** 部门名称 */
	private String DEPT_NAME = "";
	/** 排序 */
	private int ORDER_BY = 0;
	/** 部门级别 */
	private String DEPT_LEVEL = "";
	/** 排序用数组 */
	private int[] orderNumber = new int[]{0,0,0,0,0};
	/**
	 * @return the dEPT_ID
	 */
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	/**
	 * @param dept_id the dEPT_ID to set
	 */
	public void setDEPT_ID(String dept_id) {
		DEPT_ID = dept_id;
	}
	/**
	 * @return the pDEPT_ID
	 */
	public String getPDEPT_ID() {
		return PDEPT_ID;
	}
	/**
	 * @param pdept_id the pDEPT_ID to set
	 */
	public void setPDEPT_ID(String pdept_id) {
		PDEPT_ID = pdept_id;
	}
	/**
	 * @return the dEPT_NAME
	 */
	public String getDEPT_NAME() {
		return DEPT_NAME;
	}
	/**
	 * @param dept_name the dEPT_NAME to set
	 */
	public void setDEPT_NAME(String dept_name) {
		DEPT_NAME = dept_name;
	}

	/**
	 * @return the dEPT_LEVEL
	 */
	public String getDEPT_LEVEL() {
		return DEPT_LEVEL;
	}
	/**
	 * @param dept_level the dEPT_LEVEL to set
	 */
	public void setDEPT_LEVEL(String dept_level) {
		DEPT_LEVEL = dept_level;
	}
	/**
	 * @return the orderNumber
	 */
	public int[] getOrderNumber() {
		return orderNumber;
	}
	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(int[] orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * @return the oRDER_BY
	 */
	public int getORDER_BY() {
		return ORDER_BY;
	}
	/**
	 * @param order_by the oRDER_BY to set
	 */
	public void setORDER_BY(int order_by) {
		ORDER_BY = order_by;
	}
}
