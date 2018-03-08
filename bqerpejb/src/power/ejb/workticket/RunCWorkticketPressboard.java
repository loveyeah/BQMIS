package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCWorkticketPressboard entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_WORKTICKET_PRESSBOARD")
public class RunCWorkticketPressboard implements java.io.Serializable {

	// Fields

	private Long pressboardId;
	private String pressboardCode;
	private String pressboardName;
	private Long parentPressboardId;
	private Long orderBy;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;
	private String isLeaf;

	// Constructors

	/** default constructor */
	public RunCWorkticketPressboard() {
	}

	/** minimal constructor */
	public RunCWorkticketPressboard(Long pressboardId) {
		this.pressboardId = pressboardId;
	}

	/** full constructor */
	public RunCWorkticketPressboard(Long pressboardId, String pressboardCode,
			String pressboardName, Long parentPressboardId, Long orderBy,
			String modifyBy, Date modifyDate, String enterpriseCode,
			String isUse,String isLeaf) {
		this.pressboardId = pressboardId;
		this.pressboardCode = pressboardCode;
		this.pressboardName = pressboardName;
		this.parentPressboardId = parentPressboardId;
		this.orderBy = orderBy;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.isLeaf = isLeaf;
	}

	// Property accessors
	@Id
	@Column(name = "PRESSBOARD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPressboardId() {
		return this.pressboardId;
	}

	public void setPressboardId(Long pressboardId) {
		this.pressboardId = pressboardId;
	}

	@Column(name = "PRESSBOARD_CODE", length = 20)
	public String getPressboardCode() {
		return this.pressboardCode;
	}

	public void setPressboardCode(String pressboardCode) {
		this.pressboardCode = pressboardCode;
	}

	@Column(name = "PRESSBOARD_NAME", length = 100)
	public String getPressboardName() {
		return this.pressboardName;
	}

	public void setPressboardName(String pressboardName) {
		this.pressboardName = pressboardName;
	}

	@Column(name = "PARENT_PRESSBOARD_ID", precision = 10, scale = 0)
	public Long getParentPressboardId() {
		return this.parentPressboardId;
	}

	public void setParentPressboardId(Long parentPressboardId) {
		this.parentPressboardId = parentPressboardId;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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
	
	@Column(name = "IS_LEAF", length = 1)
	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

}