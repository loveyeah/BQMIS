package power.ejb.productiontec.metalSupervise.form;

import power.ejb.productiontec.metalSupervise.PtJReport;

@SuppressWarnings("serial")
public class PtJReportForm implements java.io.Serializable{
	private String fillName;
	private String fillDate;
	private PtJReport model;
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
	public PtJReport getModel() {
		return model;
	}
	public void setModel(PtJReport model) {
		this.model = model;
	}

}
