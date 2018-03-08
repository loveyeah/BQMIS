package power.ejb.equ.planrepair;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCPlanTypeFacade.
 * add by drdu 090922
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCPlanTypeFacadeRemote {
	
	/**
	 * 增加一条计划类型记录
	 * @param entity
	 * @return
	 */
	public EquCPlanType save(EquCPlanType entity)throws CodeRepeatException;

	/**
	 * 删除一条或多条计划类型记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条计划类型记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public EquCPlanType update(EquCPlanType entity) throws CodeRepeatException ;

	/**
	 * 根据ID查找一条详细信息
	 * @param id
	 * @return
	 */
	public EquCPlanType findById(Long id);

	/**
	 * 根据企业编码，名称查找列表
	 * @param enterpriseCode
	 * @param planTypeName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findEquPlanTypeList(String enterpriseCode,String planTypeName,final int... rowStartIdxAndCount);
}