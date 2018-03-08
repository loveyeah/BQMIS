/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

/**
 * 个性签名维护Bean
 * 
 * @author jincong
 * @version 1.0
 */
public class SignPictureInfo implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// 个性签名维护表
	/** 序号 */
	private Long id;
	/** 人员编码 */
	private String workerCode;
	
	// 人员编码表
	/** 姓名 */
	private String workerName;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the workerCode
	 */
	public String getWorkerCode() {
		return workerCode;
	}

	/**
	 * @param workerCode the workerCode to set
	 */
	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	/**
	 * @return the workerName
	 */
	public String getWorkerName() {
		return workerName;
	}

	/**
	 * @param workerName the workerName to set
	 */
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
}
