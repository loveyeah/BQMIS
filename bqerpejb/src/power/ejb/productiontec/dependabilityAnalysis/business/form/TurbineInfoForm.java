package power.ejb.productiontec.dependabilityAnalysis.business.form;

import power.ejb.productiontec.dependabilityAnalysis.business.PtKkxTurbineInfo;

public class TurbineInfoForm 
{
	private PtKkxTurbineInfo data;
	private String manufactureDate;
	public String getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	public PtKkxTurbineInfo getData() {
		return data;
	}
	public void setData(PtKkxTurbineInfo data) {
		this.data = data;
	}
}