package power.ejb.hr.salary.form;

@SuppressWarnings("serial")
public class SalaryPersonalForm implements java.io.Serializable{

	private Long empId;
	private String empCode;
	private Long stationId;
	private Long stationTypeId;
	private String stationTypeName;
	
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public Long getStationTypeId() {
		return stationTypeId;
	}
	public void setStationTypeId(Long stationTypeId) {
		this.stationTypeId = stationTypeId;
	}
	public String getStationTypeName() {
		return stationTypeName;
	}
	public void setStationTypeName(String stationTypeName) {
		this.stationTypeName = stationTypeName;
	}
}
