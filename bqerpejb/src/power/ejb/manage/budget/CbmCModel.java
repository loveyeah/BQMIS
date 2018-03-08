package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCModel entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_MODEL")
public class CbmCModel implements java.io.Serializable {

	// Fields

	private Long modelItemId;
	private Long daddyItemId;
	private String modelItemCode;
	private String modelItemName;
	private String modelType;
	private String isItem;
	private Long unitId;
	private String comeFrom;
	private Long modelOrder;
	private Long displayNo;
	private String modelItemExplain;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCModel() {
	}

	/** minimal constructor */
	public CbmCModel(Long modelItemId) {
		this.modelItemId = modelItemId;
	}

	/** full constructor */
	public CbmCModel(Long modelItemId, Long daddyItemId, String modelItemCode,
			String modelItemName, String modelType, String isItem, Long unitId,
			String comeFrom, Long modelOrder, Long displayNo,
			String modelItemExplain, String isUse, String enterpriseCode) {
		this.modelItemId = modelItemId;
		this.daddyItemId = daddyItemId;
		this.modelItemCode = modelItemCode;
		this.modelItemName = modelItemName;
		this.modelType = modelType;
		this.isItem = isItem;
		this.unitId = unitId;
		this.comeFrom = comeFrom;
		this.modelOrder = modelOrder;
		this.displayNo = displayNo;
		this.modelItemExplain = modelItemExplain;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "MODEL_ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getModelItemId() {
		return this.modelItemId;
	}

	public void setModelItemId(Long modelItemId) {
		this.modelItemId = modelItemId;
	}

	@Column(name = "DADDY_ITEM_ID", precision = 10, scale = 0)
	public Long getDaddyItemId() {
		return this.daddyItemId;
	}

	public void setDaddyItemId(Long daddyItemId) {
		this.daddyItemId = daddyItemId;
	}

	@Column(name = "MODEL_ITEM_CODE", length = 40)
	public String getModelItemCode() {
		return this.modelItemCode;
	}

	public void setModelItemCode(String modelItemCode) {
		this.modelItemCode = modelItemCode;
	}

	@Column(name = "MODEL_ITEM_NAME", length = 50)
	public String getModelItemName() {
		return this.modelItemName;
	}

	public void setModelItemName(String modelItemName) {
		this.modelItemName = modelItemName;
	}

	@Column(name = "MODEL_TYPE", length = 1)
	public String getModelType() {
		return this.modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	@Column(name = "IS_ITEM", length = 1)
	public String getIsItem() {
		return this.isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	@Column(name = "UNIT_ID", precision = 10, scale = 0)
	public Long getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Column(name = "COME_FROM", length = 1)
	public String getComeFrom() {
		return this.comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	@Column(name = "MODEL_ORDER", precision = 10, scale = 0)
	public Long getModelOrder() {
		return this.modelOrder;
	}

	public void setModelOrder(Long modelOrder) {
		this.modelOrder = modelOrder;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "MODEL_ITEM_EXPLAIN", length = 800)
	public String getModelItemExplain() {
		return this.modelItemExplain;
	}

	public void setModelItemExplain(String modelItemExplain) {
		this.modelItemExplain = modelItemExplain;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}