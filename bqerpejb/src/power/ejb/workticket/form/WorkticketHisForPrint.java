package power.ejb.workticket.form;

@SuppressWarnings("serial")
public class WorkticketHisForPrint implements java.io.Serializable{
	private String workticketNo;
	private String approveMan;
	private String approveDate;
	private String approveText;
	private String approveStatusId;
	private String approveStatusName;
	private String oldChargeBy;
	private String newChargeBy;
	private String oldApprovedFinishDate;
	private String newApprovedFinishDate;
	private String dutyChargeBy;//add
	private String fireBy;      //add
	private String  totalLine;//add TOTAL_LINE
	private String nobackoutLine;  //add  NOBACKOUT_LINE
    private String nobackoutNum;  // add  NOBACKOUT_NUM
    private String backoutLine;
    
    //add by fyyang 090309 for returnback
    private String id;
    private String oldChargeByName;
    private String newChargeByName;
    private String changeStatus;

	/**
	 * @return the dutyChargeBy
	 */
	public String getDutyChargeBy() {
		return dutyChargeBy;
	}
	/**
	 * @param dutyChargeBy the dutyChargeBy to set
	 */
	public void setDutyChargeBy(String dutyChargeBy) {
		this.dutyChargeBy = dutyChargeBy;
	}
	/**
	 * @return the fireBy
	 */
	public String getFireBy() {
		return fireBy;
	}
	/**
	 * @param fireBy the fireBy to set
	 */
	public void setFireBy(String fireBy) {
		this.fireBy = fireBy;
	}
	public String getApproveMan() {
		return approveMan;
	}
	public void setApproveMan(String approveMan) {
		this.approveMan = approveMan;
	}
	public String getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}
	public String getApproveText() {
		return approveText;
	}
	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}
	public String getApproveStatusId() {
		return approveStatusId;
	}
	public void setApproveStatusId(String approveStatusId) {
		this.approveStatusId = approveStatusId;
	}
	public String getApproveStatusName() {
		return approveStatusName;
	}
	public void setApproveStatusName(String approveStatusName) {
		this.approveStatusName = approveStatusName;
	}
	public String getWorkticketNo() {
		return workticketNo;
	}
	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}
	public String getOldChargeBy() {
		return oldChargeBy;
	}
	public void setOldChargeBy(String oldChargeBy) {
		this.oldChargeBy = oldChargeBy;
	}
	public String getNewChargeBy() {
		return newChargeBy;
	}
	public void setNewChargeBy(String newChargeBy) {
		this.newChargeBy = newChargeBy;
	}
	public String getOldApprovedFinishDate() {
		return oldApprovedFinishDate;
	}
	public void setOldApprovedFinishDate(String oldApprovedFinishDate) {
		this.oldApprovedFinishDate = oldApprovedFinishDate;
	}
	public String getNewApprovedFinishDate() {
		return newApprovedFinishDate;
	}
	public void setNewApprovedFinishDate(String newApprovedFinishDate) {
		this.newApprovedFinishDate = newApprovedFinishDate;
	}
	public String getTotalLine() {
		return totalLine;
	}
	public void setTotalLine(String totalLine) {
		this.totalLine = totalLine;
	}
	public String getNobackoutLine() {
		return nobackoutLine;
	}
	public void setNobackoutLine(String nobackoutLine) {
		this.nobackoutLine = nobackoutLine;
	}
	public String getNobackoutNum() {
		return nobackoutNum;
	}
	public void setNobackoutNum(String nobackoutNum) {
		this.nobackoutNum = nobackoutNum;
	}
	public String getBackoutLine() {
		return backoutLine;
	}
	public void setBackoutLine(String backoutLine) {
		this.backoutLine = backoutLine;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOldChargeByName() {
		return oldChargeByName;
	}
	public void setOldChargeByName(String oldChargeByName) {
		this.oldChargeByName = oldChargeByName;
	}
	public String getNewChargeByName() {
		return newChargeByName;
	}
	public void setNewChargeByName(String newChargeByName) {
		this.newChargeByName = newChargeByName;
	}
	public String getChangeStatus() {
		return changeStatus;
	}
	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}

	

	





}
