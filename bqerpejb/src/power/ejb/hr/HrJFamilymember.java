package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJFamilymember entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_FAMILYMEMBER")
public class HrJFamilymember implements java.io.Serializable {

	// Fields

	private Long familymemberid;
	private Long callsCodeId;
	private Long nationCodeId;
	private String name;
	private String sex;
	private Date birthday;
	private String ifMarried;
	private Long educationId;
	private String unit;
	private String headshipName;
	private String zxqsMark;
	private String ifLineally;
	private String insertby;
	private Date insertdate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long empId;
	private Long nativePlaceId;
	private Long politicsId;

	private String memo;
	// Constructors

	

	/** default constructor */
	public HrJFamilymember() {
	}

	/** minimal constructor */
	public HrJFamilymember(Long familymemberid, Long nativePlaceId,
			Long politicsId) {
		this.familymemberid = familymemberid;
		this.nativePlaceId = nativePlaceId;
		this.politicsId = politicsId;
	}

	/** full constructor */
	public HrJFamilymember(Long familymemberid, Long callsCodeId,
			Long nationCodeId, String name, String sex, Date birthday,
			String ifMarried, Long educationId, String unit,
			String headshipName, String zxqsMark, String ifLineally,
			String insertby, Date insertdate, String enterpriseCode,
			String isUse, String lastModifiedBy, Date lastModifiedDate,
			Long empId, Long nativePlaceId, Long politicsId) {
		this.familymemberid = familymemberid;
		this.callsCodeId = callsCodeId;
		this.nationCodeId = nationCodeId;
		this.name = name;
		this.sex = sex;
		this.birthday = birthday;
		this.ifMarried = ifMarried;
		this.educationId = educationId;
		this.unit = unit;
		this.headshipName = headshipName;
		this.zxqsMark = zxqsMark;
		this.ifLineally = ifLineally;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.empId = empId;
		this.nativePlaceId = nativePlaceId;
		this.politicsId = politicsId;
	}

	// Property accessors
	@Id
	@Column(name = "FAMILYMEMBERID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFamilymemberid() {
		return this.familymemberid;
	}

	public void setFamilymemberid(Long familymemberid) {
		this.familymemberid = familymemberid;
	}

	@Column(name = "CALLS_CODE_ID", precision = 10, scale = 0)
	public Long getCallsCodeId() {
		return this.callsCodeId;
	}

	public void setCallsCodeId(Long callsCodeId) {
		this.callsCodeId = callsCodeId;
	}

	@Column(name = "NATION_CODE_ID", precision = 10, scale = 0)
	public Long getNationCodeId() {
		return this.nationCodeId;
	}

	public void setNationCodeId(Long nationCodeId) {
		this.nationCodeId = nationCodeId;
	}

	@Column(name = "NAME", length = 8)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SEX", length = 1)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTHDAY", length = 7)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "IF_MARRIED", length = 1)
	public String getIfMarried() {
		return this.ifMarried;
	}

	public void setIfMarried(String ifMarried) {
		this.ifMarried = ifMarried;
	}

	@Column(name = "EDUCATION_ID", precision = 10, scale = 0)
	public Long getEducationId() {
		return this.educationId;
	}

	public void setEducationId(Long educationId) {
		this.educationId = educationId;
	}

	@Column(name = "UNIT", length = 30)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "HEADSHIP_NAME", length = 30)
	public String getHeadshipName() {
		return this.headshipName;
	}

	public void setHeadshipName(String headshipName) {
		this.headshipName = headshipName;
	}

	@Column(name = "ZXQS_MARK", length = 1)
	public String getZxqsMark() {
		return this.zxqsMark;
	}

	public void setZxqsMark(String zxqsMark) {
		this.zxqsMark = zxqsMark;
	}

	@Column(name = "IF_LINEALLY", length = 1)
	public String getIfLineally() {
		return this.ifLineally;
	}

	public void setIfLineally(String ifLineally) {
		this.ifLineally = ifLineally;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
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

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "NATIVE_PLACE_ID", precision = 10, scale = 0)
	public Long getNativePlaceId() {
		return this.nativePlaceId;
	}

	public void setNativePlaceId(Long nativePlaceId) {
		this.nativePlaceId = nativePlaceId;
	}

	@Column(name = "POLITICS_ID", precision = 10, scale = 0)
	public Long getPoliticsId() {
		return this.politicsId;
	}

	public void setPoliticsId(Long politicsId) {
		this.politicsId = politicsId;
	}
	
	@Column(name = "MEMO", length = 300)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}