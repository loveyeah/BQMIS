package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCWoTools entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_WO_TOOLS", schema = "POWER")
public class EquCWoTools implements java.io.Serializable {

	// Fields

	private Long id;
	private String code;
	private String name;
	private String type;
	private String fromCom;
	private String serUnit;
	private Double fee;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCWoTools() {
	}

	/** minimal constructor */
	public EquCWoTools(Long id) {
		this.id = id;
	}

	/** full constructor */
	public EquCWoTools(Long id, String code, String name, String type,
			String fromCom, String serUnit, Double fee, String enterpriseCode,
			String isUse) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.type = type;
		this.fromCom = fromCom;
		this.serUnit = serUnit;
		this.fee = fee;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "CODE")
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TYPE")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "FROM_COM")
	public String getFromCom() {
		return this.fromCom;
	}

	public void setFromCom(String fromCom) {
		this.fromCom = fromCom;
	}

	@Column(name = "SER_UNIT")
	public String getSerUnit() {
		return this.serUnit;
	}

	public void setSerUnit(String serUnit) {
		this.serUnit = serUnit;
	}

	@Column(name = "FEE", precision = 18, scale = 5)
	public Double getFee() {
		return this.fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
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