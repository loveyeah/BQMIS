package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.chemistry.form.ChemistryReportForm;

/**
 * 继电试验报告记录
 * @author liuyi 090720
 */
@Remote
public interface PtJdbhJSybgjlFacadeRemote {
	/**
	 * 新增一条继电试验报告记录
	 */
	public PtJdbhJSybgjl save(PtJdbhJSybgjl entity);

	/**
	 *删除一条继电试验报告记录
	 */
	public void delete(PtJdbhJSybgjl entity);

	/**
	 *更新一条继电试验报告记录
	 */
	public PtJdbhJSybgjl update(PtJdbhJSybgjl entity);

	/**
	 * 通过id查找一条继电试验报告记录
	 * @param id
	 * @return
	 */
	public PtJdbhJSybgjl findById(Long id);

	/**
	 * 查询继电试验报告记录列表
	 * @param jdsybgId 试验报告编号
	 * @param enterpriseCode 企业编号
	 */
	public PageObject findAllReprotDetails(String jdsybgId,String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 
	 */
	public void modifyRecords(List<PtJdbhJSybgjl>list);
}