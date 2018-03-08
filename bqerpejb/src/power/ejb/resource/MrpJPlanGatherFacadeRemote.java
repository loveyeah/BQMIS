package power.ejb.resource;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for MrpJPlanGatherFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface MrpJPlanGatherFacadeRemote {
	
	/**
     * 增加一条需求计划物料汇总记录
     *
     * @param entity 要增加的记录
     * @return entity 增加后记录
     */
	public MrpJPlanGather save(MrpJPlanGather entity);
	
	public void delete(MrpJPlanGather entity);

	/**
     * 修改一条需求计划物料汇总记录
     *
     * @param entity 要增加的记录
     * @return entity 增加后记录
     */
	public MrpJPlanGather update(MrpJPlanGather entity);

	/**
	 * 根据需求计划汇总表ID查询一条记录
	 * @param id
	 * @return MrpJPlanGather
	 */
	public MrpJPlanGather findById(Long id);

	public List<MrpJPlanGather> findAll();
	
	/**
     * 物料需求计划物料汇总明细
     * add by ywliu 090414
     * modify by fyyang 090522
     * modify by fyyang 20100505  flag=1----查询出所有已汇总的物资；  2----查询出已汇总未报价确定供应商的物资
     * @param rowStartIdxAndCount
     * @return
     */
//    public PageObject getMaterialGatherDetail(String materialName,String buyer,String enterpriseCode,String sdate,String edate,String flag,final int... rowStartIdxAndCount);
    //增加查询条件 mrBy、mrDept update by sychen 20100511 
	public PageObject getMaterialGatherDetail(String materialName,
			String buyer, String enterpriseCode, String sdate, String edate,String flag,
			String mrBy, String mrDept,int... rowStartIdxAndCount);
    /**
	 * add by liuyi 091104
	 * 批量修改需求计划汇总采购员
	 * @param ids
	 * @param buyer
	 */
	public void chooserBuyer(String ids,String buyer);
	
	/**
	 * add by liyi 091107
	 * 汇总退回
	 * @param backId
	 * @param backReason
	 */
	public void gatherBack(String backId,String backReason);
	/**
	 * 汇总操作 
	 * add by fyyang 091214
	 * @param detailIds
	 * @param buyer
	 * @param enterpriseCode
	 * @param workCode
	 */
	  public void doGather(String detailIds,String buyer,String enterpriseCode,String workCode);
}