package power.ejb.workticket.form;

import java.util.List;

@SuppressWarnings("serial")
public class SecurityMeasureCardModel implements java.io.Serializable{

	 private  WorkticketBaseForPrint base;
	 private List<SecurityMeasureForPrint> safety;
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
}
