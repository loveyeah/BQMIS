package power.ejb.productiontec.chemistry;

import java.util.List;
import javax.ejb.Remote;

/**
 * 化学在线仪表月报明细表
 * @author liuyi 090709
 */
@Remote
public interface PtHxjdJZxybybFacadeRemote {
	/**
	 * 增加一条化学在线仪表月报明细表记录
	 */
	public PtHxjdJZxybyb save(PtHxjdJZxybyb entity);

	/**
	 * 删除一条化学在线仪表月表明细表记录
	 */
	public void delete(PtHxjdJZxybyb entity);

	
	/**
	 * 删除一条或多条化学在线仪表月报明细表信息
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);
	
	/**
	 *更新一条化学在线仪表月报明细表记录
	 */
	public PtHxjdJZxybyb update(PtHxjdJZxybyb entity);

	/**
	 * 通过id查找一条化学在线仪表月报明细表记录
	 * @param id
	 * @return
	 */
	public PtHxjdJZxybyb findById(Long id);

	
	/**
	 * Find all PtHxjdJZxybyb entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtHxjdJZxybyb> all PtHxjdJZxybyb entities
	 */
	public List<PtHxjdJZxybyb> findAll(int... rowStartIdxAndCount);
}