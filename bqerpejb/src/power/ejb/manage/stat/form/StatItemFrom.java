package power.ejb.manage.stat.form;

public class StatItemFrom {

	/** 指标编码 */
	private String itemCode;
	/** 指标名称 */
	private String itemName;
	/** 时间类型 */
	private String dataTimeType;
	/** 节点编码 */
	private String nodeCode;
	/** 节点描述 */
	private String descriptor;
	/** 节点名称 */
	private String nodeName;
	/** 机组 */
	private String apartCode;
	/** 机组名称 */
	private String apartName;
	/** 是否生成对应 */
	private String isCorrespond;

	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * @param itemCode
	 *            the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName
	 *            the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the dataTimeType
	 */
	public String getDataTimeType() {
		return dataTimeType;
	}

	/**
	 * @param dataTimeType
	 *            the dataTimeType to set
	 */
	public void setDataTimeType(String dataTimeType) {
		this.dataTimeType = dataTimeType;
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

	public String getApartName() {
		return apartName;
	}

	public void setApartName(String apartName) {
		this.apartName = apartName;
	}

}
