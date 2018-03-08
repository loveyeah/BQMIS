package power.ejb.workticket.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunJWorkticketMeasure entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_WORKTICKET_MEASURE")
public class RunJWorkticketMeasure implements java.io.Serializable {

	// Fields

	private Long measureId;
	private String workticketNo;
	private String measureLocation;
	private String useTool;
	private String combustibleGas;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunJWorkticketMeasure() {
	}

	/** minimal constructor */
	public RunJWorkticketMeasure(Long measureId) {
		this.measureId = measureId;
	}

	/** full constructor */
	public RunJWorkticketMeasure(Long measureId, String workticketNo,
			String measureLocation, String useTool, String combustibleGas,
			String enterpriseCode, String isUse) {
		this.measureId = measureId;
		this.workticketNo = workticketNo;
		this.measureLocation = measureLocation;
		this.useTool = useTool;
		this.combustibleGas = combustibleGas;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "MEASURE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMeasureId() {
		return this.measureId;
	}

	public void setMeasureId(Long measureId) {
		this.measureId = measureId;
	}

	@Column(name = "WORKTICKET_NO", length = 22)
	public String getWorkticketNo() {
		return this.workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	@Column(name = "MEASURE_LOCATION", length = 100)
	public String getMeasureLocation() {
		return this.measureLocation;
	}

	public void setMeasureLocation(String measureLocation) {
		this.measureLocation = measureLocation;
	}

	@Column(name = "USE_TOOL", length = 100)
	public String getUseTool() {
		return this.useTool;
	}

	public void setUseTool(String useTool) {
		this.useTool = useTool;
	}

	@Column(name = "COMBUSTIBLE_GAS", length = 100)
	public String getCombustibleGas() {
		return this.combustibleGas;
	}

	public void setCombustibleGas(String combustibleGas) {
		this.combustibleGas = combustibleGas;
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