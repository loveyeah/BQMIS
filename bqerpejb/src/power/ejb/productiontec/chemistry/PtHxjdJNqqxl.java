package power.ejb.productiontec.chemistry;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtHxjdJNqqxl entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_HXJD_J_NQQXL")
public class PtHxjdJNqqxl implements java.io.Serializable {

	// Fields

	private Long nqjxlId;
	private String deviceCode;
	private String waterQuanlity;
	private String place;
	private Date startDate;
	private Date endDate;
	private String content;
	private String handleStep;
	private String handleResult;
	private String memo;
	private String fillBy;
	private Date fillDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtHxjdJNqqxl() {
	}

	/** minimal constructor */
	public PtHxjdJNqqxl(Long nqjxlId) {
		this.nqjxlId = nqjxlId;
	}

	/** full constructor */
	public PtHxjdJNqqxl(Long nqjxlId, String deviceCode, String waterQuanlity,
			String place, Date startDate, Date endDate, String content,
			String handleStep, String handleResult, String memo, String fillBy,
			Date fillDate, String enterpriseCode) {
		this.nqjxlId = nqjxlId;
		this.deviceCode = deviceCode;
		this.waterQuanlity = waterQuanlity;
		this.place = place;
		this.startDate = startDate;
		this.endDate = endDate;
		this.content = content;
		this.handleStep = handleStep;
		this.handleResult = handleResult;
		this.memo = memo;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "NQJXL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getNqjxlId() {
		return this.nqjxlId;
	}

	public void setNqjxlId(Long nqjxlId) {
		this.nqjxlId = nqjxlId;
	}

	@Column(name = "DEVICE_CODE", length = 2)
	public String getDeviceCode() {
		return this.deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	@Column(name = "WATER_QUANLITY", length = 30)
	public String getWaterQuanlity() {
		return this.waterQuanlity;
	}

	public void setWaterQuanlity(String waterQuanlity) {
		this.waterQuanlity = waterQuanlity;
	}

	@Column(name = "PLACE", length = 200)
	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "CONTENT", length = 256)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "HANDLE_STEP", length = 500)
	public String getHandleStep() {
		return this.handleStep;
	}

	public void setHandleStep(String handleStep) {
		this.handleStep = handleStep;
	}

	@Column(name = "HANDLE_RESULT", length = 500)
	public String getHandleResult() {
		return this.handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILL_DATE", length = 7)
	public Date getFillDate() {
		return this.fillDate;
	}

	public void setFillDate(Date fillDate) {
		this.fillDate = fillDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}