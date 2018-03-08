package power.ejb.productiontec.relayProtection.form;

import power.ejb.productiontec.relayProtection.PtJdbhJDzjssm;

public class CalculateInstructionForm implements java.io.Serializable
{
	// 继电保护定值计算说明
	private PtJdbhJDzjssm dzjssm;
	// 填写人姓名
	private String fillName;
	// 填写时间
	private String fillDate;
	public PtJdbhJDzjssm getDzjssm() {
		return dzjssm;
	}
	public void setDzjssm(PtJdbhJDzjssm dzjssm) {
		this.dzjssm = dzjssm;
	}
	public String getFillName() {
		return fillName;
	}
	public void setFillName(String fillName) {
		this.fillName = fillName;
	}
	public String getFillDate() {
		return fillDate;
	}
	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}
}
