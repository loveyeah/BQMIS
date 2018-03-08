package power.ejb.workticket;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCWorkticketDangerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCWorkticketDangerFacadeRemote {
	
	/**
	 * 增加一条危险点记录
	 * @param entity
	 * @return RunCWorkticketDanger 危险点对象
	 */
	public RunCWorkticketDanger save(RunCWorkticketDanger entity) throws CodeRepeatException;

	/**
	 * 删除一条危险点记录
	 * @param dangerId 危险点id
	 */
	public void delete(Long dangerId) throws CodeRepeatException;

	/**
	 * 批量删除危险点记录
	 * @param dangerIds 危险点id e.g:(1,2,3,...)
	 */
	public void deleteMulti(String dangerIds);
	
	/**
	 * 修改一条危险点记录
	 * @param entity
	 * @return RunCWorkticketDanger 危险点对象
	 */
	public RunCWorkticketDanger update(RunCWorkticketDanger entity) throws CodeRepeatException;

	/**
	 * 查找一条危险点记录
	 * @param dangerId 危险点id
	 * @return  RunCWorkticketDanger 危险点对象
	 */
	public RunCWorkticketDanger findById(Long dangerId);

	
	/**
     * 查询危险点信息列表
     * @param enterpriseCode 企业编码
     * @param dangerTypeId 危险点类型
     * @param dangerTypeName 危险点名称（模糊查询）
     * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
     * @return  PageObject
     */
	public PageObject findAll(String enterpriseCode,Long dangerTypeId,Long PDangerId,String dangerTypeName,int... rowStartIdxAndCount);
	
	/**
	 * 查询危险点信息列表（用于工作票种危险点选择页面）
	 * @param enterpriseCode 企业编码
	 * @param workticketTypeCode 工作票类型编码
	 * @param dangerTypeId 危险点类型
	 * @param dangerName 危险点名称（模糊查询）
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return
	 */
	public PageObject findListForSelect(String enterpriseCode,String workticketTypeCode,String dangerTypeId,
			String dangerName, int... rowStartIdxAndCount);
}