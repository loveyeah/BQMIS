package power.ejb.productiontec.relayProtection.form;

public class TypeForm implements java.io.Serializable
{
	// 继电保护类型编号
	private String protectTypeId;
	// 继电保护类型名称
	private String protectTypeName;
	// 类型选择
	private String chooseFlag;
	public String getProtectTypeId() {
		return protectTypeId;
	}
	public void setProtectTypeId(String protectTypeId) {
		this.protectTypeId = protectTypeId;
	}
	public String getProtectTypeName() {
		return protectTypeName;
	}
	public void setProtectTypeName(String protectTypeName) {
		this.protectTypeName = protectTypeName;
	}
	public String getChooseFlag() {
		return chooseFlag;
	}
	public void setChooseFlag(String chooseFlag) {
		this.chooseFlag = chooseFlag;
	}
	
}