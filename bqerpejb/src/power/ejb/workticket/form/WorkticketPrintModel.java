package power.ejb.workticket.form;

import java.util.List;

@SuppressWarnings("serial")
public class WorkticketPrintModel implements java.io.Serializable {
	private WorkticketBaseForPrint base;
	private List<SecurityMeasureForPrint> safety;
	private List<WorkticketHisForPrint> history;
	private WorkticketDangerPointForPrintHF dangerHF;
	private DhMeasureInfoForPrint measure;

	public WorkticketBaseForPrint getBase() {
		return base;
	}

	public void setBase(WorkticketBaseForPrint base) {
		this.base = base;
	}

	public List<SecurityMeasureForPrint> getSafety() {
		return safety;
	}

	public void setSafety(List<SecurityMeasureForPrint> safety) {
		this.safety = safety;
	}

	public List<WorkticketHisForPrint> getHistory() {
		return history;
	}

	public void setHistory(List<WorkticketHisForPrint> history) {
		this.history = history;
	}

	/**
	 * @return the dangerHF
	 */
	public WorkticketDangerPointForPrintHF getDangerHF() {
		return dangerHF;
	}

	/**
	 * @param dangerHF the dangerHF to set
	 */
	public void setDangerHF(WorkticketDangerPointForPrintHF dangerHF) {
		this.dangerHF = dangerHF;
	}

	public DhMeasureInfoForPrint getMeasure() {
		return measure;
	}

	public void setMeasure(DhMeasureInfoForPrint measure) {
		this.measure = measure;
	}

}
