package power.ejb.hr.salary.form;

@SuppressWarnings("serial")
public class SickSalaryForm implements java.io.Serializable{

	private Long sickSalaryId;
	private Double factWorkyearBottom;
	private Double factWorkyearTop;
	private Double localWorkageBottom;
	private Double localWorkageTop;
	private Double salaryScale;
	private String memo;
	public Long getSickSalaryId() {
		return sickSalaryId;
	}
	public void setSickSalaryId(Long sickSalaryId) {
		this.sickSalaryId = sickSalaryId;
	}
	public Double getFactWorkyearBottom() {
		return factWorkyearBottom;
	}
	public void setFactWorkyearBottom(Double factWorkyearBottom) {
		this.factWorkyearBottom = factWorkyearBottom;
	}
	public Double getFactWorkyearTop() {
		return factWorkyearTop;
	}
	public void setFactWorkyearTop(Double factWorkyearTop) {
		this.factWorkyearTop = factWorkyearTop;
	}
	public Double getLocalWorkageBottom() {
		return localWorkageBottom;
	}
	public void setLocalWorkageBottom(Double localWorkageBottom) {
		this.localWorkageBottom = localWorkageBottom;
	}
	public Double getLocalWorkageTop() {
		return localWorkageTop;
	}
	public void setLocalWorkageTop(Double localWorkageTop) {
		this.localWorkageTop = localWorkageTop;
	}
	public Double getSalaryScale() {
		return salaryScale;
	}
	public void setSalaryScale(Double salaryScale) {
		this.salaryScale = salaryScale;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
