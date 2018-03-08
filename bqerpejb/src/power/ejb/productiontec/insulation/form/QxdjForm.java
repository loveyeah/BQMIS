package power.ejb.productiontec.insulation.form;

@SuppressWarnings("serial")
public class QxdjForm implements java.io.Serializable{
	private Long jyqxId;
	private String accidentTitle;
	private String equCode;
	private String equName;
	private String findTime;
	private String clearTime;
	private String reasonAnalyse;
	private String bugStatus;
	private String memo;
	private String annex;
	private String fillBy;
	private String fillName;
	private String fillDate;
	
	public Long getJyqxId() {
		return jyqxId;
	}
	public void setJyqxId(Long jyqxId) {
		this.jyqxId = jyqxId;
	}
	public String getAccidentTitle() {
		return accidentTitle;
	}
	public void setAccidentTitle(String accidentTitle) {
		this.accidentTitle = accidentTitle;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public String getFindTime() {
		return findTime;
	}
	public void setFindTime(String findTime) {
		this.findTime = findTime;
	}
	public String getClearTime() {
		return clearTime;
	}
	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
	}
	public String getReasonAnalyse() {
		return reasonAnalyse;
	}
	public void setReasonAnalyse(String reasonAnalyse) {
		this.reasonAnalyse = reasonAnalyse;
	}
	public String getBugStatus() {
		return bugStatus;
	}
	public void setBugStatus(String bugStatus) {
		this.bugStatus = bugStatus;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getAnnex() {
		return annex;
	}
	public void setAnnex(String annex) {
		this.annex = annex;
	}
	public String getFillBy() {
		return fillBy;
	}
	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}
	public String getFillName() {
		return fillName;
	}
	public void setFillName(String fillName) {
		this.fillName = fillName;
	}
	public String getFillDate() {
		return fillDate;
	}
	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
}
