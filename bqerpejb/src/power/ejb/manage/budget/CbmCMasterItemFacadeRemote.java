package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 预算控制维护
 * 
 * @author liuyi090804
 */
@Remote
public interface CbmCMasterItemFacadeRemote {
	/**
	 * 新增一条预算控制维护记录
	 */
	public void save(CbmCMasterItem entity);

	/**
	 * 删除一条预算控制维护记录
	 */
	public void delete(CbmCMasterItem entity);

	/**
	 * 删除一条或多条预算控制维护记录
	 */
	public void delete(String ids);

	/**
	 * 更新一条预算控制维护记录
	 */
	public CbmCMasterItem update(CbmCMasterItem entity);

	/**
	 * 通过id查找一天预算控制维护记录
	 * 
	 * @param id
	 * @return
	 */
	public CbmCMasterItem findById(Long id);

	/**
	 * 
	 */
	public List<CbmCMasterItem> findAll(int... rowStartIdxAndCount);

	public PageObject findAll(String centerId, String enterpriseCode,
			int... rowStartIdxAndCount);

	public void saveDeptControlInput(List<CbmCMasterItem> addList,
			List<CbmCMasterItem> updaList, String deleteIds);
}