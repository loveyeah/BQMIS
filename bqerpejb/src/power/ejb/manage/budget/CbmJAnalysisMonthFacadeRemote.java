package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;
import javax.sound.midi.VoiceStatus;

import power.ear.comm.ejb.PageObject;

/**
 * 部门月度预算分析
 * 
 * @author liuyi 090813
 */
@Remote
public interface CbmJAnalysisMonthFacadeRemote {
	/**
	 * 新增一条部门月度预算分析记录
	 */
	public void save(CbmJAnalysisMonth entity);

	/**
	 * 删除一条部门月度预算分析记录
	 */
	public void delete(CbmJAnalysisMonth entity);

	/**
	 * 删除一条或多条部门月度预算分析记录
	 */
	public void delete(String ids);

	/**
	 * 更新一条部门月度预算分析记录
	 */
	public CbmJAnalysisMonth update(CbmJAnalysisMonth entity);

	/**
	 * 通过id查找一条部门月度预算分析记录
	 * 
	 * @param id
	 * @return
	 */
	public CbmJAnalysisMonth findById(Long id);

	public PageObject findAnalysisMonthList(Long centerId, String dataTime,
			String enterpriseCode, int... rowStartIdxAndCount);

	public void saveAnalysisMonthModified(List<CbmJAnalysisMonth> addList,
			List<CbmJAnalysisMonth> updateList);
}