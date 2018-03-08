package power.ejb.productiontec.retrenchenergy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJnjdJJnkb entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_JNJD_J_JNKB")
public class PtJnjdJJnkb implements java.io.Serializable {

	// Fields

	private Long jnkbId;
	private String mainTopic;
	private String content;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJnjdJJnkb() {
	}

	/** minimal constructor */
	public PtJnjdJJnkb(Long jnkbId) {
		this.jnkbId = jnkbId;
	}

	/** full constructor */
	public PtJnjdJJnkb(Long jnkbId, String mainTopic, String content,
			String memo, String enterpriseCode) {
		this.jnkbId = jnkbId;
		this.mainTopic = mainTopic;
		this.content = content;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JNKB_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJnkbId() {
		return this.jnkbId;
	}

	public void setJnkbId(Long jnkbId) {
		this.jnkbId = jnkbId;
	}

	@Column(name = "MAIN_TOPIC", length = 100)
	public String getMainTopic() {
		return this.mainTopic;
	}

	public void setMainTopic(String mainTopic) {
		this.mainTopic = mainTopic;
	}

	@Column(name = "CONTENT", length = 256)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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