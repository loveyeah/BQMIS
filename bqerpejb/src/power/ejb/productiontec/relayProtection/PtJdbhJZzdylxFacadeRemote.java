package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 保护装置对应保护类型
 * @author liuyi 090714
 */
@Remote
public interface PtJdbhJZzdylxFacadeRemote {
	/**
	 * 新增一条保护装置对应保护类型数据
	 */
	public void save(PtJdbhJZzdylx entity);

	/**
	 * 删除一条保护装置对应保护类型数据
	 */
	public void delete(PtJdbhJZzdylx entity);

	/**
	 *更新一条保护装置对应保护类型数据
	 */
	public PtJdbhJZzdylx update(PtJdbhJZzdylx entity);

	/**
	 * 通过Id查找一条保护装置对应保护类型数据
	 * @param id
	 * @return
	 */
	public PtJdbhJZzdylx findById(PtJdbhJZzdylxId id);


	/**
	 * Find all PtJdbhJZzdylx entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJdbhJZzdylx> all PtJdbhJZzdylx entities
	 */
	public List<PtJdbhJZzdylx> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 保护装置对应保护类型表中的数据进行增加或删除
	 */
	public void saveDevicesAndTypes(String devId,String typeIds,String enterpriseCode);
	
	

	/**
	 * 保护装置对应保护类型表中的数据查找
	 */
	public PageObject findDeviceProList(String devId,int... rowStartIdxAndCount);
}