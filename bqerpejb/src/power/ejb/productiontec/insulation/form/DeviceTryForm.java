package power.ejb.productiontec.insulation.form;

import power.ejb.productiontec.insulation.PtJyjdJSbtzlh;
import power.ejb.productiontec.insulation.PtJyjdJYqybtzlh;

public class DeviceTryForm implements java.io.Serializable
{
	private PtJyjdJSbtzlh pjj;
	private String nextDate;
	private String operateBy;
	private String operateName;
	private String operateDate;
	
	public PtJyjdJSbtzlh getPjj() {
		return pjj;
	}
	public void setPjj(PtJyjdJSbtzlh pjj) {
		this.pjj = pjj;
	}
	public String getNextDate() {
		return nextDate;
	}
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}
	public String getOperateBy() {
		return operateBy;
	}
	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public String getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}
}
