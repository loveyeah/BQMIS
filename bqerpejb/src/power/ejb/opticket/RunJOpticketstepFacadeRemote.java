package power.ejb.opticket;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ejb.opticket.form.OpstepInfo;

/**
 * 操作票项目管理
 */
@Remote
public interface RunJOpticketstepFacadeRemote {
	/**
	 * 增加一条操作票项目
	 * @param RunJOpticketstep entity操作票项目实体
	 * @return RunJOpticketstep 操作票对象
	 */
	public RunJOpticketstep save(RunJOpticketstep entity);

	/**
	 * 删除一条操作票项目
	 * @param RunJOpticketstep entity 操作票项目实体
	 * @return void
	 */
	public void delete(RunJOpticketstep entity);

	/**
	 *更新一条操作票操项目
	 *@param RunJOpticketstep entity 操作票项目实体
	 *@return RunJOpticketstep
	 */
	public RunJOpticketstep update(RunJOpticketstep entity);
	public List<OpstepInfo> findByOpCode(String opticketCode);
	public RunJOpticketstep findById(Long id);
	
	public List<RunJOpticketstep> findByOperateCode(String  opticketCode);
	
	public boolean saveAllOperat(List<Map> addList,List<Map> updateList,String delStr,String currentMan,String enterpriseCod);
}