package power.ejb.hr.labor;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJLaborTempeDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_LABOR_TEMPE_DETAIL")
public class HrJLaborTempeDetail implements java.io.Serializable {

	// Fields

	private Long tempeDetailId;
	private Long tempeId;
	private String deptName;
	private Long factNum;
	private Long highTempeNum;
	private Double highTempeStandard;
	private Long midTempeNum;
	private Double midTempeStandard;
	private Long lowTempeNum;
	private Double lowTempeStandard;
	private String memo;
	private String modifyBy;
	private Date modifyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJLaborTempeDetail() {
	}

	/** minimal constructor */
	public HrJLaborTempeDetail(Long tempeDetailId) {
		this.tempeDetailId = tempeDetailId;
	}

	/** full constructor */
	public HrJLaborTempeDetail(Long tempeDetailId, Long tempeId,
			String deptName, Long factNum, Long highTempeNum,
			Double highTempeStandard, Long midTempeNum,
			Double midTempeStandard, Long lowTempeNum, Double lowTempeStandard,
			String memo, String modifyBy, Date modifyDate, String isUse,
			String enterpriseCode) {
		this.tempeDetailId = tempeDetailId;
		this.tempeId = tempeId;
		this.deptName = deptName;
		this.factNum = factNum;
		this.highTempeNum = highTempeNum;
		this.highTempeStandard = highTempeStandard;
		this.midTempeNum = midTempeNum;
		this.midTempeStandard = midTempeStandard;
		this.lowTempeNum = lowTempeNum;
		this.lowTempeStandard = lowTempeStandard;
		this.memo = memo;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TEMPE_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTempeDetailId() {
		return this.tempeDetailId;
	}

	public void setTempeDetailId(Long tempeDetailId) {
		this.tempeDetailId = tempeDetailId;
	}

	@Column(name = "TEMPE_ID", precision = 10, scale = 0)
	public Long getTempeId() {
		return this.tempeId;
	}

	public void setTempeId(Long tempeId) {
		this.tempeId = tempeId;
	}

	@Column(name = "DEPT_NAME", length = 20)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "FACT_NUM", precision = 10, scale = 0)
	public Long getFactNum() {
		return this.factNum;
	}

	public void setFactNum(Long factNum) {
		this.factNum = factNum;
	}

	@Column(name = "HIGH_TEMPE_NUM", precision = 10, scale = 0)
	public Long getHighTempeNum() {
		return this.highTempeNum;
	}

	public void setHighTempeNum(Long highTempeNum) {
		this.highTempeNum = highTempeNum;
	}

	@Column(name = "HIGH_TEMPE_STANDARD", precision = 10)
	public Double getHighTempeStandard() {
		return this.highTempeStandard;
	}

	public void setHighTempeStandard(Double highTempeStandard) {
		this.highTempeStandard = highTempeStandard;
	}

	@Column(name = "MID_TEMPE_NUM", precision = 10, scale = 0)
	public Long getMidTempeNum() {
		return this.midTempeNum;
	}

	public void setMidTempeNum(Long midTempeNum) {
		this.midTempeNum = midTempeNum;
	}

	@Column(name = "MID_TEMPE_STANDARD", precision = 10)
	public Double getMidTempeStandard() {
		return this.midTempeStandard;
	}

	public void setMidTempeStandard(Double midTempeStandard) {
		this.midTempeStandard = midTempeStandard;
	}

	@Column(name = "LOW_TEMPE_NUM", precision = 10, scale = 0)
	public Long getLowTempeNum() {
		return this.lowTempeNum;
	}

	public void setLowTempeNum(Long lowTempeNum) {
		this.lowTempeNum = lowTempeNum;
	}

	@Column(name = "LOW_TEMPE_STANDARD", precision = 10)
	public Double getLowTempeStandard() {
		return this.lowTempeStandard;
	}

	public void setLowTempeStandard(Double lowTempeStandard) {
		this.lowTempeStandard = lowTempeStandard;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}