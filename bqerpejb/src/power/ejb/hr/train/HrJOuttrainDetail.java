package power.ejb.hr.train;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJOuttrainDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_OUTTRAIN_DETAIL")
public class HrJOuttrainDetail implements java.io.Serializable {

	// Fields

	private Long feeId;
	private Long trainId;
	private Long feeSortId;
	private Double actualFee;
	private String feeDept;
	private String memo;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrJOuttrainDetail() {
	}

	/** minimal constructor */
	public HrJOuttrainDetail(Long feeId) {
		this.feeId = feeId;
	}

	/** full constructor */
	public HrJOuttrainDetail(Long feeId, Long trainId, Long feeSortId,
			Double actualFee, String feeDept, String memo,
			String enterpriseCode, String isUse) {
		this.feeId = feeId;
		this.trainId = trainId;
		this.feeSortId = feeSortId;
		this.actualFee = actualFee;
		this.feeDept = feeDept;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "FEE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFeeId() {
		return this.feeId;
	}

	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}

	@Column(name = "TRAIN_ID", precision = 10, scale = 0)
	public Long getTrainId() {
		return this.trainId;
	}

	public void setTrainId(Long trainId) {
		this.trainId = trainId;
	}

	@Column(name = "FEE_SORT_ID", precision = 10, scale = 0)
	public Long getFeeSortId() {
		return this.feeSortId;
	}

	public void setFeeSortId(Long feeSortId) {
		this.feeSortId = feeSortId;
	}

	@Column(name = "ACTUAL_FEE", precision = 15, scale = 4)
	public Double getActualFee() {
		return this.actualFee;
	}

	public void setActualFee(Double actualFee) {
		this.actualFee = actualFee;
	}

	@Column(name = "FEE_DEPT", length = 20)
	public String getFeeDept() {
		return this.feeDept;
	}

	public void setFeeDept(String feeDept) {
		this.feeDept = feeDept;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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