package power.ejb.manage.contract.form;

import power.ejb.manage.system.RtCDcsNode;

@SuppressWarnings("serial")
public class DcsNodeInfo implements java.io.Serializable{

	private RtCDcsNode node;
	private String blockName; //机组名称
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
	
	public RtCDcsNode getNode() {
		return node;
	}
	public void setNode(RtCDcsNode node) {
		this.node = node;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public String getNodeCode() {
		return nodeCode;
	}
	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getApartCode() {
		return apartCode;
	}
	public void setApartCode(String apartCode) {
		this.apartCode = apartCode;
	}
	public String getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public Double getMinValue() {
		return minValue;
	}
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}
	public Double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
	public Double getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(Double standardValue) {
		this.standardValue = standardValue;
	}
	public String getCollectNow() {
		return collectNow;
	}
	public void setCollectNow(String collectNow) {
		this.collectNow = collectNow;
	}
	public String getCollectHis() {
		return collectHis;
	}
	public void setCollectHis(String collectHis) {
		this.collectHis = collectHis;
	}
	public String getIfCompute() {
		return ifCompute;
	}
	public void setIfCompute(String ifCompute) {
		this.ifCompute = ifCompute;
	}
}
