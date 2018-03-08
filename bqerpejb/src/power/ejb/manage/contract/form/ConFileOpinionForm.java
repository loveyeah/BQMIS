package power.ejb.manage.contract.form;
import java.util.Date;

public class ConFileOpinionForm implements java.io.Serializable{
	//意见id
	private Long opinionId;
	//合同id/合同变更id
	private Long keyId;
	//归档单据类型
	private String fileType;
	//已经内容
	private String opinion;
	//归档人
	private String gdWorker;
	//归档人姓名
	private String gdWorkerName;
	//退回时间
	private String withdrawalTime;
	public Long getOpinionId() {
		return opinionId;
	}
	public void setOpinionId(Long opinionId) {
		this.opinionId = opinionId;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getGdWorker() {
		return gdWorker;
	}
	public void setGdWorker(String gdWorker) {
		this.gdWorker = gdWorker;
	}
	public String getGdWorkerName() {
		return gdWorkerName;
	}
	public void setGdWorkerName(String gdWorkerName) {
		this.gdWorkerName = gdWorkerName;
	}
	public String getWithdrawalTime() {
		return withdrawalTime;
	}
	public void setWithdrawalTime(String withdrawalTime) {
		this.withdrawalTime = withdrawalTime;
	}
	public Long getKeyId() {
		return keyId;
	}
	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}

}
