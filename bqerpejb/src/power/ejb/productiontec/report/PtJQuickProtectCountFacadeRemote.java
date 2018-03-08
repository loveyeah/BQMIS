package power.ejb.productiontec.report;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 快速保护（线路高频保护及元件差动保护）统计报表
 * @author liuyi 0901016
 */
@Remote
public interface PtJQuickProtectCountFacadeRemote {
	
	public void save(PtJQuickProtectCount entity);

	
	public void delete(PtJQuickProtectCount entity);

	
	public PtJQuickProtectCount update(PtJQuickProtectCount entity);

	public PtJQuickProtectCount findById(Long id);

	public List<PtJQuickProtectCount> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);
	
	public List<PtJQuickProtectCount> findAll(int... rowStartIdxAndCount);
	
	public PageObject findAllByMonth(String month,String enterpriseCode,int...rowStartInxAndCount);
	
	public void saveMod(List<PtJQuickProtectCount> addList,List<PtJQuickProtectCount> updateList,String ids);
}