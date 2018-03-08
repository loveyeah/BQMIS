package power.ejb.manage.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RtCDcsNode entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RT_C_DCS_NODE")
public class RtCDcsNode implements java.io.Serializable {

	// Fields

	private String nodeCode;
	private String nodeName;
	private String apartCode;
	private String descriptor;
	private String nodeType;
	private Double minValue;
	private Double maxValue;
	private Double standardValue;
	private String collectNow;
	private String collectHis;
	private String ifCompute;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RtCDcsNode() {
	}

	/** minimal constructor */
	public RtCDcsNode(String nodeCode, String nodeName, String apartCode,
			String enterpriseCode) {
		this.nodeCode = nodeCode;
		this.nodeName = nodeName;
		this.apartCode = apartCode;
		this.enterpriseCode = enterpriseCode;
	}

	/** full constructor */
	public RtCDcsNode(String nodeCode, String nodeName, String apartCode,
			String descriptor, String nodeType, Double minValue,
			Double maxValue, Double standardValue, String collectNow,
			String collectHis, String ifCompute, String enterpriseCode) {
		this.nodeCode = nodeCode;
		this.nodeName = nodeName;
		this.apartCode = apartCode;
		this.descriptor = descriptor;
		this.nodeType = nodeType;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.standardValue = standardValue;
		this.collectNow = collectNow;
		this.collectHis = collectHis;
		this.ifCompute = ifCompute;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "NODE_CODE", unique = true, nullable = false, length = 50)
	public String getNodeCode() {
		return this.nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	@Column(name = "NODE_NAME", nullable = false, length = 100)
	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	@Column(name = "APART_CODE", nullable = false, length = 20)
	public String getApartCode() {
		return this.apartCode;
	}

	public void setApartCode(String apartCode) {
		this.apartCode = apartCode;
	}

	@Column(name = "DESCRIPTOR", length = 100)
	public String getDescriptor() {
		return this.descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	@Column(name = "NODE_TYPE", length = 10)
	public String getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	@Column(name = "MIN_VALUE", precision = 15, scale = 4)
	public Double getMinValue() {
		return this.minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	@Column(name = "MAX_VALUE", precision = 15, scale = 4)
	public Double getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	@Column(name = "STANDARD_VALUE", precision = 15, scale = 4)
	public Double getStandardValue() {
		return this.standardValue;
	}

	public void setStandardValue(Double standardValue) {
		this.standardValue = standardValue;
	}

	@Column(name = "COLLECT_NOW", length = 1)
	public String getCollectNow() {
		return this.collectNow;
	}

	public void setCollectNow(String collectNow) {
		this.collectNow = collectNow;
	}

	@Column(name = "COLLECT_HIS", length = 1)
	public String getCollectHis() {
		return this.collectHis;
	}

	public void setCollectHis(String collectHis) {
		this.collectHis = collectHis;
	}

	@Column(name = "IF_COMPUTE", length = 1)
	public String getIfCompute() {
		return this.ifCompute;
	}

	public void setIfCompute(String ifCompute) {
		this.ifCompute = ifCompute;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}