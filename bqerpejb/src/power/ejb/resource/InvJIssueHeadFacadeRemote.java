package power.ejb.resource;

import javax.ejb.Remote;
import java.util.List;
import java.util.Map;
import power.ear.comm.ejb.PageObject;

/**
 * 领料单表头.
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvJIssueHeadFacadeRemote {

	/**
	 * 更新领料单表头
	 * @param entity 领料单
	 * @return InvJIssueHead 领料单
	 * @throws RuntimeException  if the operation fails
	 */
	public InvJIssueHead update(InvJIssueHead entity);
	/**
	 * 新增领料单表头数据
	 *
	 * @param entity InvJLocation entity to persist
	 * @throws RuntimeException when the operation fails
	 */
	public void save(InvJIssueHead entity);
	/**
	 * 根据领料单表头的流水号查找领料单表头数据
	 * @param issueHeadId 领料单表头流水号
	 * @return InvJIssueHead 领料单
	 */
	public InvJIssueHead findById(Long issueHeadId);
	/**
	 * 根据领料单编码查找
	 * @param matCode 领料单编码
	 * @param enterpriseCode
	 * @return InvJIssueHead
	 */
	public InvJIssueHead findByMatCode(String matCode,String enterpriseCode);
	
	/**
	 * 
	 * add bjxu
	 */
	public InvJIssueHead addIssueHead(InvJIssueHead head);
	
	/**
	 * 审核领料单
	 * 090619
	 * @param ids
	 * @param checkBy
	 */
	@SuppressWarnings("unchecked")
	public void issueCheckOp(String ids,String checkBy,List<Map>list);
	
	/**
	 * 领料单取消审核
	 * add by drdu 091103
	 * @param ids
	 * @param checkBy
	 */
	public void issueCheckCancel(String ids,String checkBy);
	/**
	 *  删除领料单 (同时删除其工作流信息)
	 *  add by fyyang 
	 * @param entryId 工作流实例号
	 * @param issueId 领料单id
	 */
	public void deleteIssueHead(Long entryId,Long issueId);
}