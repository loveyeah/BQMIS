/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.comm;

/**
 * AdCWorktype entity.
 * 
 * @author xsTan
 */
public class ComAdCWorktype implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
	/** 子类别编码 */
	private String subWorktypeCode;
	/** 子类别名称 */
	private String subWorktypeName;

	// Constructors

	/** default constructor */
	public ComAdCWorktype() {
	}

	/** full constructor */
	public ComAdCWorktype(String subWorktypeCode, String subWorktypeName) {
		this.subWorktypeCode = subWorktypeCode;
		this.subWorktypeName = subWorktypeName;
	}

	/**
	 * @return 子类别编码
	 */
	public String getSubWorktypeCode() {
		return subWorktypeCode;
	}

	/**
	 * @param 子类别编码
	 */
	public void setSubWorktypeCode(String subWorktypeCode) {
		this.subWorktypeCode = subWorktypeCode;
	}

	/**
	 * @return 子类别名称
	 */
	public String getSubWorktypeName() {
		return subWorktypeName;
	}

	/**
	 * @param 子类别名称
	 */
	public void setSubWorktypeName(String subWorktypeName) {
		this.subWorktypeName = subWorktypeName;
	}
}