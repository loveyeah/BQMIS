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
 * HrCExchangetorest entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_EXCHANGETOREST")
public class HrCExchangetorest implements java.io.Serializable {

	// Fields

	private HrCExchangetorestId id;
	private Double exchangerestHours;
	private String signState;
	private String workFlowNo;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCExchangetorest() {
	}

	/** minimal constructor */
	public HrCExchangetorest(HrCExchangetorestId id) {
		this.id = id;
	}

	/** full constructor */
	public HrCExchangetorest(HrCExchangetorestId id, Double exchangerestHours,
			String signState, String workFlowNo, String lastModifiyBy,
			Date lastModifiyDate, String isUse, String enterpriseCode) {
		this.id = id;
		this.exchangerestHours = exchangerestHours;
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
			@AttributeOverride(name = "attendanceYear", column = @Column(name = "ATTENDANCE_YEAR", nullable = false, length = 4)),
			@AttributeOverride(name = "attendanceMonth", column = @Column(name = "ATTENDANCE_MONTH", nullable = false, length = 2)),
			@AttributeOverride(name = "empId", column = @Column(name = "EMP_ID", nullable = false, precision = 10, scale = 0)) })
	public HrCExchangetorestId getId() {
		return this.id;
	}

	public void setId(HrCExchangetorestId id) {
		this.id = id;
	}

	@Column(name = "EXCHANGEREST_HOURS", precision = 15, scale = 4)
	public Double getExchangerestHours() {
		return this.exchangerestHours;
	}

	public void setExchangerestHours(Double exchangerestHours) {
		this.exchangerestHours = exchangerestHours;
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

	public void setLastModifiyDate(Date difiyDlastModifiyDateate) {
		this.lastModifiyDate = difiyDlastModifiyDateate;
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