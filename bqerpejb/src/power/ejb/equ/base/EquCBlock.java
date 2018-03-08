package power.ejb.equ.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCBlock entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_BLOCK")
public class EquCBlock implements java.io.Serializable {


	private Long id;
	private String blockCode;
	private String blockName;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCBlock() {
	}

	/** minimal constructor */
	public EquCBlock(Long id, String blockCode) {
		this.id = id;
		this.blockCode = blockCode;
	}

	/** full constructor */
	public EquCBlock(Long id, String blockCode, String blockName,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.blockCode = blockCode;
		this.blockName = blockName;
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

	@Column(name = "BLOCK_CODE", nullable = false, length = 2)
	public String getBlockCode() {
		return this.blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	@Column(name = "BLOCK_NAME", length = 20)
	public String getBlockName() {
		return this.blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
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