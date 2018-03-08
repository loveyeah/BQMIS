package power.ejb.hr.labor;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 一次性劳保发卡统计表录入（明细）
 * 
 * @author fyyang 20100624
 */


 
@Remote
public interface HrJLaborSendcardDetailFacadeRemote {
	public  void findByMainIdandSave(Long mainId,Long ID,String workCode,String enterpriseCode);
	/**
	 * 查询距现在查询日期最近的记录的主表ID
	 * @param sendYear
	 * @param sendKind
	 * @param enterpriseCode
	 * @return
	 */
//    public Long     findMaxYear(String sendKind,String enterpriseCode); 
	public Long    findMaxYear(String sendYear, String sendKind,String enterpriseCode);
    
	/**保存一条明细记录
	 * @param entity
	 * @return
	 */
	public HrJLaborSendcardDetail save(HrJLaborSendcardDetail entity);
	
	/**保存一条填写的主记录和多条明细记录
	 * @param sendYear
	 * @param sendKind
	 * @param costItem
	 * @param enterpriseCode
	 * @param workCode
	 * @param addlist
	 * @param updatelist
	 */
	public void saveMainAndDetail(String flag,String sendYear, String sendKind,String costItem,String enterpriseCode,String workCode,
			List<HrJLaborSendcardDetail> addlist,
			List<HrJLaborSendcardDetail> updatelist) ;
	
	/**删除一条或者多条明细记录
	 * @param ids
	 */
	public void delete(String ids);

	
	/**修改一条明细记录
	 * @param entity
	 * @return
	 */
	public HrJLaborSendcardDetail update(HrJLaborSendcardDetail entity);

	
	/**根据ID  查找一条明细记录
	 * @param id
	 * @return
	 */
	public HrJLaborSendcardDetail findById(Long id);

	/**导入一条或者多条记录到明细表
	 * @param mainModel
	 * @param detailList
	 */
	public void insertSendcardDetail(HrJLaborSendcard mainModel,List<HrJLaborSendcardDetail> detailList);
	
	/**根据年份和季度查询一条主表记录所对应的所有明细记录
	 * @param sendYear
	 * @param sendKind
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findSendcardDetailList(String flag,String sendYear,String sendKind,int... rowStartIdxAndCount);
	
}