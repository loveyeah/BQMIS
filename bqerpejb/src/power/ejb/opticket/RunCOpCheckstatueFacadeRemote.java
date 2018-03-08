package power.ejb.opticket;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCOpCheckstatueFacade.
 * 
 * @author slTang
 */
@Remote
public interface RunCOpCheckstatueFacadeRemote {
	/**
	 * 增加
	 */
	public void save(RunCOpCheckstatue entity);

	/**
	 * 删除
	 */
	public void delete(RunCOpCheckstatue entity);

	/**
	 * 修改
	 */
	public RunCOpCheckstatue update(RunCOpCheckstatue entity);

	public RunCOpCheckstatue findById(Long id);

	/**
	 * 查找所有
	 */
	public List<RunCOpCheckstatue> findAll(String enterpriseCode);
	
	/**
	 * 查找公共
	 * @param checkBefFlag "C"
	 */
	public List<RunCOpCheckstatue> findPublic(String enterpriseCode,String checkBefFlag,String isRunning);
}