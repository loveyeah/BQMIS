package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCDept entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_DEPT")
public class HrCDept implements java.io.Serializable {

	// Fields

	private Long deptId;
	private Long pdeptId;
	private String deptCode;
	private String pdeptCode;
	private String deptName;
	private Long manger;
	private Long deptTypeId;
	private String isUse;
	private String memo;
	private String retrieveCode;
	private Long orderBy;
	private Long createBy;
	private Date createDate;
	private Long modifiyBy;
	private Date modifiyDate;
	private Long logoffBy;
	private Date logoffDate;
	private Long powerPlantId;
	private String deptIdentifier;
    //add by fyyang 090929
	private String enterpriseCode;// ENTERPRISE_CODE
	private String depFeature;//DEP_FEATURE
	private String isBanzu;//IS_BANZU
	private String deptLevel;//DEPT_LEVEL
	private String deptStatus;//DEPT_STATUS
	private Long peopleNumber;

	// Constructors

	/** default constructor */
	public HrCDept() {
	}

	/** minimal constructor */
	public HrCDept(Long deptId) {
		this.deptId = deptId;
	}

	/** full constructor */
	public HrCDept(Long deptId, Long pdeptId, String deptCode,
			String pdeptCode, String deptName, Long manger, Long deptTypeId,
			String isUse, String memo, String retrieveCode, Long orderBy,
			Long createBy, Date createDate, Long modifiyBy, Date modifiyDate,
			Long logoffBy, Date logoffDate, Long powerPlantId,String deptIdentifier,Long peopleNumber) {
		this.deptId = deptId;
		this.pdeptId = pdeptId;
		this.deptCode = deptCode;
		this.pdeptCode = pdeptCode;
		this.deptName = deptName;
		this.manger = manger;
		this.deptTypeId = deptTypeId;
		this.isUse = isUse;
		this.memo = memo;
		this.retrieveCode = retrieveCode;
		this.orderBy = orderBy;
		this.createBy = createBy;
		this.createDate = createDate;
		this.modifiyBy = modifiyBy;
		this.modifiyDate = modifiyDate;
		this.logoffBy = logoffBy;
		this.logoffDate = logoffDate;
		this.powerPlantId = powerPlantId;
		this.deptIdentifier=deptIdentifier;
		this.peopleNumber = peopleNumber;
	}

	// Property accessors
	@Id
	@Column(name = "DEPT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "PDEPT_ID", precision = 10, scale = 0)
	public Long getPdeptId() {
		return this.pdeptId;
	}

	public void setPdeptId(Long pdeptId) {
		this.pdeptId = pdeptId;
	}

	@Column(name = "DEPT_CODE", length = 20)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "PDEPT_CODE", length = 20)
	public String getPdeptCode() {
		return this.pdeptCode;
	}

	public void setPdeptCode(String pdeptCode) {
		this.pdeptCode = pdeptCode;
	}

	@Column(name = "DEPT_NAME", length = 100)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "MANGER", precision = 10, scale = 0)
	public Long getManger() {
		return this.manger;
	}

	public void setManger(Long manger) {
		this.manger = manger;
	}

	@Column(name = "DEPT_TYPE_ID", precision = 10, scale = 0)
	public Long getDeptTypeId() {
		return this.deptTypeId;
	}

	public void setDeptTypeId(Long deptTypeId) {
		this.deptTypeId = deptTypeId;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "RETRIEVE_CODE", length = 20)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "CREATE_BY", precision = 10, scale = 0)
	public Long getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(Long createBy) {
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

	@Column(name = "MODIFIY_BY", precision = 10, scale = 0)
	public Long getModifiyBy() {
		return this.modifiyBy;
	}

	public void setModifiyBy(Long modifiyBy) {
		this.modifiyBy = modifiyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIY_DATE", length = 7)
	public Date getModifiyDate() {
		return this.modifiyDate;
	}

	public void setModifiyDate(Date modifiyDate) {
		this.modifiyDate = modifiyDate;
	}

	@Column(name = "LOGOFF_BY", precision = 10, scale = 0)
	public Long getLogoffBy() {
		return this.logoffBy;
	}

	public void setLogoffBy(Long logoffBy) {
		this.logoffBy = logoffBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LOGOFF_DATE", length = 7)
	public Date getLogoffDate() {
		return this.logoffDate;
	}

	public void setLogoffDate(Date logoffDate) {
		this.logoffDate = logoffDate;
	}

	@Column(name = "POWER_PLANT_ID", precision = 10, scale = 0)
	public Long getPowerPlantId() {
		return this.powerPlantId;
	}

	public void setPowerPlantId(Long powerPlantId) {
		this.powerPlantId = powerPlantId;
	}

	@Column(name = "DEPT_IDENTIFIER", length = 2)
	public String getDeptIdentifier() {
		return deptIdentifier;
	}

	public void setDeptIdentifier(String deptIdentifier) {
		this.deptIdentifier = deptIdentifier;
	}
	
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	
	@Column(name = "DEP_FEATURE", length = 1)
	public String getDepFeature() {
		return depFeature;
	}

	public void setDepFeature(String depFeature) {
		this.depFeature = depFeature;
	}

	@Column(name = "IS_BANZU", length = 1)
	public String getIsBanzu() {
		return isBanzu;
	}

	public void setIsBanzu(String isBanzu) {
		this.isBanzu = isBanzu;
	}

	@Column(name = "DEPT_LEVEL", length = 1)
	public String getDeptLevel() {
		return deptLevel;
	}

	public void setDeptLevel(String deptLevel) {
		this.deptLevel = deptLevel;
	}
	
	@Column(name = "DEPT_STATUS", length = 1)
	public String getDeptStatus() {
		return deptStatus;
	}

	public void setDeptStatus(String deptStatus) {
		this.deptStatus = deptStatus;
	}
	@Column(name = "PEOPLE_NUMBER", precision = 10, scale = 0)
	public Long getPeopleNumber() {
		return this.peopleNumber;
	}

	public void setPeopleNumber(Long peopleNumber) {
		this.peopleNumber = peopleNumber;
	}

}