package power.ejb.productiontec.report;

import java.util.List;
import javax.ejb.Remote;

/**
 * 继电月报表（主表）
 * @author liuyi 091010
 */
@Remote
public interface PtJJdybFacadeRemote {
	/**
	 * 新增一条继电月报表（主表）数据
	 */
	public PtJJdyb save(PtJJdyb entity);

	/**
	 * 删除一条继电月报表（主表）数据
	 */
	public void delete(PtJJdyb entity);

	/**
	 * 更新一条继电月报表（主表）数据
	 */
	public PtJJdyb update(PtJJdyb entity);

	public PtJJdyb findById(Long id);

	
	public List<PtJJdyb> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	
	public List<PtJJdyb> findAll(int... rowStartIdxAndCount);
	
	public List<PtJJdyb> findSeasonRec(String fromMonth,String toMonth);
}