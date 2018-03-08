package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJBriefnews;

@SuppressWarnings("serial")
public class BriefNewsForm implements java.io.Serializable{

	private SpJBriefnews briefnews;
	private String workName;
	private String month;
	private String commondate;
	public String getCommondate() {
		return commondate;
	}
	public void setCommondate(String commondate) {
		this.commondate = commondate;
	}
	public SpJBriefnews getBriefnews() {
		return briefnews;
	}
	public void setBriefnews(SpJBriefnews briefnews) {
		this.briefnews = briefnews;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
}
