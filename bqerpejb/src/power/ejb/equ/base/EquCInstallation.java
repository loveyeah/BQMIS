package power.ejb.equ.base;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquCInstallation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_INSTALLATION")
public class EquCInstallation implements java.io.Serializable {

	// Fields

	private Long id;
	private String installationCode;
	private String installationDesc;
	private String fatherCode;
	private Long classStructureId;
	private String changeBy;
	private Date changeDate;
	private String memo;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCInstallation() {
	}

	/** minimal constructor */
	public EquCInstallation(Long id) {
		this.id = id;
	}

	/** full constructor */
	public EquCInstallation(Long id, String installationCode,
			String installationDesc, String fatherCode, Long classStructureId,
			String changeBy, Date changeDate, String memo,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.installationCode = installationCode;
		this.installationDesc = installationDesc;
		this.fatherCode = fatherCode;
		this.classStructureId = classStructureId;
		this.changeBy = changeBy;
		this.changeDate = changeDate;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
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

	@Column(name = "INSTALLATION_CODE", length = 30)
	public String getInstallationCode() {
		return this.installationCode;
	}

	public void setInstallationCode(String installationCode) {
		this.installationCode = installationCode;
	}

	@Column(name = "INSTALLATION_DESC", length = 100)
	public String getInstallationDesc() {
		return this.installationDesc;
	}

	public void setInstallationDesc(String installationDesc) {
		this.installationDesc = installationDesc;
	}

	@Column(name = "FATHER_CODE", length = 30)
	public String getFatherCode() {
		return this.fatherCode;
	}

	public void setFatherCode(String fatherCode) {
		this.fatherCode = fatherCode;
	}

	@Column(name = "CLASS_STRUCTURE_ID", precision = 10, scale = 0)
	public Long getClassStructureId() {
		return this.classStructureId;
	}

	public void setClassStructureId(Long classStructureId) {
		this.classStructureId = classStructureId;
	}

	@Column(name = "CHANGE_BY", length = 30)
	public String getChangeBy() {
		return this.changeBy;
	}

	public void setChangeBy(String changeBy) {
		this.changeBy = changeBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHANGE_DATE", length = 7)
	public Date getChangeDate() {
		return this.changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	@Column(name = "MEMO", length = 200)
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