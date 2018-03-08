package power.ejb.hr.form;

import java.util.Date;

public class Dept  implements java.io.Serializable { 
	private static final long serialVersionUID = 1L;
	private Long deptId;
	private Long pdeptId;
	private String deptCode;
	private String pdeptCode;
	private String deptName;
	private Long manger;
	private Long deptTypeId;
	private String deptTypeName;
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
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getPdeptId() {
		return pdeptId;
	}
	public void setPdeptId(Long pdeptId) {
		this.pdeptId = pdeptId;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getPdeptCode() {
		return pdeptCode;
	}
	public void setPdeptCode(String pdeptCode) {
		this.pdeptCode = pdeptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Long getManger() {
		return manger;
	}
	public void setManger(Long manger) {
		this.manger = manger;
	}
	public Long getDeptTypeId() {
		return deptTypeId;
	}
	public void setDeptTypeId(Long deptTypeId) {
		this.deptTypeId = deptTypeId;
	}
	public String getDeptTypeName() {
		return deptTypeName;
	}
	public void setDeptTypeName(String deptTypeName) {
		this.deptTypeName = deptTypeName;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getRetrieveCode() {
		return retrieveCode;
	}
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}
	public Long getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getModifiyBy() {
		return modifiyBy;
	}
	public void setModifiyBy(Long modifiyBy) {
		this.modifiyBy = modifiyBy;
	}
	public Date getModifiyDate() {
		return modifiyDate;
	}
	public void setModifiyDate(Date modifiyDate) {
		this.modifiyDate = modifiyDate;
	}
	public Long getLogoffBy() {
		return logoffBy;
	}
	public void setLogoffBy(Long logoffBy) {
		this.logoffBy = logoffBy;
	}
	public Date getLogoffDate() {
		return logoffDate;
	}
	public void setLogoffDate(Date logoffDate) {
		this.logoffDate = logoffDate;
	}
	public Long getPowerPlantId() {
		return powerPlantId;
	}
	public void setPowerPlantId(Long powerPlantId) {
		this.powerPlantId = powerPlantId;
	}
	

}
