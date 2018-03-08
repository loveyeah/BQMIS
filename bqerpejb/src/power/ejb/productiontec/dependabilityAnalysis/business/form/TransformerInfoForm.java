package power.ejb.productiontec.dependabilityAnalysis.business.form;

import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxTransformerInfo;

public class TransformerInfoForm
{
	private PtKkxTransformerInfo data;
	private String manufactureDate;
	public PtKkxTransformerInfo getData() {
		return data;
	}
	public void setData(PtKkxTransformerInfo data) {
		this.data = data;
	}
	public String getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
}