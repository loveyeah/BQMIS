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

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.InquireMaterialPrintModel;
import power.ejb.resource.form.MrpPlanRequireDetailQueryInfo;
import power.ejb.workticket.business.RunJWorkticketContent;

/**
 * Facade for entity MrpJPlanInquireDetail.
 * 
 * @see power.ejb.resource.MrpJPlanInquireDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class MrpJPlanInquireDetailFacade implements
		MrpJPlanInquireDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public MrpJPlanInquireDetail save(MrpJPlanInquireDetail entity) {
		LogUtil.log("saving MrpJPlanInquireDetail instance", Level.INFO, null);
		try {
			entity.setInquireDetailId(bll.getMaxId("MRP_J_PLAN_INQUIRE_DETAIL", "inquire_detail_id"));
			entity.setModifyDate(new Date());
			entity.setIsSelectSupplier("N");
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long id) {
		MrpJPlanInquireDetail entity = this.findById(id);
		if (entity != null) {
			entity.setIsUse("N");
			this.update(entity);
		}
	}

	public void deleteMulti(String ids) {
		String sql = "update MRP_J_PLAN_INQUIRE_DETAIL a\n"
			+ "   set a.is_use = 'N'\n" + " where a.inquire_detail_id in (" + ids
			+ ")\n" + "   and a.is_use = 'Y'";
		bll.exeNativeSQL(sql);
	}

	public MrpJPlanInquireDetail update(MrpJPlanInquireDetail entity) {
		LogUtil.log("updating MrpJPlanInquireDetail instance", Level.INFO,null);
		try {
			entity.setModifyDate(new Date());
			MrpJPlanInquireDetail result = entityManager.merge(entity);
			
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public MrpJPlanInquireDetail findById(Long id) {
		LogUtil.log("finding MrpJPlanInquireDetail instance with id: " + id,
				Level.INFO, null);
		try {
			MrpJPlanInquireDetail instance = entityManager.find(
					MrpJPlanInquireDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<MrpJPlanInquireDetail> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding MrpJPlanInquireDetail instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from MrpJPlanInquireDetail model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<MrpJPlanInquireDetail> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all MrpJPlanInquireDetail instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from MrpJPlanInquireDetail model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAllList(String strWhere,final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.inquire_detail_id,\n"
				+ "       a.gather_id,\n"
				+ "       b.material_id,\n"
				+ "       c.material_name,\n"
				+ "       c.spec_no,\n"
				+ "       a.bill_no,\n"
				+ "       a.inquire_supplier,\n"
				+ "       GETCLIENTNAME(a.inquire_supplier) client_name,\n"
				+ "       a.inquire_qty,\n"
				+ "       a.unit_price,\n"
				+ "       (a.inquire_qty * a.unit_price) total_price,\n"
				+ "       a.quality_time,\n"
				+ "       a.offer_cycle,\n"
				+ "       a.memo,\n"
				+ "       getworkername(a.modify_by),\n"
				+ "       to_char(a.modify_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       a.is_select_supplier,\n"
				+ "       b.requirement_detail_ids,\n"
				+ "       to_char(a.effect_start_date, 'yyyy-MM-dd'),\n" //add by fyyang 
				+ "       to_char(a.effect_end_date, 'yyyy-MM-dd'),\n"
				+ "       GETWORKERNAME(b.buyer),\n"
				+ "       b.gather_time, \n" // 增加分配计划时间 add by ywliu 20091111
				+ "       c.factory, \n" //生产厂家
				+ "       (select f.supplier from mrp_j_plan_requirement_detail f where f.requirement_detail_id = (select max(e.requirement_detail_id) from mrp_j_plan_requirement_detail e where  instr(','||b.requirement_detail_ids||',',','||e.requirement_detail_id||',' )<>0 )) supplier \n" // 建议供应商
				// add by liuyi 20100409 附件
				+ " ,a.file_path \n"
				+ "  from MRP_J_PLAN_INQUIRE_DETAIL a, MRP_J_PLAN_GATHER b, inv_c_material c\n"
				+ " where a.gather_id(+) = b.gather_id\n"
				+ "   and b.material_id = c.material_id\n"
				+ "   and c.is_use = 'Y'\n" 
				+ "   and a.is_use(+) = 'Y'\n"
				+ "   and b.is_use = 'Y'";

		String sqlCount = "select count(1)\n"
				+ "  from MRP_J_PLAN_INQUIRE_DETAIL a, MRP_J_PLAN_GATHER b, inv_c_material c\n"
				+ " where a.gather_id(+) = b.gather_id\n"
				+ "   and b.material_id = c.material_id\n"
				+ "   and c.is_use = 'Y'\n" 
				+ "   and a.is_use(+) = 'Y'\n"
				+ "   and b.is_use = 'Y'";

		if (strWhere != "") {
			sql = sql + " and  " + strWhere;
			sqlCount = sqlCount + " and  " + strWhere;
		}
		// modified by liuyi 091127 不要分页 按供应商排序
//		sql = sql + "  order by a.modify_date desc";
//		sqlCount = sqlCount + "  order by a.modify_date desc";
		sql += " order by  GETCLIENTNAME(a.inquire_supplier) desc,a.inquire_detail_id \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while (it.hasNext()) {
				MrpPlanRequireDetailQueryInfo info = new MrpPlanRequireDetailQueryInfo();
				Object []data = (Object[])it.next();
				info.setInquireDetailId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					info.setGatherId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					info.setMaterialId(Long.parseLong(data[2].toString()));
				if(data[3] != null)
					info.setMaterialName(data[3].toString());
				if(data[4] != null)
					info.setSpecNo(data[4].toString());
				if(data[5] != null)
					info.setBillNo(data[5].toString());
				if(data[6] != null)
					info.setInquireSupplier(Long.parseLong(data[6].toString()));
				if(data[7] != null)
					info.setSupplyName(data[7].toString());
				if(data[8] != null)
					info.setInquireQty(Double.parseDouble(data[8].toString()));
				if(data[9] != null)
					info.setUnitPrice(Double.parseDouble(data[9].toString()));
				if(data[10] != null)
					info.setTotalPrice(Double.parseDouble(data[10].toString()));
				if(data[11] != null)
					info.setQualityTime(data[11].toString());
				if(data[12] != null)
					info.setOfferCycle(data[12].toString());
				if(data[13] != null)
					info.setMemo(data[13].toString());
				if(data[14] != null)
					info.setModifyByName(data[14].toString());
				if(data[15] != null)
					info.setModifyDate(data[15].toString());
				if(data[16] != null)
					info.setIsSelectSupplier(data[16].toString());
				if(data[17] != null)
					info.setRequirementDetailIds(data[17].toString());
				if(data[18]!=null)
					info.setEffectStartDate(data[18].toString());
				if(data[19]!=null)
					info.setEffectEndDate(data[19].toString());
				if(data[20]!=null)
					info.setBuyer(data[20].toString());
				// 添加查询分配计划时间 gather_time
				if(data[21]!=null)
					info.setGatherTime(data[21].toString());
				// add by bjxu 091214
				if (data[22] != null) {
					info.setFactory(data[22].toString());
				}
				if (data[23] != null) {
					info.setSupplier(data[23].toString());
				}
				if(data[24] != null){
					info.setFilePath(data[24].toString());
				}
					
				// add by liuyi 20100406 增加需求备注及申报部门
				String sString = 
					"select distinct b.memo, getdeptname(c.mr_dept)\n" +
					" from MRP_J_PLAN_GATHER              a,\n" + 
					"      mrp_j_plan_requirement_detail b,\n" + 
					"      mrp_j_plan_requirement_head   c\n" + 
					"where a.is_use='Y'\n" + 
					"and a.enterprise_code='hfdc'\n" + 
					"and (a.is_return='N' or a.is_return is null)\n" + 
					"and a.material_id="+info.getMaterialId()+"\n" + 
					"and a.requirement_detail_ids = to_char(b.requirement_detail_id)\n" + // modify by drdu 20100510
					"  and b.requirement_head_id = c.requirement_head_id";
				List sList = bll.queryByNativeSQL(sString);
				if(sList != null && sList.size() > 0)
				{
					Object[] sdata = (Object[])sList.get(0);
					if(sdata[0] != null)
						info.setSbMemo(sdata[0].toString());
					if(sdata[1] != null)
						info.setSbDeptName(sdata[1].toString());
				}
				arrlist.add(info);
			}

		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}
	
	public PageObject findInquireDetailByGatherId(String gatherIds,final int... rowStartIdxAndCount)
	{
		String sqlWhere="   a.gather_id in ("+gatherIds+") ";
		return this.findAllList(sqlWhere, rowStartIdxAndCount);
	}

	@SuppressWarnings("unchecked")
	public List<InquireMaterialPrintModel> getPrintModel(String gatherIds, String inquireDetailId) {
		Long inquireId = null;
		if(inquireDetailId != null && !"".equals(inquireDetailId)) {
			inquireId = Long.valueOf(inquireDetailId);
		}
		String sql = "select c.material_name,\n" +
			"       c.spec_no,\n" + 
			"       c.parameter,\n" + 
			"       c.factory,\n" + 
			"       d.unit_name,\n" + 
			"       b.applied_qty\n" + 
			" ,e.memo \n" + 
			"  from MRP_J_PLAN_INQUIRE_DETAIL a,\n" + 
			"       mrp_j_plan_gather         b,\n" + 
			"       inv_c_material            c,\n" + 
			"       BP_C_MEASURE_UNIT         d\n" + 
			// add by liuyi 20100406 
			" ,mrp_j_plan_requirement_detail e \n" + 
			" where (a.gather_id in ("+gatherIds+
			") or a.inquire_detail_id = "+inquireId+
			")\n" + 
			"   and a.inquire_supplier in\n" + 
			"       (select tt.inquire_supplier\n" + 
			"          from MRP_J_PLAN_INQUIRE_DETAIL tt\n" + 
			"         where tt.inquire_detail_id = "+inquireId+
			")\n" + 
			"   and b.gather_id = a.gather_id\n" + 
			"   and b.material_id = c.material_id\n" + 
			"   and c.stock_um_id = d.unit_id \n" +
			"   and a.is_use = 'Y'"// modify by ywliu 20091027
		 + "  and b.requirement_detail_ids = to_char(e.requirement_detail_id(+)) \n";//modify by drdu 20100510
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		List<InquireMaterialPrintModel> printList = new ArrayList<InquireMaterialPrintModel>();
		while(it.hasNext()) {
			Object[] data = (Object[]) it.next();
			InquireMaterialPrintModel bean = new InquireMaterialPrintModel();
			if(data[0] != null)
				bean.setMaterialName(data[0].toString());
			if(data[1] != null)
				bean.setSpecNo(data[1].toString());
			if(data[2] != null)
				bean.setParameter(data[2].toString());
			if(data[3] != null)
				bean.setFactory(data[3].toString());
			if(data[4] != null)
				bean.setStockUmName(data[4].toString());
			if(data[5] != null)
				bean.setApprovedQty(data[5].toString());
			if(data[6] != null)
				bean.setMemo(data[6].toString());
			printList.add(bean);
		}
		return printList;
	}
	
	public PageObject findQuotedPriceList(String materialName,String buyer,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		String sqlWhere="   c.material_name like '%"+materialName.trim()+"%'   and GETWORKERNAME(b.buyer) like '%"+buyer+"%' " +
		                "   and a.is_select_supplier='Y'  \n"+
				"   and a.enterprise_code='"+enterpriseCode+"' \n";
		return this.findAllList(sqlWhere, rowStartIdxAndCount);
	}
	
	public void modifyRecords(List<MrpJPlanInquireDetail> list,String delIds){
		if(list !=null)
		{ 
//			Long id = bll.getMaxId("MRP_J_PLAN_INQUIRE_DETAIL", "inquire_detail_id");
//			int i=0;
			for (MrpJPlanInquireDetail m : list) { 
				if (m.getInquireDetailId() != null) { 
					this.update(m);
					entityManager.flush();
					this.updateGatherStatus(m.getGatherId(), null);
				} else {
					this.save(m);
					entityManager.flush();
					this.updateGatherStatus(m.getGatherId(), null);
				} 
			}
		}
		if(delIds != null&& !delIds.trim().equals(""))
		{
			this.deleteMulti(delIds);
			entityManager.flush();
			this.updateGatherStatus(null, delIds);
		}
	}
	
	
	//确定采购的供应商
	public void chooseSupplier(Long detailId)
	{
		MrpJPlanInquireDetail model = this.findById(detailId);
		Long gatherId = model.getGatherId();
		
		String sql = "update mrp_j_plan_inquire_detail t\n" +
			"   set t.is_select_supplier = 'N'\n" + 
			" where t.gather_id = "+gatherId+"";
			bll.exeNativeSQL(sql);
			
			String updateGatherSql=
				"update mrp_j_plan_gather a\n" +
				"   set a.is_enquire = 'N'\n" + 
				" where a.gather_id = (select t.gather_id\n" + 
				"                        from mrp_j_plan_inquire_detail t\n" + 
				"                       where t.inquire_detail_id ="+detailId+")";
			bll.exeNativeSQL(updateGatherSql);
		
			String sql1=
				"update mrp_j_plan_inquire_detail t\n" +
				"set t.is_select_supplier='Y'\n" + 
				"where t.inquire_detail_id="+detailId;
			bll.exeNativeSQL(sql1);
			
			String updateGatherSql1=
				"update mrp_j_plan_gather a\n" +
				"   set a.is_enquire = 'Y'\n" + 
				" where a.gather_id = (select t.gather_id\n" + 
				"                        from mrp_j_plan_inquire_detail t\n" + 
				"                       where t.inquire_detail_id ="+detailId+")";
			bll.exeNativeSQL(updateGatherSql1);
			
	}
	
	private void updateGatherStatus(Long gatherId,String delIds)
	{
		String sql="";
		if(gatherId!=null)
		{
		sql=	"update mrp_j_plan_gather a\n" +
			"set a.is_enquire='Q'\n" + 
			"where a.gather_id="+gatherId+"\n" + 
			"and\n" + 
			"(\n" + 
			"select count(*)\n" + 
			"from mrp_j_plan_inquire_detail t\n" + 
			"where t.gather_id="+gatherId+"\n" + 
			"  and t.is_use='Y'\n" + 
			")>0";
		bll.exeNativeSQL(sql);
		}
		else
		{
			if(delIds!=null&&!delIds.equals(""))
			{
				StringBuffer sb = new StringBuffer();
				sb.append("begin\n"); 
				String [] ids=delIds.split(",");
				for(int i=0;i<ids.length;i++)
				{
			    sql=
				"update mrp_j_plan_gather a\n" +
				"set a.is_enquire='Q'\n" + 
				"where a.gather_id=(select tt.gather_id from  mrp_j_plan_inquire_detail tt where tt.inquire_detail_id="+ids[i]+")\n" + 
				"and\n" + 
				"(\n" + 
				"select count(*)\n" + 
				"from mrp_j_plan_inquire_detail t\n" + 
				"where t.gather_id=(select tt.gather_id from  mrp_j_plan_inquire_detail tt where tt.inquire_detail_id="+ids[i]+")\n" + 
				"  and t.is_use='Y'\n" + 
				")>0;";
			    sb.append(sql); 
				}
				sb.append("commit;\n"); 
				sb.append("end;\n");  
				 
				bll.exeNativeSQL(sb.toString()); 
			}

		}
		

	}
	
	public PageObject getInquirePriceInfo(String materialName,String buyer,String requirementDetailId,String enterpriseCode,final int... rowStartIdxAndCount) {
		String sqlWhere="";
		if(requirementDetailId != null && !"".equals(requirementDetailId)) {// 增加需求计划明细ID 为需求计划综合查询页面的询价查询的方法 ywliu 2009/7/3
			sqlWhere = "   c.material_name like '%"+materialName.trim()+"%'   and GETWORKERNAME(b.buyer) like '%"+buyer+"%' " +
//			"   and a.is_select_supplier='Y'  \n"+ //ywliu 20091029 显示所有询价信息
			"   and a.enterprise_code='"+enterpriseCode+"' \n";
			sqlWhere += " and instr(','||b.requirement_detail_ids||',',',"+requirementDetailId+",')<>0 ";
			return this.findAllList(sqlWhere, rowStartIdxAndCount);
		} else {
			return null;
		}
		
	}
}