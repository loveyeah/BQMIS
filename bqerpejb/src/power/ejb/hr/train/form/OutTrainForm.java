package power.ejb.hr.train.form;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
public class OutTrainForm implements java.io.Serializable {

	private String trainId;
	private String workCode;
	private String workerName;
	private String sex;
	private String deptCode;
	private String deptName;
	private String stationId;
	private String technologyTitlesTypeId;
	private String nowStationId;
	private String trainSource;
	private String trainSite;
	private String trainContent;
	private String trainStartdate;
	private String trainEnddate;
	private String trainCharacter;
	private String certificateSort;
	private String certificateNo;
	private String trainTotalFee;
	private String enterpriseCode;
	private String isUse;
	private String certificateName;
	private String certificateStartTime;
	private String certificateEndTime;
	private String trainInsititution ;//add by wpzhu 20100702
	private String isReceived ;//add by wpzhu 20100702
	public String getTrainInsititution() {
		return trainInsititution;
	}
	public void setTrainInsititution(String trainInsititution) {
		this.trainInsititution = trainInsititution;
	}
	public String getIsReceived() {
		return isReceived;
	}
	public void setIsReceived(String isReceived) {
		this.isReceived = isReceived;
	}
	public String getTrainId() {
		return trainId;
	}
	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}
	public String getWorkCode() {
		return workCode;
	}
	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}
	
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public String getTechnologyTitlesTypeId() {
		return technologyTitlesTypeId;
	}
	public void setTechnologyTitlesTypeId(String technologyTitlesTypeId) {
		this.technologyTitlesTypeId = technologyTitlesTypeId;
	}
	public String getNowStationId() {
		return nowStationId;
	}
	public void setNowStationId(String nowStationId) {
		this.nowStationId = nowStationId;
	}
	public String getTrainSource() {
		return trainSource;
	}
	public void setTrainSource(String trainSource) {
		this.trainSource = trainSource;
	}
	public String getTrainSite() {
		return trainSite;
	}
	public void setTrainSite(String trainSite) {
		this.trainSite = trainSite;
	}
	public String getTrainContent() {
		return trainContent;
	}
	public void setTrainContent(String trainContent) {
		this.trainContent = trainContent;
	}
	public String getTrainStartdate() {
		return trainStartdate;
	}
	public void setTrainStartdate(String trainStartdate) {
		this.trainStartdate = trainStartdate;
	}
	public String getTrainEnddate() {
		return trainEnddate;
	}
	public void setTrainEnddate(String trainEnddate) {
		this.trainEnddate = trainEnddate;
	}
	public String getTrainCharacter() {
		return trainCharacter;
	}
	public void setTrainCharacter(String trainCharacter) {
		this.trainCharacter = trainCharacter;
	}
	public String getCertificateSort() {
		return certificateSort;
	}
	public void setCertificateSort(String certificateSort) {
		this.certificateSort = certificateSort;
	}
	public String getCertificateNo() {
		return certificateNo;
	}
	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}
	public String getTrainTotalFee() {
		return trainTotalFee;
	}
	public void setTrainTotalFee(String trainTotalFee) {
		this.trainTotalFee = trainTotalFee;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getCertificateName() {
		return certificateName;
	}
	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}
	public String getCertificateStartTime() {
		return certificateStartTime;
	}
	public void setCertificateStartTime(String certificateStartTime) {
		this.certificateStartTime = certificateStartTime;
	}
	public String getCertificateEndTime() {
		return certificateEndTime;
	}
	public void setCertificateEndTime(String certificateEndTime) {
		this.certificateEndTime = certificateEndTime;
	}
	
	

}