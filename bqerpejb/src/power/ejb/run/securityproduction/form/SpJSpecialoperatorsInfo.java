package power.ejb.run.securityproduction.form;



import power.ejb.run.securityproduction.SpJSpecialoperators;

@SuppressWarnings("serial")
public class SpJSpecialoperatorsInfo implements java.io.Serializable{

	private SpJSpecialoperators spj;
	/** ��Ա����*/
	private String workerName;
	/** �������*/
	private String medicalDate;
	/** ֤�鷢������*/
	private String offerDate;
	/** ֤����Ч��ʼ����*/
	private String offerStartDate;
	/**֤����Ч��������*/
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
