package power.ejb.equ.workbill;

@SuppressWarnings("serial")
public class EquJToolsAdd implements java.io.Serializable{
	private EquJTools baseInfo;
	private String toolsName;
	private String factToolsName;
	
	public EquJTools getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(EquJTools baseInfo) {
		this.baseInfo = baseInfo;
	}
	public String getToolsName() {
		return toolsName;
	}
	public void setToolsName(String toolsName) {
		this.toolsName = toolsName;
	}
	public String getFactToolsName() {
		return factToolsName;
	}
	public void setFactToolsName(String factToolsName) {
		this.factToolsName = factToolsName;
	}
	

}
