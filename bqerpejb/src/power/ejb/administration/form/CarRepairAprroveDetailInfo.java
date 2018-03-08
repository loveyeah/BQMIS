package power.ejb.administration.form;

public class CarRepairAprroveDetailInfo {
	// 车辆维护明细id
	private Long carwhMxId;
	// 项目代码
	private String proCode;
	// 项目详细
	private String proDetail;
	// 费用预算
	private Double price;
	// 实际费用
	private Double realPrice;
	// 车辆维护明细.修改时间
	private String carwhMxUpdateTime;
	// 车辆维护之维修项目.有无清单
	private String haveLise;
	// 车辆维护之维修项目.序号
	private Long carwhProId;
	// 车辆维护之维修项目.修改时间
	private String carwhProUpdateTime;
	// 是否是新规的记录
	private String isNew;
	/**
	 * @return the carwhMxId
	 */
	public Long getCarwhMxId() {
		return carwhMxId;
	}
	/**
	 * @param carwhMxId the carwhMxId to set
	 */
	public void setCarwhMxId(Long carwhMxId) {
		this.carwhMxId = carwhMxId;
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
	 * @return the proName
	 */
	public String getProDetail() {
		return proDetail;
	}
	/**
	 * @param proName the proName to set
	 */
	public void setProDetail(String proDetail) {
		this.proDetail = proDetail;
	}
	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	/**
	 * @return the carwhMxUpdateTime
	 */
	public String getCarwhMxUpdateTime() {
		return carwhMxUpdateTime;
	}
	/**
	 * @param carwhMxUpdateTime the carwhMxUpdateTime to set
	 */
	public void setCarwhMxUpdateTime(String carwhMxUpdateTime) {
		this.carwhMxUpdateTime = carwhMxUpdateTime;
	}
	/**
	 * @return the haveLise
	 */
	public String getHaveLise() {
		return haveLise;
	}
	/**
	 * @param haveLise the haveLise to set
	 */
	public void setHaveLise(String haveLise) {
		this.haveLise = haveLise;
	}
	/**
	 * @return the carwhProId
	 */
	public Long getCarwhProId() {
		return carwhProId;
	}
	/**
	 * @param carwhProId the carwhProId to set
	 */
	public void setCarwhProId(Long carwhProId) {
		this.carwhProId = carwhProId;
	}
	/**
	 * @return the carwhProUpdateTime
	 */
	public String getCarwhProUpdateTime() {
		return carwhProUpdateTime;
	}
	/**
	 * @param carwhProUpdateTime the carwhProUpdateTime to set
	 */
	public void setCarwhProUpdateTime(String carwhProUpdateTime) {
		this.carwhProUpdateTime = carwhProUpdateTime;
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
	 * @return the realPrice
	 */
	public Double getRealPrice() {
		return realPrice;
	}
	/**
	 * @param realPrice the realPrice to set
	 */
	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}
	
}
