/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

import java.util.Date;

// default package

/**
 * 定期工作登记 entity.
 * 
 * @author daichunlin
 */
public class VisitRegisterInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** 来访人员登记表 */
	/** 序号 */
	private Long id ;
	/** 来访人 */
	private String insertBy;
	/** 日期 */
	private String insertDate;
	/** 证件号 */
	private String paperId;
	/** 单位 */
	private String firm;
	/** 被访人 */
	private String visitedMan;
	/** 被访人部门 */
	private String visitedDep;
	/** 进厂时间 */
	private String inDate;
	/** 出厂时间 */
	private String outDate;
	/** 备注 */
	private String note;
	/** 值班人 */
	private String onDuty;
	/** 证件类别 */
	private String papertypeCd;
	/** 更新时间 */
	private Date updateTime;
	/** 更新者 */
	private String updateUser;
	/** 是否可用 */
	private String isUse;
	/** 人员基础信息表 */
	/** 中文名 */
	private String chsName;
	/** 部门信息表 */
	/** 部门名 */
	private String deptName;

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
	 * @return the insertBy
	 */
	public String getInsertBy() {
		return insertBy;
	}
	/**
	 * @param insertBy the insertBy to set
	 */
	public void setInsertBy(String insertBy) {
		this.insertBy = insertBy;
	}
	/**
	 * @return the insertDate
	 */
	public String getInsertDate() {
		return insertDate;
	}
	/**
	 * @param insertDate the insertDate to set
	 */
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	/**
	 * @return the paperId
	 */
	public String getPaperId() {
		return paperId;
	}
	/**
	 * @param paperId the paperId to set
	 */
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	/**
	 * @return the firm
	 */
	public String getFirm() {
		return firm;
	}
	/**
	 * @param firm the firm to set
	 */
	public void setFirm(String firm) {
		this.firm = firm;
	}
	/**
	 * @return the visitedMan
	 */
	public String getVisitedMan() {
		return visitedMan;
	}
	/**
	 * @param visitedMan the visitedMan to set
	 */
	public void setVisitedMan(String visitedMan) {
		this.visitedMan = visitedMan;
	}
	/**
	 * @return the visitedDep
	 */
	public String getVisitedDep() {
		return visitedDep;
	}
	/**
	 * @param visitedDep the visitedDep to set
	 */
	public void setVisitedDep(String visitedDep) {
		this.visitedDep = visitedDep;
	}
	/**
	 * @return the inDate
	 */
	public String getInDate() {
		return inDate;
	}
	/**
	 * @param inDate the inDate to set
	 */
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	/**
	 * @return the outDate
	 */
	public String getOutDate() {
		return outDate;
	}
	/**
	 * @param outDate the outDate to set
	 */
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the onDuty
	 */
	public String getOnDuty() {
		return onDuty;
	}
	/**
	 * @param onDuty the onDuty to set
	 */
	public void setOnDuty(String onDuty) {
		this.onDuty = onDuty;
	}
		
	/**
	 * @return the papertypeCd
	 */
	public String getPapertypeCd() {
		return papertypeCd;
	}
	/**
	 * @param papertypeCd the papertypeCd to set
	 */
	public void setPapertypeCd(String papertypeCd) {
		this.papertypeCd = papertypeCd;
	}
	/**
	 * @return the chsName
	 */
	public String getChsName() {
		return chsName;
	}
	/**
	 * @param chsName the chsName to set
	 */
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}

	/**
	 * @return the updateUser
	 */
	public String getUpdateUser() {
		return updateUser;
	}
	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	/**
	 * @return the isUse
	 */
	public String getIsUse() {
		return isUse;
	}
	/**
	 * @param isUse the isUse to set
	 */
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}	
	
	
}