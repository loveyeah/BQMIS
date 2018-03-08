package power.ejb.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.form.MRPGatherDetailInfo;

/**
 * Facade for entity MrpJPlanGather.
 * 
 * @see power.ejb.resource.MrpJPlanGather
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class MrpJPlanGatherFacade implements MrpJPlanGatherFacadeRemote {
	// property constants
	public static final String MATERIAL_ID = "materialId";
	public static final String APPLIED_QTY = "appliedQty";
	public static final String GATHER_BY = "gatherBy";
	public static final String REQUIREMENT_DETAIL_IDS = "requirementDetailIds";
	public static final String IS_ENQUIRE = "isEnquire";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "MrpJPlanRequirementDetailFacade")
	protected MrpJPlanRequirementDetailFacadeRemote remote;

	public MrpJPlanGather save(MrpJPlanGather entity) {
		LogUtil.log("saving MrpJPlanGather instance", Level.INFO, null);
		try {
			if (entity.getGatherId() == null) {
				entity.setGatherId(bll.getMaxId("mrp_j_plan_gather t",
						"gather_id"));
			}
			entity.setGatherTime(new java.util.Date());
			entity.setIsUse("Y");
			entity.setIsEnquire("N");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(MrpJPlanGather entity) {
		LogUtil.log("deleting MrpJPlanGather instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(MrpJPlanGather.class, entity
					.getGatherId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public MrpJPlanGather update(MrpJPlanGather entity) {
		LogUtil.log("updating MrpJPlanGather instance", Level.INFO, null);
		try {
			MrpJPlanGather result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public MrpJPlanGather findById(Long id) {
		LogUtil.log("finding MrpJPlanGather instance with id: " + id,
				Level.INFO, null);
		try {
			MrpJPlanGather instance = entityManager.find(MrpJPlanGather.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<MrpJPlanGather> findAll() {
		LogUtil.log("finding all MrpJPlanGather instances", Level.INFO, null);
		try {
			final String queryString = "select model from MrpJPlanGather model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/** modify by fyyang 090522 modify by fyyang 091222 增加汇总时间查询条件 
	 * modify by fyyang 20100505  flag=1----查询出所有已汇总的物资；  2----查询出已汇总未报价确定供应商的物资
	 * */
	@SuppressWarnings("unchecked")
	public PageObject getMaterialGatherDetail(String materialName,
			String buyer, String enterpriseCode, String sdate, String edate,String flag,
			String mrBy, String mrDept,int... rowStartIdxAndCount) {

		String strWhere = "";
		if (sdate != null && !sdate.equals("")) {
			strWhere = " \n  and c.gather_time>=to_date('" + sdate
					+ "'||' 00:00:00','yyyy-MM-dd hh24:mi:ss') \n";
		}
		if (edate != null && !edate.equals("")) {
			strWhere += " \n and   c.gather_time<=to_date('" + edate
					+ "'||' 23:59:59','yyyy-MM-dd hh24:mi:ss') \n";
		}
		
		// add by sychen 20100511-------------//
		if (mrBy != null && !mrBy.equals("")) {
			strWhere += " \n and   f.mr_by='"+mrBy+"' \n";
		}
		if (mrDept != null && !mrDept.equals("")) {
			strWhere += " \n and   f.mr_dept='"+mrDept+"' \n";
		}
		//add end ----------------------------//
		
		// 添加查询汇总时间 gather_time
		String sql = "select b.material_no,b.material_name,b.material_id,b.PARAMETER,b.spec_no,d.class_name,B.QA_CONTROL_FLAG, B.MAX_STOCK, B.PUR_UM_ID,c.applied_qty,c.gather_id,c.is_enquire,GETWORKERNAME(c.buyer) buyer,c.gather_time, \n"
				
				+ "c.requirement_detail_ids,\n"
				+ "t.memo,\n" // add by sychen 201004012
				+ "  b.factory,\n"
				+ "t.supplier,\n"
				+ "GETWORKERNAME(f.mr_by),\n"
				+ "GETDEPTNAME(f.mr_dept),\n"
				+ "t.estimated_price,\n" // add by ltong 20100427
				+ "f.mr_reason,\n" // 申请理由 add by sychen 20100510
				+ "f.plan_original_id\n" // 计划种类add by sychen 20100510
				+ "from INV_C_MATERIAL B ,mrp_j_plan_gather c,inv_c_material_class d\n"
				+ ", mrp_j_plan_requirement_detail t\n"// add by sychen
				// 201004012
				+ ", mrp_j_plan_requirement_head f\n"// add by ltong 20100427
				+ "where b.material_id = c.MATERIAL_ID and b.maertial_class_id = d.maertial_class_id \n"
				+ "and c.requirement_detail_ids=to_char(t.requirement_detail_id) \n"
				+ // add by sychen 201004012
				
				"and c.is_use='Y' and c.enterprise_code = '" + enterpriseCode + "' \n"
				+ "and  b.material_name like '%"
				+ materialName
				+ "%' \n"
				+ // add by fyyang
				"and  GETWORKERNAME(c.buyer) like '%"
				+ buyer
				+ "%' \n"
				+ // add by fyyang
				"and B.Enterprise_Code='"
				+ enterpriseCode
				+ "'  and c.enterprise_code='"
				+ enterpriseCode
				+ "' and d.enterprise_code='" + enterpriseCode + "' \n" // add by fyyang
				+ " and t.requirement_head_id=f.requirement_head_id \n";// add by ltong 20100427
		if("2".equals(flag))
		{
			sql+="and c.is_enquire<>'Y' \n" ;
		}
		sql += strWhere;
		String sqlCount = "select count(*) from ("+sql+")tt ";
		sql += " \n order by  GETWORKERNAME(c.buyer) ,c.gather_time desc,b.material_no  ";
		
		List<MRPGatherDetailInfo> list = bll.queryByNativeSQL(sql,
				rowStartIdxAndCount);
		List<MRPGatherDetailInfo> arrlist = new ArrayList<MRPGatherDetailInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			MRPGatherDetailInfo model = new MRPGatherDetailInfo();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setMaterialNo(data[0].toString());
			if (data[1] != null)
				model.setMaterialName(data[1].toString());
			if (data[2] != null)
				model.setMaterialId(data[2].toString());
			if (data[3] != null)
				model.setParameter(data[3].toString());
			if (data[4] != null)
				model.setSpecNo(data[4].toString());
			if (data[5] != null)
				model.setClassName(data[5].toString());

			if (data[6] != null)
				model.setQaControlFlag(data[6].toString());
			if (data[7] != null)
				model.setMaxStock(Double.parseDouble(data[7].toString()));
			if (data[8] != null) {
				BpCMeasureUnitFacadeRemote cc = (BpCMeasureUnitFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote("BpCMeasureUnitFacade");
				BpCMeasureUnit bp = cc.findById(Long.parseLong(data[8]
						.toString()));
				if(bp != null)
					model.setStockUmName(bp.getUnitName());
			}
			if (data[9] != null)
				model.setApprovedQty(Double.parseDouble(data[9].toString()));
			if (data[10] != null)
				model.setGatherId(data[10].toString());
			if (data[11] != null) {
				model.setIsEnquire(data[11].toString());
			}
			if (data[12] != null)
				model.setBuyer(data[12].toString());
			// 添加查询汇总时间 gather_time
			if (data[13] != null)
				model.setGatherTime(data[13].toString());

			// =============add by drdu 091112==============
			if (data[14] != null) {
				model.setRequirementDetailId(data[14].toString());
			}
			if (data[15] != null) {
				model.setMemo(data[15].toString());

			}
			// add by bjxu 091214
			if (data[16] != null) {
				model.setFactory(data[16].toString());

			}
			if (data[17] != null) {
				model.setSupplier(data[17].toString());

			}
			// add by ltong 20100427
			if (data[18] != null) {
				model.setApplyByName(data[18].toString());
			}
			if (data[19] != null) {
				model.setApplyDeptName(data[19].toString());
			}
			if (data[20] != null) {
				model
						.setEstimatedPrice(Double.parseDouble(data[20]
								.toString()));
			}
			// add by sychen 20100510-----------//
			if (data[21] != null)
				model.setApplyReason(data[21].toString());
			
			if (data[22] != null) {
				model.setPlanOriginalId(data[22].toString());
			}
			// add end------------------------//
			// ==============end============================
			arrlist.add(model);
		}
		PageObject reObject = new PageObject();
		reObject.setList(arrlist);
		reObject.setTotalCount(Long.parseLong(bll.getSingal(sqlCount)
				.toString()));
		return reObject;
	}

	/**
	 * add by liuyi 091104 批量修改需求计划汇总采购员
	 * 
	 * @param ids
	 * @param buyer
	 */
	public void chooserBuyer(String ids, String buyer) {
		if (ids != null && !"".equals(ids) && buyer != null
				&& !"".equals(buyer)) {
			String sql = "update mrp_j_plan_gather a set a.buyer='" + buyer
					+ "' \n" + "where a.gather_id in (" + ids + ") \n";
			bll.exeNativeSQL(sql);
		}
	}

	public void gatherBack(String backId, String backReason) {
		if (backId != null) {
			String sql = "select * from Mrp_j_Plan_Gather a where a.gather_id ="
					+ Long.parseLong(backId);
			List<MrpJPlanGather> list = bll.queryByNativeSQL(sql,
					MrpJPlanGather.class);
			if (list != null && list.size() > 0) {
				MrpJPlanGather gather = list.get(0);
				gather.setIsUse("N");
				gather.setIsReturn("Y");
				gather.setReturnReason(backReason);
				this.update(gather);

				String sqlBackGather = "update mrp_j_plan_requirement_detail a set a.is_generated='N' where a.requirement_detail_id in ("
						+ gather.getRequirementDetailIds() + ") \n";
				bll.exeNativeSQL(sqlBackGather);
			}
		}

	}

	// add by fyyang 091214
	public void doGather(String detailIds, String buyer, String enterpriseCode,
			String workCode) {
		// modified by liuyi 20100406 不再进行汇总
		if (detailIds != null && detailIds.length() > 0) {
			String[] arr = detailIds.split(",");
			for (int i = 0; i <= arr.length - 1; i++) {
				String sql = "insert into mrp_j_plan_gather m\n"
						+ "  (m.gather_id,\n"
						+ "   m.material_id,\n"
						+ "   m.applied_qty,\n"
						+ "   m.requirement_detail_ids,\n"
						+ "   m.buyer,\n"
						+ "   m.gather_by,\n"
						+ "   m.gather_time,\n"
						+ "   m.enterprise_code,\n"
						+ "   m.is_use,\n"
						+ "   m.is_enquire)\n"
						+ "  select (select nvl(max(tt.gather_id), 0) from mrp_j_plan_gather tt) +\n"
						+ "         row_number() over(order by a.material_id),\n"
						+ "         a.material_id,\n"
						+ "         sum(a.approved_qty),\n"
						+
						// modified by liuyi 20100406 不再进行汇总
						" '"
						+ arr[i]
						+ "', \n"
						+ "         '"
						+ buyer
						+ "',\n"
						+ "         '"
						+ workCode
						+ "',\n"
						+ "         sysdate,\n"
						+ "         '"
						+ enterpriseCode
						+ "',\n"
						+ "         'Y',\n"
						+ "         'N'\n"
						+ "    from mrp_j_plan_requirement_detail a, inv_c_material b\n"
						+ "   where a.material_id = b.material_id\n"
						+ "     and a.requirement_detail_id =" + arr[i] + " \n"
						+ "   group by a.material_id, a.supplier, b.factory";
				bll.exeNativeSQL(sql);

			}
		}

		// String sql=
		// "insert into mrp_j_plan_gather m\n" +
		// " (m.gather_id,\n" +
		// " m.material_id,\n" +
		// " m.applied_qty,\n" +
		// " m.requirement_detail_ids,\n" +
		// " m.buyer,\n" +
		// " m.gather_by,\n" +
		// " m.gather_time,\n" +
		// " m.enterprise_code,\n" +
		// " m.is_use,\n" +
		// " m.is_enquire)\n" +
		// " select (select nvl(max(tt.gather_id), 0) from mrp_j_plan_gather tt)
		// +\n" +
		// " row_number() over(order by a.material_id),\n" +
		// " a.material_id,\n" +
		// " sum(a.approved_qty),\n" +
		// " GetSameGatherIds(a.material_id,\n" +
		// " a.supplier,\n" +
		// " b.factory,\n" +
		// " '"+detailIds+"'),\n" +
		// " '"+buyer+"',\n" +
		// " '"+workCode+"',\n" +
		// " sysdate,\n" +
		// " '"+enterpriseCode+"',\n" +
		// " 'Y',\n" +
		// " 'N'\n" +
		// " from mrp_j_plan_requirement_detail a, inv_c_material b\n" +
		// " where a.material_id = b.material_id\n" +
		// " and a.requirement_detail_id in ("+detailIds+")\n" +
		// " group by a.material_id, a.supplier, b.factory";
		// bll.exeNativeSQL(sql);

		String updateSql = "update mrp_j_plan_requirement_detail a\n"
				+ "set a.is_generated='G'\n"
				+ "where a.requirement_detail_id in (" + detailIds + ")";
		bll.exeNativeSQL(updateSql);

	}

}