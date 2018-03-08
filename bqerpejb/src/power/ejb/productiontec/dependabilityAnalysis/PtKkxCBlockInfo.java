package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtKkxCBlockInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_KKX_C_BLOCK_INFO")
public class PtKkxCBlockInfo implements java.io.Serializable {

	// Fields

	private Long blockInfoId;
	private String blockCode;
	private String mgCode;
	private Double capacity;
	private String fuelName;
	private Date businessServiceDate;
	private Date statBeginDate;
	private String boilerName;
	private String steamerMachine;
	private String generationName;
	private String primaryTransformer;
	private String mgType;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtKkxCBlockInfo() {
	}

	/** minimal constructor */
	public PtKkxCBlockInfo(Long blockInfoId) {
		this.blockInfoId = blockInfoId;
	}

	/** full constructor */
	public PtKkxCBlockInfo(Long blockInfoId, String blockCode, String mgCode,
			Double capacity, String fuelName, Date businessServiceDate,
			Date statBeginDate, String boilerName, String steamerMachine,
			String generationName, String primaryTransformer, String mgType,
			String enterpriseCode) {
		this.blockInfoId = blockInfoId;
		this.blockCode = blockCode;
		this.mgCode = mgCode;
		this.capacity = capacity;
		this.fuelName = fuelName;
		this.businessServiceDate = businessServiceDate;
		this.statBeginDate = statBeginDate;
		this.boilerName = boilerName;
		this.steamerMachine = steamerMachine;
		this.generationName = generationName;
		this.primaryTransformer = primaryTransformer;
		this.mgType = mgType;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BLOCK_INFO_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBlockInfoId() {
		return this.blockInfoId;
	}

	public void setBlockInfoId(Long blockInfoId) {
		this.blockInfoId = blockInfoId;
	}

	@Column(name = "BLOCK_CODE", length = 2)
	public String getBlockCode() {
		return this.blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	@Column(name = "MG_CODE", length = 20)
	public String getMgCode() {
		return this.mgCode;
	}

	public void setMgCode(String mgCode) {
		this.mgCode = mgCode;
	}

	@Column(name = "CAPACITY", precision = 10)
	public Double getCapacity() {
		return this.capacity;
	}

	public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}

	@Column(name = "FUEL_NAME", length = 50)
	public String getFuelName() {
		return this.fuelName;
	}

	public void setFuelName(String fuelName) {
		this.fuelName = fuelName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BUSINESS_SERVICE_DATE", length = 7)
	public Date getBusinessServiceDate() {
		return this.businessServiceDate;
	}

	public void setBusinessServiceDate(Date businessServiceDate) {
		this.businessServiceDate = businessServiceDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STAT_BEGIN_DATE", length = 7)
	public Date getStatBeginDate() {
		return this.statBeginDate;
	}

	public void setStatBeginDate(Date statBeginDate) {
		this.statBeginDate = statBeginDate;
	}

	@Column(name = "BOILER_NAME", length = 200)
	public String getBoilerName() {
		return this.boilerName;
	}

	public void setBoilerName(String boilerName) {
		this.boilerName = boilerName;
	}

	@Column(name = "STEAMER_MACHINE", length = 200)
	public String getSteamerMachine() {
		return this.steamerMachine;
	}

	public void setSteamerMachine(String steamerMachine) {
		this.steamerMachine = steamerMachine;
	}

	@Column(name = "GENERATION_NAME", length = 200)
	public String getGenerationName() {
		return this.generationName;
	}

	public void setGenerationName(String generationName) {
		this.generationName = generationName;
	}

	@Column(name = "PRIMARY_TRANSFORMER", length = 200)
	public String getPrimaryTransformer() {
		return this.primaryTransformer;
	}

	public void setPrimaryTransformer(String primaryTransformer) {
		this.primaryTransformer = primaryTransformer;
	}

	@Column(name = "MG_TYPE", length = 200)
	public String getMgType() {
		return this.mgType;
	}

	public void setMgType(String mgType) {
		this.mgType = mgType;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}