package power.ejb.resource.form;

public class PlanApproveStatus implements java.io.Serializable{
private String statusCode;
private String statusName;
public String getStatusCode() {
	return statusCode;
}
public void setStatusCode(String statusCode) {
	this.statusCode = statusCode;
}
public String getStatusName() {
	return statusName;
}
public void setStatusName(String statusName) {
	this.statusName = statusName;
}
}
