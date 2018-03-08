package power.ejb.workticket.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJWorkticketMeasureData entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_WORKTICKET_MEASURE_DATA")
public class RunJWorkticketMeasureData implements java.io.Serializable {

	// Fields

	private Long measureDataId;
	private String workticketNo;
	private String measureData;
	private String measureMan;
	private Date measureDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunJWorkticketMeasureData() {
	}

	/** minimal constructor */
	public RunJWorkticketMeasureData(Long measureDataId) {
		this.measureDataId = measureDataId;
	}

	/** full constructor */
	public RunJWorkticketMeasureData(Long measureDataId, String workticketNo,
			String measureData, String measureMan, Date measureDate,
			String enterpriseCode, String isUse) {
		this.measureDataId = measureDataId;
		this.workticketNo = workticketNo;
		this.measureData = measureData;
		this.measureMan = measureMan;
		this.measureDate = measureDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "MEASURE_DATA_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMeasureDataId() {
		return this.measureDataId;
	}

	public void setMeasureDataId(Long measureDataId) {
		this.measureDataId = measureDataId;
	}

	@Column(name = "WORKTICKET_NO", length = 22)
	public String getWorkticketNo() {
		return this.workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	@Column(name = "MEASURE_DATA", length = 30)
	public String getMeasureData() {
		return this.measureData;
	}

	public void setMeasureData(String measureData) {
		this.measureData = measureData;
	}

	@Column(name = "MEASURE_MAN", length = 30)
	public String getMeasureMan() {
		return this.measureMan;
	}

	public void setMeasureMan(String measureMan) {
		this.measureMan = measureMan;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MEASURE_DATE", length = 7)
	public Date getMeasureDate() {
		return this.measureDate;
	}

	public void setMeasureDate(Date measureDate) {
		this.measureDate = measureDate;
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

}