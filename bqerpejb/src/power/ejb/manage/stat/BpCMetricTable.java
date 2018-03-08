package power.ejb.manage.stat;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpCMetricTable entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_METRIC_TABLE")
public class BpCMetricTable implements java.io.Serializable {

	// Fields

	private BpCMetricTableId id;
	private String tableName;
	private Double maxTableValue;
	private Double multiple;
	private Double startValue;
	private Double endValue;
	private Date fixDate;
	private Date endDate;
	private String ifUsed;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCMetricTable() {
	}

	/** minimal constructor */
	public BpCMetricTable(BpCMetricTableId id) {
		this.id = id;
	}

	/** full constructor */
	public BpCMetricTable(BpCMetricTableId id, String tableName,
			Double maxTableValue, Double multiple, Double startValue,
			Double endValue, Date fixDate, Date endDate, String ifUsed,
			String enterpriseCode) {
		this.id = id;
		this.tableName = tableName;
		this.maxTableValue = maxTableValue;
		this.multiple = multiple;
		this.startValue = startValue;
		this.endValue = endValue;
		this.fixDate = fixDate;
		this.endDate = endDate;
		this.ifUsed = ifUsed;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "itemCode", column = @Column(name = "ITEM_CODE", nullable = false, length = 40)),
			@AttributeOverride(name = "tableCode", column = @Column(name = "TABLE_CODE", nullable = false, length = 10)) })
	public BpCMetricTableId getId() {
		return this.id;
	}

	public void setId(BpCMetricTableId id) {
		this.id = id;
	}

	@Column(name = "TABLE_NAME", length = 40)
	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "MAX_TABLE_VALUE", precision = 15, scale = 4)
	public Double getMaxTableValue() {
		return this.maxTableValue;
	}

	public void setMaxTableValue(Double maxTableValue) {
		this.maxTableValue = maxTableValue;
	}

	@Column(name = "MULTIPLE", precision = 15, scale = 4)
	public Double getMultiple() {
		return this.multiple;
	}

	public void setMultiple(Double multiple) {
		this.multiple = multiple;
	}

	@Column(name = "START_VALUE", precision = 15, scale = 4)
	public Double getStartValue() {
		return this.startValue;
	}

	public void setStartValue(Double startValue) {
		this.startValue = startValue;
	}

	@Column(name = "END_VALUE", precision = 15, scale = 4)
	public Double getEndValue() {
		return this.endValue;
	}

	public void setEndValue(Double endValue) {
		this.endValue = endValue;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FIX_DATE", length = 7)
	public Date getFixDate() {
		return this.fixDate;
	}

	public void setFixDate(Date fixDate) {
		this.fixDate = fixDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "IF_USED", length = 1)
	public String getIfUsed() {
		return this.ifUsed;
	}

	public void setIfUsed(String ifUsed) {
		this.ifUsed = ifUsed;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}