package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractKksCMainsTransformer entity provides the base persistence definition
 * of the KksCMainsTransformer entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractKksCMainsTransformer implements
		java.io.Serializable {

	// Fields

	private String zbyqNumber;
	private String zbyqSys;
	private String zbyqPattern;
	private String zbyqSpecifiedCapycity;
	private String zbyqSpecifiedTension;
	private String zbyqConnectionPattern;
	private Long zbyqCoilCount;
	private String zbyqCoolingPattern;
	private String zbyqMakerNumber;
	private String zbyqMakerName;
	private Date zbyqMakerData;

	// Constructors

	/** default constructor */
	public AbstractKksCMainsTransformer() {
	}

	/** minimal constructor */
	public AbstractKksCMainsTransformer(String zbyqNumber) {
		this.zbyqNumber = zbyqNumber;
	}

	/** full constructor */
	public AbstractKksCMainsTransformer(String zbyqNumber, String zbyqSys,
			String zbyqPattern, String zbyqSpecifiedCapycity,
			String zbyqSpecifiedTension, String zbyqConnectionPattern,
			Long zbyqCoilCount, String zbyqCoolingPattern,
			String zbyqMakerNumber, String zbyqMakerName, Date zbyqMakerData) {
		this.zbyqNumber = zbyqNumber;
		this.zbyqSys = zbyqSys;
		this.zbyqPattern = zbyqPattern;
		this.zbyqSpecifiedCapycity = zbyqSpecifiedCapycity;
		this.zbyqSpecifiedTension = zbyqSpecifiedTension;
		this.zbyqConnectionPattern = zbyqConnectionPattern;
		this.zbyqCoilCount = zbyqCoilCount;
		this.zbyqCoolingPattern = zbyqCoolingPattern;
		this.zbyqMakerNumber = zbyqMakerNumber;
		this.zbyqMakerName = zbyqMakerName;
		this.zbyqMakerData = zbyqMakerData;
	}

	// Property accessors
	@Id
	@Column(name = "ZBYQ_NUMBER", unique = true, nullable = false, length = 10)
	public String getZbyqNumber() {
		return this.zbyqNumber;
	}

	public void setZbyqNumber(String zbyqNumber) {
		this.zbyqNumber = zbyqNumber;
	}

	@Column(name = "ZBYQ_SYS", length = 10)
	public String getZbyqSys() {
		return this.zbyqSys;
	}

	public void setZbyqSys(String zbyqSys) {
		this.zbyqSys = zbyqSys;
	}

	@Column(name = "ZBYQ_PATTERN", length = 30)
	public String getZbyqPattern() {
		return this.zbyqPattern;
	}

	public void setZbyqPattern(String zbyqPattern) {
		this.zbyqPattern = zbyqPattern;
	}

	@Column(name = "ZBYQ_SPECIFIED_CAPYCITY", length = 10)
	public String getZbyqSpecifiedCapycity() {
		return this.zbyqSpecifiedCapycity;
	}

	public void setZbyqSpecifiedCapycity(String zbyqSpecifiedCapycity) {
		this.zbyqSpecifiedCapycity = zbyqSpecifiedCapycity;
	}

	@Column(name = "ZBYQ_SPECIFIED_TENSION", length = 10)
	public String getZbyqSpecifiedTension() {
		return this.zbyqSpecifiedTension;
	}

	public void setZbyqSpecifiedTension(String zbyqSpecifiedTension) {
		this.zbyqSpecifiedTension = zbyqSpecifiedTension;
	}

	@Column(name = "ZBYQ_CONNECTION_PATTERN", length = 30)
	public String getZbyqConnectionPattern() {
		return this.zbyqConnectionPattern;
	}

	public void setZbyqConnectionPattern(String zbyqConnectionPattern) {
		this.zbyqConnectionPattern = zbyqConnectionPattern;
	}

	@Column(name = "ZBYQ_COIL_COUNT", precision = 22, scale = 0)
	public Long getZbyqCoilCount() {
		return this.zbyqCoilCount;
	}

	public void setZbyqCoilCount(Long zbyqCoilCount) {
		this.zbyqCoilCount = zbyqCoilCount;
	}

	@Column(name = "ZBYQ_COOLING_PATTERN", length = 30)
	public String getZbyqCoolingPattern() {
		return this.zbyqCoolingPattern;
	}

	public void setZbyqCoolingPattern(String zbyqCoolingPattern) {
		this.zbyqCoolingPattern = zbyqCoolingPattern;
	}

	@Column(name = "ZBYQ_MAKER_NUMBER", length = 20)
	public String getZbyqMakerNumber() {
		return this.zbyqMakerNumber;
	}

	public void setZbyqMakerNumber(String zbyqMakerNumber) {
		this.zbyqMakerNumber = zbyqMakerNumber;
	}

	@Column(name = "ZBYQ_MAKER_NAME", length = 100)
	public String getZbyqMakerName() {
		return this.zbyqMakerName;
	}

	public void setZbyqMakerName(String zbyqMakerName) {
		this.zbyqMakerName = zbyqMakerName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ZBYQ_MAKER_DATA", length = 7)
	public Date getZbyqMakerData() {
		return this.zbyqMakerData;
	}

	public void setZbyqMakerData(Date zbyqMakerData) {
		this.zbyqMakerData = zbyqMakerData;
	}

}