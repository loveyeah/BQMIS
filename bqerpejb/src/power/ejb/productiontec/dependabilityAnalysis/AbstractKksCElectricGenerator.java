package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractKksCElectricGenerator entity provides the base persistence definition
 * of the KksCElectricGenerator entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractKksCElectricGenerator implements
		java.io.Serializable {

	// Fields

	private String fdjNumber;
	private String fdjSys;
	private String fdjPattern;
	private String fdjSpecilfiedCapatity;
	private String fdjMaxOutpower;
	private String fdjSpecilfiedSpeed;
	private String fdjDeterminerElectricity;
	private String fdjOutElectricTension;
	private String fdjBowlElectricity;
	private String fdjPowerFactors;
	private String fdjCoolingPattern;
	private String fdjHydrogenPression;
	private String fdjRate;
	private String fdjMakerNumber;
	private String fdjMakerName;
	private Date fdjMakerData;

	// Constructors

	/** default constructor */
	public AbstractKksCElectricGenerator() {
	}

	/** minimal constructor */
	public AbstractKksCElectricGenerator(String fdjNumber) {
		this.fdjNumber = fdjNumber;
	}

	/** full constructor */
	public AbstractKksCElectricGenerator(String fdjNumber, String fdjSys,
			String fdjPattern, String fdjSpecilfiedCapatity,
			String fdjMaxOutpower, String fdjSpecilfiedSpeed,
			String fdjDeterminerElectricity, String fdjOutElectricTension,
			String fdjBowlElectricity, String fdjPowerFactors,
			String fdjCoolingPattern, String fdjHydrogenPression,
			String fdjRate, String fdjMakerNumber, String fdjMakerName,
			Date fdjMakerData) {
		this.fdjNumber = fdjNumber;
		this.fdjSys = fdjSys;
		this.fdjPattern = fdjPattern;
		this.fdjSpecilfiedCapatity = fdjSpecilfiedCapatity;
		this.fdjMaxOutpower = fdjMaxOutpower;
		this.fdjSpecilfiedSpeed = fdjSpecilfiedSpeed;
		this.fdjDeterminerElectricity = fdjDeterminerElectricity;
		this.fdjOutElectricTension = fdjOutElectricTension;
		this.fdjBowlElectricity = fdjBowlElectricity;
		this.fdjPowerFactors = fdjPowerFactors;
		this.fdjCoolingPattern = fdjCoolingPattern;
		this.fdjHydrogenPression = fdjHydrogenPression;
		this.fdjRate = fdjRate;
		this.fdjMakerNumber = fdjMakerNumber;
		this.fdjMakerName = fdjMakerName;
		this.fdjMakerData = fdjMakerData;
	}

	// Property accessors
	@Id
	@Column(name = "FDJ_NUMBER", unique = true, nullable = false, length = 10)
	public String getFdjNumber() {
		return this.fdjNumber;
	}

	public void setFdjNumber(String fdjNumber) {
		this.fdjNumber = fdjNumber;
	}

	@Column(name = "FDJ_SYS", length = 10)
	public String getFdjSys() {
		return this.fdjSys;
	}

	public void setFdjSys(String fdjSys) {
		this.fdjSys = fdjSys;
	}

	@Column(name = "FDJ_PATTERN", length = 30)
	public String getFdjPattern() {
		return this.fdjPattern;
	}

	public void setFdjPattern(String fdjPattern) {
		this.fdjPattern = fdjPattern;
	}

	@Column(name = "FDJ_SPECILFIED_CAPATITY", length = 10)
	public String getFdjSpecilfiedCapatity() {
		return this.fdjSpecilfiedCapatity;
	}

	public void setFdjSpecilfiedCapatity(String fdjSpecilfiedCapatity) {
		this.fdjSpecilfiedCapatity = fdjSpecilfiedCapatity;
	}

	@Column(name = "FDJ_MAX_OUTPOWER", length = 10)
	public String getFdjMaxOutpower() {
		return this.fdjMaxOutpower;
	}

	public void setFdjMaxOutpower(String fdjMaxOutpower) {
		this.fdjMaxOutpower = fdjMaxOutpower;
	}

	@Column(name = "FDJ_SPECILFIED_SPEED", length = 10)
	public String getFdjSpecilfiedSpeed() {
		return this.fdjSpecilfiedSpeed;
	}

	public void setFdjSpecilfiedSpeed(String fdjSpecilfiedSpeed) {
		this.fdjSpecilfiedSpeed = fdjSpecilfiedSpeed;
	}

	@Column(name = "FDJ_DETERMINER_ELECTRICITY", length = 10)
	public String getFdjDeterminerElectricity() {
		return this.fdjDeterminerElectricity;
	}

	public void setFdjDeterminerElectricity(String fdjDeterminerElectricity) {
		this.fdjDeterminerElectricity = fdjDeterminerElectricity;
	}

	@Column(name = "FDJ_OUT_ELECTRIC_TENSION", length = 10)
	public String getFdjOutElectricTension() {
		return this.fdjOutElectricTension;
	}

	public void setFdjOutElectricTension(String fdjOutElectricTension) {
		this.fdjOutElectricTension = fdjOutElectricTension;
	}

	@Column(name = "FDJ_BOWL_ELECTRICITY", length = 10)
	public String getFdjBowlElectricity() {
		return this.fdjBowlElectricity;
	}

	public void setFdjBowlElectricity(String fdjBowlElectricity) {
		this.fdjBowlElectricity = fdjBowlElectricity;
	}

	@Column(name = "FDJ_POWER_FACTORS", length = 10)
	public String getFdjPowerFactors() {
		return this.fdjPowerFactors;
	}

	public void setFdjPowerFactors(String fdjPowerFactors) {
		this.fdjPowerFactors = fdjPowerFactors;
	}

	@Column(name = "FDJ_COOLING_PATTERN", length = 10)
	public String getFdjCoolingPattern() {
		return this.fdjCoolingPattern;
	}

	public void setFdjCoolingPattern(String fdjCoolingPattern) {
		this.fdjCoolingPattern = fdjCoolingPattern;
	}

	@Column(name = "FDJ_HYDROGEN_PRESSION", length = 10)
	public String getFdjHydrogenPression() {
		return this.fdjHydrogenPression;
	}

	public void setFdjHydrogenPression(String fdjHydrogenPression) {
		this.fdjHydrogenPression = fdjHydrogenPression;
	}

	@Column(name = "FDJ_RATE", length = 4)
	public String getFdjRate() {
		return this.fdjRate;
	}

	public void setFdjRate(String fdjRate) {
		this.fdjRate = fdjRate;
	}

	@Column(name = "FDJ_MAKER_NUMBER", length = 20)
	public String getFdjMakerNumber() {
		return this.fdjMakerNumber;
	}

	public void setFdjMakerNumber(String fdjMakerNumber) {
		this.fdjMakerNumber = fdjMakerNumber;
	}

	@Column(name = "FDJ_MAKER_NAME", length = 100)
	public String getFdjMakerName() {
		return this.fdjMakerName;
	}

	public void setFdjMakerName(String fdjMakerName) {
		this.fdjMakerName = fdjMakerName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FDJ_MAKER_DATA", length = 7)
	public Date getFdjMakerData() {
		return this.fdjMakerData;
	}

	public void setFdjMakerData(Date fdjMakerData) {
		this.fdjMakerData = fdjMakerData;
	}

}