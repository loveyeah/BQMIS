package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PurJArrivalDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUR_J_ARRIVAL_DETAILS")
public class PurJArrivalDetails implements java.io.Serializable {

	// Fields

	private Long id;
	private String mifNo;
	private String purNo;
	private Long purLine;
	private Long materialId;
	private String lotCode;
	private Double badQty;
	private Double rcvQty;
	private Double recQty;
	private Double theQty;
	private String itemStatus;
	private String memo;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;

	// Constructors

	/** default constructor */
	public PurJArrivalDetails() {
	}

	/** minimal constructor */
	public PurJArrivalDetails(Long id, String lastModifiedBy,
			Date lastModifiedDate) {
		this.id = id;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	/** full constructor */
	public PurJArrivalDetails(Long id, String mifNo, String purNo,
			Long purLine, Long materialId, String lotCode, Double badQty,
			Double rcvQty, Double recQty, Double theQty, String itemStatus,
			String memo, String enterpriseCode, String isUse,
			String lastModifiedBy, Date lastModifiedDate) {
		this.id = id;
		this.mifNo = mifNo;
		this.purNo = purNo;
		this.purLine = purLine;
		this.materialId = materialId;
		this.lotCode = lotCode;
		this.badQty = badQty;
		this.rcvQty = rcvQty;
		this.recQty = recQty;
		this.theQty = theQty;
		this.itemStatus = itemStatus;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "MIF_NO", length = 30)
	public String getMifNo() {
		return this.mifNo;
	}

	public void setMifNo(String mifNo) {
		this.mifNo = mifNo;
	}

	@Column(name = "PUR_NO", length = 30)
	public String getPurNo() {
		return this.purNo;
	}

	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}

	@Column(name = "PUR_LINE", precision = 10, scale = 0)
	public Long getPurLine() {
		return this.purLine;
	}

	public void setPurLine(Long purLine) {
		this.purLine = purLine;
	}

	@Column(name = "MATERIAL_ID", precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "LOT_CODE", length = 30)
	public String getLotCode() {
		return this.lotCode;
	}

	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}

	@Column(name = "BAD_QTY", precision = 15, scale = 4)
	public Double getBadQty() {
		return this.badQty;
	}

	public void setBadQty(Double badQty) {
		this.badQty = badQty;
	}

	@Column(name = "RCV_QTY", precision = 15, scale = 4)
	public Double getRcvQty() {
		return this.rcvQty;
	}

	public void setRcvQty(Double rcvQty) {
		this.rcvQty = rcvQty;
	}

	@Column(name = "REC_QTY", precision = 15, scale = 4)
	public Double getRecQty() {
		return this.recQty;
	}

	public void setRecQty(Double recQty) {
		this.recQty = recQty;
	}

	@Column(name = "THE_QTY", precision = 15, scale = 4)
	public Double getTheQty() {
		return this.theQty;
	}

	public void setTheQty(Double theQty) {
		this.theQty = theQty;
	}

	@Column(name = "ITEM_STATUS", length = 1)
	public String getItemStatus() {
		return this.itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
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

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}