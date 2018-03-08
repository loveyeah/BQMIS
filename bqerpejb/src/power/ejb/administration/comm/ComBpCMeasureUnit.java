package power.ejb.administration.comm;

/**
 * AdCRight entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class ComBpCMeasureUnit implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	/** 计量单位标识 */
	private String strUnitID;
	/** 计量单位名称 */
	private String strUnitName;

	// Constructors
	public ComBpCMeasureUnit() {
	}
	
	public ComBpCMeasureUnit(String strUnitID, String strUnitName) {
		this.strUnitID = strUnitID;
		this.strUnitName = strUnitName;
	}

	/**
	 * @return 计量单位标识
	 */
	public String getStrUnitID() {
		return strUnitID;
	}

	/**
	 * @param 计量单位标识
	 */
	public void setStrUnitID(String strUnitID) {
		this.strUnitID = strUnitID;
	}

	/**
	 * @return 计量单位名称
	 */
	public String getStrUnitName() {
		return strUnitName;
	}

	/**
	 * @param 计量单位名称
	 */
	public void setStrUnitName(String strUnitName) {
		this.strUnitName = strUnitName;
	}
}