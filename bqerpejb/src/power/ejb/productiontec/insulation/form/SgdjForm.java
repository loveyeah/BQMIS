package power.ejb.productiontec.insulation.form;

@SuppressWarnings("serial")
public class SgdjForm implements java.io.Serializable{
	
	private Long jysgId;
	private String accidentTitle;
	private String equCode;
	private String equName;
	private String happenDate;
	private String handleDate;
	private String reasonAnalyse;
	private String handleStatus;
	private String memo;
	private String annex;
	private String fillByName;
	private String fillDate;
	
	public Long getJysgId() {
		return jysgId;
	}
	public void setJysgId(Long jysgId) {
		this.jysgId = jysgId;
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
	public String getHappenDate() {
		return happenDate;
	}
	public void setHappenDate(String happenDate) {
		this.happenDate = happenDate;
	}
	public String getHandleDate() {
		return handleDate;
	}
	public void setHandleDate(String handleDate) {
		this.handleDate = handleDate;
	}
	public String getReasonAnalyse() {
		return reasonAnalyse;
	}
	public void setReasonAnalyse(String reasonAnalyse) {
		this.reasonAnalyse = reasonAnalyse;
	}
	public String getHandleStatus() {
		return handleStatus;
	}
	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
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
	public String getFillByName() {
		return fillByName;
	}
	public void setFillByName(String fillByName) {
		this.fillByName = fillByName;
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
