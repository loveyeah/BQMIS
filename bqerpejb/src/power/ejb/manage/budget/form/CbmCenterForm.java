package power.ejb.manage.budget.form;

@SuppressWarnings("serial")
public class CbmCenterForm implements java.io.Serializable {

	private long centerId;
	private String depCode;
	private String depName;
	private String manager;
	private String ifDuty;
	private String manageName;
	private String costCode;//add by ltong

	public String getDepCode() {
		return depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getIfDuty() {
		return ifDuty;
	}

	public void setIfDuty(String ifDuty) {
		this.ifDuty = ifDuty;
	}

	public String getManageName() {
		return manageName;
	}

	public void setManageName(String manageName) {
		this.manageName = manageName;
	}

	public long getCenterId() {
		return centerId;
	}

	public void setCenterId(long centerId) {
		this.centerId = centerId;
	}

	public String getCostCode() {
		return costCode;
	}

	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}
}
