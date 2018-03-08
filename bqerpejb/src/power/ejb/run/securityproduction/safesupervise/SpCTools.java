package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpCTools entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_C_TOOLS", schema = "POWER")
public class SpCTools implements java.io.Serializable {

	// Fields

	private Long toolId;
	private String toolCode;
	private String toolName;
	private String toolType;
	private String toolModel;
	private Date factoryDate;
	private String memo;
	private String chargeBy;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpCTools() {
	}

	/** minimal constructor */
	public SpCTools(Long toolId) {
		this.toolId = toolId;
	}

	/** full constructor */
	public SpCTools(Long toolId, String toolCode, String toolName,
			String toolType, String toolModel, Date factoryDate, String memo,
			String chargeBy, String isUse, String enterpriseCode) {
		this.toolId = toolId;
		this.toolCode = toolCode;
		this.toolName = toolName;
		this.toolType = toolType;
		this.toolModel = toolModel;
		this.factoryDate = factoryDate;
		this.memo = memo;
		this.chargeBy = chargeBy;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TOOL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getToolId() {
		return this.toolId;
	}

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}

	@Column(name = "TOOL_CODE", length = 50)
	public String getToolCode() {
		return this.toolCode;
	}

	public void setToolCode(String toolCode) {
		this.toolCode = toolCode;
	}

	@Column(name = "TOOL_NAME", length = 200)
	public String getToolName() {
		return this.toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	@Column(name = "TOOL_TYPE", length = 1)
	public String getToolType() {
		return this.toolType;
	}

	public void setToolType(String toolType) {
		this.toolType = toolType;
	}

	@Column(name = "TOOL_MODEL", length = 100)
	public String getToolModel() {
		return this.toolModel;
	}

	public void setToolModel(String toolModel) {
		this.toolModel = toolModel;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FACTORY_DATE", length = 7)
	public Date getFactoryDate() {
		return this.factoryDate;
	}

	public void setFactoryDate(Date factoryDate) {
		this.factoryDate = factoryDate;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "CHARGE_BY", length = 20)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
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