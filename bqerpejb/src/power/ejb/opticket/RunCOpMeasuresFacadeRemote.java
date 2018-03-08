package power.ejb.opticket;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * @author slTang
 */
@Remote
public interface RunCOpMeasuresFacadeRemote {
	/**
	 *增加
	 */
	public void save(RunCOpMeasures entity) throws CodeRepeatException;

	/**
	 * 删除
	 */
	public void delete(RunCOpMeasures entity) ;

	/**
	 * 修改
	 */
	public RunCOpMeasures update(RunCOpMeasures entity) throws CodeRepeatException;

	public RunCOpMeasures findById(Long id);

	/**
	 * 根据操作任务id查找
	 */
	public List<RunCOpMeasures> findByTaskId(String enterpriseCode,String modifyBy,Long operateTaskId);
	public boolean saveAllOperat(List<Map> addList,List<Map> updateList,String delStr,String currentMan,String enterpriseCod) throws CodeRepeatException;
}