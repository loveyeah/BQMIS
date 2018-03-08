package power.ejb.manage.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ConCClientsType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_C_CLIENTS_TYPE")
public class ConCClientsType implements java.io.Serializable {

	// Fields

	private Long typeId;
	private String typeName;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConCClientsType() {
	}

	/** minimal constructor */
	public ConCClientsType(Long typeId) {
		this.typeId = typeId;
	}

	/** full constructor */
	public ConCClientsType(Long typeId, String typeName, String memo,
			String enterpriseCode) {
		this.typeId = typeId;
		this.typeName = typeName;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	@Column(name = "TYPE_NAME", length = 80)
	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}