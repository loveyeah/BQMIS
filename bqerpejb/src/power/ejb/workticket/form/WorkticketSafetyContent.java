package power.ejb.workticket.form;



@SuppressWarnings("serial")
public class WorkticketSafetyContent implements java.io.Serializable{

	private Long id;
	private String workticketNo;
	private String safetyCode;
	private Long line;
	private String safetyContent;
	private String safetyExeContent;
	private Long markcardTypeId;
	private String safetyDesc;
	private String safetyType;


	public String getSafetyDesc() {
		return safetyDesc;
	}
	public void setSafetyDesc(String safetyDesc) {
		this.safetyDesc = safetyDesc;
	}

	public Long getMarkcardTypeId() {
		return markcardTypeId;
	}
	public void setMarkcardTypeId(Long markcardTypeId) {
		this.markcardTypeId = markcardTypeId;
	}
	public String getSafetyType() {
		return safetyType;
	}
	public void setSafetyType(String safetyType) {
		this.safetyType = safetyType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWorkticketNo() {
		return workticketNo;
	}
	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}
	public String getSafetyCode() {
		return safetyCode;
	}
	public void setSafetyCode(String safetyCode) {
		this.safetyCode = safetyCode;
	}
	public Long getLine() {
		return line;
	}
	public void setLine(Long line) {
		this.line = line;
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
}
