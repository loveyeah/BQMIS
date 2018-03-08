package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardEqu entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_STANDARD_EQU")
public class EquCStandardEqu implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String kksCode;
	private String addCode1;
	private String addCode2;
	private String addCode3;
	private String memo;
	private String status;
	private Long orderby;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquCStandardEqu() {
	}

	/** minimal constructor */
	public EquCStandardEqu(Long id, String enterprisecode) {
		this.id = id;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquCStandardEqu(Long id, String woCode, String kksCode,
			String addCode1, String addCode2, String addCode3, String memo,
			String status, Long orderby, String enterprisecode, String ifUse) {
		this.id = id;
		this.woCode = woCode;
		this.kksCode = kksCode;
		this.addCode1 = addCode1;
		this.addCode2 = addCode2;
		this.addCode3 = addCode3;
		this.memo = memo;
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

	@Column(name = "WO_CODE", length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "KKS_CODE", length = 50)
	public String getKksCode() {
		return this.kksCode;
	}

	public void setKksCode(String kksCode) {
		this.kksCode = kksCode;
	}

	@Column(name = "ADD_CODE1", length = 50)
	public String getAddCode1() {
		return this.addCode1;
	}

	public void setAddCode1(String addCode1) {
		this.addCode1 = addCode1;
	}

	@Column(name = "ADD_CODE2", length = 50)
	public String getAddCode2() {
		return this.addCode2;
	}

	public void setAddCode2(String addCode2) {
		this.addCode2 = addCode2;
	}

	@Column(name = "ADD_CODE3", length = 50)
	public String getAddCode3() {
		return this.addCode3;
	}

	public void setAddCode3(String addCode3) {
		this.addCode3 = addCode3;
	}

	@Column(name = "MEMO", length = 1000)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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