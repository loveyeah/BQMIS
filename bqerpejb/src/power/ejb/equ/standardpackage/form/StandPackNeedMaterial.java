package power.ejb.equ.standardpackage.form;
public class StandPackNeedMaterial
{
	// 标准工作包编码
	private String woCode;
	// 物资id
	private Long materialId;
	// 需要的物资数量
	private Double needCount;
	// 库存中已有物资数量
	private Double hasCount;
	public String getWoCode() {
		return woCode;
	}
	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public Double getNeedCount() {
		return needCount;
	}
	public void setNeedCount(Double needCount) {
		this.needCount = needCount;
	}
	public Double getHasCount() {
		return hasCount;
	}
	public void setHasCount(Double hasCount) {
		this.hasCount = hasCount;
	}
	
}