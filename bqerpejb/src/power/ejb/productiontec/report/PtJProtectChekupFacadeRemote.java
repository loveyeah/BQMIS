package power.ejb.productiontec.report;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 继电保护装置定检完成情况报表
 * @author liuyi 091016
 */
@Remote
public interface PtJProtectChekupFacadeRemote {
	/**
	 * 新增一条继电保护装置定检完成情况报表数据
	 */
	public void save(PtJProtectChekup entity);

	/**
	 * 删除一条继电保护装置定检完成情况报表数据
	 */
	public void delete(PtJProtectChekup entity);

	/**
	 * 更新一条继电保护装置定检完成情况报表数据
	 */
	public PtJProtectChekup update(PtJProtectChekup entity);

	public PtJProtectChekup findById(Long id);

	
	public List<PtJProtectChekup> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<PtJProtectChekup> findAll(int... rowStartIdxAndCount);
	
	public PageObject findAllByMonth(String month,String enterpriseCode,int...rowStartInxAndCount);
	
	public void saveMod(List<PtJProtectChekup> addList,List<PtJProtectChekup> updateList,String ids);
}