package power.web.resource.storechange.action;

import java.util.Date;

public class StoreChangeBean {
	// 物料ID
    private Long materialId;
    // 批号
    private String lotNo;
    // 异动数量
    private Double numChange;
    // 操作仓库
    private String fromWhsNo;
    // 操作库位
    private String fromLocationId;
    // 调入仓库
    private String toWhsId;
    // 调入库位
    private String toLocationId;
    // 库存物料记录.上次修改时间
    private String lastModifiedDateWhs;
    // 库位物料记录.上次修改时间
    private String lastModifiedDateLocation;
    // 批号记录.上次修改时间
    private String lastModifiedDateLot;
	/**
	 * @return the materialId
	 */
	public Long getMaterialId() {
		return materialId;
	}
	/**
	 * @param materialId the materialId to set
	 */
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
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
	 * @return the numChange
	 */
	public Double getNumChange() {
		return numChange;
	}
	/**
	 * @param numChange the numChange to set
	 */
	public void setNumChange(Double numChange) {
		this.numChange = numChange;
	}
	/**
	 * @return the fromWhsNo
	 */
	public String getFromWhsNo() {
		return fromWhsNo;
	}
	/**
	 * @param fromWhsNo the fromWhsNo to set
	 */
	public void setFromWhsNo(String fromWhsNo) {
		this.fromWhsNo = fromWhsNo;
	}
	/**
	 * @return the fromLocationId
	 */
	public String getFromLocationId() {
		return fromLocationId;
	}
	/**
	 * @param fromLocationId the fromLocationId to set
	 */
	public void setFromLocationId(String fromLocationId) {
		this.fromLocationId = fromLocationId;
	}
	/**
	 * @return the toWhsId
	 */
	public String getToWhsId() {
		return toWhsId;
	}
	/**
	 * @param toWhsId the toWhsId to set
	 */
	public void setToWhsId(String toWhsId) {
		this.toWhsId = toWhsId;
	}
	/**
	 * @return the toLocationId
	 */
	public String getToLocationId() {
		return toLocationId;
	}
	/**
	 * @param toLocationId the toLocationId to set
	 */
	public void setToLocationId(String toLocationId) {
		this.toLocationId = toLocationId;
	}
	/**
	 * @return the lastModifiedDateWhs
	 */
	public String getLastModifiedDateWhs() {
		return lastModifiedDateWhs;
	}
	/**
	 * @param lastModifiedDateWhs the lastModifiedDateWhs to set
	 */
	public void setLastModifiedDateWhs(String lastModifiedDateWhs) {
		this.lastModifiedDateWhs = lastModifiedDateWhs;
	}
	/**
	 * @return the lastModifiedDateLocation
	 */
	public String getLastModifiedDateLocation() {
		return lastModifiedDateLocation;
	}
	/**
	 * @param lastModifiedDateLocation the lastModifiedDateLocation to set
	 */
	public void setLastModifiedDateLocation(String lastModifiedDateLocation) {
		this.lastModifiedDateLocation = lastModifiedDateLocation;
	}
	/**
	 * @return the lastModifiedDateLot
	 */
	public String getLastModifiedDateLot() {
		return lastModifiedDateLot;
	}
	/**
	 * @param lastModifiedDateLot the lastModifiedDateLot to set
	 */
	public void setLastModifiedDateLot(String lastModifiedDateLot) {
		this.lastModifiedDateLot = lastModifiedDateLot;
	}
	
}
