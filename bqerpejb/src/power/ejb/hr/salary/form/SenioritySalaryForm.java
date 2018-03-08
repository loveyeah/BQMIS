package power.ejb.hr.salary.form;

@SuppressWarnings("serial")
public class SenioritySalaryForm implements java.io.Serializable{
	
	private Long senioritySalaryId;
	private Double senioritySalary;
	private String effectStartTime;
	private String effectEndTime;
	private String memo;
	private String status;
	
	public Long getSenioritySalaryId() {
		return senioritySalaryId;
	}
	public void setSenioritySalaryId(Long senioritySalaryId) {
		this.senioritySalaryId = senioritySalaryId;
	}
	public Double getSenioritySalary() {
		return senioritySalary;
	}
	public void setSenioritySalary(Double senioritySalary) {
		this.senioritySalary = senioritySalary;
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
