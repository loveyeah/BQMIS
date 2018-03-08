package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvCMaterial entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_C_MATERIAL")
public class InvCMaterial implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long materialId;
	private String materialNo;
	private String materialName;
	private String materialDesc;
	private Long maertialClassId;
	private Long materialTypeId;
	private Long materialStatusId;
	private String abcType;
	private String isCommodity;
	private String defaultWhsNo;
	private String defaultLocationNo;
	private String isLot;
	private Double actualSaleCount;
	private Double standardCostTotal;
	private Double actualCostTotal;
	private Double markedPrice;
	private String discountCode;
	private String commissionsCode;
	private Double taxRate;
	private Double fobPrice;
	private Long stockUmId;
	private Long purUmId;
	private Double purStockUm;
	private Long saleUmId;
	private Double saleStockUm;
	private String costMethod;
	private String isReserved;
	private Long priceUmId;
	private Double priceStockUm;
	private Double priceCount;
	private String buyer;
//	private String primarySupplier;
//	private String secondarySupplier;
	private Long primarySupplier;
	private Long secondarySupplier;
	private Double maxStock;
	private Double minStock;
	private Double stdCost;
	private Double actCost;
	private Double frozenCost;
	private Double minCustomerCount;
	private Double standardCustomerCount;
	private Double incrementCustomerCount;
	private String planer;
	private String demandCode;
	private Double leadDays;
	private Double len;//private Double length;
	private Long lengthUmId;
	private Double width;
	private Long widthUmId;
	private Double heigh;
	private Long heighUmId;
	private Double weight;
	private Long wightUmId;
	private Double volumUnit;
	private Long volumUmId;
	private Double centigrade;
	private Double fahrenheit;
	private String specNo;
	private String docNo;
	private String parameter;
	private String factory;
	private Double stockYears;
	private String qualityClass;
	private String isDanger;
	private Double checkDays;
	private String qaControlFlag;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public InvCMaterial() {
	}

	/** minimal constructor */
	public InvCMaterial(Long materialId, String materialNo,
			String materialName, String isLot, String isReserved,
			String isDanger, String qaControlFlag, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.materialId = materialId;
		this.materialNo = materialNo;
		this.materialName = materialName;
		this.isLot = isLot;
		this.isReserved = isReserved;
		this.isDanger = isDanger;
		this.qaControlFlag = qaControlFlag;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvCMaterial(Long materialId, String materialNo,
			String materialName, String materialDesc, Long maertialClassId,
			Long materialTypeId, Long materialStatusId, String abcType,
			String isCommodity, String defaultWhsNo, String defaultLocationNo,
			String isLot, Double actualSaleCount, Double standardCostTotal,
			Double actualCostTotal, Double markedPrice, String discountCode,
			String commissionsCode, Double taxRate, Double fobPrice,
			Long stockUmId, Long purUmId, Double purStockUm, Long saleUmId,
			Double saleStockUm, String costMethod, String isReserved,
			Long priceUmId, Double priceStockUm, Double priceCount,
			String buyer, Long primarySupplier, Long secondarySupplier,
			Double maxStock, Double minStock, Double stdCost, Double actCost,
			Double frozenCost, Double minCustomerCount,
			Double standardCustomerCount, Double incrementCustomerCount,
			String planer, String demandCode, Double leadDays, Double length,
			Long lengthUmId, Double width, Long widthUmId, Double heigh,
			Long heighUmId, Double weight, Long wightUmId, Double volumUnit,
			Long volumUmId, Double centigrade, Double fahrenheit,
			String specNo, String docNo, String parameter, String factory,
			Double stockYears, String qualityClass, String isDanger,
			Double checkDays, String qaControlFlag, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.materialId = materialId;
		this.materialNo = materialNo;
		this.materialName = materialName;
		this.materialDesc = materialDesc;
		this.maertialClassId = maertialClassId;
		this.materialTypeId = materialTypeId;
		this.materialStatusId = materialStatusId;
		this.abcType = abcType;
		this.isCommodity = isCommodity;
		this.defaultWhsNo = defaultWhsNo;
		this.defaultLocationNo = defaultLocationNo;
		this.isLot = isLot;
		this.actualSaleCount = actualSaleCount;
		this.standardCostTotal = standardCostTotal;
		this.actualCostTotal = actualCostTotal;
		this.markedPrice = markedPrice;
		this.discountCode = discountCode;
		this.commissionsCode = commissionsCode;
		this.taxRate = taxRate;
		this.fobPrice = fobPrice;
		this.stockUmId = stockUmId;
		this.purUmId = purUmId;
		this.purStockUm = purStockUm;
		this.saleUmId = saleUmId;
		this.saleStockUm = saleStockUm;
		this.costMethod = costMethod;
		this.isReserved = isReserved;
		this.priceUmId = priceUmId;
		this.priceStockUm = priceStockUm;
		this.priceCount = priceCount;
		this.buyer = buyer;
		this.primarySupplier = primarySupplier;
		this.secondarySupplier = secondarySupplier;
		this.maxStock = maxStock;
		this.minStock = minStock;
		this.stdCost = stdCost;
		this.actCost = actCost;
		this.frozenCost = frozenCost;
		this.minCustomerCount = minCustomerCount;
		this.standardCustomerCount = standardCustomerCount;
		this.incrementCustomerCount = incrementCustomerCount;
		this.planer = planer;
		this.demandCode = demandCode;
		this.leadDays = leadDays;
		this.len = length;//this.length = length;
		this.lengthUmId = lengthUmId;
		this.width = width;
		this.widthUmId = widthUmId;
		this.heigh = heigh;
		this.heighUmId = heighUmId;
		this.weight = weight;
		this.wightUmId = wightUmId;
		this.volumUnit = volumUnit;
		this.volumUmId = volumUmId;
		this.centigrade = centigrade;
		this.fahrenheit = fahrenheit;
		this.specNo = specNo;
		this.docNo = docNo;
		this.parameter = parameter;
		this.factory = factory;
		this.stockYears = stockYears;
		this.qualityClass = qualityClass;
		this.isDanger = isDanger;
		this.checkDays = checkDays;
		this.qaControlFlag = qaControlFlag;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "MATERIAL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "MATERIAL_NO", nullable = false, length = 30)
	public String getMaterialNo() {
		return this.materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

	@Column(name = "MATERIAL_NAME", nullable = false, length = 100)
	public String getMaterialName() {
		return this.materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	@Column(name = "MATERIAL_DESC", length = 200)
	public String getMaterialDesc() {
		return this.materialDesc;
	}

	public void setMaterialDesc(String materialDesc) {
		this.materialDesc = materialDesc;
	}

	@Column(name = "MAERTIAL_CLASS_ID", precision = 10, scale = 0)
	public Long getMaertialClassId() {
		return this.maertialClassId;
	}

	public void setMaertialClassId(Long maertialClassId) {
		this.maertialClassId = maertialClassId;
	}

	@Column(name = "MATERIAL_TYPE_ID", precision = 10, scale = 0)
	public Long getMaterialTypeId() {
		return this.materialTypeId;
	}

	public void setMaterialTypeId(Long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}

	@Column(name = "MATERIAL_STATUS_ID", precision = 10, scale = 0)
	public Long getMaterialStatusId() {
		return this.materialStatusId;
	}

	public void setMaterialStatusId(Long materialStatusId) {
		this.materialStatusId = materialStatusId;
	}

	@Column(name = "ABC_TYPE", length = 10)
	public String getAbcType() {
		return this.abcType;
	}

	public void setAbcType(String abcType) {
		this.abcType = abcType;
	}

	@Column(name = "IS_COMMODITY", length = 1)
	public String getIsCommodity() {
		return this.isCommodity;
	}

	public void setIsCommodity(String isCommodity) {
		this.isCommodity = isCommodity;
	}

	@Column(name = "DEFAULT_WHS_NO", length = 10)
	public String getDefaultWhsNo() {
		return this.defaultWhsNo;
	}

	public void setDefaultWhsNo(String defaultWhsNo) {
		this.defaultWhsNo = defaultWhsNo;
	}

	@Column(name = "DEFAULT_LOCATION_NO", length = 10)
	public String getDefaultLocationNo() {
		return this.defaultLocationNo;
	}

	public void setDefaultLocationNo(String defaultLocationNo) {
		this.defaultLocationNo = defaultLocationNo;
	}

	@Column(name = "IS_LOT", nullable = false, length = 1)
	public String getIsLot() {
		return this.isLot;
	}

	public void setIsLot(String isLot) {
		this.isLot = isLot;
	}

	@Column(name = "ACTUAL_SALE_COUNT", precision = 15, scale = 4)
	public Double getActualSaleCount() {
		return this.actualSaleCount;
	}

	public void setActualSaleCount(Double actualSaleCount) {
		this.actualSaleCount = actualSaleCount;
	}

	@Column(name = "STANDARD_COST_TOTAL", precision = 18, scale = 5)
	public Double getStandardCostTotal() {
		return this.standardCostTotal;
	}

	public void setStandardCostTotal(Double standardCostTotal) {
		this.standardCostTotal = standardCostTotal;
	}

	@Column(name = "ACTUAL_COST_TOTAL", precision = 18, scale = 5)
	public Double getActualCostTotal() {
		return this.actualCostTotal;
	}

	public void setActualCostTotal(Double actualCostTotal) {
		this.actualCostTotal = actualCostTotal;
	}

	@Column(name = "MARKED_PRICE", precision = 18, scale = 5)
	public Double getMarkedPrice() {
		return this.markedPrice;
	}

	public void setMarkedPrice(Double markedPrice) {
		this.markedPrice = markedPrice;
	}

	@Column(name = "DISCOUNT_CODE", length = 10)
	public String getDiscountCode() {
		return this.discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	@Column(name = "COMMISSIONS_CODE", length = 10)
	public String getCommissionsCode() {
		return this.commissionsCode;
	}

	public void setCommissionsCode(String commissionsCode) {
		this.commissionsCode = commissionsCode;
	}

	@Column(name = "TAX_RATE", precision = 15, scale = 4)
	public Double getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	@Column(name = "FOB_PRICE", precision = 18, scale = 5)
	public Double getFobPrice() {
		return this.fobPrice;
	}

	public void setFobPrice(Double fobPrice) {
		this.fobPrice = fobPrice;
	}

	@Column(name = "STOCK_UM_ID", precision = 10, scale = 0)
	public Long getStockUmId() {
		return this.stockUmId;
	}

	public void setStockUmId(Long stockUmId) {
		this.stockUmId = stockUmId;
	}

	@Column(name = "PUR_UM_ID", precision = 10, scale = 0)
	public Long getPurUmId() {
		return this.purUmId;
	}

	public void setPurUmId(Long purUmId) {
		this.purUmId = purUmId;
	}

	@Column(name = "PUR_STOCK_UM", precision = 15, scale = 4)
	public Double getPurStockUm() {
		return this.purStockUm;
	}

	public void setPurStockUm(Double purStockUm) {
		this.purStockUm = purStockUm;
	}

	@Column(name = "SALE_UM_ID", precision = 10, scale = 0)
	public Long getSaleUmId() {
		return this.saleUmId;
	}

	public void setSaleUmId(Long saleUmId) {
		this.saleUmId = saleUmId;
	}

	@Column(name = "SALE_STOCK_UM", precision = 15, scale = 4)
	public Double getSaleStockUm() {
		return this.saleStockUm;
	}

	public void setSaleStockUm(Double saleStockUm) {
		this.saleStockUm = saleStockUm;
	}

	@Column(name = "COST_METHOD", length = 2)
	public String getCostMethod() {
		return this.costMethod;
	}

	public void setCostMethod(String costMethod) {
		this.costMethod = costMethod;
	}

	@Column(name = "IS_RESERVED", nullable = false, length = 1)
	public String getIsReserved() {
		return this.isReserved;
	}

	public void setIsReserved(String isReserved) {
		this.isReserved = isReserved;
	}

	@Column(name = "PRICE_UM_ID", precision = 10, scale = 0)
	public Long getPriceUmId() {
		return this.priceUmId;
	}

	public void setPriceUmId(Long priceUmId) {
		this.priceUmId = priceUmId;
	}

	@Column(name = "PRICE_STOCK_UM", precision = 15, scale = 4)
	public Double getPriceStockUm() {
		return this.priceStockUm;
	}

	public void setPriceStockUm(Double priceStockUm) {
		this.priceStockUm = priceStockUm;
	}

	@Column(name = "PRICE_COUNT", precision = 15, scale = 4)
	public Double getPriceCount() {
		return this.priceCount;
	}

	public void setPriceCount(Double priceCount) {
		this.priceCount = priceCount;
	}

	@Column(name = "BUYER", length = 16)
	public String getBuyer() {
		return this.buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	@Column(name = "PRIMARY_SUPPLIER", length = 10)
	public Long getPrimarySupplier() {
		return this.primarySupplier;
	}

	public void setPrimarySupplier(Long primarySupplier) {
		this.primarySupplier = primarySupplier;
	}

	@Column(name = "SECONDARY_SUPPLIER", length = 10)
	public Long getSecondarySupplier() {
		return this.secondarySupplier;
	}

	public void setSecondarySupplier(Long secondarySupplier) {
		this.secondarySupplier = secondarySupplier;
	}

	@Column(name = "MAX_STOCK", precision = 15, scale = 4)
	public Double getMaxStock() {
		return this.maxStock;
	}

	public void setMaxStock(Double maxStock) {
		this.maxStock = maxStock;
	}

	@Column(name = "MIN_STOCK", precision = 15, scale = 4)
	public Double getMinStock() {
		return this.minStock;
	}

	public void setMinStock(Double minStock) {
		this.minStock = minStock;
	}

	@Column(name = "STD_COST", precision = 18, scale = 5)
	public Double getStdCost() {
		return this.stdCost;
	}

	public void setStdCost(Double stdCost) {
		this.stdCost = stdCost;
	}

	@Column(name = "ACT_COST", precision = 18, scale = 5)
	public Double getActCost() {
		return this.actCost;
	}

	public void setActCost(Double actCost) {
		this.actCost = actCost;
	}

	@Column(name = "FROZEN_COST", precision = 18, scale = 5)
	public Double getFrozenCost() {
		return this.frozenCost;
	}

	public void setFrozenCost(Double frozenCost) {
		this.frozenCost = frozenCost;
	}

	@Column(name = "MIN_CUSTOMER_COUNT", precision = 15, scale = 4)
	public Double getMinCustomerCount() {
		return this.minCustomerCount;
	}

	public void setMinCustomerCount(Double minCustomerCount) {
		this.minCustomerCount = minCustomerCount;
	}

	@Column(name = "STANDARD_CUSTOMER_COUNT", precision = 15, scale = 4)
	public Double getStandardCustomerCount() {
		return this.standardCustomerCount;
	}

	public void setStandardCustomerCount(Double standardCustomerCount) {
		this.standardCustomerCount = standardCustomerCount;
	}

	@Column(name = "INCREMENT_CUSTOMER_COUNT", precision = 15, scale = 4)
	public Double getIncrementCustomerCount() {
		return this.incrementCustomerCount;
	}

	public void setIncrementCustomerCount(Double incrementCustomerCount) {
		this.incrementCustomerCount = incrementCustomerCount;
	}

	@Column(name = "PLANER", length = 16)
	public String getPlaner() {
		return this.planer;
	}

	public void setPlaner(String planer) {
		this.planer = planer;
	}

	@Column(name = "DEMAND_CODE", length = 10)
	public String getDemandCode() {
		return this.demandCode;
	}

	public void setDemandCode(String demandCode) {
		this.demandCode = demandCode;
	}

	@Column(name = "LEAD_DAYS", precision = 15, scale = 4)
	public Double getLeadDays() {
		return this.leadDays;
	}

	public void setLeadDays(Double leadDays) {
		this.leadDays = leadDays;
	}

	@Column(name = "LENGTH", precision = 15, scale = 4)
	public Double getLen() {
		return this.len;
	}

	public void setLen(Double length) {
		this.len = length;
	}

	@Column(name = "LENGTH_UM_ID", precision = 10, scale = 0)
	public Long getLengthUmId() {
		return this.lengthUmId;
	}

	public void setLengthUmId(Long lengthUmId) {
		this.lengthUmId = lengthUmId;
	}

	@Column(name = "WIDTH", precision = 15, scale = 4)
	public Double getWidth() {
		return this.width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	@Column(name = "WIDTH_UM_ID", precision = 10, scale = 0)
	public Long getWidthUmId() {
		return this.widthUmId;
	}

	public void setWidthUmId(Long widthUmId) {
		this.widthUmId = widthUmId;
	}

	@Column(name = "HEIGH", precision = 15, scale = 4)
	public Double getHeigh() {
		return this.heigh;
	}

	public void setHeigh(Double heigh) {
		this.heigh = heigh;
	}

	@Column(name = "HEIGH_UM_ID", precision = 10, scale = 0)
	public Long getHeighUmId() {
		return this.heighUmId;
	}

	public void setHeighUmId(Long heighUmId) {
		this.heighUmId = heighUmId;
	}

	@Column(name = "WEIGHT", precision = 15, scale = 4)
	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Column(name = "WIGHT_UM_ID", precision = 10, scale = 0)
	public Long getWightUmId() {
		return this.wightUmId;
	}

	public void setWightUmId(Long wightUmId) {
		this.wightUmId = wightUmId;
	}

	@Column(name = "VOLUM_UNIT", precision = 15, scale = 4)
	public Double getVolumUnit() {
		return this.volumUnit;
	}

	public void setVolumUnit(Double volumUnit) {
		this.volumUnit = volumUnit;
	}

	@Column(name = "VOLUM_UM_ID", precision = 10, scale = 0)
	public Long getVolumUmId() {
		return this.volumUmId;
	}

	public void setVolumUmId(Long volumUmId) {
		this.volumUmId = volumUmId;
	}

	@Column(name = "CENTIGRADE", precision = 15, scale = 4)
	public Double getCentigrade() {
		return this.centigrade;
	}

	public void setCentigrade(Double centigrade) {
		this.centigrade = centigrade;
	}

	@Column(name = "FAHRENHEIT", precision = 15, scale = 4)
	public Double getFahrenheit() {
		return this.fahrenheit;
	}

	public void setFahrenheit(Double fahrenheit) {
		this.fahrenheit = fahrenheit;
	}

	@Column(name = "SPEC_NO", length = 50)
	public String getSpecNo() {
		return this.specNo;
	}

	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}

	@Column(name = "DOC_NO", length = 50)
	public String getDocNo() {
		return this.docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	@Column(name = "PARAMETER", length = 100)
	public String getParameter() {
		return this.parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Column(name = "FACTORY", length = 100)
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Column(name = "STOCK_YEARS", precision = 15, scale = 4)
	public Double getStockYears() {
		return this.stockYears;
	}

	public void setStockYears(Double stockYears) {
		this.stockYears = stockYears;
	}

	@Column(name = "QUALITY_CLASS", length = 10)
	public String getQualityClass() {
		return this.qualityClass;
	}

	public void setQualityClass(String qualityClass) {
		this.qualityClass = qualityClass;
	}

	@Column(name = "IS_DANGER", nullable = false, length = 1)
	public String getIsDanger() {
		return this.isDanger;
	}

	public void setIsDanger(String isDanger) {
		this.isDanger = isDanger;
	}

	@Column(name = "CHECK_DAYS", precision = 15, scale = 4)
	public Double getCheckDays() {
		return this.checkDays;
	}

	public void setCheckDays(Double checkDays) {
		this.checkDays = checkDays;
	}

	@Column(name = "QA_CONTROL_FLAG", nullable = false, length = 1)
	public String getQaControlFlag() {
		return this.qaControlFlag;
	}

	public void setQaControlFlag(String qaControlFlag) {
		this.qaControlFlag = qaControlFlag;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", nullable = false, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}