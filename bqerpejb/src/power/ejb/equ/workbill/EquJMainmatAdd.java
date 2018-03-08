package power.ejb.equ.workbill;

@SuppressWarnings("serial")
public class EquJMainmatAdd implements java.io.Serializable{
	private EquJMainmat baseInfo;
	private String matName;
	private String factMatName;
	
	public EquJMainmat getBaseInfo() {
		return baseInfo;
	}
	
	public void setBaseInfo(EquJMainmat baseInfo) {
		this.baseInfo = baseInfo;
	}
	public String getMatName() {
		return matName;
	}
	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getFactMatName() {
		return factMatName;
	}

	public void setFactMatName(String factMatName) {
		this.factMatName = factMatName;
	}



}
