package power.ejb.opticket.bussiness;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for RunJOpMeasuresFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJOpMeasuresFacadeRemote {
	/**
	 * 增加
	 */
	public void save(RunJOpMeasures entity) throws CodeRepeatException;

	/**
	 * 删除
	 */
	public void delete(RunJOpMeasures entity);

	/**
	 * 修改
	 */
	public RunJOpMeasures update(RunJOpMeasures entity) throws CodeRepeatException;

	public RunJOpMeasures findById(Long id);

	public List<RunJOpMeasures> findByOpticketCode(String opticketCode);
	public boolean saveAllOperat(List<Map> addList,List<Map> updateList,String delStr,String currentMan,String enterpriseCod);
}