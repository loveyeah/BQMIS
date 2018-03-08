package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSafetools;

@SuppressWarnings("serial")
public class SafeToolInfo implements java.io.Serializable{

	private SpJSafetools tool;
	private String chargeName;
	private String strManuDate;
	private String strCheckDate;
	public SpJSafetools getTool() {
		return tool;
	}
	public void setTool(SpJSafetools tool) {
		this.tool = tool;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}
	public String getStrManuDate() {
		return strManuDate;
	}
	public void setStrManuDate(String strManuDate) {
		this.strManuDate = strManuDate;
	}
	public String getStrCheckDate() {
		return strCheckDate;
	}
	public void setStrCheckDate(String strCheckDate) {
		this.strCheckDate = strCheckDate;
	}
}
