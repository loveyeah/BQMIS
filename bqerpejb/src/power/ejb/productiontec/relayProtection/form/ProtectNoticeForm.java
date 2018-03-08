package power.ejb.productiontec.relayProtection.form;

import power.ejb.productiontec.relayProtection.PtJdbhJDztzd;

@SuppressWarnings("serial")
public class ProtectNoticeForm implements java.io.Serializable{
	
	private String effectiveDate;
	private String fillName;
	private String saveName;
	private String equName;
	private String jssmName;
	private String protectedDevice;
	
	private Long dzdjbId;
	private Long fixvalueItemId;
	private Long protectTypeId;
	private String protectTypeName;
	private String fixvalueItemName;
	private String wholeFixvalue;
	private String memo;
	private String enterpriseCode;
	
	private PtJdbhJDztzd notice;
	
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getFillName() {
		return fillName;
	}
	public void setFillName(String fillName) {
		this.fillName = fillName;
	}
	public String getSaveName() {
		return saveName;
	}
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public PtJdbhJDztzd getNotice() {
		return notice;
	}
	public void setNotice(PtJdbhJDztzd notice) {
		this.notice = notice;
	}
	public String getJssmName() {
		return jssmName;
	}
	public void setJssmName(String jssmName) {
		this.jssmName = jssmName;
	}
	public Long getDzdjbId() {
		return dzdjbId;
	}
	public void setDzdjbId(Long dzdjbId) {
		this.dzdjbId = dzdjbId;
	}
	public Long getFixvalueItemId() {
		return fixvalueItemId;
	}
	public void setFixvalueItemId(Long fixvalueItemId) {
		this.fixvalueItemId = fixvalueItemId;
	}
	public Long getProtectTypeId() {
		return protectTypeId;
	}
	public void setProtectTypeId(Long protectTypeId) {
		this.protectTypeId = protectTypeId;
	}
	public String getProtectTypeName() {
		return protectTypeName;
	}
	public void setProtectTypeName(String protectTypeName) {
		this.protectTypeName = protectTypeName;
	}
	public String getFixvalueItemName() {
		return fixvalueItemName;
	}
	public void setFixvalueItemName(String fixvalueItemName) {
		this.fixvalueItemName = fixvalueItemName;
	}
	public String getWholeFixvalue() {
		return wholeFixvalue;
	}
	public void setWholeFixvalue(String wholeFixvalue) {
		this.wholeFixvalue = wholeFixvalue;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getProtectedDevice() {
		return protectedDevice;
	}
	public void setProtectedDevice(String protectedDevice) {
		this.protectedDevice = protectedDevice;
	}
}
