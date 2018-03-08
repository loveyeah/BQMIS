/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.form;

/**
 * 物料明细bean
 * 
 * @author daichunlin
 */
public class MaterialNameQueryInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** 物料主文件 */
	/** 物料ID */	
	private String materialId;
	/** 编码 */
	private String materialNo;
	/** 名称 */
	private String materialName;
	/** 规格型号 */
	private String specNo;
	/** 材质/参数 */
	private String parameter;
	/** 存货计量单位 */
	private Long stockUmId;
	/** 生产厂家 */
	private String factory;
	/** 文档号 */
	private String docNo;
	/** 最大库存量 */
	private String maxStock;
	/** 是否免检 */
	private String qaControlFlag;
	/** 存货计量名称 */
	private String stockUmName;
	/** 物料分类 */
	/** 名称 */
	private String className;
	
	
	

	/**
	 * 物料主文件.采购计量单位
	 */
	private Long purUmId = null;

	

	/** 当前库存 */
	private String stock; //add by ywliu 20091019
	
	private Double stdCost;//标准成本 add by fyyang 20100114 
	
	// add by liuyi 20100504 需求计划物资已入库未领用
	private String inNotUsed;
	// add by liuyi 20100504 可用库存
	private String canUseStock;
	
	
	private String classNo;//物料分类 add by ltong 20100505
	
	/**
	 * @return the classNo
	 */
	public String getClassNo() {
		return classNo;
	}

	/**
	 * @param classNo the classNo to set
	 */
	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getInNotUsed() {
		return inNotUsed;
	}

	public void setInNotUsed(String inNotUsed) {
		this.inNotUsed = inNotUsed;
	}

	public String getCanUseStock() {
		return canUseStock;
	}

	public void setCanUseStock(String canUseStock) {
		this.canUseStock = canUseStock;
	}

	/**
	 * 取得物料主文件.采购计量单位
	 *
	 * @return 物料主文件.采购计量单位
	 */
	public Long getPurUmId() {
		return purUmId;
	}

	/**
	 * 设置物料主文件.采购计量单位
	 *
	 * @param argPurUmId 物料主文件.采购计量单位
	 */
	public void setPurUmId(Long argPurUmId) {
		purUmId = argPurUmId;
	}
	/**
	 * @return the stock add by ywliu 20091019
	 */
	public String getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set add by ywliu 20091019
	 */
	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getMaterialNo() {
		return materialNo;
	}
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getSpecNo() {
		return specNo;
	}
	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public Long getStockUmId() {
		return stockUmId;
	}
	public void setStockUmId(Long stockUmId) {
		this.stockUmId = stockUmId;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getDocNo() {
		return docNo;
	}
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}	
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	public String getMaxStock() {
		return maxStock;
	}
	public void setMaxStock(String maxStock) {
		this.maxStock = maxStock;
	}
	public String getQaControlFlag() {
		return qaControlFlag;
	}
	public void setQaControlFlag(String qaControlFlag) {
		this.qaControlFlag = qaControlFlag;
	}
	/**
	 * @return the stockUmName
	 */
	public String getStockUmName() {
		return stockUmName;
	}
	/**
	 * @param stockUmName the stockUmName to set
	 */
	public void setStockUmName(String stockUmName) {
		this.stockUmName = stockUmName;
	}

	public Double getStdCost() {
		return stdCost;
	}

	public void setStdCost(Double stdCost) {
		this.stdCost = stdCost;
	}	
	

}
