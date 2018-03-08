package power.ejb.hr.reward;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJRewardApprove entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_REWARD_APPROVE", schema = "POWER")
public class HrJRewardApprove implements java.io.Serializable {

	// Fields

	private Long approveId;
	private Long deptId;
	private Long detailId;
	private String content;
	private String flowListUrl;
	private String flag;

	// Constructors

	/** default constructor */
	public HrJRewardApprove() {
	}

	/** minimal constructor */
	public HrJRewardApprove(Long approveId) {
		this.approveId = approveId;
	}

	/** full constructor */
	public HrJRewardApprove(Long approveId, Long deptId, Long detailId,
			String content, String flowListUrl, String flag) {
		this.approveId = approveId;
		this.deptId = deptId;
		this.detailId = detailId;
		this.content = content;
		this.flowListUrl = flowListUrl;
		this.flag = flag;
	}

	// Property accessors
	@Id
	@Column(name = "APPROVE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getApproveId() {
		return this.approveId;
	}

	public void setApproveId(Long approveId) {
		this.approveId = approveId;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "DETAIL_ID", precision = 10, scale = 0)
	public Long getDetailId() {
		return this.detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	@Column(name = "CONTENT", length = 200)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "FLOW_LIST_URL", length = 200)
	public String getFlowListUrl() {
		return this.flowListUrl;
	}

	public void setFlowListUrl(String flowListUrl) {
		this.flowListUrl = flowListUrl;
	}

	@Column(name = "FLAG", length = 1)
	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}