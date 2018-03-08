package power.ejb.administration.comm;

import javax.persistence.Column;

public class WorkerEmployeeInform implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	/** 人员编码表.姓名*/
	private String name;
	/** 人员基本信息表.人员编码*/
	private String workerCode;
	// Constructors

	/** default constructor */
	public WorkerEmployeeInform() {
	}

	/** max constructor */
	public WorkerEmployeeInform(String name, String workerCode) {
		this.name = name;
		this.workerCode = workerCode;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME", length = 20)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the workerCode
	 */
	@Column(name = "WORKER_CODE", length = 6)
	public String getWorkerCode() {
		return workerCode;
	}

	/**
	 * @param workerCode the workerCode to set
	 */
	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}
}