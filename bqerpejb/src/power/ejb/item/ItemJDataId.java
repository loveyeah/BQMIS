package power.ejb.item;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ItemJDataId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class ItemJDataId implements java.io.Serializable {

	// Fields

	private String enterpriseCode;
	private Long machineNo;
	private String itemCode;
	private Date dataDate;

	// Constructors

	/** default constructor */
	public ItemJDataId() {
	}

	/** full constructor */
	public ItemJDataId(String enterpriseCode, Long machineNo, String itemCode,
			Date dataDate) {
		this.enterpriseCode = enterpriseCode;
		this.machineNo = machineNo;
		this.itemCode = itemCode;
		this.dataDate = dataDate;
	}

	// Property accessors

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "MACHINE_NO", nullable = false, precision = 10, scale = 0)
	public Long getMachineNo() {
		return this.machineNo;
	}

	public void setMachineNo(Long machineNo) {
		this.machineNo = machineNo;
	}

	@Column(name = "ITEM_CODE", nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_DATE", nullable = false, length = 7)
	public Date getDataDate() {
		return this.dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ItemJDataId))
			return false;
		ItemJDataId castOther = (ItemJDataId) other;

		return ((this.getEnterpriseCode() == castOther.getEnterpriseCode()) || (this
				.getEnterpriseCode() != null
				&& castOther.getEnterpriseCode() != null && this
				.getEnterpriseCode().equals(castOther.getEnterpriseCode())))
				&& ((this.getMachineNo() == castOther.getMachineNo()) || (this
						.getMachineNo() != null
						&& castOther.getMachineNo() != null && this
						.getMachineNo().equals(castOther.getMachineNo())))
				&& ((this.getItemCode() == castOther.getItemCode()) || (this
						.getItemCode() != null
						&& castOther.getItemCode() != null && this
						.getItemCode().equals(castOther.getItemCode())))
				&& ((this.getDataDate() == castOther.getDataDate()) || (this
						.getDataDate() != null
						&& castOther.getDataDate() != null && this
						.getDataDate().equals(castOther.getDataDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getEnterpriseCode() == null ? 0 : this.getEnterpriseCode()
						.hashCode());
		result = 37 * result
				+ (getMachineNo() == null ? 0 : this.getMachineNo().hashCode());
		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37 * result
				+ (getDataDate() == null ? 0 : this.getDataDate().hashCode());
		return result;
	}

}