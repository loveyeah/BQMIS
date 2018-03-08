package power.ejb.hr.train;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJOuttrain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_OUTTRAIN")
public class HrJOuttrain implements java.io.Serializable {

	// Fields

	private Long trainId;
	private String workCode;
	private String sex;
	private String deptCode;
	private String stationId;
	private String technologyTitlesTypeId;
	private String nowStationId;
	private String trainSource;
	private String trainSite;
	private String trainContent;
	private Date trainStartdate;
	private String trainCharacter;
	private String certificateSort;
	private String certificateNo;
	private Double trainTotalFee;
	private String enterpriseCode;
	private String isUse;
	private Date trainEnddate;
	private Date certificateStartTime;
	private Date certificateEndTime;
	private String trainInsititution;
	private String isReceived;

	// Constructors

	/** default constructor */
	public HrJOuttrain() {
	}

	/** minimal constructor */
	public HrJOuttrain(Long trainId) {
		this.trainId = trainId;
	}

	/** full constructor */
	public HrJOuttrain(Long trainId, String workCode, String sex,
			String deptCode, String stationId, String technologyTitlesTypeId,
			String nowStationId, String trainSource, String trainSite,
			String trainContent, Date trainStartdate, String trainCharacter,
			String certificateSort, String certificateNo, Double trainTotalFee,
			String enterpriseCode, String isUse, Date trainEnddate,
			Date certificateStartTime, Date certificateEndTime,
			String trainInsititution, String isReceived) {
		this.trainId = trainId;
		this.workCode = workCode;
		this.sex = sex;
		this.deptCode = deptCode;
		this.stationId = stationId;
		this.technologyTitlesTypeId = technologyTitlesTypeId;
		this.nowStationId = nowStationId;
		this.trainSource = trainSource;
		this.trainSite = trainSite;
		this.trainContent = trainContent;
		this.trainStartdate = trainStartdate;
		this.trainCharacter = trainCharacter;
		this.certificateSort = certificateSort;
		this.certificateNo = certificateNo;
		this.trainTotalFee = trainTotalFee;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.trainEnddate = trainEnddate;
		this.certificateStartTime = certificateStartTime;
		this.certificateEndTime = certificateEndTime;
		this.trainInsititution = trainInsititution;
		this.isReceived = isReceived;
	}

	// Property accessors
	@Id
	@Column(name = "TRAIN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTrainId() {
		return this.trainId;
	}

	public void setTrainId(Long trainId) {
		this.trainId = trainId;
	}

	@Column(name = "WORK_CODE", length = 30)
	public String getWorkCode() {
		return this.workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	@Column(name = "SEX", length = 1)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "DEPT_CODE", length = 20)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "STATION_ID", length = 50)
	public String getStationId() {
		return this.stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	@Column(name = "TECHNOLOGY_TITLES_TYPE_ID", length = 50)
	public String getTechnologyTitlesTypeId() {
		return this.technologyTitlesTypeId;
	}

	public void setTechnologyTitlesTypeId(String technologyTitlesTypeId) {
		this.technologyTitlesTypeId = technologyTitlesTypeId;
	}

	@Column(name = "NOW_STATION_ID", length = 50)
	public String getNowStationId() {
		return this.nowStationId;
	}

	public void setNowStationId(String nowStationId) {
		this.nowStationId = nowStationId;
	}

	@Column(name = "TRAIN_SOURCE", length = 50)
	public String getTrainSource() {
		return this.trainSource;
	}

	public void setTrainSource(String trainSource) {
		this.trainSource = trainSource;
	}

	@Column(name = "TRAIN_SITE", length = 50)
	public String getTrainSite() {
		return this.trainSite;
	}

	public void setTrainSite(String trainSite) {
		this.trainSite = trainSite;
	}

	@Column(name = "TRAIN_CONTENT", length = 200)
	public String getTrainContent() {
		return this.trainContent;
	}

	public void setTrainContent(String trainContent) {
		this.trainContent = trainContent;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TRAIN_STARTDATE", length = 7)
	public Date getTrainStartdate() {
		return this.trainStartdate;
	}

	public void setTrainStartdate(Date trainStartdate) {
		this.trainStartdate = trainStartdate;
	}

	@Column(name = "TRAIN_CHARACTER", length = 1)
	public String getTrainCharacter() {
		return this.trainCharacter;
	}

	public void setTrainCharacter(String trainCharacter) {
		this.trainCharacter = trainCharacter;
	}

	@Column(name = "CERTIFICATE_SORT",precision = 10, scale = 0 )
	public String getCertificateSort() {
		return this.certificateSort;
	}

	public void setCertificateSort(String certificateSort) {
		this.certificateSort = certificateSort;
	}

	@Column(name = "CERTIFICATE_NO", length = 20)
	public String getCertificateNo() {
		return this.certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	@Column(name = "TRAIN_TOTAL_FEE", precision = 15, scale = 4)
	public Double getTrainTotalFee() {
		return this.trainTotalFee;
	}

	public void setTrainTotalFee(Double trainTotalFee) {
		this.trainTotalFee = trainTotalFee;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TRAIN_ENDDATE", length = 7)
	public Date getTrainEnddate() {
		return this.trainEnddate;
	}

	public void setTrainEnddate(Date trainEnddate) {
		this.trainEnddate = trainEnddate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CERTIFICATE_START_TIME", length = 7)
	public Date getCertificateStartTime() {
		return this.certificateStartTime;
	}

	public void setCertificateStartTime(Date certificateStartTime) {
		this.certificateStartTime = certificateStartTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CERTIFICATE_END_TIME", length = 7)
	public Date getCertificateEndTime() {
		return this.certificateEndTime;
	}

	public void setCertificateEndTime(Date certificateEndTime) {
		this.certificateEndTime = certificateEndTime;
	}

	@Column(name = "TRAIN_INSITITUTION", length = 50)
	public String getTrainInsititution() {
		return this.trainInsititution;
	}

	public void setTrainInsititution(String trainInsititution) {
		this.trainInsititution = trainInsititution;
	}

	@Column(name = "IS_RECEIVED", length = 1)
	public String getIsReceived() {
		return this.isReceived;
	}

	public void setIsReceived(String isReceived) {
		this.isReceived = isReceived;
	}

}