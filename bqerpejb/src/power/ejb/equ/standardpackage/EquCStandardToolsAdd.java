package power.ejb.equ.standardpackage;

@SuppressWarnings("serial")
public class EquCStandardToolsAdd implements java.io.Serializable{
	private EquCStandardTools baseInfo;
	private String toolsName;
	public EquCStandardTools getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(EquCStandardTools baseInfo) {
		this.baseInfo = baseInfo;
	}
	public String getToolsName() {
		return toolsName;
	}
	public void setToolsName(String toolsName) {
		this.toolsName = toolsName;
	}
	

}
