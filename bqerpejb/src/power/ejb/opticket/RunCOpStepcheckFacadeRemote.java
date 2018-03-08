package power.ejb.opticket;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/** 
 * @author slTang
 */
@Remote
public interface RunCOpStepcheckFacadeRemote {
	/**
	 * 增加
	 */
	public void save(RunCOpStepcheck entity) throws CodeRepeatException;

	/**
	 * 删除
	 */
	public void delete(RunCOpStepcheck entity);

	/**
	 * 修改
	 */
	public RunCOpStepcheck update(RunCOpStepcheck entity) throws CodeRepeatException;

	public RunCOpStepcheck findById(Long id);

	/**
	 * 根据操作任务id查找
	 */
	public List<RunCOpStepcheck> findByTaskId(String enterpriseCode,Long operateTaskId);
	public boolean saveAllOperat(List<Map> addList,List<Map> updateList,String delStr,String currentMan,String enterpriseCod) throws CodeRepeatException;
}