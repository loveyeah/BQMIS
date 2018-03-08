package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJManpass entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_MANPASS")
public class AdJManpass implements java.io.Serializable {

	// Fields

	private Long id;
	private String insertby;
	private Date insertdate;
	private String paperId;
	private String firm;
	private String visitedman;
	private String visiteddep;
	private Date inDate;
	private Date outDate;
	private String note;
	private String onduty;
	private String papertypeCd;
	private String isUse;
	private String updateUser;
	private Date updateTime;
	private String enterpriseCode;
	// Constructors

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	/** default constructor */
	public AdJManpass() {
	}

	/** minimal constructor */
	public AdJManpass(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJManpass(Long id, String insertby, Date insertdate,
			String paperId, String firm, String visitedman, String visiteddep,
			Date inDate, Date outDate, String note, String onduty,
			String papertypeCd, String isUse, String updateUser, Date updateTime,String enterpriseCode) {
		this.id = id;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.paperId = paperId;
		this.firm = firm;
		this.visitedman = visitedman;
		this.visiteddep = visiteddep;
		this.inDate = inDate;
		this.outDate = outDate;
		this.note = note;
		this.onduty = onduty;
		this.papertypeCd = papertypeCd;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "INSERTBY", length = 10)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "PAPER_ID", length = 50)
	public String getPaperId() {
		return this.paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	@Column(name = "FIRM", length = 50)
	public String getFirm() {
		return this.firm;
	}

	public void setFirm(String firm) {
		this.firm = firm;
	}

	@Column(name = "VISITEDMAN", length = 20)
	public String getVisitedman() {
		return this.visitedman;
	}

	public void setVisitedman(String visitedman) {
		this.visitedman = visitedman;
	}

	@Column(name = "VISITEDDEP", length = 6)
	public String getVisiteddep() {
		return this.visiteddep;
	}

	public void setVisiteddep(String visiteddep) {
		this.visiteddep = visiteddep;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "IN_DATE", length = 7)
	public Date getInDate() {
		return this.inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OUT_DATE", length = 7)
	public Date getOutDate() {
		return this.outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	@Column(name = "NOTE", length = 1255)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "ONDUTY", length = 20)
	public String getOnduty() {
		return this.onduty;
	}

	public void setOnduty(String onduty) {
		this.onduty = onduty;
	}

	@Column(name = "PAPERTYPE_CD", length = 2)
	public String getPapertypeCd() {
		return this.papertypeCd;
	}

	public void setPapertypeCd(String papertypeCd) {
		this.papertypeCd = papertypeCd;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}