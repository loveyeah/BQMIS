package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmJDepreciationDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_DEPRECIATION_DETAIL")
public class CbmJDepreciationDetail implements java.io.Serializable {

	// Fields

	private Long depreciationDetailId;
	private Long depreciationId;
	private String assetName;
	private Double lastAsset;
	private Double addAsset;
	private Double reduceAsset;
	private Double newAsset;
	private Double depreciationRate;
	private Double depreciationNumber;
	private Double depreciationSum;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJDepreciationDetail() {
	}

	/** minimal constructor */
	public CbmJDepreciationDetail(Long depreciationDetailId, Long depreciationId) {
		this.depreciationDetailId = depreciationDetailId;
		this.depreciationId = depreciationId;
	}

	/** full constructor */
	public CbmJDepreciationDetail(Long depreciationDetailId,
			Long depreciationId, String assetName, Double lastAsset,
			Double addAsset, Double reduceAsset, Double newAsset,
			Double depreciationRate, Double depreciationNumber,
			Double depreciationSum, String memo, String isUse,
			String enterpriseCode) {
		this.depreciationDetailId = depreciationDetailId;
		this.depreciationId = depreciationId;
		this.assetName = assetName;
		this.lastAsset = lastAsset;
		this.addAsset = addAsset;
		this.reduceAsset = reduceAsset;
		this.newAsset = newAsset;
		this.depreciationRate = depreciationRate;
		this.depreciationNumber = depreciationNumber;
		this.depreciationSum = depreciationSum;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DEPRECIATION_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDepreciationDetailId() {
		return this.depreciationDetailId;
	}

	public void setDepreciationDetailId(Long depreciationDetailId) {
		this.depreciationDetailId = depreciationDetailId;
	}

	@Column(name = "DEPRECIATION_ID", nullable = false, precision = 10, scale = 0)
	public Long getDepreciationId() {
		return this.depreciationId;
	}

	public void setDepreciationId(Long depreciationId) {
		this.depreciationId = depreciationId;
	}

	@Column(name = "ASSET_NAME", length = 80)
	public String getAssetName() {
		return this.assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	@Column(name = "LAST_ASSET", precision = 15, scale = 4)
	public Double getLastAsset() {
		return this.lastAsset;
	}

	public void setLastAsset(Double lastAsset) {
		this.lastAsset = lastAsset;
	}

	@Column(name = "ADD_ASSET", precision = 15, scale = 4)
	public Double getAddAsset() {
		return this.addAsset;
	}

	public void setAddAsset(Double addAsset) {
		this.addAsset = addAsset;
	}

	@Column(name = "REDUCE_ASSET", precision = 15, scale = 4)
	public Double getReduceAsset() {
		return this.reduceAsset;
	}

	public void setReduceAsset(Double reduceAsset) {
		this.reduceAsset = reduceAsset;
	}

	@Column(name = "NEW_ASSET", precision = 15, scale = 4)
	public Double getNewAsset() {
		return this.newAsset;
	}

	public void setNewAsset(Double newAsset) {
		this.newAsset = newAsset;
	}

	@Column(name = "DEPRECIATION_RATE", precision = 15, scale = 4)
	public Double getDepreciationRate() {
		return this.depreciationRate;
	}

	public void setDepreciationRate(Double depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	@Column(name = "DEPRECIATION_NUMBER", precision = 15, scale = 4)
	public Double getDepreciationNumber() {
		return this.depreciationNumber;
	}

	public void setDepreciationNumber(Double depreciationNumber) {
		this.depreciationNumber = depreciationNumber;
	}

	@Column(name = "DEPRECIATION_SUM", precision = 15, scale = 4)
	public Double getDepreciationSum() {
		return this.depreciationSum;
	}

	public void setDepreciationSum(Double depreciationSum) {
		this.depreciationSum = depreciationSum;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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