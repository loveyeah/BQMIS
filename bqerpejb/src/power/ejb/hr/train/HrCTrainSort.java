package power.ejb.hr.train;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCTrainSort entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_TRAIN_SORT")
public class HrCTrainSort implements java.io.Serializable {

	// Fields

	private Long feeSortId;
	private String feeSortName;
	private Long orderBy;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrCTrainSort() {
	}

	/** minimal constructor */
	public HrCTrainSort(Long feeSortId) {
		this.feeSortId = feeSortId;
	}

	/** full constructor */
	public HrCTrainSort(Long feeSortId, String feeSortName, Long orderBy,
			String enterpriseCode, String isUse) {
		this.feeSortId = feeSortId;
		this.feeSortName = feeSortName;
		this.orderBy = orderBy;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "FEE_SORT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFeeSortId() {
		return this.feeSortId;
	}

	public void setFeeSortId(Long feeSortId) {
		this.feeSortId = feeSortId;
	}

	@Column(name = "FEE_SORT_NAME", length = 20)
	public String getFeeSortName() {
		return this.feeSortName;
	}

	public void setFeeSortName(String feeSortName) {
		this.feeSortName = feeSortName;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
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