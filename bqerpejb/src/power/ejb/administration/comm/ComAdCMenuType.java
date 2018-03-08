/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.comm;

/**
 * AdCRight entity.
 * 
 * @author xsTan
 */
public class ComAdCMenuType implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	/** 类别编码 */
	private String strMenuTypeCode;
	/** 类别名称 */
	private String strMenuTypeName;

	// Constructors
	public ComAdCMenuType() {
	}
	
	public ComAdCMenuType(String strMenuTypeCode, String strMenuTypeName) {
		this.strMenuTypeCode = strMenuTypeCode;
		this.strMenuTypeName = strMenuTypeName;
	}

	/**
	 * @return 类别编码
	 */
	public String getStrMenuTypeCode() {
		return strMenuTypeCode;
	}

	/**
	 * @param 类别编码
	 */
	public void setStrMenuTypeCode(String strMenuTypeCode) {
		this.strMenuTypeCode = strMenuTypeCode;
	}

	/**
	 * @return 类别名称
	 */
	public String getStrMenuTypeName() {
		return strMenuTypeName;
	}

	/**
	 * @param 类别名称
	 */
	public void setStrMenuTypeName(String strMenuTypeName) {
		this.strMenuTypeName = strMenuTypeName;
	}

}