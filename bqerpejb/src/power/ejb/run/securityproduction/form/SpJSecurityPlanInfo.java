package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSecurityPlan;

public class SpJSecurityPlanInfo implements java.io.Serializable
{
	private SpJSecurityPlan spjs;
	/**完成期限*/
	private String finishDate;
	/**负责人姓名*/
	private String chargeName;
	/**负责部门名称*/
	private String chargeDepName;
	/**填报人姓名*/
	private String fillName;
	/**填报部门名称*/
	private String fillDepName;
	/**填报日期*/
	private String fillDate;
	/**评价人姓名*/
	private String appraisalName;
	/**评价日期*/
	private String appraisalDate;
	public SpJSecurityPlan getSpjs() {
		return spjs;
	}
	public void setSpjs(SpJSecurityPlan spjs) {
		this.spjs = spjs;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getFillName() {
		return fillName;
	}
	public void setFillName(String fillName) {
		this.fillName = fillName;
	}

	public String getChargeDepName() {
		return chargeDepName;
	}
	public void setChargeDepName(String chargeDepName) {
		this.chargeDepName = chargeDepName;
	}
	public String getFillDepName() {
		return fillDepName;
	}
	public void setFillDepName(String fillDepName) {
		this.fillDepName = fillDepName;
	}
	public String getFillDate() {
		return fillDate;
	}
	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}
	public String getAppraisalName() {
		return appraisalName;
	}
	public void setAppraisalName(String appraisalName) {
		this.appraisalName = appraisalName;
	}
	public String getAppraisalDate() {
		return appraisalDate;
	}
	public void setAppraisalDate(String appraisalDate) {
		this.appraisalDate = appraisalDate;
	}
}