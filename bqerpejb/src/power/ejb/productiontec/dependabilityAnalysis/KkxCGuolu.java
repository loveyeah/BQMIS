package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * KkxCGuolu entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "KKX_C_GUOLU")
public class KkxCGuolu implements java.io.Serializable {

	// Fields

	private String glNumber;
	private String glSys;
	private String glEntiretyType;
	private String glType;
	private String glEdzfl;
	private String glZzqgdwd;
	private String glZzqhdyl;
	private String glZrzqll;
	private String glRkzrqedwd;
	private String glCkzrqedwd;
	private String glRkzrqedyl;
	private String glCkzrqedyl;
	private String glWater;
	private String glRunPattern;
	private String glFuel;
	private String glCoal;
	private String glPush;
	private String glBurn;
	private String glStove;
	private String glAir;
	private String glHotAir;
	private String glSmoke;
	private String glEfficiency;
	private String glMakerName;
	private String glMakerNumber;
	private Date glMakerData;

	// Constructors

	/** default constructor */
	public KkxCGuolu() {
	}

	/** minimal constructor */
	public KkxCGuolu(String glNumber) {
		this.glNumber = glNumber;
	}

	/** full constructor */
	public KkxCGuolu(String glNumber, String glSys, String glEntiretyType,
			String glType, String glEdzfl, String glZzqgdwd, String glZzqhdyl,
			String glZrzqll, String glRkzrqedwd, String glCkzrqedwd,
			String glRkzrqedyl, String glCkzrqedyl, String glWater,
			String glRunPattern, String glFuel, String glCoal, String glPush,
			String glBurn, String glStove, String glAir, String glHotAir,
			String glSmoke, String glEfficiency, String glMakerName,
			String glMakerNumber, Date glMakerData) {
		this.glNumber = glNumber;
		this.glSys = glSys;
		this.glEntiretyType = glEntiretyType;
		this.glType = glType;
		this.glEdzfl = glEdzfl;
		this.glZzqgdwd = glZzqgdwd;
		this.glZzqhdyl = glZzqhdyl;
		this.glZrzqll = glZrzqll;
		this.glRkzrqedwd = glRkzrqedwd;
		this.glCkzrqedwd = glCkzrqedwd;
		this.glRkzrqedyl = glRkzrqedyl;
		this.glCkzrqedyl = glCkzrqedyl;
		this.glWater = glWater;
		this.glRunPattern = glRunPattern;
		this.glFuel = glFuel;
		this.glCoal = glCoal;
		this.glPush = glPush;
		this.glBurn = glBurn;
		this.glStove = glStove;
		this.glAir = glAir;
		this.glHotAir = glHotAir;
		this.glSmoke = glSmoke;
		this.glEfficiency = glEfficiency;
		this.glMakerName = glMakerName;
		this.glMakerNumber = glMakerNumber;
		this.glMakerData = glMakerData;
	}

	// Property accessors
	@Id
	@Column(name = "GL_NUMBER", unique = true, nullable = false, length = 10)
	public String getGlNumber() {
		return this.glNumber;
	}

	public void setGlNumber(String glNumber) {
		this.glNumber = glNumber;
	}

	@Column(name = "GL_SYS", length = 10)
	public String getGlSys() {
		return this.glSys;
	}

	public void setGlSys(String glSys) {
		this.glSys = glSys;
	}

	@Column(name = "GL_ENTIRETY_TYPE", length = 10)
	public String getGlEntiretyType() {
		return this.glEntiretyType;
	}

	public void setGlEntiretyType(String glEntiretyType) {
		this.glEntiretyType = glEntiretyType;
	}

	@Column(name = "GL_TYPE", length = 30)
	public String getGlType() {
		return this.glType;
	}

	public void setGlType(String glType) {
		this.glType = glType;
	}

	@Column(name = "GL_EDZFL", length = 10)
	public String getGlEdzfl() {
		return this.glEdzfl;
	}

	public void setGlEdzfl(String glEdzfl) {
		this.glEdzfl = glEdzfl;
	}

	@Column(name = "GL_ZZQGDWD", length = 10)
	public String getGlZzqgdwd() {
		return this.glZzqgdwd;
	}

	public void setGlZzqgdwd(String glZzqgdwd) {
		this.glZzqgdwd = glZzqgdwd;
	}

	@Column(name = "GL_ZZQHDYL", length = 10)
	public String getGlZzqhdyl() {
		return this.glZzqhdyl;
	}

	public void setGlZzqhdyl(String glZzqhdyl) {
		this.glZzqhdyl = glZzqhdyl;
	}

