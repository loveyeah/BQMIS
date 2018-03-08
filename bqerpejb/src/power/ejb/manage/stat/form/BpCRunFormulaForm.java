package power.ejb.manage.stat.form;

import power.ejb.manage.stat.BpCRunFormula;

public class BpCRunFormulaForm {

	private BpCRunFormula runFormulaInfo;
	private String itemCodeName;
	private String runDataCodeName;

	public BpCRunFormula getRunFormulaInfo() {
		return runFormulaInfo;
	}

	public void setRunFormulaInfo(BpCRunFormula runFormulaInfo) {
		this.runFormulaInfo = runFormulaInfo;
	}

	public String getItemCodeName() {
		return itemCodeName;
	}

	public void setItemCodeName(String itemCodeName) {
		this.itemCodeName = itemCodeName;
	}

	public String getRunDataCodeName() {
		return runDataCodeName;
	}

	public void setRunDataCodeName(String runDataCodeName) {
		this.runDataCodeName = runDataCodeName;
	}

}
