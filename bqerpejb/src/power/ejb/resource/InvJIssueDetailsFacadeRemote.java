package power.ejb.resource;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.form.MRPIssueMaterialDetailInfo;

/**
 * 领料单明细
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvJIssueDetailsFacadeRemote {
	/**
	 * 更新领料单明细
	 * @param entity 领料单明细entity
	 * @return InvJIssueDetails 领料单明细entity
	 * @throws RuntimeException  if the operation fails
	 */
	public InvJIssueDetails update(InvJIssueDetails entity);
	/**
	 * 新增领料单表头数据
	 *
	 * @param entity InvJIssueDetails entity to persist
	 * @throws RuntimeException when the operation fails
	 */
	public void save(InvJIssueDetails entity) ;
	/**
	 * 根据领料单明细的流水号查找领料单明细数据
	 * @param issueDetailsId 领料单明细流水号
	 * @return InvJIssueDetails 领料单明细
	 */
	public InvJIssueDetails findById(Long issueDetailsId);
	/**
	 * 根据某个字段查询所有记录
	 *
	 * @param propertyName 字段名
	 * @param value 字段值
	 * @return List<InvJIssueDetails> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvJIssueDetails> findByProperty(String propertyName,
			final Object value);
	/**
	 * 根据领料单id查询所有领料单明细数据
	 * @param issueHeadId 领料单id
	 * @return 领料单明细list
	 */
	public List<InvJIssueDetails> findByIssueHeadId(Long issueHeadId);
	
	/**
	 * 根据需求计划单明细id查询领料单明细数据
	 * @param requirementDetailId
	 * @return MRPIssueMaterialDetailInfo
	 */
	public MRPIssueMaterialDetailInfo findIssueDetailByDetailID(Long requirementDetailId);
	
	/**
	 * 取工单对应物资数量
	 * add bjxu
	 * 领单IssueHeadId 
	 */
	public int getMcount(Long IssueHeadId);
	/**
	 * 取一条工单对应物资
	 * add bjxu
	 * @param IssueHeadId
	 * @param materialId
	 * @return
	 */
	public InvJIssueDetails getDetails(Long IssueHeadId,Long materialId);
	
	/**
	 * add by liuyi 091126 发料统计查询
	 * modify by fyyang 091223 按审核时间查询
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getSendMaterialAccoun(String sdate,String edate,final int... rowStartIdxAndCount);
}