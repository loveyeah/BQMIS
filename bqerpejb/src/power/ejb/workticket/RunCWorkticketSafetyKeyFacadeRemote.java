package power.ejb.workticket;

import java.text.ParseException; 
import javax.ejb.Remote; 
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 *  工作票安全措施关键词管理
 */
@Remote
public interface RunCWorkticketSafetyKeyFacadeRemote {
	/**
	 * 增加一条工作票安全关键词记录
	 * 
	 * @param entity 工作票安全关键词对象
	 * @throws CodeRepeatException
	 */
	public RunCWorkticketSafetyKey  save(RunCWorkticketSafetyKey  entity) throws CodeRepeatException;

	/**
	 * 删除一条工作票内容关键词记录
	 * @param contentKeyId 工作票内容关键词id
	 * @throws CodeRepeatException
	 */
	public void delete(Long safetyKeyId) ;

	/**
	 *  批量删除工作票内容关键词记录
	 *  @param contentKeyId 工作票内容关键词id，e.g:1,2,...
	 */
	public void deleteMulti(String safetyKeyIds);
	/**
	 * 修改一条工作票安全关键词记录
	 * @param entity 
	 * @return RunCWorkticketContentKey
	 * @throws CodeRepeatException
	 */
	public RunCWorkticketSafetyKey update(RunCWorkticketSafetyKey entity) throws CodeRepeatException;
	/**
	 * 查找一条工作票安全关键词记录记录
	 * @param id 工作票内容关键词id
	 * @return RunCWorktickSafety
	 */
	public RunCWorkticketSafetyKey findById(Long id);

	/**
	 * 查询工作票安全关键词记录列表
	 * @param workticketTypeCode 工作票类型编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return PageObject
	 * @throws ParseException
	 */
	public PageObject findAll(String enterpriseCode,String workticketTypeCode,String keyType,final int... rowStartIdxAndCount);
	
	/**
	 * 查询工作票内容关键词记录列表
	 * 考虑关键字共用的情况，workticketTypeCode为"C"
	 * @param workticketTypeCode 工作票类型编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return PageObject
	 * @throws ParseException
	 */
	public PageObject findAllWithComm(String enterpriseCode,String workticketTypeCode,String keyType,final int... rowStartIdxAndCount);
}