package power.ejb.workticket.form;

import java.util.List;

import power.ejb.workticket.business.RunJWorkticketMeasureData;

public class DhMeasureInfoForPrint implements java.io.Serializable{
	private String measureLocation;
	private String useTool;
	private String combustibleGas;
	private List<RunJWorkticketMeasureData> mesureData;
	public String getMeasureLocation() {
		return measureLocation;
	}
	public void setMeasureLocation(String measureLocation) {
		this.measureLocation = measureLocation;
	}
	public String getUseTool() {
		return useTool;
	}
	public void setUseTool(String useTool) {
		this.useTool = useTool;
	}
	public String getCombustibleGas() {
		return combustibleGas;
	}
	public void setCombustibleGas(String combustibleGas) {
		this.combustibleGas = combustibleGas;
	}
	public List<RunJWorkticketMeasureData> getMesureData() {
		return mesureData;
	}
	public void setMesureData(List<RunJWorkticketMeasureData> mesureData) {
		this.mesureData = mesureData;
	}
}
