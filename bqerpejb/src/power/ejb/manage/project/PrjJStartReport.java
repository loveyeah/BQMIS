package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjJStartReport entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRJ_J_START_REPORT")
public class PrjJStartReport implements java.io.Serializable {

	// Fields

	private Long reportId;
	private String reportCode;
	private String conttreesNo;
	private String contractName;
	private Double prjFunds;
	private Long prjTypeId;
	private Date startDate;
	private Date endDate;
	private String prjLocation;
	private String entryBy;
	private Date entryDate;
	private String approveChargeBy;
	private String approveText;
	private Date approveDate;
	private String workChargeBy;
	private String workOperateBy;
	private Date workApproveDate;
	private String backEntryBy;
	private Date backEnrtyDate;
	private String isBackEntry;
	private String enterpriseCode;
	private String isUse;
	private String reportType;
	private Long prjId; //add by fyyang 20100909 立项id
	
	

	// Constructors

	/** default constructor */
	public PrjJStartReport() {
	}

	/** minimal constructor */
	public PrjJStartReport(Long reportId) {
		this.reportId = reportId;
	}

	/** full constructor */
	public PrjJStartReport(Long reportId, String reportCode,
			String conttreesNo, String contractName, Double prjFunds,
			Long prjTypeId, Date startDate, Date endDate, String prjLocation,
			String entryBy, Date entryDate, String approveChargeBy,
			String approveText, Date approveDate, String workChargeBy,
			String workOperateBy, Date workApproveDate, String backEntryBy,
			Date backEnrtyDate, String isBackEntry, String enterpriseCode,
			String isUse, String reportType,Long prjId) {
		this.reportId = reportId;
		this.reportCode = reportCode;
		this.conttreesNo = conttreesNo;
		this.contractName = contractName;
		this.prjFunds = prjFunds;
		this.prjTypeId = prjTypeId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.prjLocation = prjLocation;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.approveChargeBy = approveChargeBy;
		this.approveText = approveText;
		this.approveDate = approveDate;
		this.workChargeBy = workChargeBy;
		this.workOperateBy = workOperateBy;
		this.workApproveDate = workApproveDate;
		this.backEntryBy = backEntryBy;
		this.backEnrtyDate = backEnrtyDate;
		this.isBackEntry = isBackEntry;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.reportType = reportType;
		this.prjId=prjId;
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

	@Column(name = "REPORT_CODE", length = 30)
	public String getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "CONTTREES_NO", length = 50)
	public String getConttreesNo() {
		return this.conttreesNo;
	}

	public void setConttreesNo(String conttreesNo) {
		this.conttreesNo = conttreesNo;
	}

	@Column(name = "CONTRACT_NAME", length = 100)
	public String getContractName() {
		return this.contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	@Column(name = "PRJ_FUNDS", precision = 15, scale = 4)
	public Double getPrjFunds() {
		return this.prjFunds;
	}

	public void setPrjFunds(Double prjFunds) {
		this.prjFunds = prjFunds;
	}

	@Column(name = "PRJ_TYPE_ID", precision = 10, scale = 0)
	public Long getPrjTypeId() {
		return this.prjTypeId;
	}

	public void setPrjTypeId(Long prjTypeId) {
		this.prjTypeId = prjTypeId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "PRJ_LOCATION", length = 50)
	public String getPrjLocation() {
		return this.prjLocation;
	}

	public void setPrjLocation(String prjLocation) {
		this.prjLocation = prjLocation;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "APPROVE_CHARGE_BY", length = 30)
	public String getApproveChargeBy() {
		return this.approveChargeBy;
	}

	public void setApproveChargeBy(String approveChargeBy) {
		this.approveChargeBy = approveChargeBy;
	}

	@Column(name = "APPROVE_TEXT", length = 100)
	public String getApproveText() {
		return this.approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "APPROVE_DATE", length = 7)
	public Date getApproveDate() {
		return this.approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Column(name = "WORK_CHARGE_BY", length = 30)
	public String getWorkChargeBy() {
		return this.workChargeBy;
	}

	public void setWorkChargeBy(String workChargeBy) {
		this.workChargeBy = workChargeBy;
	}

	@Column(name = "WORK_OPERATE_BY", length = 30)
	public String getWorkOperateBy() {
		return this.workOperateBy;
	}

	public void setWorkOperateBy(String workOperateBy) {
		this.workOperateBy = workOperateBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "WORK_APPROVE_DATE", length = 7)
	public Date getWorkApproveDate() {
		return this.workApproveDate;
	}

	public void setWorkApproveDate(Date workApproveDate) {
		this.workApproveDate = workApproveDate;
	}

	@Column(name = "BACK_ENTRY_BY", length = 30)
	public String getBackEntryBy() {
		return this.backEntryBy;
	}

	public void setBackEntryBy(String backEntryBy) {
		this.backEntryBy = backEntryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BACK_ENRTY_DATE", length = 7)
	public Date getBackEnrtyDate() {
		return this.backEnrtyDate;
	}

	public void setBackEnrtyDate(Date backEnrtyDate) {
		this.backEnrtyDate = backEnrtyDate;
	}

	@Column(name = "IS_BACK_ENTRY", length = 1)
	public String getIsBackEntry() {
		return this.isBackEntry;
	}

	public void setIsBackEntry(String isBackEntry) {
		this.isBackEntry = isBackEntry;
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

	@Column(name = "REPORT_TYPE", length = 1)
	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	
	@Column(name = "PRJ_ID", precision = 10, scale = 0)
	public Long getPrjId() {
		return prjId;
	}

	public void setPrjId(Long prjId) {
		this.prjId = prjId;
	}

}