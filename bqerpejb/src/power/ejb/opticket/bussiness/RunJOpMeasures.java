package power.ejb.opticket.bussiness;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunJOpMeasures entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_OP_MEASURES")
public class RunJOpMeasures implements java.io.Serializable {

	// Fields

	private Long dangerId;
	private String opticketCode;
	private String dangerName;
	private String measureContent;
	private String runAddFlag;
	private Long displayNo;
	private String memo;

	// Constructors

	/** default constructor */
	public RunJOpMeasures() {
	}

	/** minimal constructor */
	public RunJOpMeasures(Long dangerId) {
		this.dangerId = dangerId;
	}

	/** full constructor */
	public RunJOpMeasures(Long dangerId, String opticketCode,
			String dangerName, String measureContent, String runAddFlag,
			Long displayNo, String memo) {
		this.dangerId = dangerId;
		this.opticketCode = opticketCode;
		this.dangerName = dangerName;
		this.measureContent = measureContent;
		this.runAddFlag = runAddFlag;
		this.displayNo = displayNo;
		this.memo = memo;
	}

	// Property accessors
	@Id
	@Column(name = "DANGER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDangerId() {
		return this.dangerId;
	}

	public void setDangerId(Long dangerId) {
		this.dangerId = dangerId;
	}

	@Column(name = "OPTICKET_CODE", length = 19)
	public String getOpticketCode() {
		return this.opticketCode;
	}

	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}

	@Column(name = "DANGER_NAME", length = 500)
	public String getDangerName() {
		return this.dangerName;
	}

	public void setDangerName(String dangerName) {
		this.dangerName = dangerName;
	}

	@Column(name = "MEASURE_CONTENT", length = 500)
	public String getMeasureContent() {
		return this.measureContent;
	}

	public void setMeasureContent(String measureContent) {
		this.measureContent = measureContent;
	}

	@Column(name = "RUN_ADD_FLAG", length = 1)
	public String getRunAddFlag() {
		return this.runAddFlag;
	}

	public void setRunAddFlag(String runAddFlag) {
		this.runAddFlag = runAddFlag;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}