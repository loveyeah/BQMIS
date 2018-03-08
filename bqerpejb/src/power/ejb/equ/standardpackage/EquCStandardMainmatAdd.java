package power.ejb.equ.standardpackage;

@SuppressWarnings("serial")
public class EquCStandardMainmatAdd implements java.io.Serializable{
	private EquCStandardMainmat baseInfo;
	private String matName;
	public EquCStandardMainmat getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(EquCStandardMainmat baseInfo) {
		this.baseInfo = baseInfo;
	}
	public String getMatName() {
		return matName;
	}
	public void setMatName(String matName) {
		this.matName = matName;
	}

}
