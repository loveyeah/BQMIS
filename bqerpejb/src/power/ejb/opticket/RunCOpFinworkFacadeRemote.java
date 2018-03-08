package power.ejb.opticket;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * @author slTang
 */
@Remote
public interface RunCOpFinworkFacadeRemote {
	/**
	 * 增加
	 */
	public void save(RunCOpFinwork entity) throws CodeRepeatException;

	/**
	 * 删除
	 */
	public void delete(RunCOpFinwork entity) throws CodeRepeatException;

	/**
	 *修改
	 */
	public RunCOpFinwork update(RunCOpFinwork entity) throws CodeRepeatException;

	public RunCOpFinwork findById(Long id);

	/**
	 *根据操作任务id查找所有操作后工作表
	 */
	public List<RunCOpFinwork> findFinworkByTask(String enterpriseCode,Long operateTaskId);
	public boolean saveAllOperat(List<Map> addList,List<Map> updateList,String delStr,String currentMan,String enterpriseCod) throws CodeRepeatException;
}