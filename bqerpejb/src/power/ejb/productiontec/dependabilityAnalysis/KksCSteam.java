package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * KksCSteam entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "KKS_C_STEAM")
public class KksCSteam implements java.io.Serializable {

	// Fields

	private String qljNumber;
	private String qljSys;
	private String qljEntiretyType;
	private String qljModel;
	private String qljSpecifiedPower;
	private String qljMaxPower;
	private String qljSpecifiedSpeed;
	private String qljMainSteamTemper;
	private String qljMainSteamPressure;
	private String qljPushair;
	private String qljHeatRat;
	private String qljEnterTemper;
	private String qljReheatSteamTemper;
	private String qljReheatSteamPressure;
	private Date qljMakerData;
	private String qljMakerName;
	private String qljMakerNumber;

	// Constructors

	/** default constructor */
	public KksCSteam() {
	}

	/** minimal constructor */
	public KksCSteam(String qljNumber) {
		this.qljNumber = qljNumber;
	}

	/** full constructor */
	public KksCSteam(String qljNumber, String qljSys, String qljEntiretyType,
			String qljModel, String qljSpecifiedPower, String qljMaxPower,
			String qljSpecifiedSpeed, String qljMainSteamTemper,
			String qljMainSteamPressure, String qljPushair, String qljHeatRat,
			String qljEnterTemper, String qljReheatSteamTemper,
			String qljReheatSteamPressure, Date qljMakerData,
			String qljMakerName, String qljMakerNumber) {
		this.qljNumber = qljNumber;
		this.qljSys = qljSys;
		this.qljEntiretyType = qljEntiretyType;
		this.qljModel = qljModel;
		this.qljSpecifiedPower = qljSpecifiedPower;
		this.qljMaxPower = qljMaxPower;
		this.qljSpecifiedSpeed = qljSpecifiedSpeed;
		this.qljMainSteamTemper = qljMainSteamTemper;
		this.qljMainSteamPressure = qljMainSteamPressure;
		this.qljPushair = qljPushair;
		this.qljHeatRat = qljHeatRat;
		this.qljEnterTemper = qljEnterTemper;
		this.qljReheatSteamTemper = qljReheatSteamTemper;
		this.qljReheatSteamPressure = qljReheatSteamPressure;
		this.qljMakerData = qljMakerData;
		this.qljMakerName = qljMakerName;
		this.qljMakerNumber = qljMakerNumber;
	}

	// Property accessors
	@Id
	@Column(name = "QLJ_NUMBER", unique = true, nullable = false, length = 10)
	public String getQljNumber() {
		return this.qljNumber;
	}

	public void setQljNumber(String qljNumber) {
		this.qljNumber = qljNumber;
	}

	@Column(name = "QLJ_SYS", length = 10)
	public String getQljSys() {
		return this.qljSys;
	}

	public void setQljSys(String qljSys) {
		this.qljSys = qljSys;
	}

	@Column(name = "QLJ_ENTIRETY_TYPE", length = 10)
	public String getQljEntiretyType() {
		return this.qljEntiretyType;
	}

	public void setQljEntiretyType(String qljEntiretyType) {
		this.qljEntiretyType = qljEntiretyType;
	}

	@Column(name = "QLJ_MODEL", length = 30)
	public String getQljModel() {
		return this.qljModel;
	}

	public void setQljModel(String qljModel) {
		this.qljModel = qljModel;
	}

	@Column(name = "QLJ_SPECIFIED_POWER", length = 10)
	public String getQljSpecifiedPower() {
		return this.qljSpecifiedPower;
	}

	public void setQljSpecifiedPower(String qljSpecifiedPower) {
		this.qljSpecifiedPower = qljSpecifiedPower;
	}

	@Column(name = "QLJ_MAX_POWER", length = 10)
	public String getQljMaxPower() {
		return this.qljMaxPower;
	}

	public void setQljMaxPower(String qljMaxPower) {
		this.qljMaxPower = qljMaxPower;
	}

	@Column(name = "QLJ_SPECIFIED_SPEED", length = 10)
	public String getQljSpecifiedSpeed() {
		return this.qljSpecifiedSpeed;
	}

	public void setQljSpecifiedSpeed(String qljSpecifiedSpeed) {
		this.qljSpecifiedSpeed = qljSpecifiedSpeed;
	}

	@Column(name = "QLJ_MAIN_STEAM_TEMPER", length = 10)
	public String getQljMainSteamTemper() {
		return this.qljMainSteamTemper;
	}

	public void setQljMainSteamTemper(String qljMainSteamTemper) {
		this.qljMainSteamTemper = qljMainSteamTemper;
	}

	@Column(name = "QLJ_MAIN_STEAM_PRESSURE", length = 10)
	public String getQljMainSteamPressure() {
		return this.qljMainSteamPressure;
	}

	public void setQljMainSteamPressure(String qljMainSteamPressure) {
		this.qljMainSteamPressure = qljMainSteamPressure;
	}

	@Column(name = "QLJ_PUSHAIR", length = 10)
	public String getQljPushair() {
		return this.qljPushair;
	}

	public void setQljPushair(String qljPushair) {
		this.qljPushair = qljPushair;
	}

	@Column(name = "QLJ_HEAT_RAT", length = 10)
	public String getQljHeatRat() {
		return this.qljHeatRat;
	}

	public void setQljHeatRat(String qljHeatRat) {
		this.qljHeatRat = qljHeatRat;
	}

	@Column(name = "QLJ_ENTER_TEMPER", length = 10)
	public String getQljEnterTemper() {
		return this.qljEnterTemper;
	}

	public void setQljEnterTemper(String qljEnterTemper) {
		this.qljEnterTemper = qljEnterTemper;
	}

	@Column(name = "QLJ_REHEAT_STEAM_TEMPER", length = 10)
	public String getQljReheatSteamTemper() {
		return this.qljReheatSteamTemper;
	}

	public void setQljReheatSteamTemper(String qljReheatSteamTemper) {
		this.qljReheatSteamTemper = qljReheatSteamTemper;
	}

	@Column(name = "QLJ_REHEAT_STEAM_PRESSURE", length = 10)
	public String getQljReheatSteamPressure() {
		return this.qljReheatSteamPressure;
	}

	public void setQljReheatSteamPressure(String qljReheatSteamPressure) {
		this.qljReheatSteamPressure = qljReheatSteamPressure;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "QLJ_MAKER_DATA", length = 7)
	public Date getQljMakerData() {
		return this.qljMakerData;
	}

	public void setQljMakerData(Date qljMakerData) {
		this.qljMakerData = qljMakerData;
	}

	@Column(name = "QLJ_MAKER_NAME", length = 100)
	public String getQljMakerName() {
		return this.qljMakerName;
	}

	public void setQljMakerName(String qljMakerName) {
		this.qljMakerName = qljMakerName;
	}

	@Column(name = "QLJ_MAKER_NUMBER", length = 20)
	public String getQljMakerNumber() {
		return this.qljMakerNumber;
	}

	public void setQljMakerNumber(String qljMakerNumber) {
		this.qljMakerNumber = qljMakerNumber;
	}

}