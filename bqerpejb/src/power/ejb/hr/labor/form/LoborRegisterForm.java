package power.ejb.hr.labor.form;

import java.util.List;

import power.ejb.hr.labor.HrJLaborRegister;

@SuppressWarnings("serial")
public class LoborRegisterForm implements java.io.Serializable{
	private HrJLaborRegister loborRegister;
	private List<HrJLaborRegister> loborlList;
	private String deptId;
	private String deptCode;
	private String deptName;
	private Long lbWorkId;
	private String lbWorkName;
	private String empCode;
	private String chsName;
	private Long sendNum;
	private Long materialNum;
	private Long laborRegisterId;
	private String sendSeason;
	

	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Long getLbWorkId() {
		return lbWorkId;
	}
	public void setLbWorkId(Long lbWorkId) {
		this.lbWorkId = lbWorkId;
	}
	public String getLbWorkName() {
		return lbWorkName;
	}
	public void setLbWorkName(String lbWorkName) {
		this.lbWorkName = lbWorkName;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getChsName() {
		return chsName;
	}
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	public Long getSendNum() {
		return sendNum;
	}
	public void setSendNum(Long sendNum) {
		this.sendNum = sendNum;
	}
	public Long getMaterialNum() {
		return materialNum;
	}
	public void setMaterialNum(Long materialNum) {
		this.materialNum = materialNum;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getSendSeason() {
		return sendSeason;
	}
	public void setSendSeason(String sendSeason) {
		this.sendSeason = sendSeason;
	}
	public Long getLaborRegisterId() {
		return laborRegisterId;
	}
	public void setLaborRegisterId(Long laborRegisterId) {
		this.laborRegisterId = laborRegisterId;
	}
	public HrJLaborRegister getLoborRegister() {
		return loborRegister;
	}
	public void setLoborRegister(HrJLaborRegister loborRegister) {
		this.loborRegister = loborRegister;
	}
	public List<HrJLaborRegister> getLoborlList() {
		return loborlList;
	}
	public void setLoborlList(List<HrJLaborRegister> loborlList) {
		this.loborlList = loborlList;
	}

}
