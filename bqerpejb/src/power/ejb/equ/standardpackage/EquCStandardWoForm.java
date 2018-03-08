package power.ejb.equ.standardpackage;

@SuppressWarnings("serial")
public class EquCStandardWoForm implements java.io.Serializable {
	private EquCStandardWo standardInfo;
	private String professionName;
	private String description;
	private long priority;
	private double jopDuration;
	private String speciality;
	private long interruptable;
	private long downTime;
	private String LaborCode;
	private String Calnum;
	private String Status;
	private String Enterprisecode;
	private String IsUse;
	private String SpecialityName;
	private String kksCode;
	private String equName;
	private String repairmodelName;


	
	

	public String getRepairmodelName() {
		return repairmodelName;
	}

	public void setRepairmodelName(String repairmodelName) {
		this.repairmodelName = repairmodelName;
	}

	/**
	 * @return the standardInfo
	 */
	public EquCStandardWo getStandardInfo() {
		return standardInfo;
	}

	/**
	 * @param standardInfo
	 *            the standardInfo to set
	 */
	public void setStandardInfo(EquCStandardWo standardInfo) {
		this.standardInfo = standardInfo;
	}

	/**
	 * @return the professionName
	 */
	public String getProfessionName() {
		return professionName;
	}

	/**
	 * @param professionName the professionName to set
	 */
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public double getJopDuration() {
		return jopDuration;
	}

	public void setJopDuration(double jopDuration) {
		this.jopDuration = jopDuration;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public long getInterruptable() {
		return interruptable;
	}

	public void setInterruptable(long interruptable) {
		this.interruptable = interruptable;
	}

	public long getDownTime() {
		return downTime;
	}

	public void setDownTime(long downTime) {
		this.downTime = downTime;
	}

	public String getLaborCode() {
		return LaborCode;
	}

	public void setLaborCode(String laborCode) {
		LaborCode = laborCode;
	}

	public String getCalnum() {
		return Calnum;
	}

	public void setCalnum(String calnum) {
		Calnum = calnum;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getEnterprisecode() {
		return Enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		Enterprisecode = enterprisecode;
	}

	public String getIsUse() {
		return IsUse;
	}

	public void setIsUse(String isUse) {
		IsUse = isUse;
	}

	public String getSpecialityName() {
		return SpecialityName;
	}

	public void setSpecialityName(String specialityName) {
		SpecialityName = specialityName;
	}

	public String getKksCode() {
		return kksCode;
	}

	public void setKksCode(String kksCode) {
		this.kksCode = kksCode;
	}

	public String getEquName() {
		return equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

}
