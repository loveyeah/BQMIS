package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjJEndCheck entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRJ_J_END_CHECK")
public class PrjJEndCheck implements java.io.Serializable {

	// Fields

	private Long checkId;
	private Long conId;
	private String conName;
	private String reportCode;
	private Double projectPrice;
	private Long contractorId;
	private String contractorName;
	private String prjLocation;
	private Date startDate;
	private Date endDate;
	private Date checkDate;
	private String quantities;
	private String entryBy;
	private Date entryDate;
	private String projectContent;
	private String endInfo;
	private String checkText;
	private String qualityAssess;
	private String constructChargeBy;
	private String contractorChargeBy;
	private String backEntryBy;
	private Date backEnrtyDate;
	private String isBackEntry;
	private String enterpriseCode;
	private String isUse;
	private String prjId;

	// Constructors

	/** default constructor */
	public PrjJEndCheck() {
	}

	/** minimal constructor */
	public PrjJEndCheck(Long checkId) {
		this.checkId = checkId;
	}

	/** full constructor */
	public PrjJEndCheck(Long checkId, Long conId, String conName,
			String reportCode, Double projectPrice, Long contractorId,
			String contractorName, String prjLocation, Date startDate,
			Date endDate, Date checkDate, String quantities, String entryBy,
			Date entryDate, String projectContent, String endInfo,
			String checkText, String qualityAssess, String constructChargeBy,
			String contractorChargeBy, String backEntryBy, Date backEnrtyDate,
			String isBackEntry,String enterpriseCode,String isUse) {
		this.checkId = checkId;
		this.conId = conId;
		this.conName = conName;
		this.reportCode = reportCode;
		this.projectPrice = projectPrice;
		this.contractorId = contractorId;
		this.contractorName = contractorName;
		this.prjLocation = prjLocation;
		this.startDate = startDate;
		this.endDate = endDate;
		this.checkDate = checkDate;
		this.quantities = quantities;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.projectContent = projectContent;
		this.endInfo = endInfo;
		this.checkText = checkText;
		this.qualityAssess = qualityAssess;
		this.constructChargeBy = constructChargeBy;
		this.contractorChargeBy = contractorChargeBy;
		this.backEntryBy = backEntryBy;
		this.backEnrtyDate = backEnrtyDate;
		this.isBackEntry = isBackEntry;
		this.enterpriseCode=enterpriseCode;
		this.isUse=isUse;
	}

	// Property accessors
	@Id
	@Column(name = "CHECK_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCheckId() {
		return this.checkId;
	}

	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}

	@Column(name = "CON_ID", precision = 10, scale = 0)
	public Long getConId() {
		return this.conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	@Column(name = "CON_NAME", length = 100)
	public String getConName() {
		return this.conName;
	}

	public void setConName(String conName) {
		this.conName = conName;
	}

	@Column(name = "REPORT_CODE", length = 20)
	public String getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "PROJECT_PRICE", precision = 18, scale = 5)
	public Double getProjectPrice() {
		return this.projectPrice;
	}

	public void setProjectPrice(Double projectPrice) {
		this.projectPrice = projectPrice;
	}

	@Column(name = "CONTRACTOR_ID", precision = 10, scale = 0)
	public Long getContractorId() {
		return this.contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	@Column(name = "CONTRACTOR_NAME", length = 100)
	public String getContractorName() {
		return this.contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	@Column(name = "PRJ_LOCATION", length = 50)
	public String getPrjLocation() {
		return this.prjLocation;
	}

	public void setPrjLocation(String prjLocation) {
		this.prjLocation = prjLocation;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "CHECK_DATE", length = 7)
	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name = "QUANTITIES", length = 30)
	public String getQuantities() {
		return this.quantities;
	}

	public void setQuantities(String quantities) {
		this.quantities = quantities;
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

	@Column(name = "PROJECT_CONTENT", length = 300)
	public String getProjectContent() {
		return this.projectContent;
	}

	public void setProjectContent(String projectContent) {
		this.projectContent = projectContent;
	}

	@Column(name = "END_INFO", length = 300)
	public String getEndInfo() {
		return this.endInfo;
	}

	public void setEndInfo(String endInfo) {
		this.endInfo = endInfo;
	}

	@Column(name = "CHECK_TEXT", length = 300)
	public String getCheckText() {
		return this.checkText;
	}

	public void setCheckText(String checkText) {
		this.checkText = checkText;
	}

	@Column(name = "QUALITY_ASSESS", length = 30)
	public String getQualityAssess() {
		return this.qualityAssess;
	}

	public void setQualityAssess(String qualityAssess) {
		this.qualityAssess = qualityAssess;
	}

	@Column(name = "CONSTRUCT_CHARGE_BY", length = 30)
	public String getConstructChargeBy() {
		return this.constructChargeBy;
	}

	public void setConstructChargeBy(String constructChargeBy) {
		this.constructChargeBy = constructChargeBy;
	}

	@Column(name = "CONTRACTOR_CHARGE_BY", length = 30)
	public String getContractorChargeBy() {
		return this.contractorChargeBy;
	}

	public void setContractorChargeBy(String contractorChargeBy) {
		this.contractorChargeBy = contractorChargeBy;
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
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	@Column(name = "PRJ_ID", length = 10)
	public String getPrjId() {
		return prjId;
	}
	
	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}

}