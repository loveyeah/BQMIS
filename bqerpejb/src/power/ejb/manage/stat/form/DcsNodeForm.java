package power.ejb.manage.stat.form;

public class DcsNodeForm {

	/** 节点编码 */
	private String nodeCode;
	/** 节点描述 */
	private String descriptor;
	/** 节点名称 */
	private String nodeName;
	/** 单元 */
	private String apartCode;
	/** 单元 */
	private String apartName;
	/** 是否生成对应 */
	private String isCorrespond;

	/**
	 * @return the nodeCode
	 */
	public String getNodeCode() {
		return nodeCode;
	}

	/**
	 * @param nodeCode
	 *            the nodeCode to set
	 */
	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	/**
	 * @return the descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * @param descriptor
	 *            the descriptor to set
	 */
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName
	 *            the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return the apartCode
	 */
	public String getApartCode() {
		return apartCode;
	}

	/**
	 * @param apartCode
	 *            the apartCode to set
	 */
	public void setApartCode(String apartCode) {
		this.apartCode = apartCode;
	}

	/**
	 * @return the isCorrespond
	 */
	public String getIsCorrespond() {
		return isCorrespond;
	}

	/**
	 * @param isCorrespond
	 *            the isCorrespond to set
	 */
	public void setIsCorrespond(String isCorrespond) {
		this.isCorrespond = isCorrespond;
	}

	public String getApartName() {
		return apartName;
	}

	public void setApartName(String apartName) {
		this.apartName = apartName;
	}

}
