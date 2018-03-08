package power.ejb.manage.stat;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * BpCItemCollectSetup entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_ITEM_COLLECT_SETUP")
public class BpCItemCollectSetup implements java.io.Serializable {

	// Fields

	private BpCItemCollectSetupId id;
	private String ifCollect;
	private String ifAutoSetup;
	private String dataTimeType;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCItemCollectSetup() {
	}

	/** minimal constructor */
	public BpCItemCollectSetup(BpCItemCollectSetupId id) {
		this.id = id;
	}

	/** full constructor */
	public BpCItemCollectSetup(BpCItemCollectSetupId id, String ifCollect,
			String ifAutoSetup, String dataTimeType, String enterpriseCode) {
		this.id = id;
		this.ifCollect = ifCollect;
		this.ifAutoSetup = ifAutoSetup;
		this.dataTimeType = dataTimeType;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "itemCode", column = @Column(name = "ITEM_CODE", nullable = false, length = 40)),
			@AttributeOverride(name = "dataTimeDot", column = @Column(name = "DATA_TIME_DOT", nullable = false, precision = 22, scale = 0)) })
	public BpCItemCollectSetupId getId() {
		return this.id;
	}

	public void setId(BpCItemCollectSetupId id) {
		this.id = id;
	}

	@Column(name = "IF_COLLECT", length = 1)
	public String getIfCollect() {
		return this.ifCollect;
	}

	public void setIfCollect(String ifCollect) {
		this.ifCollect = ifCollect;
	}

	@Column(name = "IF_AUTO_SETUP", length = 1)
	public String getIfAutoSetup() {
		return this.ifAutoSetup;
	}

	public void setIfAutoSetup(String ifAutoSetup) {
		this.ifAutoSetup = ifAutoSetup;
	}

	@Column(name = "DATA_TIME_TYPE", length = 1)
	public String getDataTimeType() {
		return this.dataTimeType;
	}

	public void setDataTimeType(String dataTimeType) {
		this.dataTimeType = dataTimeType;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}