package power.ejb.productiontec.chemistry.form;

import power.ejb.productiontec.chemistry.PtHxjdJNqqxl;

@SuppressWarnings("serial")
public class CondenserLeakForm implements java.io.Serializable{
	
	private PtHxjdJNqqxl condenser;
	private String startDate;
	private String endDate;
	private String fillDate;
	private String fillName;
	private String deviceName;
	private Long nqjxlDetailId;
	private String projectNames;
	private Double itemName1;
	private Double itemName2;
	private Double itemName3;
	private Double itemName4;
	private String enterpriseCode;
	//主表中ID
	private Long nqjxlId; 
	//明细表中ID
	private Long nqjxlIdDet;
	

	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getFillDate() {
		return fillDate;
	}
	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}
	public String getFillName() {
		return fillName;
	}
	public void setFillName(String fillName) {
		this.fillName = fillName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Long getNqjxlDetailId() {
		return nqjxlDetailId;
	}
	public void setNqjxlDetailId(Long nqjxlDetailId) {
		this.nqjxlDetailId = nqjxlDetailId;
	}
	public String getProjectNames() {
		return projectNames;
	}
	public void setProjectNames(String projectNames) {
		this.projectNames = projectNames;
	}
	public Double getItemName1() {
		return itemName1;
	}
	public void setItemName1(Double itemName1) {
		this.itemName1 = itemName1;
	}
	public Double getItemName2() {
		return itemName2;
	}
	public void setItemName2(Double itemName2) {
		this.itemName2 = itemName2;
	}
	public Double getItemName3() {
		return itemName3;
	}
	public void setItemName3(Double itemName3) {
		this.itemName3 = itemName3;
	}
	public Double getItemName4() {
		return itemName4;
	}
	public void setItemName4(Double itemName4) {
		this.itemName4 = itemName4;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public PtHxjdJNqqxl getCondenser() {
		return condenser;
	}
	public void setCondenser(PtHxjdJNqqxl condenser) {
		this.condenser = condenser;
	}
	public Long getNqjxlId() {
		return nqjxlId;
	}
	public void setNqjxlId(Long nqjxlId) {
		this.nqjxlId = nqjxlId;
	}
	public Long getNqjxlIdDet() {
		return nqjxlIdDet;
	}
	public void setNqjxlIdDet(Long nqjxlIdDet) {
		this.nqjxlIdDet = nqjxlIdDet;
	}
}
