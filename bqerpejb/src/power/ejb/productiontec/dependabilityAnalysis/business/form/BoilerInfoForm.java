package power.ejb.productiontec.dependabilityAnalysis.business.form;

import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxBoilerInfo;

public class BoilerInfoForm 
{
	private PtKkxBoilerInfo data;
	private String manufactureDate;
	public String getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	public PtKkxBoilerInfo getData() {
		return data;
	}
	public void setData(PtKkxBoilerInfo data) {
		this.data = data;
	}
}