package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtKkxAuxiliaryInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_KKX_AUXILIARY_INFO")
public class PtKkxAuxiliaryInfo implements java.io.Serializable {

	// Fields

	private Long auxiliaryId;
	private Long blockId;
	private Long auxiliaryTypeId;
	private String auxiliaryCode;
	private String auxiliaryName;
	private String auxiliaryModel;
	private String updateNo;
	private Date startProDate;
	private Date stopStatDate;
	private Date leaveFactoryDate;
	private Date statDate;
	private Date stopUseDate;
	private String leaveFactoryNo;
	private String factoryCode;
	private String produceFactory;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtKkxAuxiliaryInfo() {
	}

	/** minimal constructor */
	public PtKkxAuxiliaryInfo(Long auxiliaryId) {
		this.auxiliaryId = auxiliaryId;
	}

	/** full constructor */
	public PtKkxAuxiliaryInfo(Long auxiliaryId, Long blockId,
			Long auxiliaryTypeId, String auxiliaryCode, String auxiliaryName,
			String auxiliaryModel, String updateNo, Date startProDate,
			Date stopStatDate, Date leaveFactoryDate, Date statDate,
			Date stopUseDate, String leaveFactoryNo, String factoryCode,
			String produceFactory, String isUse, String enterpriseCode) {
		this.auxiliaryId = auxiliaryId;
		this.blockId = blockId;
		this.auxiliaryTypeId = auxiliaryTypeId;
		this.auxiliaryCode = auxiliaryCode;
		this.auxiliaryName = auxiliaryName;
		this.auxiliaryModel = auxiliaryModel;
		this.updateNo = updateNo;
		this.startProDate = startProDate;
		this.stopStatDate = stopStatDate;
		this.leaveFactoryDate = leaveFactoryDate;
		this.statDate = statDate;
		this.stopUseDate = stopUseDate;
		this.leaveFactoryNo = leaveFactoryNo;
		this.factoryCode = factoryCode;
		this.produceFactory = produceFactory;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "AUXILIARY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAuxiliaryId() {
		return this.auxiliaryId;
	}

	public void setAuxiliaryId(Long auxiliaryId) {
		this.auxiliaryId = auxiliaryId;
	}

	@Column(name = "BLOCK_ID", precision = 10, scale = 0)
	public Long getBlockId() {
		return this.blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	@Column(name = "AUXILIARY_TYPE_ID", precision = 10, scale = 0)
	public Long getAuxiliaryTypeId() {
		return this.auxiliaryTypeId;
	}

	public void setAuxiliaryTypeId(Long auxiliaryTypeId) {
		this.auxiliaryTypeId = auxiliaryTypeId;
	}

	@Column(name = "AUXILIARY_CODE", length = 20)
	public String getAuxiliaryCode() {
		return this.auxiliaryCode;
	}

	public void setAuxiliaryCode(String auxiliaryCode) {
		this.auxiliaryCode = auxiliaryCode;
	}

	@Column(name = "AUXILIARY_NAME", length = 50)
	public String getAuxiliaryName() {
		return this.auxiliaryName;
	}

	public void setAuxiliaryName(String auxiliaryName) {
		this.auxiliaryName = auxiliaryName;
	}

	@Column(name = "AUXILIARY_MODEL", length = 20)
	public String getAuxiliaryModel() {
		return this.auxiliaryModel;
	}

	public void setAuxiliaryModel(String auxiliaryModel) {
		this.auxiliaryModel = auxiliaryModel;
	}

	@Column(name = "UPDATE_NO", length = 20)
	public String getUpdateNo() {
		return this.updateNo;
	}

	public void setUpdateNo(String updateNo) {
		this.updateNo = updateNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_PRO_DATE", length = 7)
	public Date getStartProDate() {
		return this.startProDate;
	}

	public void setStartProDate(Date startProDate) {
		this.startProDate = startProDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "STOP_STAT_DATE", length = 7)
	public Date getStopStatDate() {
		return this.stopStatDate;
	}

	public void setStopStatDate(Date stopStatDate) {
		this.stopStatDate = stopStatDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LEAVE_FACTORY_DATE", length = 7)
	public Date getLeaveFactoryDate() {
		return this.leaveFactoryDate;
	}

	public void setLeaveFactoryDate(Date leaveFactoryDate) {
		this.leaveFactoryDate = leaveFactoryDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "STAT_DATE", length = 7)
	public Date getStatDate() {
		return this.statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "STOP_USE_DATE", length = 7)
	public Date getStopUseDate() {
		return this.stopUseDate;
	}

	public void setStopUseDate(Date stopUseDate) {
		this.stopUseDate = stopUseDate;
	}

	@Column(name = "LEAVE_FACTORY_NO", length = 20)
	public String getLeaveFactoryNo() {
		return this.leaveFactoryNo;
	}

	public void setLeaveFactoryNo(String leaveFactoryNo) {
		this.leaveFactoryNo = leaveFactoryNo;
	}

	@Column(name = "FACTORY_CODE", length = 20)
	public String getFactoryCode() {
		return this.factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	@Column(name = "PRODUCE_FACTORY", length = 50)
	public String getProduceFactory() {
		return this.produceFactory;
	}

	public void setProduceFactory(String produceFactory) {
		this.produceFactory = produceFactory;
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