package power.ejb.opticket.bussiness;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for RunJOpStepcheckFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJOpStepcheckFacadeRemote {
	/**
	 * 增加
	 */
	public void save(RunJOpStepcheck entity) throws CodeRepeatException;

	/**
	 * 删除
	 */
	public void delete(RunJOpStepcheck entity);

	/**
	 * 修改
	 */
	public RunJOpStepcheck update(RunJOpStepcheck entity) throws CodeRepeatException;

	public RunJOpStepcheck findById(Long id);

	public List<RunJOpStepcheck> findByOpticketCode(String opticketCode);
	public boolean saveAllOperat(List<Map> addList,List<Map> updateList,String delStr,String currentMan,String enterpriseCod);
}