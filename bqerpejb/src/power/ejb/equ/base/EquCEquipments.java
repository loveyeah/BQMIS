package power.ejb.equ.base;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquCEquipments entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_EQUIPMENTS")
public class EquCEquipments implements java.io.Serializable {

	// Fields

	private Long equId;
	private String locationCode;
	private String attributeCode;
	private String installationCode;
	private String PAttributeCode;
	private String equName;
	private String estimationLevel;
	private Date estimationDate;
	private String assetnum;
	private Long classsStructureId;
	private Long equFailurecodeId;
	private Long priority;
	private String materialCode;
	private String binnum;
	private String mainpertainflag;
	private String calnum;
	private Long movePointNumber;
	private Long whishtPointNumber;
	private String isrunning;
	private String disabled;
	private Date statusdate;
	private Double totdowntime;
	private String manufacturer;
	private String vendor;
	private Double purchaseprice;
	private Date installdate;
	private Date warrantyexpdate;
	private Double designlife;
	private Double invcost;
	private String glaccount;
	private String rotsuspacct;
	private Double replacecost;
	private Double totalcost;
	private Double ytdcost;
	private Double budgetCost;
	private Double unchargedcost;
	private Double totunchargedcost;
	private String belongProfession;
	private String belongTeam;
	private String chargeBy;
	private Date changeDate;
	private String changeBy;
	private String bugCode;
	private String checkMan;
	private String assetType;
	private String lubricateMark;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCEquipments() {
	}

	/** minimal constructor */
	public EquCEquipments(Long equId) {
		this.equId = equId;
	}

	/** full constructor */
	public EquCEquipments(Long equId, String locationCode,
			String attributeCode, String installationCode,
			String PAttributeCode, String equName, String estimationLevel,
			Date estimationDate, String assetnum, Long classsStructureId,
			Long equFailurecodeId, Long priority, String materialCode,
			String binnum, String mainpertainflag, String calnum,
			Long movePointNumber, Long whishtPointNumber, String isrunning,
			String disabled, Date statusdate, Double totdowntime,
			String manufacturer, String vendor, Double purchaseprice,
			Date installdate, Date warrantyexpdate, Double designlife,
			Double invcost, String glaccount, String rotsuspacct,
			Double replacecost, Double totalcost, Double ytdcost,
			Double budgetCost, Double unchargedcost, Double totunchargedcost,
			String belongProfession, String belongTeam, String chargeBy,
			Date changeDate, String changeBy, String bugCode, String checkMan,
			String assetType, String lubricateMark, String enterpriseCode,
			String isUse) {
		this.equId = equId;
		this.locationCode = locationCode;
		this.attributeCode = attributeCode;
		this.installationCode = installationCode;
		this.PAttributeCode = PAttributeCode;
		this.equName = equName;
		this.estimationLevel = estimationLevel;
		this.estimationDate = estimationDate;
		this.assetnum = assetnum;
		this.classsStructureId = classsStructureId;
		this.equFailurecodeId = equFailurecodeId;
		this.priority = priority;
		this.materialCode = materialCode;
		this.binnum = binnum;
		this.mainpertainflag = mainpertainflag;
		this.calnum = calnum;
		this.movePointNumber = movePointNumber;
		this.whishtPointNumber = whishtPointNumber;
		this.isrunning = isrunning;
		this.disabled = disabled;
		this.statusdate = statusdate;
		this.totdowntime = totdowntime;
		this.manufacturer = manufacturer;
		this.vendor = vendor;
		this.purchaseprice = purchaseprice;
		this.installdate = installdate;
		this.warrantyexpdate = warrantyexpdate;
		this.designlife = designlife;
		this.invcost = invcost;
		this.glaccount = glaccount;
		this.rotsuspacct = rotsuspacct;
		this.replacecost = replacecost;
		this.totalcost = totalcost;
		this.ytdcost = ytdcost;
		this.budgetCost = budgetCost;
		this.unchargedcost = unchargedcost;
		this.totunchargedcost = totunchargedcost;
		this.belongProfession = belongProfession;
		this.belongTeam = belongTeam;
		this.chargeBy = chargeBy;
		this.changeDate = changeDate;
		this.changeBy = changeBy;
		this.bugCode = bugCode;
		this.checkMan = checkMan;
		this.assetType = assetType;
		this.lubricateMark = lubricateMark;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "EQU_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEquId() {
		return this.equId;
	}

	public void setEquId(Long equId) {
		this.equId = equId;
	}

