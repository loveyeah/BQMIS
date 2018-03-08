package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for ZbCReportFacade.
 * 
 * @author drdu 20100603
 */
@Remote
public interface ZbCReportFacadeRemote {

	/**
	 *增加一条指标报表类型树记录
	 * @param entity
	 */
	public void save(ZbCReport entity);

	
	public void delete(ZbCReport entity);

	/**
	 * 修改一条指标报表类型树记录
	 * @param entity
	 * @return
	 */
	public ZbCReport update(ZbCReport entity);

	/**
	 * 根据ID查找一条指标报表类型树记录详细
	 * @param id
	 * @return
	 */
	public ZbCReport findById(Long id);
	
	/**
	 * 根据父编码查找指标报表类型树记录
	 * @param fReportCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<ZbCReport> findReportTreeList(String fReportCode,String enterpriseCode);
	
	/**
	 * 根据报表类型ID查找是否孩子节点
	 * @param reportId
	 * @param enterpriseCode
	 * @return
	 */
	public boolean IfHasChild(String code, String enterpriseCode);
	
	/**
	 * 根据ID，企业编码查找记录详细
	 * @param reportId
	 * @param enterpriseCode
	 * @return
	 */
	public ZbCReport findByCode(String reportId, String enterpriseCode);
	
	/**
	 * 根据ID查找记录详细
	 * @param reportId
	 * @return
	 */
	public Object findReportInfo(String reportCode);
	
}