package power.ejb.run.securityproduction.safesupervise;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJWeldingRepairFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJWeldingRepairFacadeRemote {
	/**
	 * 保存电动工器具和电焊机检验记录
	 * 
	 * @return
	 */
	public void save(SpJWeldingRepair entity);

	public void delete(SpJWeldingRepair entity);

	/**
	 * 更新电动工器具和电焊机检验记录
	 * 
	 * @return
	 */

	public SpJWeldingRepair update(SpJWeldingRepair entity);

	public SpJWeldingRepair findById(Long id);

	public List<SpJWeldingRepair> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<SpJWeldingRepair> findAll(int... rowStartIdxAndCount);

	/**
	 * 查询电动工器具和电焊机检验记录
	 * 
	 * @return
	 */
	public PageObject getWelding(String beginTime, String endTime,
			String toolCode, String toolType, String enterprise,
			String isMaint, String fillBy, int... rowStartIdxAndCount);

	/**
	 * 删除电动工器具和电焊机检验记录 一条或多条
	 * 
	 * @return
	 */
	public String deleteWeldinglist(String ids);
}