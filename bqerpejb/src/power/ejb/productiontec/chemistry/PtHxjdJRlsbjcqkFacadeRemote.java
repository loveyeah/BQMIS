package power.ejb.productiontec.chemistry;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtHxjdJRlsbjcqkFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtHxjdJRlsbjcqkFacadeRemote {
	
	/**
	 * 增加一条热力设备检查情况记录
	 * @param entity
	 * @return
	 */
	public PtHxjdJRlsbjcqk save(PtHxjdJRlsbjcqk entity);
	
	/**
	 * 删除一条或多条热力设备检情况记录
	 * @param ids
	 */
	public void deleteMulti(String ids);
	
	/**
	 * 修改一条热力设备检查情况记录
	 * @param entity
	 * @return
	 */
	public PtHxjdJRlsbjcqk update(PtHxjdJRlsbjcqk entity);

	/**
	 * 根据ID查找一条热力设备检查情况信息
	 * @param id
	 * @return
	 */
	public PtHxjdJRlsbjcqk findById(Long id);

	/**
	 * 根据企业编码，检查时间段，机组查找热力设备检查记录
	 * @param enterpriseCode
	 * @param deviceCode
	 * @param sDate
	 * @param eDate
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findThermalEquCheckList(String enterpriseCode,String deviceCode,String sDate,String eDate,final int... rowStartIdxAndCount);
}