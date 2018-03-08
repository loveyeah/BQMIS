package power.ejb.productiontec.dependabilityAnalysis.business.form;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BlockForm implements Serializable{
	
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
	private String productionDate;
	private String statDate;
	private String stopStatDate;
	//锅炉生产厂家
	private String boilerFactory;
	//汽轮机生产厂家
	private String turbineFactory;
	//发电机生产厂家
	private String generatorFactory;
	//主变压器生产厂家
	private String transformerFactory;
	
	public Long getBlockId() {
		return blockId;
	}
	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}
	public String getBlockCode() {
		return blockCode;
	}
	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public String getHoldingCompany() {
		return holdingCompany;
	}
	public void setHoldingCompany(String holdingCompany) {
		this.holdingCompany = holdingCompany;
	}
	public Double getHoldingPercent() {
		return holdingPercent;
	}
	public void setHoldingPercent(Double holdingPercent) {
		this.holdingPercent = holdingPercent;
	}
	public String getManageCompany() {
		return manageCompany;
	}
	public void setManageCompany(String manageCompany) {
		this.manageCompany = manageCompany;
	}
	public Double getNameplateCapability() {
		return nameplateCapability;
	}
	public void setNameplateCapability(Double nameplateCapability) {
		this.nameplateCapability = nameplateCapability;
	}
	public String getAttemperCompany() {
		return attemperCompany;
	}
	public void setAttemperCompany(String attemperCompany) {
		this.attemperCompany = attemperCompany;
	}
	public String getBelongGrid() {
		return belongGrid;
	}
	public void setBelongGrid(String belongGrid) {
		this.belongGrid = belongGrid;
	}
	public String getBlockType() {
		return blockType;
	}
	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}
	public String getFuelName() {
		return fuelName;
	}
	public void setFuelName(String fuelName) {
		this.fuelName = fuelName;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getStatDate() {
		return statDate;
	}
	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}
	public String getStopStatDate() {
		return stopStatDate;
	}
	public void setStopStatDate(String stopStatDate) {
		this.stopStatDate = stopStatDate;
	}
	public String getBoilerFactory() {
		return boilerFactory;
	}
	public void setBoilerFactory(String boilerFactory) {
		this.boilerFactory = boilerFactory;
	}
	public String getTurbineFactory() {
		return turbineFactory;
	}
	public void setTurbineFactory(String turbineFactory) {
		this.turbineFactory = turbineFactory;
	}
	public String getGeneratorFactory() {
		return generatorFactory;
	}
	public void setGeneratorFactory(String generatorFactory) {
		this.generatorFactory = generatorFactory;
	}
	public String getTransformerFactory() {
		return transformerFactory;
	}
	public void setTransformerFactory(String transformerFactory) {
		this.transformerFactory = transformerFactory;
	}
}
