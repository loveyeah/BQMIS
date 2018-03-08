package power.ejb.workticket;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCWorkticketDangerTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCWorkticketDangerTypeFacadeRemote {
	
	/**
	 * 增加一条危险点类型记录
	 * @param entity
	 * @return RunCWorkticketDangerType 危险点类型对象
	 */
	public RunCWorkticketDangerType save(RunCWorkticketDangerType entity) throws CodeRepeatException;

	/**
	 * 删除一条危险点类型记录
	 * @param dangerTypeId 危险点类型id
	 */
	public void delete(Long dangerTypeId) throws CodeRepeatException;
    
	/**
	 * 批量删除危险点类型记录
	 * @param dangerTypeIds 危险点类型id e.g:(1,2,3,...)
	 */
	public void deleteMulti(String dangerTypeIds);
	
	/**
	 * 修改一条危险点类型记录
	 * @param entity
	 * @return RunCWorkticketDangerType 危险点类型对象
	 */
	public RunCWorkticketDangerType update(RunCWorkticketDangerType entity) throws CodeRepeatException;

	/**
	 * 查找一条危险点类型记录
	 * @param dangerTypeId 危险点类型id
	 * @return  RunCWorkticketDangerType 危险点类型对象
	 */
	public RunCWorkticketDangerType findById(Long dangerTypeId);

	/**
     * 查询危险点类型信息列表
     * @param enterpriseCode 企业编码
     * @param workticketTypeCode 工作票类型
     * @param markcardTypeName 危险点类型名称（模糊查询）
     * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
     * @return  PageObject
     */
	public PageObject findAll(String enterpriseCode,String workticketTypeCode,String dangerTypeName,int... rowStartIdxAndCount);
}