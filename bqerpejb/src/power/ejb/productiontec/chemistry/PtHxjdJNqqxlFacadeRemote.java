package power.ejb.productiontec.chemistry;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.chemistry.form.CondenserLeakForm;

/**
 * Remote interface for PtHxjdJNqqxlFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtHxjdJNqqxlFacadeRemote {
	
	/**
	 * 增加一条凝汽器泄漏台帐记录
	 * @param entity
	 * @return
	 */
	public PtHxjdJNqqxl save(PtHxjdJNqqxl entity);

	/**
	 * 删除一条或多条凝汽器泄漏台帐记录
	 * @param ids
	 */
	public void deleteMulti(String ids) ;

	/**
	 * 修改一条凝汽器泄漏台帐记录
	 * @param entity
	 * @return
	 */
	public PtHxjdJNqqxl update(PtHxjdJNqqxl entity);

	/**
	 * 根据ID查找一条凝汽器泄漏台帐信息
	 * @param id
	 * @return
	 */
	public PtHxjdJNqqxl findById(Long id);

	/**
	 * 修改主表，明细表记录
	 * @param list
	 * @param zxybybzbId
	 */
	public void modifyRecords(List<CondenserLeakForm> list, String nqjxlId);
	/**
	 * 根据机组、凝结水水质，时间段查找列表信息
	 * @param deviceCode
	 * @param sDate
	 * @param eDate
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findCondenserLeakList(String deviceCode,String sDate,String eDate,String enterpriseCode,final int... rowStartIdxAndCount);
}