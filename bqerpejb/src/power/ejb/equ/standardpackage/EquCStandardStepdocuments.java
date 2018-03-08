package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardStepdocuments entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_STANDARD_STEPDOCUMENTS")
public class EquCStandardStepdocuments implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String operationStep;
	private String fileCode;
	private String fileName;
	private String fileType;
	private Double fileSize;
	private String relateFile;
	private String filePath;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquCStandardStepdocuments() {
	}

	/** minimal constructor */
	public EquCStandardStepdocuments(Long id, String woCode,
			String operationStep, String enterprisecode) {
		this.id = id;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquCStandardStepdocuments(Long id, String woCode,
			String operationStep, String fileCode, String fileName,
			String fileType, Double fileSize, String relateFile,
			String filePath, String enterprisecode, String ifUse) {
		this.id = id;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.fileCode = fileCode;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.relateFile = relateFile;
		this.filePath = filePath;
		this.enterprisecode = enterprisecode;
		this.ifUse = ifUse;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "WO_CODE", nullable = false, length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "OPERATION_STEP", nullable = false, length = 20)
	public String getOperationStep() {
		return this.operationStep;
	}

	public void setOperationStep(String operationStep) {
		this.operationStep = operationStep;
	}

	@Column(name = "FILE_CODE", length = 20)
	public String getFileCode() {
		return this.fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	@Column(name = "FILE_NAME", length = 100)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_TYPE", length = 2)
	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Column(name = "FILE_SIZE", precision = 18, scale = 5)
	public Double getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "RELATE_FILE", length = 100)
	public String getRelateFile() {
		return this.relateFile;
	}

	public void setRelateFile(String relateFile) {
		this.relateFile = relateFile;
	}

	@Column(name = "FILE_PATH", length = 200)
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "ENTERPRISECODE", nullable = false, length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "IF_USE", length = 1)
	public String getIfUse() {
		return this.ifUse;
	}

	public void setIfUse(String ifUse) {
		this.ifUse = ifUse;
	}

}