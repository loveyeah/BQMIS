package power.ejb.workticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
 
@Entity
@Table(name = "RUN_C_WORKTICKET_FIRELEVEL")
public class RunCWorkticketFirelevel implements java.io.Serializable {

	// Fields

	private Long firelevelId;
	private String firelevelName;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;
	private Long orderBy;
	
	// Constructors

	/** default constructor */
	public RunCWorkticketFirelevel() {
	}

	/** minimal constructor */
	public RunCWorkticketFirelevel(Long firelevelId) {
		this.firelevelId = firelevelId;
	} 

	// Property accessors
	@Id
	@Column(name = "FIRELEVEL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFirelevelId() {
		return this.firelevelId;
	}

	public void setFirelevelId(Long firelevelId) {
		this.firelevelId = firelevelId;
	}

	@Column(name = "FIRELEVEL_NAME", length = 30)
	public String getFirelevelName() {
		return this.firelevelName;
	}

	public void setFirelevelName(String firelevelName) {
		this.firelevelName = firelevelName;
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