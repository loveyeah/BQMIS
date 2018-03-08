package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJShiftParmFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJShiftParmFacadeRemote {
	
	/**
	 * 增加一条交接班参数信息
	 * @param entity
	 */
	public void save(RunJShiftParm entity);

	/**
	 * 删除一条交接班参数信息
	 * @param entity
	 */
	public void delete(RunJShiftParm entity);

	/**
	 * 修改一条交接班参数信息
	 * @param entity
	 */
	public RunJShiftParm update(RunJShiftParm entity);

	/**
	 * 获得一条交接班参数信息
	 * @param id
	 * @return
	 */
	public RunJShiftParm findById(Long id);

    /**
     * 查询所有交接班参数信息
     * @param rowStartIdxAndCount
     * @return
     */
	public List<RunJShiftParm> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 根据运行日志id查询交接班参数列表
	 * @param runLogId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findParmList(Long runLogId,final int... rowStartIdxAndCount);
	
	/**
	 * 根据原运行日志id插入新运行日志id的交接班参数
	 * @param oldRunLogId
	 * @param newRunLongId
	 */
	public void insertNewShiftParm(Long oldRunLogId,Long newRunLongId);
	/**
	 * 查询最新运行日志id
	 * @param specialCode
	 * @param enterprisecode
	 * @return
	 */
	public Long findMaxLogid(String specialCode,String enterprisecode);
	/**
	 * 根据运行日志id和维护参数id获取运行日志参数id
	 * @param runlogid
	 * @param parmId
	 * @param enterpriseCode
	 * @return
	 */
	public Long findIdByparmid(Long runlogid,Long parmId,String enterpriseCode);
	/**
	 * 获取运行日志号（运行日志参数查询用）
	 * @param specialcode
	 * @param enterprisecode
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	public List getDistinctRunlog(String specialcode,String enterprisecode,String fromdate,String todate);
	/**
	 * 获取运行参数（运行日志参数查询用）
	 * @param specialcode
	 * @param enterprisecode
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	public List getDistinctParm(String specialcode,String enterprisecode,String fromdate,String todate);
	/**
	 * 获取运行参数值（运行日志参数查询用）
	 * @param enterprisecode
	 * @param parmId
	 * @param runlogno
	 * @return
	 */
	public RunJShiftParm queryRunlogParm(String enterprisecode,Long parmId,String runlogno);
}