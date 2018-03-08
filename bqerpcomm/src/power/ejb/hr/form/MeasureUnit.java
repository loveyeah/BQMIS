package power.ejb.hr.form;

@SuppressWarnings("serial")
public class MeasureUnit implements java.io.Serializable { 
	private Long unitId;//单位ID
	private String unitName;//单位名称
	private String unitAlias;//单位别名
	private String retrieveCode;//检索码
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getUnitAlias() {
		return unitAlias;
	}
	public void setUnitAlias(String unitAlias) {
		this.unitAlias = unitAlias;
	}
	public String getRetrieveCode() {
		return retrieveCode;
	}
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}
}
