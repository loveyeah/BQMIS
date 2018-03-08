package power.ejb.productiontec.insulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJyjdJSbtzlh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JYJD_J_SBTZLH")
public class PtJyjdJSbtzlh implements java.io.Serializable {

	// Fields

	private Long deviceId;
	private String deviceName;
	private Long testCycle;
	private String factory;
	private String sizes;
	private String userRange;
	private String voltage;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJyjdJSbtzlh() {
	}

	/** minimal constructor */
	public PtJyjdJSbtzlh(Long deviceId) {
		this.deviceId = deviceId;
	}

	/** full constructor */
	public PtJyjdJSbtzlh(Long deviceId, String deviceName, Long testCycle,
			String factory, String sizes, String userRange, String voltage,
			String memo, String enterpriseCode) {
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.testCycle = testCycle;
		this.factory = factory;
		this.sizes = sizes;
		this.userRange = userRange;
		this.voltage = voltage;
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

	@Column(name = "DEVICE_NAME", length = 100)
	public String getDeviceName() {
		return this.deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Column(name = "TEST_CYCLE", precision = 22, scale = 0)
	public Long getTestCycle() {
		return this.testCycle;
	}

	public void setTestCycle(Long testCycle) {
		this.testCycle = testCycle;
	}

	@Column(name = "FACTORY", length = 100)
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Column(name = "SIZES", length = 50)
	public String getSizes() {
		return this.sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	@Column(name = "USER_RANGE", length = 50)
	public String getUserRange() {
		return this.userRange;
	}

	public void setUserRange(String userRange) {
		this.userRange = userRange;
	}

	@Column(name = "VOLTAGE", length = 50)
	public String getVoltage() {
		return this.voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
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