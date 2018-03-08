/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.form;


/**
 * 库存物料查询及选择Bean
 * 
 * @author jincong
 * @version 1.0
 */
public class MaterialQueryAndSelectInfo implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// 物料主文件
	/** 物料编码 */
	private String materialNo;
	/** 物料ID */
	private Long materialId;
	/** 物料名称 */
	private String materialName;
	/** 规格型号 */
	private String specNo;
	/** 存货计量单位 */
	private String stockUmId;
	
	// 仓库主文件
	/** 仓库名称 */
	private String whsName;
	/** 仓库编码 */
	private String whsNo;
	
	// 库位主文件
	/** 库位名称 */
	private String locationName;
	/** 库位号 */
	private String locationNo;
	
	// 批号记录表
	/** 批号 */
	private String lotNo;
	/** 当前库存 */
	private String nowCount;
	/** 上次修改日期 */
	private String lastModifiedDateLot;
	
	// 库存物料记录
	/** 上次修改日期 */
	private String lastModifiedDateWhs;
	
	// 库位物料记录
	/** 上次修改日期 */
	private String lastModifiedDateLocation;
	
	//add by fyyang 090702
	/**
	 * 标准成本
	 */
	private Double stdCost;
	
	/**
	 * 总成本
	 */
	private Double allCost;
	
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
	 * @return the stockUmId
	 */
	public String getStockUmId() {
		return stockUmId;
	}
	/**
	 * @param stockUmId the stockUmId to set
	 */
	public void setStockUmId(String stockUmId) {
		this.stockUmId = stockUmId;
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
	 * @return the whsNo
	 */
	public String getWhsNo() {
		return whsNo;
	}
	/**
	 * @param whsNo the whsNo to set
	 */
	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}
	/**
	 * @return the locationNo
	 */
	public String getLocationNo() {
		return locationNo;
	}
	/**
	 * @param locationNo the locationNo to set
	 */
	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}
	/**
	 * @return the nowCount
	 */
	public String getNowCount() {
		return nowCount;
	}
	/**
	 * @param nowCount the nowCount to set
	 */
	public void setNowCount(String nowCount) {
		this.nowCount = nowCount;
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
	public Double getStdCost() {
		return stdCost;
	}
	public void setStdCost(Double stdCost) {
		this.stdCost = stdCost;
	}
	public Double getAllCost() {
		return allCost;
	}
	public void setAllCost(Double allCost) {
		this.allCost = allCost;
	}
}
