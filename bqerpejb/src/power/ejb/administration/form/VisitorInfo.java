/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * 来访人员bean
 * @author chaihao
 * 
 */
public class VisitorInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 来访人 */
	private String insertBy;
	/** 来访日期 */
	private String insertDate;
	/** 证件号 */
	private String paperId;
	/** 来访人单位 */
	private String firm;
	/** 被访人 */
	private String name;
	/** 被访人部门 */
	private String depName;
	/** 进厂时间 */
	private String inDate;
	/** 出厂时间 */
	private String outDate;
	/** 备注 */
	private String note;
	/** 值班人 */
	private String onDuty;
	/** 证件类别 */
	private String paperTypeName;

	/**
	 * @return 来访人
	 */
	public String getInsertBy() {
		return insertBy;
	}

	/**
	 * @param 来访人
	 *            设置来访人
	 */
	public void setInsertBy(String insertBy) {
		this.insertBy = insertBy;
	}

	/**
	 * @return 来访日期
	 */
	public String getInsertDate() {
		return insertDate;
	}

	/**
	 * @param 来访日期
	 *            设置来访日期
	 */
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}

	/**
	 * @return 证件类别
	 */
	public String getPaperTypeName() {
		return paperTypeName;
	}

	/**
	 * @param 证件类别
	 *            设置证件类别
	 */
	public void setPaperTypeName(String paperTypeName) {
		this.paperTypeName = paperTypeName;
	}

	/**
	 * @return 证件号
	 */
	public String getPaperId() {
		return paperId;
	}

	/**
	 * @param 证件号
	 *            设置证件号
	 */
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	/**
	 * @return 来访人单位
	 */
	public String getFirm() {
		return firm;
	}

	/**
	 * @param 来访人单位
	 *            设置来访人单位
	 */
	public void setFirm(String firm) {
		this.firm = firm;
	}

	/**
	 * @return 被访人
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param 被访人
	 *            设置被访人
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 被访人部门
	 */
	public String getDepName() {
		return depName;
	}

	/**
	 * @param 被访人部门
	 *            设置被访人部门
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/**
	 * @return 进厂时间
	 */
	public String getInDate() {
		return inDate;
	}

	/**
	 * @param 进厂时间
	 *            设置进厂时间
	 */
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	/**
	 * @return 出厂时间
	 */
	public String getOutDate() {
		return outDate;
	}

	/**
	 * @param 出厂时间
	 *            设置出厂时间
	 */
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	/**
	 * @return 备注
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param 备注
	 *            设置备注
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return 值班人
	 */
	public String getOnDuty() {
		return onDuty;
	}

	/**
	 * @param 值班人
	 *            设置值班人
	 */
	public void setOnDuty(String onDuty) {
		this.onDuty = onDuty;
	}
}