	@Column(name = "LOCATION_CODE", length = 30)
	public String getLocationCode() {
		return this.locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	@Column(name = "ATTRIBUTE_CODE", length = 30)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "INSTALLATION_CODE", length = 30)
	public String getInstallationCode() {
		return this.installationCode;
	}

	public void setInstallationCode(String installationCode) {
		this.installationCode = installationCode;
	}

	@Column(name = "P_ATTRIBUTE_CODE", length = 30)
	public String getPAttributeCode() {
		return this.PAttributeCode;
	}

	public void setPAttributeCode(String PAttributeCode) {
		this.PAttributeCode = PAttributeCode;
	}

	@Column(name = "EQU_NAME", length = 100)
	public String getEquName() {
		return this.equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	@Column(name = "ESTIMATION_LEVEL", length = 1)
	public String getEstimationLevel() {
		return this.estimationLevel;
	}

	public void setEstimationLevel(String estimationLevel) {
		this.estimationLevel = estimationLevel;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ESTIMATION_DATE", length = 7)
	public Date getEstimationDate() {
		return this.estimationDate;
	}

	public void setEstimationDate(Date estimationDate) {
		this.estimationDate = estimationDate;
	}

	@Column(name = "ASSETNUM", length = 20)
	public String getAssetnum() {
		return this.assetnum;
	}

	public void setAssetnum(String assetnum) {
		this.assetnum = assetnum;
	}

	@Column(name = "CLASSS_STRUCTURE_ID", precision = 10, scale = 0)
	public Long getClasssStructureId() {
		return this.classsStructureId;
	}

	public void setClasssStructureId(Long classsStructureId) {
		this.classsStructureId = classsStructureId;
	}

	@Column(name = "EQU_FAILURECODE_ID", precision = 10, scale = 0)
	public Long getEquFailurecodeId() {
		return this.equFailurecodeId;
	}

	public void setEquFailurecodeId(Long equFailurecodeId) {
		this.equFailurecodeId = equFailurecodeId;
	}

	@Column(name = "PRIORITY", precision = 10, scale = 0)
	public Long getPriority() {
		return this.priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	@Column(name = "MATERIAL_CODE", length = 20)
	public String getMaterialCode() {
		return this.materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	@Column(name = "BINNUM", length = 10)
	public String getBinnum() {
		return this.binnum;
	}

	public void setBinnum(String binnum) {
		this.binnum = binnum;
	}

	@Column(name = "MAINPERTAINFLAG", length = 1)
	public String getMainpertainflag() {
		return this.mainpertainflag;
	}

	public void setMainpertainflag(String mainpertainflag) {
		this.mainpertainflag = mainpertainflag;
	}

	@Column(name = "CALNUM", length = 10)
	public String getCalnum() {
		return this.calnum;
	}

	public void setCalnum(String calnum) {
		this.calnum = calnum;
	}

	@Column(name = "MOVE_POINT_NUMBER", precision = 10, scale = 0)
	public Long getMovePointNumber() {
		return this.movePointNumber;
	}

	public void setMovePointNumber(Long movePointNumber) {
		this.movePointNumber = movePointNumber;
	}

	@Column(name = "WHISHT_POINT_NUMBER", precision = 10, scale = 0)
	public Long getWhishtPointNumber() {
		return this.whishtPointNumber;
	}

	public void setWhishtPointNumber(Long whishtPointNumber) {
		this.whishtPointNumber = whishtPointNumber;
	}

	@Column(name = "ISRUNNING", length = 1)
	public String getIsrunning() {
		return this.isrunning;
	}

	public void setIsrunning(String isrunning) {
		this.isrunning = isrunning;
	}

	@Column(name = "DISABLED", length = 1)
	public String getDisabled() {
		return this.disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "STATUSDATE", length = 7)
	public Date getStatusdate() {
		return this.statusdate;
	}

	public void setStatusdate(Date statusdate) {
		this.statusdate = statusdate;
	}

	@Column(name = "TOTDOWNTIME", precision = 15, scale = 4)
	public Double getTotdowntime() {
		return this.totdowntime;
	}

	public void setTotdowntime(Double totdowntime) {
		this.totdowntime = totdowntime;
	}

	@Column(name = "MANUFACTURER", length = 8)
	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@Column(name = "VENDOR", length = 30)
	public String getVendor() {
		return this.vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	@Column(name = "PURCHASEPRICE", precision = 15, scale = 4)
	public Double getPurchaseprice() {
		return this.purchaseprice;
	}

	public void setPurchaseprice(Double purchaseprice) {
		this.purchaseprice = purchaseprice;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "INSTALLDATE", length = 7)
	public Date getInstalldate() {
		return this.installdate;
	}

	public void setInstalldate(Date installdate) {
		this.installdate = installdate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "WARRANTYEXPDATE", length = 7)
	public Date getWarrantyexpdate() {
		return this.warrantyexpdate;
	}

	public void setWarrantyexpdate(Date warrantyexpdate) {
		this.warrantyexpdate = warrantyexpdate;
	}

	@Column(name = "DESIGNLIFE", precision = 15, scale = 4)
	public Double getDesignlife() {
		return this.designlife;
	}

	public void setDesignlife(Double designlife) {
		this.designlife = designlife;
	}

	@Column(name = "INVCOST", precision = 15, scale = 4)
	public Double getInvcost() {
		return this.invcost;
	}

	public void setInvcost(Double invcost) {
		this.invcost = invcost;
	}

	@Column(name = "GLACCOUNT", length = 20)
	public String getGlaccount() {
		return this.glaccount;
	}

	public void setGlaccount(String glaccount) {
		this.glaccount = glaccount;
	}

	@Column(name = "ROTSUSPACCT", length = 20)
	public String getRotsuspacct() {
		return this.rotsuspacct;
	}

	public void setRotsuspacct(String rotsuspacct) {
		this.rotsuspacct = rotsuspacct;
	}

	@Column(name = "REPLACECOST", precision = 15, scale = 4)
	public Double getReplacecost() {
		return this.replacecost;
	}

	public void setReplacecost(Double replacecost) {
		this.replacecost = replacecost;
	}

	@Column(name = "TOTALCOST", precision = 15, scale = 4)
	public Double getTotalcost() {
		return this.totalcost;
	}

	public void setTotalcost(Double totalcost) {
		this.totalcost = totalcost;
	}

	@Column(name = "YTDCOST", precision = 15, scale = 4)
	public Double getYtdcost() {
		return this.ytdcost;
	}

	public void setYtdcost(Double ytdcost) {
		this.ytdcost = ytdcost;
	}

	@Column(name = "BUDGET_COST", precision = 15, scale = 4)
	public Double getBudgetCost() {
		return this.budgetCost;
	}

	public void setBudgetCost(Double budgetCost) {
		this.budgetCost = budgetCost;
	}

	@Column(name = "UNCHARGEDCOST", precision = 15, scale = 4)
	public Double getUnchargedcost() {
		return this.unchargedcost;
	}

	public void setUnchargedcost(Double unchargedcost) {
		this.unchargedcost = unchargedcost;
	}

	@Column(name = "TOTUNCHARGEDCOST", precision = 15, scale = 4)
	public Double getTotunchargedcost() {
		return this.totunchargedcost;
	}

	public void setTotunchargedcost(Double totunchargedcost) {
		this.totunchargedcost = totunchargedcost;
	}

	@Column(name = "BELONG_PROFESSION", length = 4)
	public String getBelongProfession() {
		return this.belongProfession;
	}

	public void setBelongProfession(String belongProfession) {
		this.belongProfession = belongProfession;
	}

	@Column(name = "BELONG_TEAM", length = 20)
	public String getBelongTeam() {
		return this.belongTeam;
	}

	public void setBelongTeam(String belongTeam) {
		this.belongTeam = belongTeam;
	}

	@Column(name = "CHARGE_BY", length = 30)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHANGE_DATE", length = 7)
	public Date getChangeDate() {
		return this.changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	@Column(name = "CHANGE_BY", length = 30)
	public String getChangeBy() {
		return this.changeBy;
	}

	public void setChangeBy(String changeBy) {
		this.changeBy = changeBy;
	}

	@Column(name = "BUG_CODE", length = 30)
	public String getBugCode() {
		return this.bugCode;
	}

	public void setBugCode(String bugCode) {
		this.bugCode = bugCode;
	}

	@Column(name = "CHECK_MAN", length = 30)
	public String getCheckMan() {
		return this.checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	@Column(name = "ASSET_TYPE", length = 10)
	public String getAssetType() {
		return this.assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	@Column(name = "LUBRICATE_MARK", length = 2)
	public String getLubricateMark() {
		return this.lubricateMark;
	}

	public void setLubricateMark(String lubricateMark) {
		this.lubricateMark = lubricateMark;
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

}