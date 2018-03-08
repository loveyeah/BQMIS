package power.ejb.run.runlog;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJRunlogMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_RUNLOG_MAIN", uniqueConstraints = {})
public class RunJRunlogMain implements java.io.Serializable {

	// Fields

	private Long runLogid;
	private String runLogno;
	private Long shiftId;
	private Long shiftTimeId;
	private String specialityCode;
	private Long weatherKeyId;
	private String shiftCharge;
	private String awayClassLeader;
	private String takeClassLeader;
	private Date awayClassTime;
	private Date takeClassTime;
	private String approveMan;
	private String approveContent;
	private String isDelay;
	private String delayMan;
	private String delayContent;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunJRunlogMain() {
	}

	/** minimal constructor */
	public RunJRunlogMain(Long runLogid, String runLogno) {
		this.runLogid = runLogid;
		this.runLogno = runLogno;
	}

	/** full constructor */
	public RunJRunlogMain(Long runLogid, String runLogno, Long shiftId,
			Long shiftTimeId, String specialityCode, Long weatherKeyId,
			String shiftCharge, String awayClassLeader, String takeClassLeader,
			Date awayClassTime, Date takeClassTime, String approveMan,
			String approveContent, String isDelay, String delayMan,
			String delayContent, String isUse, String enterpriseCode) {
		this.runLogid = runLogid;
		this.runLogno = runLogno;
		this.shiftId = shiftId;
		this.shiftTimeId = shiftTimeId;
		this.specialityCode = specialityCode;
		this.weatherKeyId = weatherKeyId;
		this.shiftCharge = shiftCharge;
		this.awayClassLeader = awayClassLeader;
		this.takeClassLeader = takeClassLeader;
		this.awayClassTime = awayClassTime;
		this.takeClassTime = takeClassTime;
		this.approveMan = approveMan;
		this.approveContent = approveContent;
		this.isDelay = isDelay;
		this.delayMan = delayMan;
		this.delayContent = delayContent;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RUN_LOGID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getRunLogid() {
		return this.runLogid;
	}

	public void setRunLogid(Long runLogid) {
		this.runLogid = runLogid;
	}

	@Column(name = "RUN_LOGNO", unique = false, nullable = false, insertable = true, updatable = true, length = 15)
	public String getRunLogno() {
		return this.runLogno;
	}

	public void setRunLogno(String runLogno) {
		this.runLogno = runLogno;
	}

	@Column(name = "SHIFT_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getShiftId() {
		return this.shiftId;
	}

	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}

	@Column(name = "SHIFT_TIME_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getShiftTimeId() {
		return this.shiftTimeId;
	}

	public void setShiftTimeId(Long shiftTimeId) {
		this.shiftTimeId = shiftTimeId;
	}

	@Column(name = "SPECIALITY_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	@Column(name = "WEATHER_KEY_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getWeatherKeyId() {
		return this.weatherKeyId;
	}

	public void setWeatherKeyId(Long weatherKeyId) {
		this.weatherKeyId = weatherKeyId;
	}

	@Column(name = "SHIFT_CHARGE", unique = false, nullable = true, insertable = true, updatable = true, length = 6)
	public String getShiftCharge() {
		return this.shiftCharge;
	}

	public void setShiftCharge(String shiftCharge) {
		this.shiftCharge = shiftCharge;
	}

	@Column(name = "AWAY_CLASS_LEADER", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getAwayClassLeader() {
		return this.awayClassLeader;
	}

	public void setAwayClassLeader(String awayClassLeader) {
		this.awayClassLeader = awayClassLeader;
	}

	@Column(name = "TAKE_CLASS_LEADER", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getTakeClassLeader() {
		return this.takeClassLeader;
	}

	public void setTakeClassLeader(String takeClassLeader) {
		this.takeClassLeader = takeClassLeader;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AWAY_CLASS_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getAwayClassTime() {
		return this.awayClassTime;
	}

	public void setAwayClassTime(Date awayClassTime) {
		this.awayClassTime = awayClassTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TAKE_CLASS_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getTakeClassTime() {
		return this.takeClassTime;
	}

	public void setTakeClassTime(Date takeClassTime) {
		this.takeClassTime = takeClassTime;
	}

	@Column(name = "APPROVE_MAN", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getApproveMan() {
		return this.approveMan;
	}

	public void setApproveMan(String approveMan) {
		this.approveMan = approveMan;
	}

	@Column(name = "APPROVE_CONTENT", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getApproveContent() {
		return this.approveContent;
	}

	public void setApproveContent(String approveContent) {
		this.approveContent = approveContent;
	}

	@Column(name = "IS_DELAY", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsDelay() {
		return this.isDelay;
	}

	public void setIsDelay(String isDelay) {
		this.isDelay = isDelay;
	}

	@Column(name = "DELAY_MAN", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getDelayMan() {
		return this.delayMan;
	}

	public void setDelayMan(String delayMan) {
		this.delayMan = delayMan;
	}

	@Column(name = "DELAY_CONTENT", unique = false, nullable = true, insertable = true, updatable = true, length = 400)
	public String getDelayContent() {
		return this.delayContent;
	}

	public void setDelayContent(String delayContent) {
		this.delayContent = delayContent;
	}

	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}