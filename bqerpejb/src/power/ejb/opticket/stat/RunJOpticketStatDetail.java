package power.ejb.opticket.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunJOpticketStatDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_J_OPTICKET_STAT_DETAIL")
public class RunJOpticketStatDetail implements java.io.Serializable {

	// Fields

	private Long id;
	private Long reportId;
	private Long shiftId;
	private String workcode;
	private Long opticketCount;
	private Long opticketItemCount;
	private Long noProblemOpticketCount;
	private Long noProblemOpticketItemCount;
	private String isclear;
	private Long orderBy; 
	

	/** default constructor */
	public RunJOpticketStatDetail() {
	}

	/** minimal constructor */
	public RunJOpticketStatDetail(Long id) {
		this.id = id;
	}

	 

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "REPORT_ID", precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	@Column(name = "SHIFT_ID", precision = 10, scale = 0)
	public Long getShiftId() {
		return this.shiftId;
	}

	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}

	@Column(name = "WORKCODE", length = 16)
	public String getWorkcode() {
		return this.workcode;
	}

	public void setWorkcode(String workcode) {
		this.workcode = workcode;
	}

	@Column(name = "OPTICKET_COUNT", precision = 10, scale = 0)
	public Long getOpticketCount() {
		return this.opticketCount;
	}

	public void setOpticketCount(Long opticketCount) {
		this.opticketCount = opticketCount;
	}

	@Column(name = "OPTICKET_ITEM_COUNT", precision = 10, scale = 0)
	public Long getOpticketItemCount() {
		return this.opticketItemCount;
	}

	public void setOpticketItemCount(Long opticketItemCount) {
		this.opticketItemCount = opticketItemCount;
	}

	@Column(name = "NO_PROBLEM_OPTICKET_COUNT", precision = 10, scale = 0)
	public Long getNoProblemOpticketCount() {
		return this.noProblemOpticketCount;
	}

	public void setNoProblemOpticketCount(Long noProblemOpticketCount) {
		this.noProblemOpticketCount = noProblemOpticketCount;
	}

	@Column(name = "NO_PROBLEM_OPTICKET_ITEM_COUNT", precision = 10, scale = 0)
	public Long getNoProblemOpticketItemCount() {
		return this.noProblemOpticketItemCount;
	}

	public void setNoProblemOpticketItemCount(Long noProblemOpticketItemCount) {
		this.noProblemOpticketItemCount = noProblemOpticketItemCount;
	}

	@Column(name = "ISCLEAR", length = 1)
	public String getIsclear() {
		return this.isclear;
	}

	public void setIsclear(String isclear) {
		this.isclear = isclear;
	}
	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

}