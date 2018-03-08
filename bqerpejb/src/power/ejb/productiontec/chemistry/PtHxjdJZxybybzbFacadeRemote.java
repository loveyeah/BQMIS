package power.ejb.productiontec.chemistry;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.chemistry.form.ChemistryReportForm;
import power.ejb.workticket.business.RunJWorkticketSafety;

/**
 * 化学在线仪表月报主表
 * @author liuyi 090708
 */
@Remote
public interface PtHxjdJZxybybzbFacadeRemote {
	/**
	 * 增加一条化学在线仪表月报信息
	 */
	public PtHxjdJZxybybzb save(PtHxjdJZxybybzb entity);

	
	/**
	 * 删除一条或多条绝缘仪器仪表月报信息
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);
	/**
	 * 删除一条记录
	 * @param entity
	 */
	public void delete(PtHxjdJZxybybzb entity);

	/**
	 * 修改一条化学在线仪表月报信息
	 */
	public PtHxjdJZxybybzb update(PtHxjdJZxybybzb entity);

	/**
	 * 通过id查找一条记录
	 * @param id
	 * @return
	 */
	public PtHxjdJZxybybzb findById(Long id);


	/**
	 * 查询化学在线仪表月报列表
	 * @param name 机组名称
	 * @param month 月份
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name,String month, String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 根据化学在线仪表月报主表的id和企业编码查询化学在线仪表月报主表、明细表和化学在线仪器维护表
	 * @param zxybybzbId 化学在线仪表月报主表id
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findDetailsAll(String zxybybzbId,String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 
	 */
	public void modifyRecords(List<ChemistryReportForm>list,String zxybybzbId);
}