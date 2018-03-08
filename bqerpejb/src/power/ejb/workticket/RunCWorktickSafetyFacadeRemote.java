package power.ejb.workticket;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCWorktickSafetyFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCWorktickSafetyFacadeRemote {

	
	/**
	 * 增加一条安全措施项目记录 
	 * @param entity 安措对象
	 * @return RunCWorktickSafety
	 * @throws CodeRepeatException
	 */
	public RunCWorktickSafety save(RunCWorktickSafety entity) throws CodeRepeatException;

    
	/**
	 * 删除一条安措项目记录
	 * @param safetyId 安措id
	 * @throws CodeRepeatException
	 */
	public void delete(Long safetyId) throws CodeRepeatException;
	/**
	 * 批量删除安措项目记录
	 * @param safetyIds 安措id，e.g:1,2,...
	 */
	public void deleteMulti(String safetyIds);

	/**
	 * 修改一条安措项目记录
	 * @param entity 
	 * @return RunCWorktickSafety
	 * @throws CodeRepeatException
	 */
	public RunCWorktickSafety update(RunCWorktickSafety entity) throws CodeRepeatException;
   
	/**
	 * 查找一条安措项目记录
	 * @param id 安措id
	 * @return RunCWorktickSafety
	 */
	public RunCWorktickSafety findById(Long id);
	/**
	 * 查找维护好的安措类型
	 * @param enterpriseCode 企业编码
	 * @param workticketType 工作票类型编码
	 * @return List<RunCWorktickSafety>
	 */
	public List<RunCWorktickSafety> getMaintSafetyBy(String enterpriseCode,String workticketType);
	/**
	 * 查询安措项目记录列表
	 * @param workticketTypeCode 工作票类型编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return
	 * @throws ParseException
	 */
	public PageObject findAll(String workticketTypeCode,String enterpriseCode,final int... rowStartIdxAndCount) throws ParseException;
}