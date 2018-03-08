package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardPackfilename entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_STANDARD_PACKFILENAME")
public class EquCStandardPackfilename implements java.io.Serializable {

	// Fields

	private Long id;
	private String filepackageCode;
	private String name;
	private String enterprisecode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCStandardPackfilename() {
	}

	/** minimal constructor */
	public EquCStandardPackfilename(Long id) {
		this.id = id;
	}

	/** full constructor */
	public EquCStandardPackfilename(Long id, String filepackageCode,
			String name, String enterprisecode, String isUse) {
		this.id = id;
		this.filepackageCode = filepackageCode;
		this.name = name;
		this.enterprisecode = enterprisecode;
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

	@Column(name = "FILEPACKAGE_CODE", length = 30)
	public String getFilepackageCode() {
		return this.filepackageCode;
	}

	public void setFilepackageCode(String filepackageCode) {
		this.filepackageCode = filepackageCode;
	}

	@Column(name = "NAME", length = 80)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ENTERPRISECODE", length = 20)
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