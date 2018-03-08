package power.ejb.equ.workbill;

@SuppressWarnings("serial")
public class EquJStandardServiceAdd implements java.io.Serializable{
	private EquJStandardService baseInfo;
	private String servName;
	private String factServName;
	
	public EquJStandardService getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(EquJStandardService baseInfo) {
		this.baseInfo = baseInfo;
	}
	public String getServName() {
		return servName;
	}
	public void setServName(String servName) {
		this.servName = servName;
	}
	public String getFactServName() {
		return factServName;
	}
	public void setFactServName(String factServName) {
		this.factServName = factServName;
	}


}
