package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJSecurityPlanFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSecurityPlanFacadeRemote {
	
	/**
	 * 增加一条安措计划项目信息
	 * @param entity
	 * @return
	 */
	public SpJSecurityPlan save(SpJSecurityPlan entity);

	/**
	 * 修改一条安措计划项目信息
	 * @param entity
	 * @return
	 */
	public SpJSecurityPlan update(SpJSecurityPlan entity);
	
	/**
	 * 删除一条或多条安措计划项目信息
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);

	/**
	 * 查找一条安措计划项目信息
	 * @param id
	 * @return
	 */
	public SpJSecurityPlan findById(Long id);

	
	/**
	 * 查询特种人员信息列表
	 * @param queryString 查询条件
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String queryString,String enterpriseCode,int... rowStartIdxAndCount);
}