package power.ejb.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.MrpJPlanRequirementDetailInfo;

/**
 * 物料需求计划明细
 * 
 * @see power.ejb.logistics.MrpJPlanRequirementDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class MrpJPlanRequirementDetailFacade implements
		MrpJPlanRequirementDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @return entity 增加后记录
     */
	public MrpJPlanRequirementDetail save(MrpJPlanRequirementDetail entity) {
		LogUtil.log("saving MrpJPlanRequirementDetail instance", Level.INFO,
				null);
		try {
			// 流水号自动采番
		    entity.setRequirementDetailId(bll.getMaxId("MRP_J_PLAN_REQUIREMENT_DETAIL", "REQUIREMENT_DETAIL_ID"));
		    // 设定修改时间
            entity.setLastModifiedDate(new Date());
            // 设定是否使用
            entity.setIsUse("Y");
            
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条记录
	 *
	 * @param entity 采购订单与需求计划关联对象
	 * @throws RuntimeException when the operation fails
	 */
	public void delete(MrpJPlanRequirementDetail entity) {
		LogUtil.log("deleting MrpJPlanRequirementDetail instance", Level.INFO,
				null);
		try {
			// 是否使用设为N
			entity.setIsUse("N");
			// 设定修改时间
			entity.setLastModifiedDate(new Date());
			
			// 更新
			update(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

    /**
     * 修改记录
     *
     * @param entity 要修改的记录
     * @return entity 修改后记录
     */
	public MrpJPlanRequirementDetail update(MrpJPlanRequirementDetail entity) {
		LogUtil.log("updating MrpJPlanRequirementDetail instance", Level.INFO,
				null);
		try {
			// 设定修改时间
			entity.setLastModifiedDate(new Date());
			MrpJPlanRequirementDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public MrpJPlanRequirementDetail findById(Long id,String enterpriseCode) {
		LogUtil.log(
				"finding MrpJPlanRequirementDetail instance with id: " + id,
				Level.INFO, null);
		try {
			MrpJPlanRequirementDetail instance = entityManager.find(
					MrpJPlanRequirementDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all MrpJPlanRequirementDetail entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the MrpJPlanRequirementDetail property to query
	 * @param value
	 *            the property value to match
	 * @return List<MrpJPlanRequirementDetail> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<MrpJPlanRequirementDetail> findByProperty(String enterpriseCode,String propertyName,
			final Object value) {
		LogUtil.log(
				"finding MrpJPlanRequirementDetail instance with property: "
						+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from MrpJPlanRequirementDetail model where model."
					+ propertyName + "= :propertyValue and ENTERPRISE_CODE='"+enterpriseCode+"' and IS_USE='Y'";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all MrpJPlanRequirementDetail entities.
	 * 
	 * @return List<MrpJPlanRequirementDetail> all MrpJPlanRequirementDetail
	 *         entities
	 */
	@SuppressWarnings("unchecked")
	public List<MrpJPlanRequirementDetail> findAll() {
		LogUtil.log("finding all MrpJPlanRequirementDetail instances",
				Level.INFO, null);
		try {
			final String queryString = "select model from MrpJPlanRequirementDetail model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public List<MrpJPlanRequirementDetailInfo> findMaterialByWorkCode(String workCode,String enterpriseCode)
	{
		List<MrpJPlanRequirementDetailInfo> resultList=new ArrayList<MrpJPlanRequirementDetailInfo>();
		String sql=
			"select  distinct e.material_name,e.material_no,a.requirement_head_id\n" +
			"  from mrp_j_plan_requirement_detail a,\n" + 
			"       PUR_J_PLAN_ORDER              b,\n" + 
			"       PUR_J_ORDER_DETAILS           c,\n" + 
			"       mrp_j_plan_requirement_head   d,\n" + 
			"       inv_c_material e\n" + 
			" where a.requirement_detail_id = b.requirement_detail_id\n" + 
			"   and b.pur_no = c.pur_no\n" + 
			"   and a.requirement_head_id = d.requirement_head_id\n" + 
			"   and e.material_id=a.material_id\n" + 
			"    and c.material_id=a.material_id \n"+ //and by fyyang 20100325
			"   and d.mr_by = '"+workCode+"'\n" + 
			"   and a.approved_qty>nvl(a.iss_qty,0)\n" + 
			"   and   c.rcv_qty>0 and c.rcv_qty>nvl(a.iss_qty,0) \n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and c.is_use = 'Y'\n" + 
			"   and c.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and d.is_use = 'Y'\n" + 
			"   and d.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and e.is_use='Y'\n" + 
			"   and e.enterprise_code='"+enterpriseCode+"'";
		
	      List  list=bll.queryByNativeSQL(sql);
	      Iterator  it=list.iterator();
	      while(it.hasNext())
	      {
	    	  MrpJPlanRequirementDetailInfo model=new MrpJPlanRequirementDetailInfo();
	    	  Object [] data=(Object [])it.next();
	    	  if(data[0]!=null)
	    		  model.setMaterialName(data[0].toString());
	    	  if(data[1]!=null)
	    		  model.setMaterialNo(data[1].toString());
	    	  if(data[2]!=null)
	    		  model.setMrNo(data[2].toString());
	    	   resultList.add(model);
	      }
		
		return resultList;

	}
	
	/*
	 * 查询该汇总对应的需求物资明细
	 * add by fyyang 091105
	 */
	public List<MrpJPlanRequirementDetailInfo> getMaterialDetailByGatherId(Long gatherId)
	{
		List<MrpJPlanRequirementDetailInfo> resultList=new ArrayList<MrpJPlanRequirementDetailInfo>();
		String sql=
			"select t.requirement_detail_id,\n" +
			"       b.material_no,\n" + 
			"       b.material_name,\n" + 
			"       b.spec_no,\n" + 
			"       GETUNITNAME(b.stock_um_id),\n" + 
			"       GETWORKERNAME(c.mr_by),\n" + 
			"       GETDEPTNAME(c.mr_dept),\n" + 
			"       c.mr_reason,\n" + 
			"       t.applied_qty,\n" + 
			"       t.approved_qty,\n" + 
			"       c.plan_original_id,\n" + 
			"       t.estimated_price,t.due_date,t.supplier,b.factory \n"+
			"  from mrp_j_plan_requirement_detail t,\n" + 
			"       INV_C_MATERIAL                b,\n" + 
			"       mrp_j_plan_requirement_head   c\n" + 
			" where t.material_id = b.material_id\n" + 
			"   and t.requirement_head_id = c.requirement_head_id\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and instr(',' || (select a.requirement_detail_ids\n" + 
			"                       from mrp_j_plan_gather a\n" + 
			"                      where a.gather_id = "+gatherId+") || ',',\n" + 
			"             ',' || t.requirement_detail_id || ',') <> 0\n" + 
			"   and t.is_use = 'Y'";
		
         List list=bll.queryByNativeSQL(sql);
         Iterator it= list.iterator();
         while(it.hasNext())
         {
        	 Object [] data=(Object [])it.next();
        	 MrpJPlanRequirementDetailInfo model=new MrpJPlanRequirementDetailInfo();
        	 if(data[0]!=null)
        	 {
        		 model.setRequirementDetailId(Long.parseLong(data[0].toString()));
        	 }
        	 if(data[1]!=null)
        	 {
        		 model.setMaterialNo(data[1].toString());
        	 }
        	 if(data[2]!=null)
        	 {
        		 model.setMaterialName(data[2].toString());
        	 }
        	 if(data[3]!=null)
        	 {
        		 model.setMaterSize(data[3].toString());
        	 }
        	 if(data[4]!=null)
        	 {
        		 model.setStockUmName(data[4].toString());
        	 }
        	 if(data[5]!=null)
        	 {
        		model.setApplyByName(data[5].toString()); 
        	 }
        	 if(data[6]!=null)
        	 {
        		 model.setApplyDeptName(data[6].toString());
        	 }
        	 if(data[7]!=null)
        	 {
        		 model.setApplyReason(data[7].toString());
        	 }
        	 if(data[8]!=null)
        	 {
        		 model.setAppliedQty(Double.parseDouble(data[8].toString()));
        	 }
        	 if(data[9]!=null)
        	 {
        		 model.setApprovedQty(Double.parseDouble(data[9].toString()));
        	 }
        	 if(data[10]!=null)
        	 {
        		 model.setPlanOriginalId(data[10].toString());
        	 }
        	 if(data[11]!=null)
        	 {
        		 model.setEstimatedPrice(Double.parseDouble(data[11].toString()));
        	 }
        	 if(data[12]!=null)
        	 {
        		 model.setDueDate(data[12].toString());
        	 }
        	 if(data[13]!=null)
        	 {
        		 model.setSupplier(data[13].toString());
        	 }
        	 if(data[14]!=null)
        	 {
        		 model.setFactory(data[14].toString());
        	 }
        	 resultList.add(model);
         }
         return resultList;
	}
	
	/**
	 * 根据领料单号或采购单号查询其对应的需求计划单信息
	 * add  by fyyang 20100318
	 * @param orderNo
	 * @param flag 1--采购单 2--领料单
	 * @return
	 */
	public List<MrpJPlanRequirementDetailInfo> getMaterialDetailByPurOrIssue(String orderNo,String flag)
	{
		List<MrpJPlanRequirementDetailInfo> resultList=new ArrayList<MrpJPlanRequirementDetailInfo>();
		String sql=
			"select t.requirement_detail_id,\n" +
			"             b.material_no,\n" + 
			"             b.material_name,\n" + 
			"             b.spec_no,\n" + 
			"             GETUNITNAME(b.stock_um_id),\n" + 
			"             GETWORKERNAME(c.mr_by),\n" + 
			"             GETDEPTNAME(c.mr_dept),\n" + 
			"             c.mr_reason,\n" + 
			"             t.applied_qty,\n" + 
			"             t.approved_qty,\n" + 
			"             c.plan_original_id,\n" + 
			"             t.estimated_price,t.due_date,t.supplier,b.factory ,c.wf_no\n" + 
			"        from mrp_j_plan_requirement_detail t,\n" + 
			"             INV_C_MATERIAL                b,\n" + 
			"             mrp_j_plan_requirement_head   c ,\n" ;
		if(flag.equals("1"))
		{
			sql+="             pur_j_plan_order d  where d.requirement_detail_id=t.requirement_detail_id\n" +
			"and d.pur_no='"+orderNo+"' \n";
		}
		 if(flag.equals("2"))
		{
			sql+="            inv_j_issue_head d,inv_j_issue_details e where e.issue_head_id=d.issue_head_id  and e.requirement_detail_id=t.requirement_detail_id and d.MR_NO=c.mr_no and d.issue_no='"+orderNo+"' \n";
		}
			
			
		sql+=	"       and t.material_id = b.material_id\n" + 
			"         and t.requirement_head_id = c.requirement_head_id\n" + 
			
			"         and d.is_use='Y'\n" + 
			"         and d.enterprise_code='hfdc'\n" + 
			"           and b.is_use = 'Y'\n" + 
			"         and t.is_use = 'Y'";

		
		
         List list=bll.queryByNativeSQL(sql);
         Iterator it= list.iterator();
         while(it.hasNext())
         {
        	 Object [] data=(Object [])it.next();
        	 MrpJPlanRequirementDetailInfo model=new MrpJPlanRequirementDetailInfo();
        	 if(data[0]!=null)
        	 {
        		 model.setRequirementDetailId(Long.parseLong(data[0].toString()));
        	 }
        	 if(data[1]!=null)
        	 {
        		 model.setMaterialNo(data[1].toString());
        	 }
        	 if(data[2]!=null)
        	 {
        		 model.setMaterialName(data[2].toString());
        	 }
        	 if(data[3]!=null)
        	 {
        		 model.setMaterSize(data[3].toString());
        	 }
        	 if(data[4]!=null)
        	 {
        		 model.setStockUmName(data[4].toString());
        	 }
        	 if(data[5]!=null)
        	 {
        		model.setApplyByName(data[5].toString()); 
        	 }
        	 if(data[6]!=null)
        	 {
        		 model.setApplyDeptName(data[6].toString());
        	 }
        	 if(data[7]!=null)
        	 {
        		 model.setApplyReason(data[7].toString());
        	 }
        	 if(data[8]!=null)
        	 {
        		 model.setAppliedQty(Double.parseDouble(data[8].toString()));
        	 }
        	 if(data[9]!=null)
        	 {
        		 model.setApprovedQty(Double.parseDouble(data[9].toString()));
        	 }
        	 if(data[10]!=null)
        	 {
        		 model.setPlanOriginalId(data[10].toString());
        	 }
        	 if(data[11]!=null)
        	 {
        		 model.setEstimatedPrice(Double.parseDouble(data[11].toString()));
        	 }
        	 if(data[12]!=null)
        	 {
        		 model.setDueDate(data[12].toString());
        	 }
        	 if(data[13]!=null)
        	 {
        		 model.setSupplier(data[13].toString());
        	 }
        	 if(data[14]!=null)
        	 {
        		 model.setFactory(data[14].toString());
        	 }
        	 if(data[15]!=null)
        	 {
        		 model.setWfNo(data[15].toString());
        	 }
        	 resultList.add(model);
         }
         return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<MrpJPlanRequirementDetailInfo> findBackOrCancelInfoByWorkCode(String workCode,String enterpriseCode)
	{
		List<MrpJPlanRequirementDetailInfo> resultList=new ArrayList<MrpJPlanRequirementDetailInfo>();
		String sql=
			"select t.mr_no,\n" +
			"       a.cancel_date,\n" + 
			"       a.is_use,\n" + 
			"       c.material_no,\n" + 
			"       c.material_name,\n" + 
			"       a.cancel_reason\n" + 
			"  from mrp_j_plan_requirement_detail a,\n" + 
			"       inv_c_material                c,\n" + 
			"       mrp_j_plan_requirement_head   t\n" + 
			" where a.is_use = 'C'\n" + 
			"   and a.material_id = c.material_id\n" + 
			"   and t.requirement_head_id = a.requirement_head_id\n" + 
			"   and c.is_use = 'Y'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and c.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.mr_by = '"+workCode+"'\n" + 
			"   and a.cancel_date >= sysdate - 7\n" + 
			"union\n" + 
			"select t.mr_no, t.mr_date, t.mr_status, '', '', ''\n" + 
			"  from mrp_j_plan_requirement_head t,\n" + 
			"       (select h.mr_no, max(h.approve_date) backDate\n" + 
			"          from mrp_j_plan_requirement_his h\n" + 
			"         where h.approve_status = '9'\n" + 
			"         group by h.mr_no) b\n" + 
			" where t.mr_by = '"+workCode+"'\n" + 
			"   and t.mr_status = '9'\n" + 
			"   and t.mr_no = b.mr_no\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and b.backDate >= sysdate - 7";
		
		  List list=bll.queryByNativeSQL(sql);
	         Iterator it= list.iterator();
	         while(it.hasNext())
	         {
	        	 Object [] data=(Object [])it.next();
	        	 MrpJPlanRequirementDetailInfo model=new MrpJPlanRequirementDetailInfo();
	        	 if(data[0]!=null)
	        	 {
	        		model.setMrNo(data[0].toString());
	        	 }
	        	 if(data[2]!=null)
	        	 {
	        		model.setUseFlag(data[2].toString());
	        	 }
	        	 if(data[3]!=null)
	        	 {
	        		model.setMaterialNo(data[3].toString());
	        	 }
	        	 if(data[4]!=null)
	        	 {
	        		model.setMaterialName(data[4].toString());
	        	 }
	        	 if(data[5]!=null)
	        	 {
	        		model.setCancelReason(data[5].toString());
	        	 }
	        	 resultList.add(model);
	         }
		return resultList;
	}

}