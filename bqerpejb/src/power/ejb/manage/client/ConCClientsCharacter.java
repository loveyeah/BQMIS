package power.ejb.manage.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ConCClientsCharacter entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_C_CLIENTS_CHARACTER")
public class ConCClientsCharacter implements java.io.Serializable {

	// Fields

	private Long characterId;
	private String characterName;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConCClientsCharacter() {
	}

	/** minimal constructor */
	public ConCClientsCharacter(Long characterId) {
		this.characterId = characterId;
	}

	/** full constructor */
	public ConCClientsCharacter(Long characterId, String characterName,
			String memo, String enterpriseCode) {
		this.characterId = characterId;
		this.characterName = characterName;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "CHARACTER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCharacterId() {
		return this.characterId;
	}

	public void setCharacterId(Long characterId) {
		this.characterId = characterId;
	}

	@Column(name = "CHARACTER_NAME", length = 80)
	public String getCharacterName() {
		return this.characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
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