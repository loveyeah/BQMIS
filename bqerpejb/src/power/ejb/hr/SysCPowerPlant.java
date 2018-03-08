package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysCPowerPlant entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_C_POWER_PLANT", schema = "power")
public class SysCPowerPlant implements java.io.Serializable
{

	// Fields

	private Long powerPlantId;
	private Long ppowerPlantId;
	private String ppowerPlantCode;
	private String powerPlantCode;
	private String powerPlantName;
	private String powerPlantAlias;
	private Long natureId;
	private Long powerTypeId;
	private String unitsCapacity;
	private Long runsaleStatusId;
	private Long schedulingRelationsId;
	private Long accountingMethodsId;
	private String manager;
	private String isPowerPlant;
	private String currentUnit;
	private Long regionalismTypeId;
	private Long city;
	private String isUse;
	private String memo;
	private String retrieveCode;
	private Long createBy;
	private Date createDate;
	private Long modifiyBy;
	private Date modifiyDate;
	private Long logoffBy;
	private Date logoffDate;

	// Constructors

	/** default constructor */
	public SysCPowerPlant()
	{
	}

	/** minimal constructor */
	public SysCPowerPlant(Long powerPlantId)
	{
		this.powerPlantId = powerPlantId;
	}

	/** full constructor */
	public SysCPowerPlant(Long powerPlantId, Long ppowerPlantId,
			String ppowerPlantCode, String powerPlantCode,
			String powerPlantName, String powerPlantAlias, Long natureId,
			Long powerTypeId, String unitsCapacity, Long runsaleStatusId,
			Long schedulingRelationsId, Long accountingMethodsId,
			String manager, String isPowerPlant, String currentUnit,
			Long regionalismTypeId, Long city, String isUse, String memo,
			String retrieveCode, Long createBy, Date createDate,
			Long modifiyBy, Date modifiyDate, Long logoffBy, Date logoffDate)
	{
		this.powerPlantId = powerPlantId;
		this.ppowerPlantId = ppowerPlantId;
		this.ppowerPlantCode = ppowerPlantCode;
		this.powerPlantCode = powerPlantCode;
		this.powerPlantName = powerPlantName;
		this.powerPlantAlias = powerPlantAlias;
		this.natureId = natureId;
		this.powerTypeId = powerTypeId;
		this.unitsCapacity = unitsCapacity;
		this.runsaleStatusId = runsaleStatusId;
		this.schedulingRelationsId = schedulingRelationsId;
		this.accountingMethodsId = accountingMethodsId;
		this.manager = manager;
		this.isPowerPlant = isPowerPlant;
		this.currentUnit = currentUnit;
		this.regionalismTypeId = regionalismTypeId;
		this.city = city;
		this.isUse = isUse;
		this.memo = memo;
		this.retrieveCode = retrieveCode;
		this.createBy = createBy;
		this.createDate = createDate;
		this.modifiyBy = modifiyBy;
		this.modifiyDate = modifiyDate;
		this.logoffBy = logoffBy;
		this.logoffDate = logoffDate;
	}

