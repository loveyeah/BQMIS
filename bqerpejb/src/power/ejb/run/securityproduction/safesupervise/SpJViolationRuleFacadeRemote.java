package power.ejb.run.securityproduction.safesupervise;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 
 * 反违章管理
 * @author fyyang 20100626
 */
@Remote
public interface SpJViolationRuleFacadeRemote {
	
	/**
	 * 增加一条反违章管理信息
	 * @param entity
	 * @return
	 */
	public SpJViolationRule save(SpJViolationRule entity);

	/**
	 * 删除一条或多条反违章管理信息
	 * @param ids
	 */
	public void delete(String ids);

	/**
	 * 修改一条反违章管理信息
	 * @param entity
	 * @return
	 */
	public SpJViolationRule update(SpJViolationRule entity);

	/**
	 * 查找一条反违章管理信息
	 * @param id
	 * @return
	 */
	public SpJViolationRule findById(Long id);
	
	/**
	 * 查找反违章管理信息列表
	 * @param strDate
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findViolationRuleList(String strDate,String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 查询该月违章管理信息列表
	 * @param strMonth
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject queryViolationRuleList(String strMonth,String enterpriseCode,int... rowStartIdxAndCount);
}