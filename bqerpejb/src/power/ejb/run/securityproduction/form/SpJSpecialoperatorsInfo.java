package power.ejb.run.securityproduction.form;



import power.ejb.run.securityproduction.SpJSpecialoperators;

@SuppressWarnings("serial")
public class SpJSpecialoperatorsInfo implements java.io.Serializable{

	private SpJSpecialoperators spj;
	/** 人员姓名*/
	private String workerName;
	/** 体检日期*/
	private String medicalDate;
	/** 证书发放日期*/
	private String offerDate;
	/** 证书有效开始日期*/
	private String offerStartDate;
	/**证书有效结束日期*/
	private String offerEndDate;
	public SpJSpecialoperators getSpj() {
		return spj;
	}
	public void setSpj(SpJSpecialoperators spj) {
		this.spj = spj;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getMedicalDate() {
		return medicalDate;
	}
	public void setMedicalDate(String medicalDate) {
		this.medicalDate = medicalDate;
	}
	public String getOfferDate() {
		return offerDate;
	}
	public void setOfferDate(String offerDate) {
		this.offerDate = offerDate;
	}
	public String getOfferStartDate() {
		return offerStartDate;
	}
	public void setOfferStartDate(String offerStartDate) {
		this.offerStartDate = offerStartDate;
	}
	public String getOfferEndDate() {
		return offerEndDate;
	}
	public void setOfferEndDate(String offerEndDate) {
		this.offerEndDate = offerEndDate;
	}
	
}
