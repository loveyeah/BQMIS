package power.ejb.manage.budget;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 预算主题维护
 * 
 * @author liuyi090803
 */
@Remote
public interface CbmCTopicFacadeRemote {
	/**
	 * 新增一条预算主题维护数据
	 */
	public String save(CbmCTopic entity);

	/**
	 * 删除一条预算主题维护数据
	 */
	public void delete(CbmCTopic entity);

	public void delete(String ids);

	/**
	 * 更新一条预算主题维护数据
	 */
	public String update(CbmCTopic entity);

	/**
	 * 通过id查找一条预算主题维护数据
	 * 
	 * @param id
	 * @return
	 */
	public CbmCTopic findById(String id);

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount);

	public String save(List<CbmCTopic> addList, List<CbmCTopic> updateList,
			String ids);
}