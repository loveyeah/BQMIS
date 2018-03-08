package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpCFireControl entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_C_FIRE_CONTROL")
public class SpCFireControl implements java.io.Serializable {

	// Fields

	private Long id;
	private String deployPart;
	private String type;
	private String param;
	private Double controlNumber;
	private String serialCode;
	private Date validDate;
	private Date checkDate;
	private Date changeDate;
	private String checkBy;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	
	// Constructors

	/** default constructor */
	public SpCFireControl() {
	}

	/** minimal constructor */
	public SpCFireControl(Long id) {
		this.id = id;
	}

	/** full constructor */
	public SpCFireControl(Long id, String deployPart, String type,
			String param, Double number, String serialCode, Date validDate,
			Date checkDate, Date changeDate, String checkBy, String memo,
			String isUse, String enterpriseCode) {
		this.id = id;
		this.deployPart = deployPart;
		this.type = type;
		this.param = param;
		this.controlNumber = number;
		this.serialCode = serialCode;
		this.validDate = validDate;
		this.checkDate = checkDate;
		this.changeDate = changeDate;
		this.checkBy = checkBy;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DEPLOY_PART", length = 50)
	public String getDeployPart() {
		return this.deployPart;
	}

	public void setDeployPart(String deployPart) {
		this.deployPart = deployPart;
	}

	@Column(name = "TYPE", length = 50)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "PARAM", length = 50)
	public String getParam() {
		return this.param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Column(name = "SERIAL_CODE", length = 30)
	public String getSerialCode() {
		return this.serialCode;
	}

	public void setSerialCode(String serialCode) {
		this.serialCode = serialCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "VALID_DATE", length = 7)
	public Date getValidDate() {
		return this.validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHECK_DATE", length = 7)
	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHANGE_DATE", length = 7)
	public Date getChangeDate() {
		return this.changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	@Column(name = "CHECK_BY", length = 20)
	public String getCheckBy() {
		return this.checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	@Column(name = "MEMO", length = 300)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
	
	@Column(name = "CONTROL_NUMBER", precision = 15, scale = 4)
	public Double getControlNumber() {
		return controlNumber;
	}

	public void setControlNumber(Double controlNumber) {
		this.controlNumber = controlNumber;
	}

}