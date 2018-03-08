package power.ejb.productiontec.retrenchenergy;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJnjdJZcqjngh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JNJD_J_ZCQJNGH")
public class PtJnjdJZcqjngh implements java.io.Serializable {

	// Fields

	private Long jnghzdId;
	private String mainTopic;
	private Date commonDate;
	private String content;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJnjdJZcqjngh() {
	}

	/** minimal constructor */
	public PtJnjdJZcqjngh(Long jnghzdId) {
		this.jnghzdId = jnghzdId;
	}

	/** full constructor */
	public PtJnjdJZcqjngh(Long jnghzdId, String mainTopic, Date commonDate,
			String content, String memo, String enterpriseCode) {
		this.jnghzdId = jnghzdId;
		this.mainTopic = mainTopic;
		this.commonDate = commonDate;
		this.content = content;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JNGHZD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJnghzdId() {
		return this.jnghzdId;
	}

	public void setJnghzdId(Long jnghzdId) {
		this.jnghzdId = jnghzdId;
	}

	@Column(name = "MAIN_TOPIC", length = 100)
	public String getMainTopic() {
		return this.mainTopic;
	}

	public void setMainTopic(String mainTopic) {
		this.mainTopic = mainTopic;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "COMMON_DATE", length = 7)
	public Date getCommonDate() {
		return this.commonDate;
	}

	public void setCommonDate(Date commonDate) {
		this.commonDate = commonDate;
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