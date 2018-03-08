package power.ejb.run.timework;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCTimeworktype entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_C_TIMEWORKTYPE", schema = "POWER")
public class RunCTimeworktype implements java.io.Serializable {

	// Fields

	private Long id;
	private String code;
	private String name;
	private Long orderby;
	private String memo;
	private String showtype;
	private String keywordsinsql;
	private String status;
	private String enterprisecode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCTimeworktype() {
	}

	/** minimal constructor */
	public RunCTimeworktype(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunCTimeworktype(Long id, String code, String name, Long orderby,
			String memo, String showtype, String keywordsinsql, String status,
			String enterprisecode, String isUse) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.orderby = orderby;
		this.memo = memo;
		this.showtype = showtype;
		this.keywordsinsql = keywordsinsql;
		this.status = status;
		this.enterprisecode = enterprisecode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CODE", length = 6)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ORDERBY", precision = 22, scale = 0)
	public Long getOrderby() {
		return this.orderby;
	}

	public void setOrderby(Long orderby) {
		this.orderby = orderby;
	}

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "SHOWTYPE", length = 1)
	public String getShowtype() {
		return this.showtype;
	}

	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}

	@Column(name = "KEYWORDSINSQL", length = 200)
	public String getKeywordsinsql() {
		return this.keywordsinsql;
	}

	public void setKeywordsinsql(String keywordsinsql) {
		this.keywordsinsql = keywordsinsql;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ENTERPRISECODE", length = 30)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}