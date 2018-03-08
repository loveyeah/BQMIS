package power.ejb.opticket.bussiness;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * @author slTang
 */
@Remote
public interface RunJOpFinworkFacadeRemote {
	/**
	 * 增加
	 */
	public void save(RunJOpFinwork entity) throws CodeRepeatException;

	/**
	 * 删除
	 */
	public void delete(RunJOpFinwork entity);

	/**
	 * 修改
	 */
	public RunJOpFinwork update(RunJOpFinwork entity) throws CodeRepeatException;

	public RunJOpFinwork findById(Long id);
	
	public List<RunJOpFinwork> findByOpticketCode(String opticketCode);
	public boolean saveAllOperat(List<Map> addList,List<Map> updateList,String delStr,String currentMan,String enterpriseCode);
}