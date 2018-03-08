/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCStatitem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_STATITEM")
public class HrCStatitem implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Long statId;
	private Long statItemId;
	private String statItemName;
	private String statItemByname;
	private String statItemUnit;
	private String statItemType;
	private Long orderBy;
	private String useFlg;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCStatitem() {
	}

	/** minimal constructor */
	public HrCStatitem(Long statId) {
		this.statId = statId;
	}

	/** full constructor */
	public HrCStatitem(Long statId, Long statItemId, String statItemName,
			String statItemByname, String statItemUnit, String statItemType,
			Long orderBy, String useFlg, String lastModifiyBy,
			Date lastModifiyDate, String isUse, String enterpriseCode) {
		this.statId = statId;
		this.statItemId = statItemId;
		this.statItemName = statItemName;
		this.statItemByname = statItemByname;
		this.statItemUnit = statItemUnit;
		this.statItemType = statItemType;
		this.orderBy = orderBy;
		this.useFlg = useFlg;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "STAT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getStatId() {
		return this.statId;
	}

	public void setStatId(Long statId) {
		this.statId = statId;
	}

	@Column(name = "STAT_ITEM_ID", precision = 10, scale = 0)
	public Long getStatItemId() {
		return this.statItemId;
	}

	public void setStatItemId(Long statItemId) {
		this.statItemId = statItemId;
	}

	@Column(name = "STAT_ITEM_NAME", length = 20)
	public String getStatItemName() {
		return this.statItemName;
	}

	public void setStatItemName(String statItemName) {
		this.statItemName = statItemName;
	}

	@Column(name = "STAT_ITEM_BYNAME", length = 20)
	public String getStatItemByname() {
		return this.statItemByname;
	}

	public void setStatItemByname(String statItemByname) {
		this.statItemByname = statItemByname;
	}

	@Column(name = "STAT_ITEM_UNIT", length = 6)
	public String getStatItemUnit() {
		return this.statItemUnit;
	}

	public void setStatItemUnit(String statItemUnit) {
		this.statItemUnit = statItemUnit;
	}

	@Column(name = "STAT_ITEM_TYPE", length = 20)
	public String getStatItemType() {
		return this.statItemType;
	}

	public void setStatItemType(String statItemType) {
		this.statItemType = statItemType;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "USE_FLG", length = 1)
	public String getUseFlg() {
		return this.useFlg;
	}

	public void setUseFlg(String useFlg) {
		this.useFlg = useFlg;
	}

	@Column(name = "LAST_MODIFIY_BY", length = 16)
	public String getLastModifiyBy() {
		return this.lastModifiyBy;
	}

	public void setLastModifiyBy(String lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIY_DATE", length = 7)
	public Date getLastModifiyDate() {
		return this.lastModifiyDate;
	}

	public void setLastModifiyDate(Date lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}