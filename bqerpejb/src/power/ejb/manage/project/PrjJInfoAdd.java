package power.ejb.manage.project;

public class PrjJInfoAdd implements java.io.Serializable{
	private PrjJInfo prjjInfo;
	private String chargeByName;
	private String prjTypeName;
	private String enterByName;
	private String changeDepName;
	private String constructionUnitName;

	private String planStartDate;
	private String planEndDate;
	private String factStartDate;
	private String factEndDate;
	private String entryDate;
	private String prjStatus;
	private Long prjStatusId;
	private String prjStatusType;
	//费用来源
	private String itemName;
//	public PrjJInfo getModel() {
//		return model;
//	}
//	public void setModel(PrjJInfo model) {
//		this.model = model;
//	}
	public String getChargeByName() {
		return chargeByName;
	}
	public void setChargeByName(String chargeByName) {
		this.chargeByName = chargeByName;
	}
	public String getPrjTypeName() {
		return prjTypeName;
	}
	public void setPrjTypeName(String prjTypeName) {
		this.prjTypeName = prjTypeName;
	}
	public String getEnterByName() {
		return enterByName;
	}
	public void setEnterByName(String enterByName) {
		this.enterByName = enterByName;
	}
	public String getChangeDepName() {
		return changeDepName;
	}
	public void setChangeDepName(String changeDepName) {
		this.changeDepName = changeDepName;
	}
	public PrjJInfo getPrjjInfo() {
		return prjjInfo;
	}
	public void setPrjjInfo(PrjJInfo prjjInfo) {
		this.prjjInfo = prjjInfo;
	}
	public String getPlanStartDate() {
		return planStartDate;
	}
	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}
	public String getPlanEndDate() {
		return planEndDate;
	}
	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getPrjStatus() {
		return prjStatus;
	}
	public void setPrjStatus(String prjStatus) {
		this.prjStatus = prjStatus;
	}
	public Long getPrjStatusId() {
		return prjStatusId;
	}
	public void setPrjStatusId(Long prjStatusId) {
		this.prjStatusId = prjStatusId;
	}
	public String getFactStartDate() {
		return factStartDate;
	}
	public void setFactStartDate(String factStartDate) {
		this.factStartDate = factStartDate;
	}

	public String getFactEndDate() {
		return factEndDate;
	}
	public void setFactEndDate(String factEndDate) {
		this.factEndDate = factEndDate;
	}
	public String getConstructionUnitName() {
		return constructionUnitName;
	}
	public void setConstructionUnitName(String constructionUnitName) {
		this.constructionUnitName = constructionUnitName;
	}
	public String getPrjStatusType() {
		return prjStatusType;
	}
	public void setPrjStatusType(String prjStatusType) {
		this.prjStatusType = prjStatusType;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	

}
