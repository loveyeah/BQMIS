package power.ejb.workticket;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * 动火票内容管理 
 * @author wzhyan
 */
@Remote
public interface RunCWorkticketFireContentFacadeRemote {
	/**
	 * 动火票内容增加
	 * @param RunCWorkticketFireContent entity
	 * 动火票内容对象 
	 * @return RunCWorkticketFireContent
	 * 动火票内容对象
	 */
	public RunCWorkticketFireContent save(RunCWorkticketFireContent entity) throws CodeRepeatException;

	/**
	 * 单个删除或者批量删除动火票内容
	 * @param String firecontentIds
	 * 动火票内容id集(如:1,2,3)
	 */
	public void deleteMutil(String firecontentIds);

	/**
	 * 修改动火票内容
	 * @param RunCWorkticketFireContent entity
	 * 动火票内容对象 
	 * @return RunCWorkticketFireContent
	 * 动火票内容对象 
	 */
	public RunCWorkticketFireContent update(RunCWorkticketFireContent entity) throws CodeRepeatException;
	/**
	 * 根据主键查找
	 * @param Long id
	 * 动火票内容编码
	 * @return RunCWorkticketFireContent
	 * 动火票内容
	 */
	public RunCWorkticketFireContent findById(Long id); 
	/**
	 * 根据名称或者Id模糊查找动火票内容列表
	 * @param String contentLike
	 * 名称或者Id
	 * @param String enterpriseCode
	 * 企业编码
	 * @return List<RunCWorkticketFireContent>
	 * 动火票内容列表
	 */
	public List<RunCWorkticketFireContent> findByNameOrId(String enterpriseCode,String contentLike);
}