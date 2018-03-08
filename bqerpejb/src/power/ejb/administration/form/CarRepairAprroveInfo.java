package power.ejb.administration.form;

public class CarRepairAprroveInfo {
	// 材料结算清单id
	private Long id;
	// 车辆维护ID
	private String whId;
	// 项目代码
	private String proCode;
	// 单位
	private String unit;
	// 预算数量
	private Double num;
	// 预算单价
	private Double unitPrice;
	// 实际数量
	private Double realNum;
	// 实际单价
	private Double realUnitPrice;
	// 零件名称
	private String partName;
	// 备注
	private String note;
	// 修改时间
	private String updateTime;
	// 是否是新规的记录
	private String isNew;
	// 金额
	private Double sum;
	// 实际金额
	private Double realSum;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the whId
	 */
	public String getWhId() {
		return whId;
	}
	/**
	 * @param whId the whId to set
	 */
	public void setWhId(String whId) {
		this.whId = whId;
	}
	/**
	 * @return the proCode
	 */
	public String getProCode() {
		return proCode;
	}
	/**
	 * @param proCode the proCode to set
	 */
	public void setProCode(String proCode) {
		this.proCode = proCode;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the num
	 */
	public Double getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(Double num) {
		this.num = num;
	}
	/**
	 * @return the unitPrice
	 */
	public Double getUnitPrice() {
		return unitPrice;
	}
	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	/**
	 * @return the partName
	 */
	public String getPartName() {
		return partName;
	}
	/**
	 * @param partName the partName to set
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the isNew
	 */
	public String getIsNew() {
		return isNew;
	}
	/**
	 * @param isNew the isNew to set
	 */
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	/**
	 * @return the sum
	 */
	public Double getSum() {
		return sum;
	}
	/**
	 * @param sum the sum to set
	 */
	public void setSum(Double sum) {
		this.sum = sum;
	}
	/**
	 * @return the realNum
	 */
	public Double getRealNum() {
		return realNum;
	}
	/**
	 * @param realNum the realNum to set
	 */
	public void setRealNum(Double realNum) {
		this.realNum = realNum;
	}
	/**
	 * @return the realUnitPrice
	 */
	public Double getRealUnitPrice() {
		return realUnitPrice;
	}
	/**
	 * @param realUnitPrice the realUnitPrice to set
	 */
	public void setRealUnitPrice(Double realUnitPrice) {
		this.realUnitPrice = realUnitPrice;
	}
	/**
	 * @return the realSum
	 */
	public Double getRealSum() {
		return realSum;
	}
	/**
	 * @param realSum the realSum to set
	 */
	public void setRealSum(Double realSum) {
		this.realSum = realSum;
	}
	
}
