package power.ejb.opticket;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * @author slTang
 */
@Remote
public interface RunCOpticketstepFacadeRemote {
	/**
	 * 增加
	 */
	public void save(RunCOpticketstep entity) throws CodeRepeatException;

	/**
	 * 删除
	 */
	public void delete(RunCOpticketstep entity);

	/**
	 * 修改
	 */
	public RunCOpticketstep update(RunCOpticketstep entity) throws CodeRepeatException;

	public RunCOpticketstep findById(Long id);

	public List<RunCOpticketstep> findFinworkByTask(String enterpriseCode,Long operateTaskId);
	public boolean saveAllOperat(List<Map> addList,List<Map> updateList,String delStr,String currentMan,String enterpriseCod) throws CodeRepeatException;

}