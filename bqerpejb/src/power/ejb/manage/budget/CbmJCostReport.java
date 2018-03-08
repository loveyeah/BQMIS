package power.ejb.manage.budget;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CbmJCostReport entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_COST_REPORT")
public class CbmJCostReport implements java.io.Serializable {

	// Fields

	private Long reportId;
	private Long itemId;
	private String reportMoneyUpper;
	private Double reportMoneyLower;
	private String reportDept;
	private String reportBy;
	private Date reportDate;
	private String reportUse;
	private String memo;
	private Long workFlowNo;
	private String workFlowStatus;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJCostReport() {
	}

	/** minimal constructor */
	public CbmJCostReport(Long reportId) {
		this.reportId = reportId;
	}

	/** full constructor */
	public CbmJCostReport(Long reportId, Long itemId, String reportMoneyUpper,
			Double reportMoneyLower, String reportDept, String reportBy,
			Date reportDate, String reportUse, String memo, Long workFlowNo,
			String workFlowStatus, String isUse, String enterpriseCode) {
		this.reportId = reportId;
		this.itemId = itemId;
		this.reportMoneyUpper = reportMoneyUpper;
		this.reportMoneyLower = reportMoneyLower;
		this.reportDept = reportDept;
		this.reportBy = reportBy;
		this.reportDate = reportDate;
		this.reportUse = reportUse;
		this.memo = memo;
		this.workFlowNo = workFlowNo;
		this.workFlowStatus = workFlowStatus;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REPORT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "REPORT_MONEY_UPPER", length = 50)
	public String getReportMoneyUpper() {
		return this.reportMoneyUpper;
	}

	public void setReportMoneyUpper(String reportMoneyUpper) {
		this.reportMoneyUpper = reportMoneyUpper;
	}

	@Column(name = "REPORT_MONEY_LOWER", precision = 15, scale = 4)
	public Double getReportMoneyLower() {
		return this.reportMoneyLower;
	}

	public void setReportMoneyLower(Double reportMoneyLower) {
		this.reportMoneyLower = reportMoneyLower;
	}

	@Column(name = "REPORT_DEPT", length = 20)
	public String getReportDept() {
		return this.reportDept;
	}

	public void setReportDept(String reportDept) {
		this.reportDept = reportDept;
	}

	@Column(name = "REPORT_BY", length = 30)
	public String getReportBy() {
		return this.reportBy;
	}

	public void setReportBy(String reportBy) {
		this.reportBy = reportBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPORT_DATE", length = 7)
	public Date getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	@Column(name = "REPORT_USE", length = 500)
	public String getReportUse() {
		return this.reportUse;
	}

	public void setReportUse(String reportUse) {
		this.reportUse = reportUse;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WORK_FLOW_STATUS", length = 2)
	public String getWorkFlowStatus() {
		return this.workFlowStatus;
	}

	public void setWorkFlowStatus(String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
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