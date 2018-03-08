/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

/**
 * 合计项名称bean
 * 
 * @author zhengzhipeng
 * 
 */
public class StatItemNameInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields

	/** 合计项名称ID */
	private String statItemId;
	/** 合计项名称 */
	private String statItemName;

	/**
	 * @return the statItemName
	 */
	public String getStatItemName() {
		return statItemName;
	}

	/**
	 * @param statItemName
	 *            the statItemName to set
	 */
	public void setStatItemName(String statItemName) {
		this.statItemName = statItemName;
	}

	/**
	 * @return the statItemId
	 */
	public String getStatItemId() {
		return statItemId;
	}

	/**
	 * @param statItemId
	 *            the statItemId to set
	 */
	public void setStatItemId(String statItemId) {
		this.statItemId = statItemId;
	}

}
