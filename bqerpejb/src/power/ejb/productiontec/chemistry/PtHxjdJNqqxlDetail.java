package power.ejb.productiontec.chemistry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtHxjdJNqqxlDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_HXJD_J_NQQXL_DETAIL")
public class PtHxjdJNqqxlDetail implements java.io.Serializable {

	// Fields

	private Long nqjxlDetailId;
	private Long nqjxlId;
	private String projectNames;
	private Double itemName1;
	private Double itemName2;
	private Double itemName3;
	private Double itemName4;
	private Double itemName5;
	private Double itemName6;
	private String enterpriseCode;
	// Constructors

	/** default constructor */
	public PtHxjdJNqqxlDetail() {
	}

	/** minimal constructor */
	public PtHxjdJNqqxlDetail(Long nqjxlDetailId) {
		this.nqjxlDetailId = nqjxlDetailId;
	}

	/** full constructor */
	public PtHxjdJNqqxlDetail(Long nqjxlDetailId, Long nqjxlId,
			String projectNames, Double itemName1, Double itemName2,
			Double itemName3, Double itemName4, Double itemName5,
			Double itemName6,String enterpriseCode) {
		this.nqjxlDetailId = nqjxlDetailId;
		this.nqjxlId = nqjxlId;
		this.projectNames = projectNames;
		this.itemName1 = itemName1;
		this.itemName2 = itemName2;
		this.itemName3 = itemName3;
		this.itemName4 = itemName4;
		this.itemName5 = itemName5;
		this.itemName6 = itemName6;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "NQJXL_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getNqjxlDetailId() {
		return this.nqjxlDetailId;
	}

	public void setNqjxlDetailId(Long nqjxlDetailId) {
		this.nqjxlDetailId = nqjxlDetailId;
	}

	@Column(name = "NQJXL_ID", precision = 10, scale = 0)
	public Long getNqjxlId() {
		return this.nqjxlId;
	}

	public void setNqjxlId(Long nqjxlId) {
		this.nqjxlId = nqjxlId;
	}

	@Column(name = "PROJECT_NAMES", length = 40)
	public String getProjectNames() {
		return this.projectNames;
	}

	public void setProjectNames(String projectNames) {
		this.projectNames = projectNames;
	}

	@Column(name = "ITEM_NAME1", precision = 15, scale = 4)
	public Double getItemName1() {
		return this.itemName1;
	}

	public void setItemName1(Double itemName1) {
		this.itemName1 = itemName1;
	}

	@Column(name = "ITEM_NAME2", precision = 15, scale = 4)
	public Double getItemName2() {
		return this.itemName2;
	}

	public void setItemName2(Double itemName2) {
		this.itemName2 = itemName2;
	}

	@Column(name = "ITEM_NAME3", precision = 15, scale = 4)
	public Double getItemName3() {
		return this.itemName3;
	}

	public void setItemName3(Double itemName3) {
		this.itemName3 = itemName3;
	}

	@Column(name = "ITEM_NAME4", precision = 15, scale = 4)
	public Double getItemName4() {
		return this.itemName4;
	}

	public void setItemName4(Double itemName4) {
		this.itemName4 = itemName4;
	}

	@Column(name = "ITEM_NAME5", precision = 15, scale = 4)
	public Double getItemName5() {
		return this.itemName5;
	}

	public void setItemName5(Double itemName5) {
		this.itemName5 = itemName5;
	}

	@Column(name = "ITEM_NAME6", precision = 15, scale = 4)
	public Double getItemName6() {
		return this.itemName6;
	}

	public void setItemName6(Double itemName6) {
		this.itemName6 = itemName6;
	}
	
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	

}