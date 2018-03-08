package power.ejb.hr.labor;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJLaborTempe entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_LABOR_TEMPE")
public class HrJLaborTempe implements java.io.Serializable {

	// Fields

	private Long tempeId;
	private String tempeMonth;
	private String costItem;
	private String entryBy;
	private Date entryDate;
	private String tempeState;
	private Long workFlowNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJLaborTempe() {
	}

	/** minimal constructor */
	public HrJLaborTempe(Long tempeId) {
		this.tempeId = tempeId;
	}

	/** full constructor */
	public HrJLaborTempe(Long tempeId, String tempeMonth, String costItem,
			String entryBy, Date entryDate, String tempeState, Long workFlowNo,
			String isUse, String enterpriseCode) {
		this.tempeId = tempeId;
		this.tempeMonth = tempeMonth;
		this.costItem = costItem;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.tempeState = tempeState;
		this.workFlowNo = workFlowNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TEMPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTempeId() {
		return this.tempeId;
	}

	public void setTempeId(Long tempeId) {
		this.tempeId = tempeId;
	}

	@Column(name = "TEMPE_MONTH", length = 10)
	public String getTempeMonth() {
		return this.tempeMonth;
	}

	public void setTempeMonth(String tempeMonth) {
		this.tempeMonth = tempeMonth;
	}

	@Column(name = "COST_ITEM", length = 20)
	public String getCostItem() {
		return this.costItem;
	}

	public void setCostItem(String costItem) {
		this.costItem = costItem;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "TEMPE_STATE", length = 1)
	public String getTempeState() {
		return this.tempeState;
	}

	public void setTempeState(String tempeState) {
		this.tempeState = tempeState;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
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