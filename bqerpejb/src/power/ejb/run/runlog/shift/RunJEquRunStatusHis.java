package power.ejb.run.runlog.shift;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJEquRunStatusHis entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_J_EQU_RUN_STATUS_HIS")
public class RunJEquRunStatusHis implements java.io.Serializable {

	// Fields

	private Long runStatusHisId;
	private Long runLogid;
	private String runLogno;
	private String attributeCode;
	private String equName;
	private Long fromStatusId;
	private String fromStatusName;
	private Long toStatusId;
	private String toStatusName;
	private Double standingTime;
	private String operaterBy;
	private Date operateTime;
	private String operateMemo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunJEquRunStatusHis() {
	}

	/** minimal constructor */
	public RunJEquRunStatusHis(Long runStatusHisId, Long runLogid) {
		this.runStatusHisId = runStatusHisId;
		this.runLogid = runLogid;
	}

	/** full constructor */
	public RunJEquRunStatusHis(Long runStatusHisId, Long runLogid,
			String runLogno, String attributeCode, String equName,
			Long fromStatusId, String fromStatusName, Long toStatusId,
			String toStatusName, Double standingTime, String operaterBy,
			Date operateTime, String operateMemo, String isUse,
			String enterpriseCode) {
		this.runStatusHisId = runStatusHisId;
		this.runLogid = runLogid;
		this.runLogno = runLogno;
		this.attributeCode = attributeCode;
		this.equName = equName;
		this.fromStatusId = fromStatusId;
		this.fromStatusName = fromStatusName;
		this.toStatusId = toStatusId;
		this.toStatusName = toStatusName;
		this.standingTime = standingTime;
		this.operaterBy = operaterBy;
		this.operateTime = operateTime;
		this.operateMemo = operateMemo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RUN_STATUS_HIS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRunStatusHisId() {
		return this.runStatusHisId;
	}

	public void setRunStatusHisId(Long runStatusHisId) {
		this.runStatusHisId = runStatusHisId;
	}

	@Column(name = "RUN_LOGID", nullable = false, precision = 10, scale = 0)
	public Long getRunLogid() {
		return this.runLogid;
	}

	public void setRunLogid(Long runLogid) {
		this.runLogid = runLogid;
	}

	@Column(name = "RUN_LOGNO", length = 15)
	public String getRunLogno() {
		return this.runLogno;
	}

	public void setRunLogno(String runLogno) {
		this.runLogno = runLogno;
	}

	@Column(name = "ATTRIBUTE_CODE", length = 30)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "EQU_NAME", length = 100)
	public String getEquName() {
		return this.equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	@Column(name = "FROM_STATUS_ID", precision = 10, scale = 0)
	public Long getFromStatusId() {
		return this.fromStatusId;
	}

	public void setFromStatusId(Long fromStatusId) {
		this.fromStatusId = fromStatusId;
	}

	@Column(name = "FROM_STATUS_NAME", length = 60)
	public String getFromStatusName() {
		return this.fromStatusName;
	}

	public void setFromStatusName(String fromStatusName) {
		this.fromStatusName = fromStatusName;
	}

	@Column(name = "TO_STATUS_ID", precision = 10, scale = 0)
	public Long getToStatusId() {
		return this.toStatusId;
	}

	public void setToStatusId(Long toStatusId) {
		this.toStatusId = toStatusId;
	}

	@Column(name = "TO_STATUS_NAME", length = 60)
	public String getToStatusName() {
		return this.toStatusName;
	}

	public void setToStatusName(String toStatusName) {
		this.toStatusName = toStatusName;
	}

	@Column(name = "STANDING_TIME", precision = 15, scale = 4)
	public Double getStandingTime() {
		return this.standingTime;
	}

	public void setStandingTime(Double standingTime) {
		this.standingTime = standingTime;
	}

	@Column(name = "OPERATER_BY", length = 16)
	public String getOperaterBy() {
		return this.operaterBy;
	}

	public void setOperaterBy(String operaterBy) {
		this.operaterBy = operaterBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPERATE_TIME")
	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	@Column(name = "OPERATE_MEMO", length = 256)
	public String getOperateMemo() {
		return this.operateMemo;
	}

	public void setOperateMemo(String operateMemo) {
		this.operateMemo = operateMemo;
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