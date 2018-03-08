package power.ejb.productiontec.relayProtection.form;

@SuppressWarnings("serial")
public class PtCLbyxmdyForm implements java.io.Serializable{
	private Long sylbId;
	private Long syxmId;
	private String enterpriseCode;
	private String sylbName;
	private String syxmName;
	private String flag;
	public Long getSylbId() {
		return sylbId;
	}
	public void setSylbId(Long sylbId) {
		this.sylbId = sylbId;
	}
	public Long getSyxmId() {
		return syxmId;
	}
	public void setSyxmId(Long syxmId) {
		this.syxmId = syxmId;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getSylbName() {
		return sylbName;
	}
	public void setSylbName(String sylbName) {
		this.sylbName = sylbName;
	}
	public String getSyxmName() {
		return syxmName;
	}
	public void setSyxmName(String syxmName) {
		this.syxmName = syxmName;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
