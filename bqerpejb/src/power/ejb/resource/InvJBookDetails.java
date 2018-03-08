package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvJBookDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_J_BOOK_DETAILS")
public class InvJBookDetails implements java.io.Serializable {

	// Fields

	private Long id;
	private String bookNo;
	private String bookDetailNo;
	private Long materialId;
	private String whsNo;
	private String locationNo;
	private String lotNo;
	private Double bookQty;
	private Double physicalQty;
	private String reason;
	private Date bookDate;
	private String bookStatus;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;

	// Constructors

	/** default constructor */
	public InvJBookDetails() {
	}

	/** minimal constructor */
	public InvJBookDetails(Long id, String bookNo, Double bookQty,
			Double physicalQty, Date bookDate, String bookStatus,
			String enterpriseCode, String isUse, String lastModifiedBy,
			Date lastModifiedDate) {
		this.id = id;
		this.bookNo = bookNo;
		this.bookQty = bookQty;
		this.physicalQty = physicalQty;
		this.bookDate = bookDate;
		this.bookStatus = bookStatus;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	/** full constructor */
	public InvJBookDetails(Long id, String bookNo, String bookDetailNo,
			Long materialId, String whsNo, String locationNo, String lotNo,
			Double bookQty, Double physicalQty, String reason, Date bookDate,
			String bookStatus, String enterpriseCode, String isUse,
			String lastModifiedBy, Date lastModifiedDate) {
		this.id = id;
		this.bookNo = bookNo;
		this.bookDetailNo = bookDetailNo;
		this.materialId = materialId;
		this.whsNo = whsNo;
		this.locationNo = locationNo;
		this.lotNo = lotNo;
		this.bookQty = bookQty;
		this.physicalQty = physicalQty;
		this.reason = reason;
		this.bookDate = bookDate;
		this.bookStatus = bookStatus;
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

	@Column(name = "BOOK_NO", nullable = false, length = 30)
	public String getBookNo() {
		return this.bookNo;
	}

	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}

	@Column(name = "BOOK_DETAIL_NO", length = 30)
	public String getBookDetailNo() {
		return this.bookDetailNo;
	}

	public void setBookDetailNo(String bookDetailNo) {
		this.bookDetailNo = bookDetailNo;
	}

	@Column(name = "MATERIAL_ID", precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "WHS_NO", length = 10)
	public String getWhsNo() {
		return this.whsNo;
	}

	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}

	@Column(name = "LOCATION_NO", length = 10)
	public String getLocationNo() {
		return this.locationNo;
	}

	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}

	@Column(name = "LOT_NO", length = 30)
	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	@Column(name = "BOOK_QTY", nullable = false, precision = 15, scale = 4)
	public Double getBookQty() {
		return this.bookQty;
	}

	public void setBookQty(Double bookQty) {
		this.bookQty = bookQty;
	}

	@Column(name = "PHYSICAL_QTY", precision = 15, scale = 4)
	public Double getPhysicalQty() {
		return this.physicalQty;
	}

	public void setPhysicalQty(Double physicalQty) {
		this.physicalQty = physicalQty;
	}

	@Column(name = "REASON", length = 100)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BOOK_DATE", nullable = false, length = 7)
	public Date getBookDate() {
		return this.bookDate;
	}

	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}

	@Column(name = "BOOK_STATUS", nullable = false, length = 3)
	public String getBookStatus() {
		return this.bookStatus;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", nullable = false, length = 1)
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