package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCItemReportNew entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_ITEM_REPORT_NEW")
public class BpCItemReportNew implements java.io.Serializable {

	// Fields

	private Long id;
	private Long reportCode;
	private String itemCode;
	private String dataType;
	private Long displayNo;
	private String itemAlias;
	private String enterpriseCode;
	private String itemBaseName;
	private String itemSecondName;
	private String dataTimeType;
	private Long conkersNo ;
	// add by liuyi 20100524 
	private String isShowZero;
	
	
	// ConstructorsCONKERS_NO

	/** default constructor */
	public BpCItemReportNew() {
	}

	/** minimal constructor */
	public BpCItemReportNew(Long id, Long reportCode, String itemCode) {
		this.id = id;
		this.reportCode = reportCode;
		this.itemCode = itemCode;
	}

	/** full constructor */
	public BpCItemReportNew(Long id, Long reportCode, String itemCode,
			String dataType, Long displayNo, String itemAlias,
			String enterpriseCode, String itemBaseName, String itemSecondName,
			String dataTimeType) {
		this.id = id;
		this.reportCode = reportCode;
		this.itemCode = itemCode;
		this.dataType = dataType;
		this.displayNo = displayNo;
		this.itemAlias = itemAlias;
		this.enterpriseCode = enterpriseCode;
		this.itemBaseName = itemBaseName;
		this.itemSecondName = itemSecondName;
		this.dataTimeType = dataTimeType;
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

	@Column(name = "REPORT_CODE", nullable = false, precision = 10, scale = 0)
	public Long getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(Long reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "ITEM_CODE", nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "DATA_TYPE", length = 1)
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "ITEM_ALIAS", length = 100)
	public String getItemAlias() {
		return this.itemAlias;
	}

	public void setItemAlias(String itemAlias) {
		this.itemAlias = itemAlias;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "ITEM_BASE_NAME", length = 50)
	public String getItemBaseName() {
		return this.itemBaseName;
	}

	public void setItemBaseName(String itemBaseName) {
		this.itemBaseName = itemBaseName;
	}

	@Column(name = "ITEM_SECOND_NAME", length = 50)
	public String getItemSecondName() {
		return this.itemSecondName;
	}

	public void setItemSecondName(String itemSecondName) {
		this.itemSecondName = itemSecondName;
	}

	@Column(name = "DATA_TIME_TYPE", length = 1)
	public String getDataTimeType() {
		return this.dataTimeType;
	}

	public void setDataTimeType(String dataTimeType) {
		this.dataTimeType = dataTimeType;
	}

	@Column(name = "CONKERS_NO", length = 1)
	public Long getConkersNo() {
		return conkersNo;
	}
	
	public void setConkersNo(Long conkersNo) {
		this.conkersNo = conkersNo;
	}
	
	@Column(name = "IS_SHOW_ZERO",length = 1)
	public String getIsShowZero(){
		return this.isShowZero;
	}

	public void setIsShowZero(String isShowZero){
		this.isShowZero = isShowZero;
	}

}