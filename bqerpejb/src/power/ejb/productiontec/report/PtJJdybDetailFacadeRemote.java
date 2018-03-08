package power.ejb.productiontec.report;

import java.util.List;
import javax.ejb.Remote;

/**
 * 继电月报表（明细表）
 * @author liuyi 091010
 */
@Remote
public interface PtJJdybDetailFacadeRemote {
	/**
	 * 新增一条继电月报表（明细表）数据
	 */
	public void save(PtJJdybDetail entity);

	/**
	 * 删除一条继电月报表（明细表）数据
	 */
	public void delete(PtJJdybDetail entity);

	/**
	 * 更新一条继电月报表（明细表）数据
	 */
	public PtJJdybDetail update(PtJJdybDetail entity);

	public PtJJdybDetail findById(Long id);

	public List<PtJJdybDetail> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<PtJJdybDetail> findAll(int... rowStartIdxAndCount);
	
	public void treatRecords(List<PtJJdybDetail> addList,List<PtJJdybDetail> updateList,String ids);
}