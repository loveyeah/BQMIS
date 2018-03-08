package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardRepairmethod entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_STANDARD_REPAIRMETHOD")
public class EquCStandardRepairmethod implements java.io.Serializable {

	// Fields

	private Long id;
	private String repairmodeCode;
	private String repairmethodName;
	private String repairmethodContent;
	private String repairmethodFile;
	private String status;
	private Long orderby;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquCStandardRepairmethod() {
	}

	/** minimal constructor */
	public EquCStandardRepairmethod(Long id, String enterprisecode) {
		this.id = id;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquCStandardRepairmethod(Long id, String repairmodeCode,
			String repairmethodName, String repairmethodContent,
			String repairmethodFile, String status, Long orderby,
			String enterprisecode, String ifUse) {
		this.id = id;
		this.repairmodeCode = repairmodeCode;
		this.repairmethodName = repairmethodName;
		this.repairmethodContent = repairmethodContent;
		this.repairmethodFile = repairmethodFile;
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

	@Column(name = "REPAIRMETHOD_NAME", length = 50)
	public String getRepairmethodName() {
		return this.repairmethodName;
	}

	public void setRepairmethodName(String repairmethodName) {
		this.repairmethodName = repairmethodName;
	}

	@Column(name = "REPAIRMETHOD_CONTENT", length = 2000)
	public String getRepairmethodContent() {
		return this.repairmethodContent;
	}

	public void setRepairmethodContent(String repairmethodContent) {
		this.repairmethodContent = repairmethodContent;
	}

	@Column(name = "REPAIRMETHOD_FILE", length = 200)
	public String getRepairmethodFile() {
		return this.repairmethodFile;
	}

	public void setRepairmethodFile(String repairmethodFile) {
		this.repairmethodFile = repairmethodFile;
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