package power.ejb.run.securityproduction.safesupervise;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
/**
 *  动态检查明细
 * @author fyyang
 * 2010-04-20
 */
@Remote
public interface SpCDynamicCheckDetailFacadeRemote {
	
	/**
	 * 增加动态检查明细信息
	 * @param entity
	 * @return
	 */
	public SpCDynamicCheckDetail save(SpCDynamicCheckDetail entity);

   /**
    * 删除一条或多条动态检查明细信息
    * @param ids
    */
	public void delete(String ids);

 /**
  * 修改动态检查明细信息
  * @param entity
  * @return
  */
	public SpCDynamicCheckDetail update(SpCDynamicCheckDetail entity);
   
	/**
	 * 通过id查找动态检查明细信息
	 * @param id
	 * @return
	 */
	public SpCDynamicCheckDetail findById(Long id);
	
	/**
	 * 查询动态检查明细信息列表(根据年份及季度)
	 * @param year  年度
	 * @param season 季度
	 * @param enterpriseCode
	 * @param entryBy 填写人
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findDynamicListForCheck(String year,String season,String enterpriseCode,String entryBy,int... rowStartIdxAndCount);
	
	/**
	 * 查询整改计划信息列表
	 * @param year
	 * @param season
	 * @param enterpriseCode
	 * @param dutyBy 整改责任人
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findDynamicListForPlan(String year,String season,String enterpriseCode,String dutyBy,int... rowStartIdxAndCount);
	
	/**
	 * 整改完成情况统计
	 * @param year
	 * @param season
	 * @param enterpriseCode
	 * @param existQuestion  存在问题
	 * @param isFinish 是否完成
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findDynamicListForQuery(String year,String season,String enterpriseCode,String existQuestion,String isFinish,int... rowStartIdxAndCount);
	
	/**
	 * 问题汇总查询 
	 * add by fyyang 20100510
	 * @param monthDate 月份 e.g: '2010-01'
	 * @param enterpriseCode
	 * @param problemKind 问题类别  （6---25项反措）
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject queryDynamicGatherList(String monthDate,String enterpriseCode,String problemKind,int... rowStartIdxAndCount);
	
	/**
	 * 问题汇总明细查询
	 * @param monthDate 月份 e.g: '2010-01'
	 * @param enterpriseCode
	 * @param problemKind 问题类别  （6---25项反措）
	 * @param deptCode 责任部门编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findDynamicDetailForGather(String monthDate,String enterpriseCode,String problemKind,String deptCode,int... rowStartIdxAndCount);
	
	
	/**
	 * 保存导入的数据
	 * add by fyyang 20100514
	 * @param mainEntity
	 * @param detailList
	 */
	public void importInfo(SpCDynamicCheckMain mainEntity,List<SpCDynamicCheckDetail> detailList);
	
	/**
	 * 检查输入的人员名称是否存在
	 * add by fyyang 20100514
	 * @param dutyNames
	 * @param superNames
	 * @return
	 */
	public String  checkInputManName(String dutyNames,String superNames,String dutyCr,String superCr);
}