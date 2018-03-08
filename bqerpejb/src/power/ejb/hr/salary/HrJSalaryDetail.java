package power.ejb.hr.salary;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJSalaryDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_SALARY_DETAIL")
public class HrJSalaryDetail implements java.io.Serializable {

	// Fields

	private Long salaryDetailId;
	private Long salaryId;
	private Long salaryTypeId;
	private Double salaryMoney;
	private String memo;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJSalaryDetail() {
	}

	/** minimal constructor */
	public HrJSalaryDetail(Long salaryDetailId) {
		this.salaryDetailId = salaryDetailId;
	}

	/** full constructor */
	public HrJSalaryDetail(Long salaryDetailId, Long salaryId,
			Long salaryTypeId, Double salaryMoney, String memo,
			String modifyBy, Date modifyDate, String enterpriseCode) {
		this.salaryDetailId = salaryDetailId;
		this.salaryId = salaryId;
		this.salaryTypeId = salaryTypeId;
		this.salaryMoney = salaryMoney;
		this.memo = memo;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SALARY_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSalaryDetailId() {
		return this.salaryDetailId;
	}

	public void setSalaryDetailId(Long salaryDetailId) {
		this.salaryDetailId = salaryDetailId;
	}

	@Column(name = "SALARY_ID", precision = 10, scale = 0)
	public Long getSalaryId() {
		return this.salaryId;
	}

	public void setSalaryId(Long salaryId) {
		this.salaryId = salaryId;
	}

	@Column(name = "SALARY_TYPE_ID", precision = 10, scale = 0)
	public Long getSalaryTypeId() {
		return this.salaryTypeId;
	}

	public void setSalaryTypeId(Long salaryTypeId) {
		this.salaryTypeId = salaryTypeId;
	}

	@Column(name = "SALARY_MONEY", precision = 10)
	public Double getSalaryMoney() {
		return this.salaryMoney;
	}

	public void setSalaryMoney(Double salaryMoney) {
		this.salaryMoney = salaryMoney;
	}

	@Column(name = "MEMO", length = 500)
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}