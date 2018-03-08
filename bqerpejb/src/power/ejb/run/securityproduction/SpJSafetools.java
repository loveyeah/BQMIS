package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJSafetools entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_SAFETOOLS")
public class SpJSafetools implements java.io.Serializable {

	// Fields

	private Long toolsId;
	private String toolsNames;
	private String toolsCode;
	private String toolsSizes;
	private String factory;
	private Date manuDate;
	private String chargeMan;
	private Date checkDate;
	private String state;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafetools() {
	}

	/** minimal constructor */
	public SpJSafetools(Long toolsId) {
		this.toolsId = toolsId;
	}

	/** full constructor */
	public SpJSafetools(Long toolsId, String toolsNames, String toolsCode,
			String toolsSizes, String factory, Date manuDate, String chargeMan,
			Date checkDate, String state, String memo, String enterpriseCode) {
		this.toolsId = toolsId;
		this.toolsNames = toolsNames;
		this.toolsCode = toolsCode;
		this.toolsSizes = toolsSizes;
		this.factory = factory;
		this.manuDate = manuDate;
		this.chargeMan = chargeMan;
		this.checkDate = checkDate;
		this.state = state;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TOOLS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getToolsId() {
		return this.toolsId;
	}

	public void setToolsId(Long toolsId) {
		this.toolsId = toolsId;
	}

	@Column(name = "TOOLS_NAMES", length = 50)
	public String getToolsNames() {
		return this.toolsNames;
	}

	public void setToolsNames(String toolsNames) {
		this.toolsNames = toolsNames;
	}

	@Column(name = "TOOLS_CODE", length = 30)
	public String getToolsCode() {
		return this.toolsCode;
	}

	public void setToolsCode(String toolsCode) {
		this.toolsCode = toolsCode;
	}

	@Column(name = "TOOLS_SIZES", length = 30)
	public String getToolsSizes() {
		return this.toolsSizes;
	}

	public void setToolsSizes(String toolsSizes) {
		this.toolsSizes = toolsSizes;
	}

	@Column(name = "FACTORY", length = 128)
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MANU_DATE", length = 7)
	public Date getManuDate() {
		return this.manuDate;
	}

	public void setManuDate(Date manuDate) {
		this.manuDate = manuDate;
	}

	@Column(name = "CHARGE_MAN", length = 16)
	public String getChargeMan() {
		return this.chargeMan;
	}

	public void setChargeMan(String chargeMan) {
		this.chargeMan = chargeMan;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECK_DATE", length = 7)
	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name = "STATE", length = 16)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}