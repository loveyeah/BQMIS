package power.ejb.run.runlog.shift;
@SuppressWarnings("serial")

public class RunJShiftEqustatusModel implements java.io.Serializable{
	private Long shiftEqustatusId;
	private Long runLogid;
	private String runLogno;
	private String specialityCode;
	private String specialityName;
	private String attributeCode;
	private String equName;
	private Long equStatusId;
	private String equStatusName;
	private String isUse;
	private String enterpriseCode;
	private String colorValue;
	public Long getShiftEqustatusId() {
		return shiftEqustatusId;
	}
	public void setShiftEqustatusId(Long shiftEqustatusId) {
		this.shiftEqustatusId = shiftEqustatusId;
	}
	public Long getRunLogid() {
		return runLogid;
	}
	public void setRunLogid(Long runLogid) {
		this.runLogid = runLogid;
	}
	public String getRunLogno() {
		return runLogno;
	}
	public void setRunLogno(String runLogno) {
		this.runLogno = runLogno;
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
	public String getAttributeCode() {
		return attributeCode;
	}
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public Long getEquStatusId() {
		return equStatusId;
	}
	public void setEquStatusId(Long equStatusId) {
		this.equStatusId = equStatusId;
	}
	public String getEquStatusName() {
		return equStatusName;
	}
	public void setEquStatusName(String equStatusName) {
		this.equStatusName = equStatusName;
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
	public String getColorValue() {
		return colorValue;
	}
	public void setColorValue(String colorValue) {
		this.colorValue = colorValue;
	}
	
}
