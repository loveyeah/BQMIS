package power.ejb.manage.client.form;

@SuppressWarnings("serial")
public class ConJAppraiseRecordForm implements java.io.Serializable{

	private Long recordId;
	private Long eventId;
	private Long intervalId;
	private Long cliendId;
	private Double appraisePoint;
	private String memo;
	private String enterpriseCode;
	private String  eventName;
	private String gatherFlag;
	private String clientName;
	private Long appraiseMark;
	private String appraiseCriterion;
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public Long getIntervalId() {
		return intervalId;
	}
	public void setIntervalId(Long intervalId) {
		this.intervalId = intervalId;
	}
	public Long getCliendId() {
		return cliendId;
	}
	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}
	public Double getAppraisePoint() {
		return appraisePoint;
	}
	public void setAppraisePoint(Double appraisePoint) {
		this.appraisePoint = appraisePoint;
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
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getGatherFlag() {
		return gatherFlag;
	}
	public void setGatherFlag(String gatherFlag) {
		this.gatherFlag = gatherFlag;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public Long getAppraiseMark() {
		return appraiseMark;
	}
	public void setAppraiseMark(Long appraiseMark) {
		this.appraiseMark = appraiseMark;
	}
	public String getAppraiseCriterion() {
		return appraiseCriterion;
	}
	public void setAppraiseCriterion(String appraiseCriterion) {
		this.appraiseCriterion = appraiseCriterion;
	}
}
