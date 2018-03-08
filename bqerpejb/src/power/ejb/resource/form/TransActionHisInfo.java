package power.ejb.resource.form;

import java.util.Date;

public class TransActionHisInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 1事务历史表.流水号 */
	private Long transHisId;
	/** 2事务历史表.单号 */
	private String orderNo;
	/** 3事务历史表.序号*/
	private Long sequenceNo;
	/** 4事务作用.事务名称*/
	private String transName;
	/** 5物料主文件.物料编码*/
	private String materialNo;
	/** 6物料主文件.物料名称*/
	private String materialName;
	/** 7物料主文件.规格型号*/
	private String specNo;
	/** 8物料主文件.材质/参数*/
	private String parameter;
	/** 9事务历史表.异动数量*/
	private Double transQty;
	/** 共通.单位名称*/
	private String transUmId;
	/** 1事务历史表.操作人*/
	private String operatedBy;
	/** 2事务历史表.操作时间*/
	private String operatedDate;
	/** 3操作仓库.仓库名称*/
	private String whsName; 	
	/** 4操作库位.库位名称*/
	private String locationName;
	/** 5调入仓库.仓库名称*/
	private String whsNameTwo;
	/** 6调入库位.库位名称*/
	private String locationNameTwo;
	/** 7事务历史表.批号*/
	private String lotNo;
	/** 8事务历史表.备注*/
	private String memo;
	/** 9事务历史表.采购单号*/ 
	private String purNo;
	/** 10事务历史表.物料单价*/
	private Double price;
	/** 11事务历史表.标准成本*/
	private Double stdCost;
	/** 12事务历史表.到货单号*/
	private String arrivalNo;
	/** 13事务历史表.仓库编号*/
	private String whsNo;
	/** 物料ID*/
	private String materialId;
	
	/**
	 * @return the arrivalNo
	 */
	public String getArrivalNo() {
		return arrivalNo;
	}
	/**
	 * @param arrivalNo the arrivalNo to set
	 */
	public void setArrivalNo(String arrivalNo) {
		this.arrivalNo = arrivalNo;
	}
	/**
	 * @return the transHisId
	 */
	public Long getTransHisId() {
		return transHisId;
	}
	/**
	 * @param transHisId the transHisId to set
	 */
	public void setTransHisId(Long transHisId) {
		this.transHisId = transHisId;
	}
	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return the sequenceNo
	 */
	public Long getSequenceNo() {
		return sequenceNo;
	}
	/**
	 * @param sequenceNo the sequenceNo to set
	 */
	public void setSequenceNo(Long sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	/**
	 * @return the transName
	 */
	public String getTransName() {
		return transName;
	}
	/**
	 * @param transName the transName to set
	 */
	public void setTransName(String transName) {
		this.transName = transName;
	}
	/**
	 * @return the materialNo
	 */
	public String getMaterialNo() {
		return materialNo;
	}
	/**
	 * @param materialNo the materialNo to set
	 */
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	/**
	 * @return the materialName
	 */
	public String getMaterialName() {
		return materialName;
	}
	/**
	 * @param materialName the materialName to set
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	/**
	 * @return the specNo
	 */
	public String getSpecNo() {
		return specNo;
	}
	/**
	 * @param specNo the specNo to set
	 */
	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}
	/**
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}
	/**
	 * @param parameter the parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	/**
	 * @return the transQty
	 */
	public Double getTransQty() {
		return transQty;
	}
	/**
	 * @param transQty the transQty to set
	 */
	public void setTransQty(Double transQty) {
		this.transQty = transQty;
	}
	/**
	 * @return the transUmId
	 */
	public String getTransUmId() {
		return transUmId;
	}
	/**
	 * @param transUmId the transUmId to set
	 */
	public void setTransUmId(String transUmId) {
		this.transUmId = transUmId;
	}
	/**
	 * @return the operatedBy
	 */
	public String getOperatedBy() {
		return operatedBy;
	}
	/**
	 * @param operatedBy the operatedBy to set
	 */
	public void setOperatedBy(String operatedBy) {
		this.operatedBy = operatedBy;
	}
	/**
	 * @return the operatedDate
	 */
	public String getOperatedDate() {
		return operatedDate;
	}
	/**
	 * @param operatedDate the operatedDate to set
	 */
	public void setOperatedDate(String operatedDate) {
		this.operatedDate = operatedDate;
	}
	/**
	 * @return the whsName
	 */
	public String getWhsName() {
		return whsName;
	}
	/**
	 * @param whsName the whsName to set
	 */
	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}
	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}
	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	/**
	 * @return the whsNameTwo
	 */
	public String getWhsNameTwo() {
		return whsNameTwo;
	}
	/**
	 * @param whsNameTwo the whsNameTwo to set
	 */
	public void setWhsNameTwo(String whsNameTwo) {
		this.whsNameTwo = whsNameTwo;
	}
	/**
	 * @return the locationNameTwo
	 */
	public String getLocationNameTwo() {
		return locationNameTwo;
	}
	/**
	 * @param locationNameTwo the locationNameTwo to set
	 */
	public void setLocationNameTwo(String locationNameTwo) {
		this.locationNameTwo = locationNameTwo;
	}
	/**
	 * @return the lotNo
	 */
	public String getLotNo() {
		return lotNo;
	}
	/**
	 * @param lotNo the lotNo to set
	 */
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPurNo() {
		return purNo;
	}
	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getStdCost() {
		return stdCost;
	}
	public void setStdCost(Double stdCost) {
		this.stdCost = stdCost;
	}
	public String getWhsNo() {
		return whsNo;
	}
	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}
	/**
	 * @return the materialId
	 */
	public String getMaterialId() {
		return materialId;
	}
	/**
	 * @param materialId the materialId to set
	 */
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
}
