package power.ejb.manage.contract.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.shift.RunCUnitprofession;


/**
 * @author slTang
 *
 */
@Remote
public interface ConJArchivesFacadeRemote {
	/**
	 * 增加案卷
	 */
	public void save(ConJArchives entity);

	/**
	 *删除案卷
	 */
	public void delete(ConJArchives entity);

	/**
	 * 修改案卷
	 */
	public ConJArchives update(ConJArchives entity);
	
	/**
	 * 批量删除案卷
	 * @param archivesIds String 案卷id集
	 * @return  删除的行数 
	 */
	public int deleteMu(String archivesIds);
	
	public ConJArchives findById(Long id);
	
	public PageObject findArchives(Long conTypeId,String enterpriseCode,final int... rowStartIdxAndCount);
	/**
	 * 模糊查询
	 */
	public PageObject fuzzyQuery(String enterpriseCode,String undertakeNo,final int... rowStartIdxAndCount);
	/**
	 * 间隔查询 
	 */
	public PageObject separatedQuery(String enterpriseCode,String[] undertakeNo,final int... rowStartIdxAndCount);
	/**
	 * 区间查询
	 */
	public PageObject intervalQuery(String enterpriseCode,String undertakeNo1,String undertakeNo2,final int... rowStartIdxAndCount);
	/**
	 * 档号查询
	 */
	public List<ConJArchives> findfileNoList(String enterprisecode,Long conTypeId);
}