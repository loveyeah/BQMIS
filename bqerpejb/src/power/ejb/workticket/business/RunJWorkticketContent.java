package power.ejb.workticket.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJWorkticketContent entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_WORKTICKET_CONTENT")
public class RunJWorkticketContent implements java.io.Serializable {

	// Fields

	private Long id;
	private String workticketNo;
	private Long worktypeId;
	private String worktypeName;
	private Long flagId;
	private String flagDesc;
	private Long line;
	private Long frontKeyId;
	private String frontKeyDesc;
	private Long locationId;
	private String locationName;
	private String attributeCode;
	private String equName;
	private Long backKeyId;
	private String backKeyDesc;
	private String createBy;
	private Date createDate;
	private String isreturn;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunJWorkticketContent() {
	}

	/** minimal constructor */
	public RunJWorkticketContent(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunJWorkticketContent(Long id, String workticketNo, Long worktypeId,
			String worktypeName, Long flagId, Long line, Long frontKeyId,
			String frontKeyDesc, Long locationId, String locationName,
			String attributeCode, String equName, Long backKeyId,
			String backKeyDesc, String createBy, Date createDate,
			String isreturn, String enterpriseCode, String isUse) {
		this.id = id;
		this.workticketNo = workticketNo;
		this.worktypeId = worktypeId;
		this.worktypeName = worktypeName;
		this.flagId = flagId;
		this.line = line;
		this.frontKeyId = frontKeyId;
		this.frontKeyDesc = frontKeyDesc;
		this.locationId = locationId;
		this.locationName = locationName;
		this.attributeCode = attributeCode;
		this.equName = equName;
		this.backKeyId = backKeyId;
		this.backKeyDesc = backKeyDesc;
		this.createBy = createBy;
		this.createDate = createDate;
		this.isreturn = isreturn;
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

	@Column(name = "WORKTICKET_NO", length = 22)
	public String getWorkticketNo() {
		return this.workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	@Column(name = "WORKTYPE_ID", precision = 10, scale = 0)
	public Long getWorktypeId() {
		return this.worktypeId;
	}

	public void setWorktypeId(Long worktypeId) {
		this.worktypeId = worktypeId;
	}

	@Column(name = "WORKTYPE_NAME", length = 100)
	public String getWorktypeName() {
		return this.worktypeName;
	}

	public void setWorktypeName(String worktypeName) {
		this.worktypeName = worktypeName;
	}

	@Column(name = "FLAG_ID", precision = 10, scale = 0)
	public Long getFlagId() {
		return this.flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}

	@Column(name = "LINE", precision = 10, scale = 0)
	public Long getLine() {
		return this.line;
	}

	public void setLine(Long line) {
		this.line = line;
	}

	@Column(name = "FRONT_KEY_ID", precision = 10, scale = 0)
	public Long getFrontKeyId() {
		return this.frontKeyId;
	}

	public void setFrontKeyId(Long frontKeyId) {
		this.frontKeyId = frontKeyId;
	}

	@Column(name = "FRONT_KEY_DESC", length = 100)
	public String getFrontKeyDesc() {
		return this.frontKeyDesc;
	}

	public void setFrontKeyDesc(String frontKeyDesc) {
		this.frontKeyDesc = frontKeyDesc;
	}

	@Column(name = "LOCATION_ID", precision = 10, scale = 0)
	public Long getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	@Column(name = "LOCATION_NAME", length = 100)
	public String getLocationName() {
		return this.locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
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

	@Column(name = "BACK_KEY_ID", precision = 10, scale = 0)
	public Long getBackKeyId() {
		return this.backKeyId;
	}

	public void setBackKeyId(Long backKeyId) {
		this.backKeyId = backKeyId;
	}
	@Column(name = "FLAG_DESC", length = 2)
	public String getFlagDesc() {
		return flagDesc;
	}

	public void setFlagDesc(String flagDesc) {
		this.flagDesc = flagDesc;
	}
	@Column(name = "BACK_KEY_DESC", length = 100)
	public String getBackKeyDesc() {
		return this.backKeyDesc;
	}

	public void setBackKeyDesc(String backKeyDesc) {
		this.backKeyDesc = backKeyDesc;
	}

	@Column(name = "CREATE_BY", length = 30)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "ISRETURN", length = 1)
	public String getIsreturn() {
		return this.isreturn;
	}

	public void setIsreturn(String isreturn) {
		this.isreturn = isreturn;
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