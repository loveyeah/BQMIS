package power.ejb.manage.client.form;

public class ConJClientsQualificationForm {

	/**资质登记ID */
	private Long qualificationId;
	/**合作伙伴ID */
	private Long cliendId;
	/** 资质材料名称*/
	private String aptitudeName;
	/** 发证机构*/
	private String qualificationOrg;
	/** 发证日期*/
	private String sendPaperDate;
	/**证书生效日期 */
	private String beginDate;
	/**证书失效日期 */
	private String endDate;
	/** 备注*/
	private String memo;
	/**上次修改人名称 */
	private String lastModifiedName;
	/**上次修改日期 */
	private String lastModifiedDate;
	/** 合作伙伴名称*/
	private String clientName;
	/** 上次修改人编码*/
	private String lastModifiedBy;
	
	
	public Long getQualificationId() {
		return qualificationId;
	}
	public void setQualificationId(Long qualificationId) {
		this.qualificationId = qualificationId;
	}
	public Long getCliendId() {
		return cliendId;
	}
	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}
	public String getAptitudeName() {
		return aptitudeName;
	}
	public void setAptitudeName(String aptitudeName) {
		this.aptitudeName = aptitudeName;
	}
	public String getQualificationOrg() {
		return qualificationOrg;
	}
	public void setQualificationOrg(String qualificationOrg) {
		this.qualificationOrg = qualificationOrg;
	}
	public String getSendPaperDate() {
		return sendPaperDate;
	}
	public void setSendPaperDate(String sendPaperDate) {
		this.sendPaperDate = sendPaperDate;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getLastModifiedName() {
		return lastModifiedName;
	}
	public void setLastModifiedName(String lastModifiedName) {
		this.lastModifiedName = lastModifiedName;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
