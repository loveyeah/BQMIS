package power.ejb.productiontec.chemistry;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtHxjdJZxybybzb entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_HXJD_J_ZXYBYBZB")
public class PtHxjdJZxybybzb implements java.io.Serializable {

	// Fields

	private Long zxybybzbId;
	private String deviceCode;
	private Date reportTime;
	private String memo;
	private String fillBy;
	private Date fillDate;
	private Long workFlowNo;
	private Long workFlowStatus;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtHxjdJZxybybzb() {
	}

	/** minimal constructor */
	public PtHxjdJZxybybzb(Long zxybybzbId) {
		this.zxybybzbId = zxybybzbId;
	}

	/** full constructor */
	public PtHxjdJZxybybzb(Long zxybybzbId, String deviceCode, Date reportTime,
			String memo, String fillBy, Date fillDate, Long workFlowNo,
			Long workFlowStatus, String enterpriseCode) {
		this.zxybybzbId = zxybybzbId;
		this.deviceCode = deviceCode;
		this.reportTime = reportTime;
		this.memo = memo;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.workFlowNo = workFlowNo;
		this.workFlowStatus = workFlowStatus;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ZXYBYBZB_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getZxybybzbId() {
		return this.zxybybzbId;
	}

	public void setZxybybzbId(Long zxybybzbId) {
		this.zxybybzbId = zxybybzbId;
	}

	@Column(name = "DEVICE_CODE", length = 2)
	public String getDeviceCode() {
		return this.deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_TIME", length = 7)
	public Date getReportTime() {
		return this.reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "FILL_DATE", length = 7)
	public Date getFillDate() {
		return this.fillDate;
	}

	public void setFillDate(Date fillDate) {
		this.fillDate = fillDate;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WORK_FLOW_STATUS", precision = 1, scale = 0)
	public Long getWorkFlowStatus() {
		return this.workFlowStatus;
	}

	public void setWorkFlowStatus(Long workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}