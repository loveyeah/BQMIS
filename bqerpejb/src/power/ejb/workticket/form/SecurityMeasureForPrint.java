package power.ejb.workticket.form;

@SuppressWarnings("serial")
public class SecurityMeasureForPrint implements java.io.Serializable{
	 private String workticketNo;
	 private String safetyCode;
	 private String safetyDesc;
     private String safetyContent;
     private String safetyExeContent;
     private String markCard;
     private String cardCount;
     private String safetyExeBy;
	public String getSafetyCode() {
		return safetyCode;
	}
	public void setSafetyCode(String safetyCode) {
		this.safetyCode = safetyCode;
	}
	public String getSafetyDesc() {
		return safetyDesc;
	}
	public void setSafetyDesc(String safetyDesc) {
		this.safetyDesc = safetyDesc;
	}
	public String getSafetyContent() {
		return safetyContent;
	}
	public void setSafetyContent(String safetyContent) {
		this.safetyContent = safetyContent;
	}
	public String getSafetyExeContent() {
		return safetyExeContent;
	}
	public void setSafetyExeContent(String safetyExeContent) {
		this.safetyExeContent = safetyExeContent;
	}
	public String getMarkCard() {
		return markCard;
	}
	public void setMarkCard(String markCard) {
		this.markCard = markCard;
	}
	public String getCardCount() {
		return cardCount;
	}
	public void setCardCount(String cardCount) {
		this.cardCount = cardCount;
	}
	public String getWorkticketNo() {
		return workticketNo;
	}
	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}
	public String getSafetyExeBy() {
		return safetyExeBy;
	}
	public void setSafetyExeBy(String safetyExeBy) {
		this.safetyExeBy = safetyExeBy;
	}
}
