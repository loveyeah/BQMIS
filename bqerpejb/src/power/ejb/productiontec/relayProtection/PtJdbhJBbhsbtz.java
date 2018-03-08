package power.ejb.productiontec.relayProtection;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJdbhJBbhsbtz entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_J_BBHSBTZ")
public class PtJdbhJBbhsbtz implements java.io.Serializable {

	// Fields

	private Long protectedDeviceId;
	private String equCode;
	private String equLevel;
	private String voltage;
	private String installPlace;
	private String manufacturer;
	private String sizes;
	private String outFactoryNo;
	private Date outFactoryDate;
	private String chargeMan;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhJBbhsbtz() {
	}

	/** minimal constructor */
	public PtJdbhJBbhsbtz(Long protectedDeviceId) {
		this.protectedDeviceId = protectedDeviceId;
	}

	/** full constructor */
	public PtJdbhJBbhsbtz(Long protectedDeviceId, String equCode, String equLevel,
			String voltage, String installPlace, String manufacturer,
			String sizes, String outFactoryNo, Date outFactoryDate,
			String chargeMan, String memo, String enterpriseCode) {
		this.protectedDeviceId = protectedDeviceId;
		this.equCode = equCode;
		this.equLevel = equLevel;
		this.voltage = voltage;
		this.installPlace = installPlace;
		this.manufacturer = manufacturer;
		this.sizes = sizes;
		this.outFactoryNo = outFactoryNo;
		this.outFactoryDate = outFactoryDate;
		this.chargeMan = chargeMan;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PROTECTED_DEVICE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getProtectedDeviceId() {
		return this.protectedDeviceId;
	}

	public void setProtectedDeviceId(Long protectedDeviceId) {
		this.protectedDeviceId = protectedDeviceId;
	}





	@Column(name = "VOLTAGE", length = 20)
	public String getVoltage() {
		return this.voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	@Column(name = "INSTALL_PLACE", length = 50)
	public String getInstallPlace() {
		return this.installPlace;
	}

	public void setInstallPlace(String installPlace) {
		this.installPlace = installPlace;
	}

	@Column(name = "MANUFACTURER", length = 100)
	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@Column(name = "SIZES", length = 50)
	public String getSizes() {
		return this.sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	@Column(name = "OUT_FACTORY_NO", length = 50)
	public String getOutFactoryNo() {
		return this.outFactoryNo;
	}

	public void setOutFactoryNo(String outFactoryNo) {
		this.outFactoryNo = outFactoryNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OUT_FACTORY_DATE", length = 7)
	public Date getOutFactoryDate() {
		return this.outFactoryDate;
	}

	public void setOutFactoryDate(Date outFactoryDate) {
		this.outFactoryDate = outFactoryDate;
	}

	@Column(name = "CHARGE_MAN", length = 16)
	public String getChargeMan() {
		return this.chargeMan;
	}

	public void setChargeMan(String chargeMan) {
		this.chargeMan = chargeMan;
	}

	@Column(name = "MEMO", length = 256)
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

	@Column(name = "EQU_LEVEL", length = 20)
	public String getEquLevel() {
		return equLevel;
	}

	public void setEquLevel(String equLevel) {
		this.equLevel = equLevel;
	}

	@Column(name = "EQU_CODE", length = 30)
	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

}