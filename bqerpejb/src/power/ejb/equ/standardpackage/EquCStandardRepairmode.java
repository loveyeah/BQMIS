package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardRepairmode entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_STANDARD_REPAIRMODE")
public class EquCStandardRepairmode implements java.io.Serializable {

	// Fields

	private Long id;
	private String repairmodeCode;
	private String repairmodeName;
	private String repairmodeMemo;
	private String status;
	private Long orderby;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquCStandardRepairmode() {
	}

	/** minimal constructor */
	public EquCStandardRepairmode(Long id, String enterprisecode) {
		this.id = id;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquCStandardRepairmode(Long id, String repairmodeCode,
			String repairmodeName, String repairmodeMemo, String status,
			Long orderby, String enterprisecode, String ifUse) {
		this.id = id;
		this.repairmodeCode = repairmodeCode;
		this.repairmodeName = repairmodeName;
		this.repairmodeMemo = repairmodeMemo;
		this.status = status;
		this.orderby = orderby;
		this.enterprisecode = enterprisecode;
		this.ifUse = ifUse;
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

	@Column(name = "REPAIRMODE_CODE", length = 20)
	public String getRepairmodeCode() {
		return this.repairmodeCode;
	}

	public void setRepairmodeCode(String repairmodeCode) {
		this.repairmodeCode = repairmodeCode;
	}

	@Column(name = "REPAIRMODE_NAME", length = 50)
	public String getRepairmodeName() {
		return this.repairmodeName;
	}

	public void setRepairmodeName(String repairmodeName) {
		this.repairmodeName = repairmodeName;
	}

	@Column(name = "REPAIRMODE_MEMO", length = 50)
	public String getRepairmodeMemo() {
		return this.repairmodeMemo;
	}

	public void setRepairmodeMemo(String repairmodeMemo) {
		this.repairmodeMemo = repairmodeMemo;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ORDERBY", precision = 10, scale = 0)
	public Long getOrderby() {
		return this.orderby;
	}

	public void setOrderby(Long orderby) {
		this.orderby = orderby;
	}

	@Column(name = "ENTERPRISECODE", nullable = false, length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "IF_USE", length = 1)
	public String getIfUse() {
		return this.ifUse;
	}

	public void setIfUse(String ifUse) {
		this.ifUse = ifUse;
	}

}