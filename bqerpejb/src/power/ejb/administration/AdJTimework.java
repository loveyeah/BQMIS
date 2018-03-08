package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJTimework entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_TIMEWORK")
public class AdJTimework implements java.io.Serializable {

	// Fields

	private Long id;
	private Date workDate;
	private String result;
	private String workExplain;
	private String operator;
	private String classSequence;
	private String mark;
	private String memo;
	private String isUse;
	private String worktypeCode;
	private String subWorktypeCode;
	private String workitemCode;
	private String crtUser;
	private String dcmStatus;
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
	public AdJTimework() {
	}

	/** minimal constructor */
	public AdJTimework(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJTimework(Long id, Date workDate, String result,
			String workExplain, String operator, String classSequence,
			String mark, String memo, String isUse, String worktypeCode,
			String subWorktypeCode, String workitemCode, String crtUser,
			String dcmStatus, String updateUser, Date updateTime,String enterpriseCode) {
		this.id = id;
		this.workDate = workDate;
		this.result = result;
		this.workExplain = workExplain;
		this.operator = operator;
		this.classSequence = classSequence;
		this.mark = mark;
		this.memo = memo;
		this.isUse = isUse;
		this.worktypeCode = worktypeCode;
		this.subWorktypeCode = subWorktypeCode;
		this.workitemCode = workitemCode;
		this.crtUser = crtUser;
		this.dcmStatus = dcmStatus;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "WORK_DATE", length = 7)
	public Date getWorkDate() {
		return this.workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	@Column(name = "RESULT", length = 100)
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "WORK_EXPLAIN", length = 200)
	public String getWorkExplain() {
		return this.workExplain;
	}

	public void setWorkExplain(String workExplain) {
		this.workExplain = workExplain;
	}

	@Column(name = "OPERATOR", length = 6)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "CLASS_SEQUENCE", length = 1)
	public String getClassSequence() {
		return this.classSequence;
	}

	public void setClassSequence(String classSequence) {
		this.classSequence = classSequence;
	}

	@Column(name = "MARK", length = 1)
	public String getMark() {
		return this.mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "WORKTYPE_CODE", length = 2)
	public String getWorktypeCode() {
		return this.worktypeCode;
	}

	public void setWorktypeCode(String worktypeCode) {
		this.worktypeCode = worktypeCode;
	}

	@Column(name = "SUB_WORKTYPE_CODE", length = 4)
	public String getSubWorktypeCode() {
		return this.subWorktypeCode;
	}

	public void setSubWorktypeCode(String subWorktypeCode) {
		this.subWorktypeCode = subWorktypeCode;
	}

	@Column(name = "WORKITEM_CODE", length = 6)
	public String getWorkitemCode() {
		return this.workitemCode;
	}

	public void setWorkitemCode(String workitemCode) {
		this.workitemCode = workitemCode;
	}

	@Column(name = "CRT_USER", length = 10)
	public String getCrtUser() {
		return this.crtUser;
	}

	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
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