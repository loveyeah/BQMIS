package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.CbmJBudgetItemForm;

/**
 * Remote interface for CbmCCenterTopicFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCCenterTopicFacadeRemote {

	/**
	 * 增加一条部门预算主题记录
	 * 
	 * @param entity
	 */
	public void save(CbmCCenterTopic entity);

	/**
	 * 增加部门预算主题记录
	 * 
	 * @param addList
	 */
	public void save(List<CbmCCenterTopic> addList);

	public Long isNew(Long cTopicId);

	/**
	 * 删除一条或多条部门预算主题记录
	 * 
	 * @param ids
	 */
	public void deleteMuti(String ids);

	/**
	 * 修改一条部门预算主题记录
	 * 
	 * @param entity
	 * @return
	 */
	public CbmCCenterTopic update(CbmCCenterTopic entity);

	/**
	 * 修改部门预算主题记录
	 * 
	 * @param updateList
	 */
	public void update(List<CbmCCenterTopic> updateList);

	public CbmCCenterTopic findById(Long id);

	/**
	 * 查找部门预算主题记录列表
	 * 
	 * @param cTopicId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String centerId, String enterpriseCode,
			final int... rowStartIdxAndCount);

	/**
	 * 查找预算部门主题维护列表 部门在部门维护表中数据 add by liuyi 090805
	 * 
	 * @param cTopicId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAllInMaint(String enterpriseCode, String isquery,
			final int... rowStartIdxAndCount);

	/**
	 * 通过主题找到相应的部门
	 */
	public List<CbmJBudgetItemForm> getDeptList(Long topicId,
			String enterprseCode);

	/**
	 * 通过主题id,部门id,指标id,判断数据来源
	 */
	public String judgeSource(Long topicId, Long centerId, Long itemId,
			String enterpriseCode);
}