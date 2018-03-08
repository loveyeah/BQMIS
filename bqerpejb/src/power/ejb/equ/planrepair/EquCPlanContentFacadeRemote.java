package power.ejb.equ.planrepair;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCPlanContentFacade.
 * add by drdu 090922
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCPlanContentFacadeRemote {

	/**
	 * 增加一条检修项目内容记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public EquCPlanContent save(EquCPlanContent entity)throws CodeRepeatException ;

	/**
	 * 删除一条或多条检修项目内容记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条检修项目内容记录
	 * @param entity
	 * @return
	 */
	public EquCPlanContent update(EquCPlanContent entity)throws CodeRepeatException;

	/**
	 * 根据ID查找一条检修项目内容记录的详细信息
	 * @param id
	 * @return
	 */
	public EquCPlanContent findById(Long id);

	/**
	 * 根据名称，企业编码查询列表
	 * @param enterpriseCode
	 * @param contentName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findEquPlanContentList(String enterpriseCode,String contentName,final int... rowStartIdxAndCount);
}