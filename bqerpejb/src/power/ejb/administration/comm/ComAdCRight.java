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
public class ComAdCRight implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
	/** 工作类别 */
	private String worktypeCode;
	/** 工作类别名 */
	private String worktypeName;
	/** 工作类别名 */
	private String userCode;

	// Constructors

	public ComAdCRight() {
	}
	
	public ComAdCRight(String worktypeCode) {
		this.worktypeCode = worktypeCode;
	}

	/**
	 * @return 工作类别
	 */
	public String getWorktypeCode() {
		return this.worktypeCode;
	}

	/**
	 * @param 工作类别
	 */
	public void setWorktypeCode(String worktypeCode) {
		this.worktypeCode = worktypeCode;
	}

	/**
	 * @return the worktypeName
	 */
	public String getWorktypeName() {
		return worktypeName;
	}

	/**
	 * @param worktypeName the worktypeName to set
	 */
	public void setWorktypeName(String worktypeName) {
		this.worktypeName = worktypeName;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
}