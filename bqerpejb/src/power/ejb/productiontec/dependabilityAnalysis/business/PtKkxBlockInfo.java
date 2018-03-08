package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtKkxBlockInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_KKX_BLOCK_INFO")
public class PtKkxBlockInfo implements java.io.Serializable {

	// Fields

	private Long blockId;
	private String blockCode;
	private String blockName;
	private String holdingCompany;
	private Double holdingPercent;
	private String manageCompany;
	private Double nameplateCapability;
	private String attemperCompany;
	private String belongGrid;
	private String blockType;
	private String fuelName;
	private Date productionDate;
	private Date statDate;
	private Date stopStatDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtKkxBlockInfo() {
	}

	/** minimal constructor */
	public PtKkxBlockInfo(Long blockId) {
		this.blockId = blockId;
	}

	/** full constructor */
	public PtKkxBlockInfo(Long blockId, String blockCode, String blockName,
			String holdingCompany, Double holdingPercent, String manageCompany,
			Double nameplateCapability, String attemperCompany,
			String belongGrid, String blockType, String fuelName,
			Date productionDate, Date statDate, Date stopStatDate,
			String isUse, String enterpriseCode) {
		this.blockId = blockId;
		this.blockCode = blockCode;
		this.blockName = blockName;
		this.holdingCompany = holdingCompany;
		this.holdingPercent = holdingPercent;
		this.manageCompany = manageCompany;
		this.nameplateCapability = nameplateCapability;
		this.attemperCompany = attemperCompany;
		this.belongGrid = belongGrid;
		this.blockType = blockType;
		this.fuelName = fuelName;
		this.productionDate = productionDate;
		this.statDate = statDate;
		this.stopStatDate = stopStatDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BLOCK_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBlockId() {
		return this.blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	@Column(name = "BLOCK_CODE", length = 40)
	public String getBlockCode() {
		return this.blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	@Column(name = "BLOCK_NAME", length = 80)
	public String getBlockName() {
		return this.blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	@Column(name = "HOLDING_COMPANY", length = 100)
	public String getHoldingCompany() {
		return this.holdingCompany;
	}

	public void setHoldingCompany(String holdingCompany) {
		this.holdingCompany = holdingCompany;
	}

	@Column(name = "HOLDING_PERCENT", precision = 15, scale = 4)
	public Double getHoldingPercent() {
		return this.holdingPercent;
	}

	public void setHoldingPercent(Double holdingPercent) {
		this.holdingPercent = holdingPercent;
	}

	@Column(name = "MANAGE_COMPANY", length = 100)
	public String getManageCompany() {
		return this.manageCompany;
	}

	public void setManageCompany(String manageCompany) {
		this.manageCompany = manageCompany;
	}

	@Column(name = "NAMEPLATE_CAPABILITY", precision = 15, scale = 4)
	public Double getNameplateCapability() {
		return this.nameplateCapability;
	}

	public void setNameplateCapability(Double nameplateCapability) {
		this.nameplateCapability = nameplateCapability;
	}

	@Column(name = "ATTEMPER_COMPANY", length = 100)
	public String getAttemperCompany() {
		return this.attemperCompany;
	}

	public void setAttemperCompany(String attemperCompany) {
		this.attemperCompany = attemperCompany;
	}

	@Column(name = "BELONG_GRID", length = 100)
	public String getBelongGrid() {
		return this.belongGrid;
	}

	public void setBelongGrid(String belongGrid) {
		this.belongGrid = belongGrid;
	}

	@Column(name = "BLOCK_TYPE", length = 40)
	public String getBlockType() {
		return this.blockType;
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}

	@Column(name = "FUEL_NAME", length = 20)
	public String getFuelName() {
		return this.fuelName;
	}

	public void setFuelName(String fuelName) {
		this.fuelName = fuelName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PRODUCTION_DATE", length = 7)
	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STAT_DATE", length = 7)
	public Date getStatDate() {
		return this.statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STOP_STAT_DATE", length = 7)
	public Date getStopStatDate() {
		return this.stopStatDate;
	}

	public void setStopStatDate(Date stopStatDate) {
		this.stopStatDate = stopStatDate;
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