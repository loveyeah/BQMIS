package power.ejb.productiontec.relayProtection;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJdbhJBhzztz entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_J_BHZZTZ")
public class PtJdbhJBhzztz implements java.io.Serializable {

	// Fields

	private Long deviceId;
	private Long protectedDeviceId;
	private String equCode;
	private String voltage;
	private String deviceType;
	private String sizeType;
	private String sizes;
	private String manufacturer;
	private Date outFactoryDate;
	private String outFactoryNo;
	private String installPlace;
	private Long testCycle;
	private Date lastCheckDate;
	private Date nextCheckDate;
	private String chargeBy;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhJBhzztz() {
	}

	/** minimal constructor */
	public PtJdbhJBhzztz(Long deviceId) {
		this.deviceId = deviceId;
	}

	/** full constructor */
	public PtJdbhJBhzztz(Long deviceId, Long protectedDeviceId, String equCode,
			String voltage, String deviceType, String sizeType, String sizes,
			String manufacturer, Date outFactoryDate, String outFactoryNo,
			String installPlace, Long testCycle, Date lastCheckDate,
			Date nextCheckDate, String chargeBy, String memo,
			String enterpriseCode) {
		this.deviceId = deviceId;
		this.protectedDeviceId = protectedDeviceId;
		this.equCode = equCode;
		this.voltage = voltage;
		this.deviceType = deviceType;
		this.sizeType = sizeType;
		this.sizes = sizes;
		this.manufacturer = manufacturer;
		this.outFactoryDate = outFactoryDate;
		this.outFactoryNo = outFactoryNo;
		this.installPlace = installPlace;
		this.testCycle = testCycle;
		this.lastCheckDate = lastCheckDate;
		this.nextCheckDate = nextCheckDate;
		this.chargeBy = chargeBy;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DEVICE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "PROTECTED_DEVICE_ID", precision = 10, scale = 0)
	public Long getProtectedDeviceId() {
		return this.protectedDeviceId;
	}

	public void setProtectedDeviceId(Long protectedDeviceId) {
		this.protectedDeviceId = protectedDeviceId;
	}


	@Column(name = "EQU_CODE", length = 30)
	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

	@Column(name = "VOLTAGE", length = 20)
	public String getVoltage() {
		return this.voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	@Column(name = "DEVICE_TYPE", length = 20)
	public String getDeviceType() {
		return this.deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	@Column(name = "SIZE_TYPE", length = 20)
	public String getSizeType() {
		return this.sizeType;
	}

	public void setSizeType(String sizeType) {
		this.sizeType = sizeType;
	}

	@Column(name = "SIZES", length = 50)
	public String getSizes() {
		return this.sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	@Column(name = "MANUFACTURER", length = 50)
	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OUT_FACTORY_DATE", length = 7)
	public Date getOutFactoryDate() {
		return this.outFactoryDate;
	}

	public void setOutFactoryDate(Date outFactoryDate) {
		this.outFactoryDate = outFactoryDate;
	}

	@Column(name = "OUT_FACTORY_NO", length = 50)
	public String getOutFactoryNo() {
		return this.outFactoryNo;
	}

	public void setOutFactoryNo(String outFactoryNo) {
		this.outFactoryNo = outFactoryNo;
	}

	@Column(name = "INSTALL_PLACE", length = 50)
	public String getInstallPlace() {
		return this.installPlace;
	}

	public void setInstallPlace(String installPlace) {
		this.installPlace = installPlace;
	}

	@Column(name = "TEST_CYCLE", precision = 4, scale = 0)
	public Long getTestCycle() {
		return this.testCycle;
	}

	public void setTestCycle(Long testCycle) {
		this.testCycle = testCycle;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_CHECK_DATE", length = 7)
	public Date getLastCheckDate() {
		return this.lastCheckDate;
	}

	public void setLastCheckDate(Date lastCheckDate) {
		this.lastCheckDate = lastCheckDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "NEXT_CHECK_DATE", length = 7)
	public Date getNextCheckDate() {
		return this.nextCheckDate;
	}

	public void setNextCheckDate(Date nextCheckDate) {
		this.nextCheckDate = nextCheckDate;
	}

	@Column(name = "CHARGE_BY", length = 16)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
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

}