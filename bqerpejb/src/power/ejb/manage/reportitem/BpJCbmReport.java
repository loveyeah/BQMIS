package power.ejb.manage.reportitem;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJCbmReport entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_CBM_REPORT", schema = "POWER")
public class BpJCbmReport implements java.io.Serializable {

	// Fields

	private Long reId;
	private Long itemId;
	private Long blockId;
	private Long reportId;
	private String yearMonth;
	private Double value;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJCbmReport() {
	}

	/** minimal constructor */
	public BpJCbmReport(Long reId, Long itemId, Long blockId, Long reportId,
			String yearMonth) {
		this.reId = reId;
		this.itemId = itemId;
		this.blockId = blockId;
		this.reportId = reportId;
		this.yearMonth = yearMonth;
	}

	/** full constructor */
	public BpJCbmReport(Long reId, Long itemId, Long blockId, Long reportId,
			String yearMonth, Double value, String enterpriseCode) {
		this.reId = reId;
		this.itemId = itemId;
		this.blockId = blockId;
		this.reportId = reportId;
		this.yearMonth = yearMonth;
		this.value = value;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getReId() {
		return this.reId;
	}

	public void setReId(Long reId) {
		this.reId = reId;
	}

	@Column(name = "ITEM_ID", nullable = false, precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "BLOCK_ID", nullable = false, precision = 10, scale = 0)
	public Long getBlockId() {
		return this.blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	@Column(name = "REPORT_ID", nullable = false, precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	
	@Column(name = "YEAR_MONTH", length = 10)
	public String getYearMonth() {
		return this.yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Column(name = "VALUE", precision = 15, scale = 4)
	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}