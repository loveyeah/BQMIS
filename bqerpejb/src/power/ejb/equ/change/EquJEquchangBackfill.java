package power.ejb.equ.change;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquJEquchangBackfill entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_EQUCHANG_BACKFILL", schema = "POWER")
public class EquJEquchangBackfill implements java.io.Serializable {

	// Fields

	private Long backfillId;
	private String equChangeNo;
	private String deptCode;
	private String backfillBy;
	private Date exeDate;
	private String exeSituation;
	private String isBackfill;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public EquJEquchangBackfill() {
	}

	/** minimal constructor */
	public EquJEquchangBackfill(Long backfillId) {
		this.backfillId = backfillId;
	}

	/** full constructor */
	public EquJEquchangBackfill(Long backfillId, String equChangeNo,
			String deptCode, String backfillBy, Date exeDate,
			String exeSituation, String isBackfill, String isUse,
			String enterpriseCode) {
		this.backfillId = backfillId;
		this.equChangeNo = equChangeNo;
		this.deptCode = deptCode;
		this.backfillBy = backfillBy;
		this.exeDate = exeDate;
		this.exeSituation = exeSituation;
		this.isBackfill = isBackfill;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BACKFILL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBackfillId() {
		return this.backfillId;
	}

	public void setBackfillId(Long backfillId) {
		this.backfillId = backfillId;
	}

	@Column(name = "EQU_CHANGE_NO", length = 20)
	public String getEquChangeNo() {
		return this.equChangeNo;
	}

	public void setEquChangeNo(String equChangeNo) {
		this.equChangeNo = equChangeNo;
	}

	@Column(name = "DEPT_CODE", length = 20)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "BACKFILL_BY", length = 20)
	public String getBackfillBy() {
		return this.backfillBy;
	}

	public void setBackfillBy(String backfillBy) {
		this.backfillBy = backfillBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EXE_DATE", length = 7)
	public Date getExeDate() {
		return this.exeDate;
	}

	public void setExeDate(Date exeDate) {
		this.exeDate = exeDate;
	}

	@Column(name = "EXE_SITUATION", length = 200)
	public String getExeSituation() {
		return this.exeSituation;
	}

	public void setExeSituation(String exeSituation) {
		this.exeSituation = exeSituation;
	}

	@Column(name = "IS_BACKFILL", length = 1)
	public String getIsBackfill() {
		return this.isBackfill;
	}

	public void setIsBackfill(String isBackfill) {
		this.isBackfill = isBackfill;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}