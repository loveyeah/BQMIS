package power.ejb.equ.standardpackage;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * WoJWorkorder entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "WO_J_WORKORDER")
public class WoJWorkorder implements java.io.Serializable {

	// Fields

	private Long id;
	private String failureCode;
	private String attributeCode;
	private String equName;
	private String checkAttr;
	private String preContent;
	private String description;
	private String parameters;
	private String problem;
	private String spareParts;
	private String annex;
	private Date startDate;
	private Date endDate;
	private String supervisor;
	private String participants;
	private String enterprisecode;
	private String isUse;

	// Constructors

	/** default constructor */
	public WoJWorkorder() {
	}

	/** minimal constructor */
	public WoJWorkorder(Long id) {
		this.id = id;
	}

	/** full constructor */
	public WoJWorkorder(Long id, String failureCode, String attributeCode,
			String equName, String checkAttr, String preContent,
			String description, String parameters, String problem,
			String spareParts, String annex, Date startDate, Date endDate,
			String supervisor, String participants, String enterprisecode,
			String isUse) {
		this.id = id;
		this.failureCode = failureCode;
		this.attributeCode = attributeCode;
		this.equName = equName;
		this.checkAttr = checkAttr;
		this.preContent = preContent;
		this.description = description;
		this.parameters = parameters;
		this.problem = problem;
		this.spareParts = spareParts;
		this.annex = annex;
		this.startDate = startDate;
		this.endDate = endDate;
		this.supervisor = supervisor;
		this.participants = participants;
		this.enterprisecode = enterprisecode;
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

	@Column(name = "FAILURE_CODE", length = 15)
	public String getFailureCode() {
		return this.failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	@Column(name = "ATTRIBUTE_CODE", length = 30)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "EQU_NAME", length = 100)
	public String getEquName() {
		return this.equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	@Column(name = "CHECK_ATTR", length = 1000)
	public String getCheckAttr() {
		return this.checkAttr;
	}

	public void setCheckAttr(String checkAttr) {
		this.checkAttr = checkAttr;
	}

	@Column(name = "PRE_CONTENT", length = 4000)
	public String getPreContent() {
		return this.preContent;
	}

	public void setPreContent(String preContent) {
		this.preContent = preContent;
	}

	@Column(name = "DESCRIPTION", length = 4000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "PARAMETERS", length = 2000)
	public String getParameters() {
		return this.parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	@Column(name = "PROBLEM", length = 2000)
	public String getProblem() {
		return this.problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	@Column(name = "SPARE_PARTS", length = 2000)
	public String getSpareParts() {
		return this.spareParts;
	}

	public void setSpareParts(String spareParts) {
		this.spareParts = spareParts;
	}

	@Column(name = "ANNEX")
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "SUPERVISOR", length = 16)
	public String getSupervisor() {
		return this.supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	@Column(name = "PARTICIPANTS", length = 1000)
	public String getParticipants() {
		return this.participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	@Column(name = "ENTERPRISECODE", length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}