	@Column(name = "GL_ZRZQLL", length = 10)
	public String getGlZrzqll() {
		return this.glZrzqll;
	}

	public void setGlZrzqll(String glZrzqll) {
		this.glZrzqll = glZrzqll;
	}

	@Column(name = "GL_RKZRQEDWD", length = 10)
	public String getGlRkzrqedwd() {
		return this.glRkzrqedwd;
	}

	public void setGlRkzrqedwd(String glRkzrqedwd) {
		this.glRkzrqedwd = glRkzrqedwd;
	}

	@Column(name = "GL_CKZRQEDWD", length = 10)
	public String getGlCkzrqedwd() {
		return this.glCkzrqedwd;
	}

	public void setGlCkzrqedwd(String glCkzrqedwd) {
		this.glCkzrqedwd = glCkzrqedwd;
	}

	@Column(name = "GL_RKZRQEDYL", length = 10)
	public String getGlRkzrqedyl() {
		return this.glRkzrqedyl;
	}

	public void setGlRkzrqedyl(String glRkzrqedyl) {
		this.glRkzrqedyl = glRkzrqedyl;
	}

	@Column(name = "GL_CKZRQEDYL", length = 10)
	public String getGlCkzrqedyl() {
		return this.glCkzrqedyl;
	}

	public void setGlCkzrqedyl(String glCkzrqedyl) {
		this.glCkzrqedyl = glCkzrqedyl;
	}

	@Column(name = "GL_WATER", length = 10)
	public String getGlWater() {
		return this.glWater;
	}

	public void setGlWater(String glWater) {
		this.glWater = glWater;
	}

	@Column(name = "GL_RUN_PATTERN", length = 8)
	public String getGlRunPattern() {
		return this.glRunPattern;
	}

	public void setGlRunPattern(String glRunPattern) {
		this.glRunPattern = glRunPattern;
	}

	@Column(name = "GL_FUEL", length = 2)
	public String getGlFuel() {
		return this.glFuel;
	}

	public void setGlFuel(String glFuel) {
		this.glFuel = glFuel;
	}

	@Column(name = "GL_COAL", length = 2)
	public String getGlCoal() {
		return this.glCoal;
	}

	public void setGlCoal(String glCoal) {
		this.glCoal = glCoal;
	}

	@Column(name = "GL_PUSH", length = 4)
	public String getGlPush() {
		return this.glPush;
	}

	public void setGlPush(String glPush) {
		this.glPush = glPush;
	}

	@Column(name = "GL_BURN", length = 6)
	public String getGlBurn() {
		return this.glBurn;
	}

	public void setGlBurn(String glBurn) {
		this.glBurn = glBurn;
	}

	@Column(name = "GL_STOVE", length = 2)
	public String getGlStove() {
		return this.glStove;
	}

	public void setGlStove(String glStove) {
		this.glStove = glStove;
	}

	@Column(name = "GL_AIR", length = 6)
	public String getGlAir() {
		return this.glAir;
	}

	public void setGlAir(String glAir) {
		this.glAir = glAir;
	}

	@Column(name = "GL_HOT_AIR", length = 10)
	public String getGlHotAir() {
		return this.glHotAir;
	}

	public void setGlHotAir(String glHotAir) {
		this.glHotAir = glHotAir;
	}

	@Column(name = "GL_SMOKE", length = 10)
	public String getGlSmoke() {
		return this.glSmoke;
	}

	public void setGlSmoke(String glSmoke) {
		this.glSmoke = glSmoke;
	}

	@Column(name = "GL_EFFICIENCY", length = 4)
	public String getGlEfficiency() {
		return this.glEfficiency;
	}

	public void setGlEfficiency(String glEfficiency) {
		this.glEfficiency = glEfficiency;
	}

	@Column(name = "GL_MAKER_NAME", length = 100)
	public String getGlMakerName() {
		return this.glMakerName;
	}

	public void setGlMakerName(String glMakerName) {
		this.glMakerName = glMakerName;
	}

	@Column(name = "GL_MAKER_NUMBER", length = 20)
	public String getGlMakerNumber() {
		return this.glMakerNumber;
	}

	public void setGlMakerNumber(String glMakerNumber) {
		this.glMakerNumber = glMakerNumber;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "GL_MAKER_DATA", length = 7)
	public Date getGlMakerData() {
		return this.glMakerData;
	}

	public void setGlMakerData(Date glMakerData) {
		this.glMakerData = glMakerData;
	}

}