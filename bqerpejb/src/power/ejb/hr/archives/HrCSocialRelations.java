package power.ejb.hr.archives;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCSocialRelations entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_SOCIAL_RELATIONS")
public class HrCSocialRelations implements java.io.Serializable {

	// Fields

	private Long relationId;
	private Long empId;
	private String relationName;
	private String relationTitle;
	private String isDeath;
	private Date deathDate;
	private String isMajorProblem;
	private String majorProblem;
	private String professional;
	private String face;
	private String isNationals;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCSocialRelations() {
	}

	/** minimal constructor */
	public HrCSocialRelations(Long relationId) {
		this.relationId = relationId;
	}

	/** full constructor */
	public HrCSocialRelations(Long relationId, Long empId, String relationName,
			String relationTitle, String isDeath, Date deathDate,
			String isMajorProblem, String majorProblem, String professional,
			String face, String isNationals, String isUse, String enterpriseCode) {
		this.relationId = relationId;
		this.empId = empId;
		this.relationName = relationName;
		this.relationTitle = relationTitle;
		this.isDeath = isDeath;
		this.deathDate = deathDate;
		this.isMajorProblem = isMajorProblem;
		this.majorProblem = majorProblem;
		this.professional = professional;
		this.face = face;
		this.isNationals = isNationals;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RELATION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRelationId() {
		return this.relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "RELATION_NAME", length = 30)
	public String getRelationName() {
		return this.relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	@Column(name = "RELATION_TITLE", length = 20)
	public String getRelationTitle() {
		return this.relationTitle;
	}

	public void setRelationTitle(String relationTitle) {
		this.relationTitle = relationTitle;
	}

	@Column(name = "IS_DEATH", length = 1)
	public String getIsDeath() {
		return this.isDeath;
	}

	public void setIsDeath(String isDeath) {
		this.isDeath = isDeath;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DEATH_DATE", length = 7)
	public Date getDeathDate() {
		return this.deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	@Column(name = "IS_MAJOR_PROBLEM", length = 1)
	public String getIsMajorProblem() {
		return this.isMajorProblem;
	}

	public void setIsMajorProblem(String isMajorProblem) {
		this.isMajorProblem = isMajorProblem;
	}

	@Column(name = "MAJOR_PROBLEM", length = 300)
	public String getMajorProblem() {
		return this.majorProblem;
	}

	public void setMajorProblem(String majorProblem) {
		this.majorProblem = majorProblem;
	}

	@Column(name = "PROFESSIONAL", length = 50)
	public String getProfessional() {
		return this.professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	@Column(name = "FACE", length = 20)
	public String getFace() {
		return this.face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	@Column(name = "IS_NATIONALS", length = 1)
	public String getIsNationals() {
		return this.isNationals;
	}

	public void setIsNationals(String isNationals) {
		this.isNationals = isNationals;
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