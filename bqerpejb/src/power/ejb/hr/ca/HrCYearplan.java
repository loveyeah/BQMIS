/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCYearplan entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_YEARPLAN")
public class HrCYearplan implements java.io.Serializable {

	// Fields

	private HrCYearplanId id;
	private Double days;
	private Double hours;
	private String signState;
	private String workFlowNo;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCYearplan() {
	}

	/** minimal constructor */
	public HrCYearplan(HrCYearplanId id) {
		this.id = id;
	}

	/** full constructor */
	public HrCYearplan(HrCYearplanId id, Double days, Double hours,
			String signState, String workFlowNo, String lastModifiyBy,
			Date lastModifiyDate, String isUse, String enterpriseCode) {
		this.id = id;
		this.days = days;
		this.hours = hours;
		this.signState = signState;
		this.workFlowNo = workFlowNo;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "planYear", column = @Column(name = "PLAN_YEAR", nullable = false, length = 4)),
			@AttributeOverride(name = "empId", column = @Column(name = "EMP_ID", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "vacationTypeId", column = @Column(name = "VACATION_TYPE_ID", nullable = false, precision = 10, scale = 0)) })
	public HrCYearplanId getId() {
		return this.id;
	}

	public void setId(HrCYearplanId id) {
		this.id = id;
	}

	@Column(name = "DAYS", precision = 15, scale = 4)
	public Double getDays() {
		return this.days;
	}

	public void setDays(Double days) {
		this.days = days;
	}

	@Column(name = "HOURS", precision = 15, scale = 4)
	public Double getHours() {
		return this.hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	@Column(name = "SIGN_STATE", length = 1)
	public String getSignState() {
		return this.signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}

	@Column(name = "WORK_FLOW_NO", length = 26)
	public String getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "LAST_MODIFIY_BY", length = 16)
	public String getLastModifiyBy() {
		return this.lastModifiyBy;
	}

	public void setLastModifiyBy(String lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIY_DATE", length = 7)
	public Date getLastModifiyDate() {
		return this.lastModifiyDate;
	}

	public void setLastModifiyDate(Date lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}