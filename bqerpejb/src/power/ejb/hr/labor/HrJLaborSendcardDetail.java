package power.ejb.hr.labor;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJLaborSendcardDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_LABOR_SENDCARD_DETAIL")
public class HrJLaborSendcardDetail implements java.io.Serializable {

	// Fields

	private Long detailId;
	private Long sendcardId;
	private String deptName;
	private Long factNum;
	private Double sendStandard;
	private String signName;
	private String memo;
	private String modifyBy;
	private Date modifyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJLaborSendcardDetail() {
	}

	/** minimal constructor */
	public HrJLaborSendcardDetail(Long detailId) {
		this.detailId = detailId;
	}

	/** full constructor */
	public HrJLaborSendcardDetail(Long detailId, Long sendcardId,
			String deptName, Long factNum, Double sendStandard,
			String signName, String memo, String modifyBy, Date modifyDate,
			String isUse, String enterpriseCode) {
		this.detailId = detailId;
		this.sendcardId = sendcardId;
		this.deptName = deptName;
		this.factNum = factNum;
		this.sendStandard = sendStandard;
		this.signName = signName;
		this.memo = memo;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDetailId() {
		return this.detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	@Column(name = "SENDCARD_ID", precision = 10, scale = 0)
	public Long getSendcardId() {
		return this.sendcardId;
	}

	public void setSendcardId(Long sendcardId) {
		this.sendcardId = sendcardId;
	}

	@Column(name = "DEPT_NAME", length = 20)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "FACT_NUM", precision = 10, scale = 0)
	public Long getFactNum() {
		return this.factNum;
	}

	public void setFactNum(Long factNum) {
		this.factNum = factNum;
	}

	@Column(name = "SEND_STANDARD", precision = 10)
	public Double getSendStandard() {
		return this.sendStandard;
	}

	public void setSendStandard(Double sendStandard) {
		this.sendStandard = sendStandard;
	}

	@Column(name = "SIGN_NAME", length = 20)
	public String getSignName() {
		return this.signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}