	// Property accessors
	@Id
	@Column(name = "POWER_PLANT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPowerPlantId()
	{
		return this.powerPlantId;
	}

	public void setPowerPlantId(Long powerPlantId)
	{
		this.powerPlantId = powerPlantId;
	}

	@Column(name = "PPOWER_PLANT_ID", precision = 10, scale = 0)
	public Long getPpowerPlantId()
	{
		return this.ppowerPlantId;
	}

	public void setPpowerPlantId(Long ppowerPlantId)
	{
		this.ppowerPlantId = ppowerPlantId;
	}

	@Column(name = "PPOWER_PLANT_CODE", length = 20)
	public String getPpowerPlantCode()
	{
		return this.ppowerPlantCode;
	}

	public void setPpowerPlantCode(String ppowerPlantCode)
	{
		this.ppowerPlantCode = ppowerPlantCode;
	}

	@Column(name = "POWER_PLANT_CODE", length = 20)
	public String getPowerPlantCode()
	{
		return this.powerPlantCode;
	}

	public void setPowerPlantCode(String powerPlantCode)
	{
		this.powerPlantCode = powerPlantCode;
	}

	@Column(name = "POWER_PLANT_NAME", length = 100)
	public String getPowerPlantName()
	{
		return this.powerPlantName;
	}

	public void setPowerPlantName(String powerPlantName)
	{
		this.powerPlantName = powerPlantName;
	}

	@Column(name = "POWER_PLANT_ALIAS", length = 100)
	public String getPowerPlantAlias()
	{
		return this.powerPlantAlias;
	}

	public void setPowerPlantAlias(String powerPlantAlias)
	{
		this.powerPlantAlias = powerPlantAlias;
	}

	@Column(name = "NATURE_ID", precision = 10, scale = 0)
	public Long getNatureId()
	{
		return this.natureId;
	}

	public void setNatureId(Long natureId)
	{
		this.natureId = natureId;
	}

	@Column(name = "POWER_TYPE_ID", precision = 10, scale = 0)
	public Long getPowerTypeId()
	{
		return this.powerTypeId;
	}

	public void setPowerTypeId(Long powerTypeId)
	{
		this.powerTypeId = powerTypeId;
	}

	@Column(name = "UNITS_CAPACITY", length = 20)
	public String getUnitsCapacity()
	{
		return this.unitsCapacity;
	}

	public void setUnitsCapacity(String unitsCapacity)
	{
		this.unitsCapacity = unitsCapacity;
	}

	@Column(name = "RUNSALE_STATUS_ID", precision = 10, scale = 0)
	public Long getRunsaleStatusId()
	{
		return this.runsaleStatusId;
	}

	public void setRunsaleStatusId(Long runsaleStatusId)
	{
		this.runsaleStatusId = runsaleStatusId;
	}

	@Column(name = "SCHEDULING_RELATIONS_ID", precision = 10, scale = 0)
	public Long getSchedulingRelationsId()
	{
		return this.schedulingRelationsId;
	}

	public void setSchedulingRelationsId(Long schedulingRelationsId)
	{
		this.schedulingRelationsId = schedulingRelationsId;
	}

	@Column(name = "ACCOUNTING_METHODS_ID", precision = 10, scale = 0)
	public Long getAccountingMethodsId()
	{
		return this.accountingMethodsId;
	}

	public void setAccountingMethodsId(Long accountingMethodsId)
	{
		this.accountingMethodsId = accountingMethodsId;
	}

	@Column(name = "MANAGER", length = 20)
	public String getManager()
	{
		return this.manager;
	}

	public void setManager(String manager)
	{
		this.manager = manager;
	}

	@Column(name = "IS_POWER_PLANT", length = 1)
	public String getIsPowerPlant()
	{
		return this.isPowerPlant;
	}

	public void setIsPowerPlant(String isPowerPlant)
	{
		this.isPowerPlant = isPowerPlant;
	}

	@Column(name = "CURRENT_UNIT", length = 1)
	public String getCurrentUnit()
	{
		return this.currentUnit;
	}

	public void setCurrentUnit(String currentUnit)
	{
		this.currentUnit = currentUnit;
	}

	@Column(name = "REGIONALISM_TYPE_ID", precision = 2, scale = 0)
	public Long getRegionalismTypeId()
	{
		return this.regionalismTypeId;
	}

	public void setRegionalismTypeId(Long regionalismTypeId)
	{
		this.regionalismTypeId = regionalismTypeId;
	}

	@Column(name = "CITY", precision = 10, scale = 0)
	public Long getCity()
	{
		return this.city;
	}

	public void setCity(Long city)
	{
		this.city = city;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse()
	{
		return this.isUse;
	}

	public void setIsUse(String isUse)
	{
		this.isUse = isUse;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo()
	{
		return this.memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	@Column(name = "RETRIEVE_CODE", length = 20)
	public String getRetrieveCode()
	{
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode)
	{
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "CREATE_BY", precision = 10, scale = 0)
	public Long getCreateBy()
	{
		return this.createBy;
	}

	public void setCreateBy(Long createBy)
	{
		this.createBy = createBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate()
	{
		return this.createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	@Column(name = "MODIFIY_BY", precision = 10, scale = 0)
	public Long getModifiyBy()
	{
		return this.modifiyBy;
	}

	public void setModifiyBy(Long modifiyBy)
	{
		this.modifiyBy = modifiyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIY_DATE", length = 7)
	public Date getModifiyDate()
	{
		return this.modifiyDate;
	}

	public void setModifiyDate(Date modifiyDate)
	{
		this.modifiyDate = modifiyDate;
	}

	@Column(name = "LOGOFF_BY", precision = 10, scale = 0)
	public Long getLogoffBy()
	{
		return this.logoffBy;
	}

	public void setLogoffBy(Long logoffBy)
	{
		this.logoffBy = logoffBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LOGOFF_DATE", length = 7)
	public Date getLogoffDate()
	{
		return this.logoffDate;
	}

	public void setLogoffDate(Date logoffDate)
	{
		this.logoffDate = logoffDate;
	}

}