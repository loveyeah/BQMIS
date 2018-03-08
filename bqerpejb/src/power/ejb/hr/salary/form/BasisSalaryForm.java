package power.ejb.hr.salary.form;

@SuppressWarnings("serial")
public class BasisSalaryForm implements java.io.Serializable{

	private Long basisSalaryId;
	private Double basisSalary;
	private String effectStartTime;
	private String effectEndTime;
	private String memo;
	private String status;
	
	public Long getBasisSalaryId() {
		return basisSalaryId;
	}
	public void setBasisSalaryId(Long basisSalaryId) {
		this.basisSalaryId = basisSalaryId;
	}
	public Double getBasisSalary() {
		return basisSalary;
	}
	public void setBasisSalary(Double basisSalary) {
		this.basisSalary = basisSalary;
	}
	public String getEffectStartTime() {
		return effectStartTime;
	}
	public void setEffectStartTime(String effectStartTime) {
		this.effectStartTime = effectStartTime;
	}
	public String getEffectEndTime() {
		return effectEndTime;
	}
	public void setEffectEndTime(String effectEndTime) {
		this.effectEndTime = effectEndTime;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
