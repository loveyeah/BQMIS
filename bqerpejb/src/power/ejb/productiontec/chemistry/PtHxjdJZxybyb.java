package power.ejb.productiontec.chemistry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtHxjdJZxybyb entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_HXJD_J_ZXYBYB")
public class PtHxjdJZxybyb implements java.io.Serializable {

	// Fields

	private Long zxybybId;
	private Long zxybybzbId;
	private Long meterId;
	private Long mustThrowNum;
	private Long equipNum;
	private Long throwNum;
	private Double equipRate;
	private Double throwRate;
	private Double searchRate;

	// Constructors

	/** default constructor */
	public PtHxjdJZxybyb() {
	}

	/** minimal constructor */
	public PtHxjdJZxybyb(Long zxybybId) {
		this.zxybybId = zxybybId;
	}

	/** full constructor */
	public PtHxjdJZxybyb(Long zxybybId, Long zxybybzbId, Long meterId,
			Long mustThrowNum, Long equipNum, Long throwNum, Double equipRate,
			Double throwRate, Double searchRate) {
		this.zxybybId = zxybybId;
		this.zxybybzbId = zxybybzbId;
		this.meterId = meterId;
		this.mustThrowNum = mustThrowNum;
		this.equipNum = equipNum;
		this.throwNum = throwNum;
		this.equipRate = equipRate;
		this.throwRate = throwRate;
		this.searchRate = searchRate;
	}

	// Property accessors
	@Id
	@Column(name = "ZXYBYB_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getZxybybId() {
		return this.zxybybId;
	}

	public void setZxybybId(Long zxybybId) {
		this.zxybybId = zxybybId;
	}

	@Column(name = "ZXYBYBZB_ID", precision = 10, scale = 0)
	public Long getZxybybzbId() {
		return this.zxybybzbId;
	}

	public void setZxybybzbId(Long zxybybzbId) {
		this.zxybybzbId = zxybybzbId;
	}

	@Column(name = "METER_ID", precision = 10, scale = 0)
	public Long getMeterId() {
		return this.meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	@Column(name = "MUST_THROW_NUM", precision = 10, scale = 0)
	public Long getMustThrowNum() {
		return this.mustThrowNum;
	}

	public void setMustThrowNum(Long mustThrowNum) {
		this.mustThrowNum = mustThrowNum;
	}

	@Column(name = "EQUIP_NUM", precision = 10, scale = 0)
	public Long getEquipNum() {
		return this.equipNum;
	}

	public void setEquipNum(Long equipNum) {
		this.equipNum = equipNum;
	}

	@Column(name = "THROW_NUM", precision = 10, scale = 0)
	public Long getThrowNum() {
		return this.throwNum;
	}

	public void setThrowNum(Long throwNum) {
		this.throwNum = throwNum;
	}

	@Column(name = "EQUIP_RATE", precision = 10)
	public Double getEquipRate() {
		return this.equipRate;
	}

	public void setEquipRate(Double equipRate) {
		this.equipRate = equipRate;
	}

	@Column(name = "THROW_RATE", precision = 10)
	public Double getThrowRate() {
		return this.throwRate;
	}

	public void setThrowRate(Double throwRate) {
		this.throwRate = throwRate;
	}

	@Column(name = "SEARCH_RATE", precision = 10)
	public Double getSearchRate() {
		return this.searchRate;
	}

	public void setSearchRate(Double searchRate) {
		this.searchRate = searchRate;
	}

}