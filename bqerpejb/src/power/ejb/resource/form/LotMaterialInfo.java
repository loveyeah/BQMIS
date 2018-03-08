package power.ejb.resource.form;

import java.io.Serializable;
/**
 *批号物资盘点
 * @author chenshoujiang
 */
public class LotMaterialInfo implements Serializable {
	/** serial id */
	private static final long serialVersionUID = 1L;
	
	private Long materialId;
	private String materialNo;
	private String materialName;
	private String specNo;
	private String stockUmId;
	private String whsNo;
	private String whsName;
	private String location;
	private String locationName;
	private String lotNo;
	private String account;
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public String getMaterialNo() {
		return materialNo;
	}
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getSpecNo() {
		return specNo;
	}
	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}
	public String getStockUmId() {
		return stockUmId;
	}
	public void setStockUmId(String stockUmId) {
		this.stockUmId = stockUmId;
	}
	public String getWhsNo() {
		return whsNo;
	}
	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}
	public String getWhsName() {
		return whsName;
	}
	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}
