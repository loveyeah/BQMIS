package power.ejb.manage.exam.form;

@SuppressWarnings("serial")
public class CashModel implements java.io.Serializable{
	private Long declarDetailId;
	private Long  affiliatedId;
	private String itemName;
	private String unitName;
	private Double planValue;
	private Double realValue;
	private String   complete;
	private Double cash;
	private String affiliatedDept;
	private String affiliatedLevel;
	private String affiliatedValue;
	private String memo;
	private String  datetype;

	//add by drdu 091201
	private String status;
	
	// add by liuyi 091207
	private Long topicId;
	private String topicName;
	private String coefficient;
	private String itemCode;
	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(String coefficient) {
		this.coefficient = coefficient;
	}
	public String getDatetype() {
		return datetype;
	}
	public void setDatetype(String datetype) {
		this.datetype = datetype;
	}
	public Long getAffiliatedId() {
		return affiliatedId;
	}
	public void setAffiliatedId(Long affiliatedId) {
		this.affiliatedId = affiliatedId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Double getPlanValue() {
		return planValue;
	}
	public void setPlanValue(Double planValue) {
		this.planValue = planValue;
	}
	public Double getRealValue() {
		return realValue;
	}
	public void setRealValue(Double realValue) {
		this.realValue = realValue;
	}
	public Double getCash() {
		return cash;
	}
	public void setCash(Double cash) {
		this.cash = cash;
	}
	public String getAffiliatedDept() {
		return affiliatedDept;
	}
	public void setAffiliatedDept(String affiliatedDept) {
		this.affiliatedDept = affiliatedDept;
	}
	public String getAffiliatedLevel() {
		return affiliatedLevel;
	}
	public void setAffiliatedLevel(String affiliatedLevel) {
		this.affiliatedLevel = affiliatedLevel;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getAffiliatedValue() {
		return affiliatedValue;
	}
	public void setAffiliatedValue(String affiliatedValue) {
		this.affiliatedValue = affiliatedValue;
	}

	
	public String getComplete() {
		return complete;
	}
	public void setComplete(String complete) {
		this.complete = complete;
	}
	public Long getDeclarDetailId() {
		return declarDetailId;
	}
	public void setDeclarDetailId(Long declarDetailId) {
		this.declarDetailId = declarDetailId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
