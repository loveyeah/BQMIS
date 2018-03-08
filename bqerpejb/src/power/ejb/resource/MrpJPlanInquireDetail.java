package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * MrpJPlanInquireDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MRP_J_PLAN_INQUIRE_DETAIL")
public class MrpJPlanInquireDetail implements java.io.Serializable {

	// Fields

	private Long inquireDetailId;
	private Long gatherId;
	private String billNo;
	private Long inquireSupplier;
	private Double inquireQty;
	private Double unitPrice;
	private Double totalPrice;
	private String qualityTime;
	private String offerCycle;
	private String memo;
	private String modifyBy;
	private Date modifyDate;
	private String isSelectSupplier;
	private String enterpriseCode;
	private String isUse;
	private Date effectStartDate; //报价有效开始日期
	private Date  effectEndDate; //报价有效结束日期
	private String filePath;// add by liuyi 20100409 附件地址

	// Constructors

	/** default constructor */
	public MrpJPlanInquireDetail() {
	}

	/** minimal constructor */
	public MrpJPlanInquireDetail(Long inquireDetailId, String enterpriseCode) {
		this.inquireDetailId = inquireDetailId;
		this.enterpriseCode = enterpriseCode;
	}

	/** full constructor */
	public MrpJPlanInquireDetail(Long inquireDetailId, Long gatherId,
			String billNo, Long inquireSupplier, Double inquireQty,
			Double unitPrice, Double totalPrice, String qualityTime,
			String offerCycle, String memo, String modifyBy, Date modifyDate,
			String isSelectSupplier, String enterpriseCode, String isUse,Date effectStartDate,Date  effectEndDate) {
		this.inquireDetailId = inquireDetailId;
		this.gatherId = gatherId;
		this.billNo = billNo;
		this.inquireSupplier = inquireSupplier;
		this.inquireQty = inquireQty;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
		this.qualityTime = qualityTime;
		this.offerCycle = offerCycle;
		this.memo = memo;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.isSelectSupplier = isSelectSupplier;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.effectStartDate=effectStartDate;
		this.effectEndDate=effectEndDate;
	}

	// Property accessors
	@Id
	@Column(name = "INQUIRE_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getInquireDetailId() {
		return this.inquireDetailId;
	}

	public void setInquireDetailId(Long inquireDetailId) {
		this.inquireDetailId = inquireDetailId;
	}

	@Column(name = "GATHER_ID", precision = 10, scale = 0)
	public Long getGatherId() {
		return this.gatherId;
	}

	public void setGatherId(Long gatherId) {
		this.gatherId = gatherId;
	}

	@Column(name = "BILL_NO", length = 15)
	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@Column(name = "INQUIRE_SUPPLIER", precision = 10, scale = 0)
	public Long getInquireSupplier() {
		return this.inquireSupplier;
	}

	public void setInquireSupplier(Long inquireSupplier) {
		this.inquireSupplier = inquireSupplier;
	}

	@Column(name = "INQUIRE_QTY", precision = 15, scale = 4)
	public Double getInquireQty() {
		return this.inquireQty;
	}

	public void setInquireQty(Double inquireQty) {
		this.inquireQty = inquireQty;
	}

	@Column(name = "UNIT_PRICE", precision = 18, scale = 5)
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "TOTAL_PRICE", precision = 18, scale = 5)
	public Double getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Column(name = "QUALITY_TIME", length = 20)
	public String getQualityTime() {
		return this.qualityTime;
	}

	public void setQualityTime(String qualityTime) {
		this.qualityTime = qualityTime;
	}

	@Column(name = "OFFER_CYCLE", length = 20)
	public String getOfferCycle() {
		return this.offerCycle;
	}

	public void setOfferCycle(String offerCycle) {
		this.offerCycle = offerCycle;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Column(name = "IS_SELECT_SUPPLIER", length = 1)
	public String getIsSelectSupplier() {
		return this.isSelectSupplier;
	}

	public void setIsSelectSupplier(String isSelectSupplier) {
		this.isSelectSupplier = isSelectSupplier;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECT_START_DATE", length = 7)
	public Date getEffectStartDate() {
		return effectStartDate;
	}

	public void setEffectStartDate(Date effectStartDate) {
		this.effectStartDate = effectStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECT_END_DATE", length = 7)
	public Date getEffectEndDate() {
		return effectEndDate;
	}

	public void setEffectEndDate(Date effectEndDate) {
		this.effectEndDate = effectEndDate;
	}
	@Column(name = "FILE_PATH", length = 1000)
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}