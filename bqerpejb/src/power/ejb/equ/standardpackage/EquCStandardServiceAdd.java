package power.ejb.equ.standardpackage;

@SuppressWarnings("serial")
public class EquCStandardServiceAdd implements java.io.Serializable{
	private EquCStandardService baseInfo;
	private String servName;
	public EquCStandardService getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(EquCStandardService baseInfo) {
		this.baseInfo = baseInfo;
	}
	public String getServName() {
		return servName;
	}
	public void setServName(String servName) {
		this.servName = servName;
	}

}
