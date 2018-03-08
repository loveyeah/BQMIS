package power.ejb.workticket.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJWorkticketFireContent entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_WORKTICKET_FIRE_CONTENT")
public class RunJWorkticketFireContent implements java.io.Serializable {

	// Fields

	private Long id;
	private String workticketNo;
	private Long firecontentId;
	private Long orderBy;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunJWorkticketFireContent() {
	}

	/** minimal constructor */
	public RunJWorkticketFireContent(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunJWorkticketFireContent(Long id, String workticketNo,
			Long firecontentId, Long orderBy, String modifyBy, Date modifyDate,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.workticketNo = workticketNo;
		this.firecontentId = firecontentId;
		this.orderBy = orderBy;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
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

	@Column(name = "WORKTICKET_NO", length = 22)
	public String getWorkticketNo() {
		return this.workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	@Column(name = "FIRECONTENT_ID", precision = 10, scale = 0)
	public Long getFirecontentId() {
		return this.firecontentId;
	}

	public void setFirecontentId(Long firecontentId) {
		this.firecontentId = firecontentId;
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

}