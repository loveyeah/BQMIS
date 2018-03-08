package power.ejb.item;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ItemJData entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ITEM_J_DATA")
public class ItemJData implements java.io.Serializable {

	// Fields

	private ItemJDataId id;
	private Double dataValue;
	private Double adjustValue;

	// Constructors

	/** default constructor */
	public ItemJData() {
	}

	/** minimal constructor */
	public ItemJData(ItemJDataId id, Double dataValue) {
		this.id = id;
		this.dataValue = dataValue;
	}

	/** full constructor */
	public ItemJData(ItemJDataId id, Double dataValue, Double adjustValue) {
		this.id = id;
		this.dataValue = dataValue;
		this.adjustValue = adjustValue;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "enterpriseCode", column = @Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)),
			@AttributeOverride(name = "machineNo", column = @Column(name = "MACHINE_NO", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "itemCode", column = @Column(name = "ITEM_CODE", nullable = false, length = 40)),
			@AttributeOverride(name = "dataDate", column = @Column(name = "DATA_DATE", nullable = false, length = 7)) })
	public ItemJDataId getId() {
		return this.id;
	}

	public void setId(ItemJDataId id) {
		this.id = id;
	}

	@Column(name = "DATA_VALUE", nullable = false, precision = 15, scale = 4)
	public Double getDataValue() {
		return this.dataValue;
	}

	public void setDataValue(Double dataValue) {
		this.dataValue = dataValue;
	}

	@Column(name = "ADJUST_VALUE", precision = 15, scale = 4)
	public Double getAdjustValue() {
		return this.adjustValue;
	}

	public void setAdjustValue(Double adjustValue) {
		this.adjustValue = adjustValue;
	}

}