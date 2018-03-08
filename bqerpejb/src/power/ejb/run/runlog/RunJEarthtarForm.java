package power.ejb.run.runlog;

import java.util.Date;

@SuppressWarnings("serial")
public class RunJEarthtarForm implements java.io.Serializable{
	private Long earthRecordId;
	private Long installLogid;
	private Long earthId;
	private String earthName;
	private String installMan;
	private String installManName;
	private String installTime;
	private String installPlace;
	private String installCharger;
	private String installChargerName;
	private String backoutMan;
	private String backoutManName;
	private String backoutTime;
	private Long backoutLogid;
	private String backoutCharger;
	private String backoutChargerName;
	private String isBack;
	private String isUse;
	private String enterpriseCode;
	private String specialityCode;
	private String specialityName;
	public Long getEarthRecordId() {
		return earthRecordId;
	}
	public void setEarthRecordId(Long earthRecordId) {
		this.earthRecordId = earthRecordId;
	}
	public Long getInstallLogid() {
		return installLogid;
	}
	public void setInstallLogid(Long installLogid) {
		this.installLogid = installLogid;
	}
	public Long getEarthId() {
		return earthId;
	}
	public void setEarthId(Long earthId) {
		this.earthId = earthId;
	}
	public String getEarthName() {
		return earthName;
	}
	public void setEarthName(String earthName) {
		this.earthName = earthName;
	}
	public String getInstallMan() {
		return installMan;
	}
	public void setInstallMan(String installMan) {
		this.installMan = installMan;
	}
	public String getInstallManName() {
		return installManName;
	}
	public void setInstallManName(String installManName) {
		this.installManName = installManName;
	}
	
	public String getInstallPlace() {
		return installPlace;
	}
	public void setInstallPlace(String installPlace) {
		this.installPlace = installPlace;
	}
	public String getInstallCharger() {
		return installCharger;
	}
	public void setInstallCharger(String installCharger) {
		this.installCharger = installCharger;
	}
	public String getInstallChargerName() {
		return installChargerName;
	}
	public void setInstallChargerName(String installChargerName) {
		this.installChargerName = installChargerName;
	}
	public String getBackoutMan() {
		return backoutMan;
	}
	public void setBackoutMan(String backoutMan) {
		this.backoutMan = backoutMan;
	}
	public String getBackoutManName() {
		return backoutManName;
	}
	public void setBackoutManName(String backoutManName) {
		this.backoutManName = backoutManName;
	}
	
	public Long getBackoutLogid() {
		return backoutLogid;
	}
	public void setBackoutLogid(Long backoutLogid) {
		this.backoutLogid = backoutLogid;
	}
	public String getBackoutCharger() {
		return backoutCharger;
	}
	public void setBackoutCharger(String backoutCharger) {
		this.backoutCharger = backoutCharger;
	}
	public String getBackoutChargerName() {
		return backoutChargerName;
	}
	public void setBackoutChargerName(String backoutChargerName) {
		this.backoutChargerName = backoutChargerName;
	}
	public String getIsBack() {
		return isBack;
	}
	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getSpecialityCode() {
		return specialityCode;
	}
	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getInstallTime() {
		return installTime;
	}
	public void setInstallTime(String installTime) {
		this.installTime = installTime;
	}
	public String getBackoutTime() {
		return backoutTime;
	}
	public void setBackoutTime(String backoutTime) {
		this.backoutTime = backoutTime;
	}
}
