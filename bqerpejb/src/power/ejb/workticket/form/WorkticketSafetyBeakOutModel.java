package power.ejb.workticket.form;

import power.ejb.workticket.business.RunJWorkticketSafety;

@SuppressWarnings("serial")
public class WorkticketSafetyBeakOutModel implements java.io.Serializable {
	private RunJWorkticketSafety model;
	private Long id; 
	private String safetyName;
	private String safetyContent;
	private String exeMan;
	private String exeDesc;
	private String exeDate;
	private String breakOutMan;
	private String beakOutDate;
	private String isBreakOut;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSafetyName() {
		return safetyName;
	}
	public void setSafetyName(String safetyName) {
		this.safetyName = safetyName;
	}
	public String getSafetyContent() {
		return safetyContent;
	}
	public void setSafetyContent(String safetyContent) {
		this.safetyContent = safetyContent;
	}
	public String getExeMan() {
		return exeMan;
	}
	public void setExeMan(String exeMan) {
		this.exeMan = exeMan;
	}
	public String getExeDate() {
		return exeDate;
	}
	public void setExeDate(String exeDate) {
		this.exeDate = exeDate;
	}
	public String getBreakOutMan() {
		return breakOutMan;
	}
	public void setBreakOutMan(String breakOutMan) {
		this.breakOutMan = breakOutMan;
	}
	public String getBeakOutDate() {
		return beakOutDate;
	}
	public void setBeakOutDate(String beakOutDate) {
		this.beakOutDate = beakOutDate;
	}
	public String getExeDesc() {
		return exeDesc;
	}
	public void setExeDesc(String exeDesc) {
		this.exeDesc = exeDesc;
	}
	public String getIsBreakOut() {
		return isBreakOut;
	}
	public void setIsBreakOut(String isBreakOut) {
		this.isBreakOut = isBreakOut;
	}
	public RunJWorkticketSafety getModel() {
		return model;
	}
	public void setModel(RunJWorkticketSafety model) {
		this.model = model;
	}
	
}
