package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 成本分析
 * @author liuyi 091019
 */
@Remote
public interface CbmJCostFacadeRemote {
	/**
	 * 新增一条成本分析数据
	 */
	public void save(CbmJCost entity);

	/**
	 * 删除一条成本分析数据
	 */
	public void delete(CbmJCost entity);

	/**
	 * 更新一条成本分析数据
	 */
	public CbmJCost update(CbmJCost entity);

	public CbmJCost findById(Long id);

	
	public List<CbmJCost> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	
	public List<CbmJCost> findAll(int... rowStartIdxAndCount);
	
	public PageObject findAllCostRec(String time,String enterpriseCode,int...rowStartIdxAndCount);
	
	public void saveAllModRec(List<CbmJCost> addList,List<CbmJCost> updateList);
}