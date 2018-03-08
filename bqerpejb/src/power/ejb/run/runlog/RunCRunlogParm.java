package power.ejb.run.runlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCRunlogParm entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_RUNLOG_PARM")
public class RunCRunlogParm implements java.io.Serializable {

	// Fields

	private Long runlogParmId;
	private Long itemId;
	private String itemCode;
	private String itemName;
	private String specialityCode;
	private Long unitMessureId;
	private String statType;
	private Long diaplayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCRunlogParm() {
	}

	/** minimal constructor */
	public RunCRunlogParm(Long runlogParmId) {
		this.runlogParmId = runlogParmId;
	}

	/** full constructor */
	public RunCRunlogParm(Long runlogParmId, Long itemId, String itemCode,
			String itemName, String specialityCode, Long unitMessureId,
			String statType, Long diaplayNo, String isUse, String enterpriseCode) {
		this.runlogParmId = runlogParmId;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.specialityCode = specialityCode;
		this.unitMessureId = unitMessureId;
		this.statType = statType;
		this.diaplayNo = diaplayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RUNLOG_PARM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRunlogParmId() {
		return this.runlogParmId;
	}

	public void setRunlogParmId(Long runlogParmId) {
		this.runlogParmId = runlogParmId;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "ITEM_CODE", length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "ITEM_NAME", length = 100)
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "SPECIALITY_CODE", length = 10)
	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	@Column(name = "UNIT_MESSURE_ID", precision = 10, scale = 0)
	public Long getUnitMessureId() {
		return this.unitMessureId;
	}

	public void setUnitMessureId(Long unitMessureId) {
		this.unitMessureId = unitMessureId;
	}

	@Column(name = "STAT_TYPE", length = 5)
	public String getStatType() {
		return this.statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}

	@Column(name = "DIAPLAY_NO", precision = 10, scale = 0)
	public Long getDiaplayNo() {
		return this.diaplayNo;
	}

	public void setDiaplayNo(Long diaplayNo) {
		this.diaplayNo = diaplayNo;
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