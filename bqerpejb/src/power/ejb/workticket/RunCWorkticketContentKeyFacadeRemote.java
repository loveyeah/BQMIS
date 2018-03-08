package power.ejb.workticket;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCWorkticketContentKeyFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCWorkticketContentKeyFacadeRemote {
	/**
	 * 增加一条工作票内容关键词记录
	 * 
	 * @param entity 工作票内容关键词对象
	 * @throws CodeRepeatException
	 */
	public RunCWorkticketContentKey save(RunCWorkticketContentKey entity) throws CodeRepeatException;

	/**
	 * 删除一条工作票内容关键词记录
	 * @param contentKeyId 工作票内容关键词id
	 * @throws CodeRepeatException
	 */
	public void delete(Long contentKeyId) throws CodeRepeatException;

	/**
	 *  批量删除工作票内容关键词记录
	 *  @param contentKeyId 工作票内容关键词id，e.g:1,2,...
	 */
	public void deleteMulti(String contentKeyIds);
	/**
	 * 修改一条工作票内容关键词记录
	 * @param entity 
	 * @return RunCWorkticketContentKey
	 * @throws CodeRepeatException
	 */
	public RunCWorkticketContentKey update(RunCWorkticketContentKey entity) throws CodeRepeatException;
	/**
	 * 查找一条工作票内容关键词记录
	 * @param id 工作票内容关键词id
	 * @return RunCWorktickSafety
	 */
	public RunCWorkticketContentKey findById(Long id);

	/**
	 * 查询工作票内容关键词记录列表
	 * @param workticketTypeCode 工作票类型编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return PageObject
	 * @throws ParseException
	 */
	public PageObject findAll(String workticketTypeCode,String enterpriseCode,String keyType,final int... rowStartIdxAndCount);
	
	/**
	 * 查询工作票内容关键词记录列表
	 * 考虑关键字共用的情况，workticketTypeCode为"C"
	 * @param workticketTypeCode 工作票类型编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return PageObject
	 * @throws ParseException
	 */
	public PageObject findAllWithComm(String workticketTypeCode,String enterpriseCode,String keyType,final int... rowStartIdxAndCount);
}