package power.ejb.equ.base;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquCBugsolution entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_BUGSOLUTION")
public class EquCBugsolution implements java.io.Serializable {

	// Fields

	private Long equSolutionId;
	private Long bugReasonId;
	private String bugCode;
	private String equSolutionDesc;
	private String entryBy;
	private Date entryDate;
	private String fileName;
	private String filePath;
	private String solutionFile;
	private String memo;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCBugsolution() {
	}

	/** minimal constructor */
	public EquCBugsolution(Long equSolutionId) {
		this.equSolutionId = equSolutionId;
	}

	/** full constructor */
	public EquCBugsolution(Long equSolutionId, Long bugReasonId,
			String bugCode, String equSolutionDesc, String entryBy,
			Date entryDate, String fileName, String filePath,
			String solutionFile, String memo, String enterpriseCode,
			String isUse) {
		this.equSolutionId = equSolutionId;
		this.bugReasonId = bugReasonId;
		this.bugCode = bugCode;
		this.equSolutionDesc = equSolutionDesc;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.fileName = fileName;
		this.filePath = filePath;
		this.solutionFile = solutionFile;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "EQU_SOLUTION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEquSolutionId() {
		return this.equSolutionId;
	}

	public void setEquSolutionId(Long equSolutionId) {
		this.equSolutionId = equSolutionId;
	}

	@Column(name = "BUG_REASON_ID", precision = 10, scale = 0)
	public Long getBugReasonId() {
		return this.bugReasonId;
	}

	public void setBugReasonId(Long bugReasonId) {
		this.bugReasonId = bugReasonId;
	}

	@Column(name = "BUG_CODE", length = 30)
	public String getBugCode() {
		return this.bugCode;
	}

	public void setBugCode(String bugCode) {
		this.bugCode = bugCode;
	}

	@Column(name = "EQU_SOLUTION_DESC", length = 1000)
	public String getEquSolutionDesc() {
		return this.equSolutionDesc;
	}

	public void setEquSolutionDesc(String equSolutionDesc) {
		this.equSolutionDesc = equSolutionDesc;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "FILE_NAME", length = 500)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_PATH", length = 1000)
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "SOLUTION_FILE")
	public String getSolutionFile() {
		return this.solutionFile;
	}

	public void setSolutionFile(String solutionFile) {
		this.solutionFile = solutionFile;
	}

	@Column(name = "MEMO", length = 1000)
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

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}