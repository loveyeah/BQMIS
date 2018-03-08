package power.ejb.productiontec.relayProtection.form;

import power.ejb.productiontec.relayProtection.PtJdbhJSybg;

@SuppressWarnings("serial")
public class ExperimentReportForm implements java.io.Serializable{

	private PtJdbhJSybg report;
	/** 装置名称 */
	private String deviceName;
	/** 类别名称 */
	private String sortName;
	/** 实验日期 */
    private	String strTestDate;
    /** 上次实验日期 */
    private String strLastTestDate;
    /** 本次计划实验日期 */
    private String strPlanTestDate;

    
    /** 负责人 */
    private String chargeByName;
    /** 测试人员姓名 */
    private String testByName;

	public String getTestByName() {
		return testByName;
	}

	public void setTestByName(String testByName) {
		this.testByName = testByName;
	}

	public PtJdbhJSybg getReport() {
		return report;
	}

	public void setReport(PtJdbhJSybg report) {
		this.report = report;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getStrTestDate() {
		return strTestDate;
	}

	public void setStrTestDate(String strTestDate) {
		this.strTestDate = strTestDate;
	}

	public String getStrLastTestDate() {
		return strLastTestDate;
	}

	public void setStrLastTestDate(String strLastTestDate) {
		this.strLastTestDate = strLastTestDate;
	}

	public String getStrPlanTestDate() {
		return strPlanTestDate;
	}

	public void setStrPlanTestDate(String strPlanTestDate) {
		this.strPlanTestDate = strPlanTestDate;
	}

	

	public String getChargeByName() {
		return chargeByName;
	}

	public void setChargeByName(String chargeByName) {
		this.chargeByName = chargeByName;
	}
}
