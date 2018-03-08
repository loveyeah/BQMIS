package power.ejb.opticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCOpMeasures entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_OP_MEASURES")
public class RunCOpMeasures implements java.io.Serializable {

	// Fields

	private Long dangerId;
	private Long operateTaskId;
	private String dangerName;
	private String measureContent;
	private Long displayNo;
	private String memo;
	private String enterpriseCode;
	private String isUse;
	private String modifyBy;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public RunCOpMeasures() {
	}

	/** minimal constructor */
	public RunCOpMeasures(Long dangerId) {
		this.dangerId = dangerId;
	}

	/** full constructor */
	public RunCOpMeasures(Long dangerId, Long operateTaskId, String dangerName,
			String measureContent, Long displayNo, String memo,
			String enterpriseCode, String isUse, String modifyBy,
			Date modifyDate) {
		this.dangerId = dangerId;
		this.operateTaskId = operateTaskId;
		this.dangerName = dangerName;
		this.measureContent = measureContent;
		this.displayNo = displayNo;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
	}

	// Property accessors
	@Id
	@Column(name = "DANGER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDangerId() {
		return this.dangerId;
	}

	public void setDangerId(Long dangerId) {
		this.dangerId = dangerId;
	}

	@Column(name = "OPERATE_TASK_ID", precision = 10, scale = 0)
	public Long getOperateTaskId() {
		return this.operateTaskId;
	}

	public void setOperateTaskId(Long operateTaskId) {
		this.operateTaskId = operateTaskId;
	}

	@Column(name = "DANGER_NAME", length = 500)
	public String getDangerName() {
		return this.dangerName;
	}

	public void setDangerName(String dangerName) {
		this.dangerName = dangerName;
	}

	@Column(name = "MEASURE_CONTENT", length = 500)
	public String getMeasureContent() {
		return this.measureContent;
	}

	public void setMeasureContent(String measureContent) {
		this.measureContent = measureContent;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
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

}