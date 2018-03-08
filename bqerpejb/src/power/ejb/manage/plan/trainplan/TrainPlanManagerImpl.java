package power.ejb.manage.plan.trainplan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sun.org.apache.bcel.internal.generic.NEW;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCDept;
import power.ejb.hr.LogUtil;
import power.ejb.manage.plan.trainplan.form.BpJTrainingBackfillForm;
import power.ejb.manage.plan.trainplan.form.BpJTrainingSumForm;
import power.ejb.manage.plan.trainplan.form.BpJTrainingTypeForm;
import power.ejb.manage.plan.trainplan.form.BpTrainPlanApproveForm;

@Stateless
public class TrainPlanManagerImpl implements TrainPlanManager {

	@PersistenceContext
	private EntityManager entityManager;

	@EJB(beanName = "NativeSqlHelper")
	/* protected NativeSqlHelperRemote bll; */
	protected NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
			.getInstance().getFacadeRemote("NativeSqlHelper");

	// 类别
	// public Long getMaxId(BpCTrainingType entity){
	// String sql="select count(*)+1 from BP_C_TRAINING_TYPE";
	// Long count;
	//		 
	// count = Long.valueOf(bll.getSingal(sql).toString());
	// return count ;
	// }
	public boolean checkinput(BpCTrainingType entity) {
		String st = "select b.training_type_id  from  BP_C_TRAINING_TYPE b  where b.training_type_name='"
				+ entity.getTrainingTypeName() + "'";

				// modified by liuyi 091214 
//				" and  b.memo='" + entity.getMemo() + "' ";
		// add by liuyi 091214 增加修改时的唯一性检查
		if(entity.getTrainingTypeId() != null)
			st += " and b.training_type_id <>" + entity.getTrainingTypeId();
		int a = bll.exeNativeSQL(st);
		if (a > 0) {
			return true;
		} else

			return false;
	}

	public boolean addTrainPlanType(BpCTrainingType entity) {
		/*
		 * if (!this.checkName(entity.getTypeName(),entity.getEnterpriseCode(),
		 * entity.getTypeId()))
		 */
		if (entity.getTrainingTypeId() == null) {
			entity.setTrainingTypeId(bll.getMaxId("Bp_C_Training_Type",
					"Training_Type_Id"));
		}
		entityManager.persist(entity);
		return true;
	}

	public boolean delTrainPlanType(String ids) {

		String sql = "delete from BP_C_TRAINING_TYPE b where b.training_type_id in("
				+ ids + ")";
		bll.exeNativeSQL(sql);
		return true;

		// return false;
	}

	public boolean updateTrainPlanType(Long id, BpCTrainingType entity) {
		/*
		 * String sql="update CON_C_CLIENTS_TYPE b set
		 * b.training_type_name="+entity.getTrainingTypeName() +",set
		 * b.memo="+entity.getMemo()+" where b.training_type_id ="+id+"";
		 */

		String sql = "update BP_C_TRAINING_TYPE b\n"
				+ "set (training_type_name,memo)=(select '"
				+ entity.getTrainingTypeName() + "','" + entity.getMemo()
				+ "' from dual)\n" + " where b.training_type_id =" + id + "";

		System.out.println("update sql is:" + sql);
		bll.exeNativeSQL(sql);
		return true;
	}

	// TODO Auto-generated method stub

	public PageObject getTrainPlanList(String enterpriseCode,
			final int... rowStartIdxAndCount) {

		try {
			PageObject result = new PageObject();

			String sql = "select b.* from BP_C_TRAINING_TYPE b where b.enterprise_code='"
					+ enterpriseCode + "' order by b.training_type_id";
			String sqlcount = "select count(*)  from  BP_C_TRAINING_TYPE";

			// System.out.println("sql is:"+sql);
			List<BpCTrainingType> list = bll.queryByNativeSQL(sql,
					BpCTrainingType.class, rowStartIdxAndCount);
			result.setList(list);
			Long totalCount = Long.valueOf(bll.getSingal(sqlcount).toString());
			result.setTotalCount(totalCount);

			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAllTypeList(String fuzzy, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from BP_C_TRAINING_TYPE t\n"
					+ "where t.training_type_name like '%" + fuzzy + "%'\n"
					+ "and t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_use='Y'";

			List<BpCTrainingType> list = bll.queryByNativeSQL(sql,
					BpCTrainingType.class, rowStartIdxAndCount);
			String sqlCount = "select count(*) from BP_C_TRAINING_TYPE t\n"
					+ "where t.training_type_name like '%" + fuzzy + "%'\n"
					+ "and t.enterprise_code='" + enterpriseCode + "'\n"
					+ "and t.is_use='Y'";

			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	// 明细
	public BpJTrainingDetail addTrainPlanApplyDetail(BpJTrainingDetail entity,
			String planTime, String planDept, Long mainId)
			throws ParseException, CodeRepeatException {

		if (!this.checkLevel(entity.getTrainingTypeId(), entity
				.getEnterpriseCode(), mainId, entity.getTrainingDetailId())
				|| entity.getTrainingLevel() == 2) {
			entity.setTrainingMainId(mainId);
			Long id = bll.getMaxId("BP_J_TRAINING_DETAIL ",
					"training_detail_id");
			entity.setTrainingDetailId(id);
			entity.setIsUse("Y");
			entityManager.persist(entity);
			entityManager.flush();
		} else {
			throw new CodeRepeatException("该类别培训计划的一级项目已经存在！");
		}
		return entity;
	}

	// modified by liuyi 20100427 
	public void save(List<BpJTrainingDetail> addList,
			List<BpJTrainingDetail> updateList, String deleteId,
			String planTime, String planDept, String enterpriseCode,String workerCode)
			throws ParseException, CodeRepeatException {
		Long mainId;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
//		if (this.addPlanApplyMain(planTime, planDept) == null) {
		if (this.addPlanApplyMain(planTime, planDept,workerCode) == null) {
			// 增加主表
			BpJTrainingMain model = new BpJTrainingMain();
			mainId = bll.getMaxId("bp_j_training_main", "training_main_id");
			model.setTrainingMainId(mainId);
			model.setTrainingDep(planDept);
			model.setIsUse("Y");
			model.setWorkflowStatus(0l);
			model.setEnterpriseCode(enterpriseCode);
			// add by liuyi 20100427 
			model.setFillBy(workerCode);
			model.setTrainingMonth(df.parse(planTime));
			entityManager.persist(model);
		} else {
//			mainId = this.addPlanApplyMain(planTime, planDept);
			mainId = this.addPlanApplyMain(planTime, planDept,workerCode);
		}

		if (addList.size() > 0) {

			for (BpJTrainingDetail entity : addList) {
				this.addTrainPlanApplyDetail(entity, planTime, planDept,
								mainId);
			}
		}
		if (updateList.size() > 0) {
			for (BpJTrainingDetail entity : updateList) {
				this.updateTrainPlanApplyDetail(entity, mainId);
			}
		}
		if (deleteId.length() > 0) {
			this.delTrainPlanApplyDetail(deleteId);
		}
	}

	public BpJTrainingDetail updateTrainPlanApplyDetail(
			BpJTrainingDetail entity, Long mainId) throws ParseException,
			CodeRepeatException {
		// TODO Auto-generated method stub

		try {
			if (!this.checkLevel(entity.getTrainingTypeId(), entity
					.getEnterpriseCode(), mainId, entity.getTrainingDetailId())
					|| entity.getTrainingLevel() == 2) {
				entityManager.merge(entity);

			} else {
				throw new CodeRepeatException("该类别培训计划的一级项目已经存在！");
			}
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}

	}

	public BpJTrainingDetail delTrainPlanApplyDetail(String ids) {
		// TODO Auto-generated method stub

		try {
			String sql = "UPDATE bp_j_training_detail t\n"
					+ "   SET t.is_use = 'N'\n"
					+ " WHERE t.training_detail_id IN (" + ids + ")";
			bll.exeNativeSQL(sql);
			return null;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}

	}

	public PageObject getTrainPlanDetailList(String planTime, String planDept,
			String planType, String enterpriseCode) {
		// TODO Auto-generated method stub
		return null;
	}

	// modified by liuyi 20100427 
//	private Long addPlanApplyMain(String planTime, String planDept) {
	private Long addPlanApplyMain(String planTime, String planDept,String workerCode) {

		String sql = "select t.training_main_id from bp_j_training_main t where t.training_month=to_date('"
				+ planTime
				+ "','yyyy-MM') and t.training_dep='"
				+ planDept
				+ "' and t.is_use='Y'"
				+" and t.FILL_BY ='" + workerCode + "' \n";
		Long mainId = (bll.getSingal(sql) == null) ? 0 : Long.parseLong(bll
				.getSingal(sql).toString());
		if (mainId > 0) {
			return mainId;
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public PageObject findAllList(String strWhere,
			final int... rowStartIdxAndCount) {
		PageObject obj = new PageObject();
		String sql = "select\n"
				+ "b.training_detail_id,\n"
				+ "b.training_main_id,\n"
				+ "b.training_type_id,\n"
				+ "b.training_name,\n"
				+ "b.training_level,\n"
				+ "b.training_number,\n"
				+ "b.training_hours,\n"
				+ "b.charge_by,\n"
				+ "b.fill_by,\n"
				+ "to_char(b.fill_date,'yyyy-mm-dd'),\n"
				+ "a.training_dep,\n"
				+ "getdeptname(a.training_dep) deptName,\n"
				+ "(select c.training_type_name from BP_C_TRAINING_TYPE c where c.training_type_id = b.training_type_id) planTypeName,\n"
				+ "getworkername(b.charge_by) chargeName,\n"
				+ "getworkername(b.fill_by) fillName,\n"
				+ "\n"
				+ "	a.workflow_no,\n"
				+ "\n"
				+ " a.workflow_status,\n"
				+ "\n"
				+ "a.report_by,\n"
				+ "\n"
				+ "getworkername(a.report_by),\n"
				+ "\n"
				+ "to_char(a.report_time,'yyyy-mm-dd') reportDate \n"
				+ "\n"
				// add by liuyi 20100427 填报人
				+ " , a.fill_by mainFillBy ,getworkername(a.fill_by) mainFillName "
				+ "from BP_J_TRAINING_MAIN a ,BP_J_TRAINING_DETAIL b\n"
				+ "where  a.is_use = 'Y' and b.is_use = 'Y'  and b.training_main_id = a.training_main_id\n"
				;

		if (strWhere != null && !strWhere.equals("")) {
			sql += strWhere;
		}
		String sqlcount = "select count(*) from (" + sql + ") \n";
		// add by liuyi 091218
		sql += "order by a.training_dep,a.fill_by,b.training_type_id,b.training_level,b.training_detail_id ";

		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
		List arraylist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			BpJTrainingTypeForm fmodel = new BpJTrainingTypeForm();
			BpJTrainingDetail model = new BpJTrainingDetail();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setTrainingDetailId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setTrainingMainId(Long.parseLong(data[1].toString()));
			if (data[2] != null)
				model.setTrainingTypeId(Long.parseLong(data[2].toString()));
			if (data[3] != null)
				model.setTrainingName(data[3].toString());
			if (data[4] != null)
				model.setTrainingLevel(Long.parseLong(data[4].toString()));
			if (data[5] != null)
				model.setTrainingNumber(Long.parseLong(data[5].toString()));
			if (data[6] != null)//modify by drdu 20100104
			{
				model.setTrainingHours(Double.parseDouble(data[6].toString()));
			}else{
				model.setTrainingHours(0.0);
			}
			if (data[7] != null)
				model.setChargeBy(data[7].toString());
			if (data[8] != null)
				model.setFillBy(data[8].toString());
			if (data[9] != null)
				fmodel.setFillDate(data[9].toString());
			if (data[10] != null)
				fmodel.setDeptCode(data[10].toString());
			if (data[11] != null)
				fmodel.setDeptName(data[11].toString());
			if (data[12] != null)
				fmodel.setPlanTypeName(data[12].toString());
			if (data[13] != null)
				fmodel.setChargeName(data[13].toString());
			if (data[14] != null)
				fmodel.setBillName(data[14].toString());
			if (data[15] != null)
				fmodel.setWorkflowNo(data[15].toString());
			if (data[16] != null)
				fmodel.setWorkflowStatus(data[16].toString());
			if (data[17] != null)
				fmodel.setReportBy(data[17].toString());
			if (data[18] != null)
				fmodel.setReportByName(data[18].toString());
			if (data[19] != null)
				fmodel.setReportByName(data[19].toString());

			if(data[20] != null)
				fmodel.setFillBy(data[20].toString());
			if(data[21] != null)
				fmodel.setFillName(data[21].toString());
			fmodel.setTrainDetail(model);
			arraylist.add(fmodel);
		}
		obj.setList(arraylist);
		obj.setTotalCount(count);
		return obj;

	}

	@SuppressWarnings("unchecked")
	public PageObject findTrainPlanApplyList(String approve, String entryIds,
			String planType, String planDate, String planDept,
			String enterpriseCode, String isApply,String workerCode,int... rowStartIdxAndCount) throws Exception {
		String strWhere = " and a.enterprise_code='" + enterpriseCode + "'\n"
				+ " and b.enterprise_code='" + enterpriseCode + "'\n";
				
		if (planDate != null && !planDate.equals("")) {
			strWhere += " and to_char(a.training_month,'yyyy-mm')='" + planDate
					+ "'\n";

		}

		if (approve != null && approve.equals("approve")) {
			strWhere +=
			// " and a.workflow_status='1'" +
			" and a.workflow_no in (" + entryIds + ")";
		}
		if (planType != null && !planType.equals("")) {
			strWhere += "and b.training_type_id ='" + planType + "'";
		}
		//add by bjxu 100125 部门培训计划查询页面可以看其他部门
		if(approve !=null && (approve.equals("dept") || approve.equals("approve"))){
			
		}else{
			strWhere += "and a.training_dep='" + planDept + "'";
		}
		
		// add by liuyi 20100427 isApply:1,为培训计划申请 同一管理部门可以有多条主记录
		if(isApply != null && isApply.equals("1")){
			strWhere += " and a.FILL_BY ='" + workerCode + "' \n";
		}
		return this.findAllList(strWhere, rowStartIdxAndCount);
	}

	@SuppressWarnings("unchecked")
	// modified by liuyi 20100427 isGatherQuery 是否为汇总查询，1是，汇总查询时，未汇总的数据查询不到
	public PageObject findPlanGatherQueryList(String approve, String entryIds,
			String planType, String planDate, String planDept,
			String enterpriseCode,String isGatherQuery, int... rowStartIdxAndCount) throws Exception {
		String strWhere = " and a.enterprise_code='" + enterpriseCode + "'\n"
				+ " and b.enterprise_code='" + enterpriseCode + "'";
		if (planDept != null && !planDept.equals("")) {
			strWhere += "and a.training_dep ='" + planDept + "'\n";
		}
		if (planDate != null && !planDate.equals("")) {
			strWhere += " and to_char(a.training_month,'yyyy-mm')='" + planDate
					+ "'\n";

		}

		if (approve != null && approve.equals("approve")) {
			strWhere +=
			// " and a.workflow_status='1'" +
			" and a.workflow_no in (" + entryIds + ")";
		}
		
		//各部门审批通过了的计划
		if (approve != null && approve.equals("signStatus")) {
			strWhere +=
				// " and a.workflow_status='1'" +
				"  and a.workflow_status='2'";
		}
		if (planType != null && !planType.equals("")) {
			strWhere += "and b.training_type_id ='" + planType + "'";
		}
		
		// add by liuyi 201004127 汇总查询时，查不到未汇总的数据
		if(isGatherQuery != null && isGatherQuery.equals("1")){
			strWhere += " and a.training_gather_id is not null \n";
		}
//		strWhere += "order by deptName,b.training_type_id";
		return this.findAllList(strWhere, rowStartIdxAndCount);
	}
	
	
	public BpJTrainingDetail findByDetailId(Long id) {
		try {
			BpJTrainingDetail instance = entityManager.find(
					BpJTrainingDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 判断同一类别下为一级的项目级别是否重复
	 * 
	 * @param trainingTypeId
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean checkLevel(Long trainingTypeId, String enterpriseCode,
			Long maidId, Long detailId) {
		boolean isSame = false;
		String sql = "SELECT COUNT(*)\n" + " FROM bp_j_training_detail a\n"
				+ "WHERE a.training_type_id= " + trainingTypeId + "\n"
				+ "and a.training_main_id= " + maidId + "\n"
				+ "and a.training_level = '1'\n" + "AND a.is_use = 'Y'\n"
				+ "and a.enterprise_code='" + enterpriseCode + "'";

		if (detailId != null ) {
			sql += "  and a.training_detail_id <> " + detailId;
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

	// 操作主表
	public BpJTrainingMain findById(Long mainId) {
		try {
			BpJTrainingMain model = entityManager.find(BpJTrainingMain.class,
					mainId);
			return model;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJTrainingMain update(BpJTrainingMain entity) {
		try {
			BpJTrainingMain model = entityManager.merge(entity);
			return model;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public BpJTrainingTypeForm getBpTrainMainList(String planTime,
			String planDept) {

		BpJTrainingTypeForm model = null;
		String sql = "select  getworkername(b.report_by)reportBy,to_char(b.report_time,'yyyy-mm-dd') reportDate,\n"
				+ "decode(b.workflow_status,0,'未上报',1,'审批中',2,'审批已通过',3,'审批退回',4,'部门培训员已汇总')workstarus,getdeptname(b.training_dep)\n"
				+ "  from BP_J_TRAINING_MAIN b\n"
				+ "  where b.training_dep ='"
				+ planDept
				+ "'\n"
				+ "  and to_char( b.training_month,'yyyy-mm')='"
				+ planTime
				+ "'";
		List<Object[]> list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			model = new BpJTrainingTypeForm();
			Object[] f = list.get(0);
			if (f[0] != null)
				model.setReportBy(f[0].toString());
			if (f[1] != null)
				model.setReportTime(f[1].toString());
			if (f[2] != null)
				model.setWorkflowStatus(f[2].toString());
			if (f[3] != null)
				model.setDeptName(f[3].toString());

		}
		return model;

	}

	// 汇总
	public BpJTrainingGather findByGatherId(Long gatherId) {
		try {
			BpJTrainingGather model = entityManager.find(
					BpJTrainingGather.class, gatherId);
			return model;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJTrainingGather update(BpJTrainingGather entity) {
		try {
			BpJTrainingGather model = entityManager.merge(entity);
			return model;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			return null;
		}
	}

	public PageObject BpPlanGatherList(String planTime, String enterpriseCode,
			int... rowStartIdxAndCount) {
		String strWhere = " and  to_char(a.training_month,'yyyy-mm') = '"
				+ planTime + "'\n" + " and a.training_gather_id is null"
				+ " and a.workflow_status =2";
		return this.findAllList(strWhere, rowStartIdxAndCount);
	}

	public PageObject trainPlanGatherAfter(String approve, String entryIds,
			String planTime, String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject obj = new PageObject();
		String sql = "select\n"
				+ "b.training_name,\n"
				+ "b.training_level,\n"
				+ "b.training_number,\n"
				+ "b.training_hours,\n"
				+ "getdeptname(a.training_dep) deptName,\n"
				+ "(select c.training_type_name from BP_C_TRAINING_TYPE c where c.training_type_id = b.training_type_id) planTypeName,\n"
				+ "b.charge_by,\n"
				+ "d.workflow_no,\n"
				+ "d.workflow_status,\n"
				+ "d.training_gather_id,\n"
				+ "b.training_detail_id,\n"
				+ "b.training_type_id\n,"
				+ "to_char(d.training_month,'yyyy-mm') trainingMonth \n"
				+ "from bp_j_training_gather d, BP_J_TRAINING_MAIN a ,BP_J_TRAINING_DETAIL b\n"
				+ "where  a.is_use = 'Y' and b.is_use = 'Y'\n"
				+ "and d.is_use ='Y'\n"
				+ "\n" + "and a.training_gather_id=d.training_gather_id\n"
				+ "and b.training_main_id = a.training_main_id\n";

		if (approve != null && approve.equals("approve")) {
			sql +=
			// " and d.workflow_status='1'" +
			" and d.workflow_no in (" + entryIds + ") \n";
			if(planTime==null||planTime.equals(""))
			{
				//add by fyyang 20100524 如无月份则取待审批的最大月份
				sql+=
					"and  to_char(d.training_month,'yyyy-mm') =\n" +
					"(\n" + 
					" select  to_char(max(dd.training_month),'yyyy-MM') from bp_j_training_gather dd\n" + 
					" where dd.is_use='Y'\n" + 
					"and dd.workflow_no in (" + entryIds + ")\n"+
					") \n";

			}
			else
			{
				sql+= "and  to_char(d.training_month,'yyyy-mm') = '" + planTime+"' \n";
			}
		}
		else
		{
			sql+= "and  to_char(d.training_month,'yyyy-mm') = '" + planTime+"'\n";
		}
		// modified by liuyi 091218 
//		sql+= "order by b.training_type_id,b.training_level ";
		sql+= " order by a.training_dep,b.training_type_id, b.training_level,b.training_detail_id ";
		String sqlcount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
		List arraylist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			BpJTrainingTypeForm fmodel = new BpJTrainingTypeForm();
			BpJTrainingDetail model = new BpJTrainingDetail();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setTrainingName(data[0].toString());
			if (data[1] != null)
				model.setTrainingLevel(Long.parseLong(data[1].toString()));
			if (data[2] != null)
				model.setTrainingNumber(Long.parseLong(data[2].toString()));
			if (data[3] != null)
				model.setTrainingHours(Double.parseDouble(data[3].toString()));
			if (data[4] != null)
				fmodel.setDeptName(data[4].toString());
			if (data[5] != null)
				fmodel.setPlanTypeName(data[5].toString());
			if (data[6] != null)
				model.setChargeBy(data[6].toString());
			if (data[7] != null)
				fmodel.setWorkflowNo(data[7].toString());
			if (data[8] != null)
				fmodel.setWorkflowStatus(data[8].toString());
			if (data[9] != null)
				fmodel.setGatherId(data[9].toString());
			if(data[10] != null)
				model.setTrainingDetailId(Long.parseLong(data[10].toString()));
			if(data[11] != null)
				model.setTrainingTypeId(Long.parseLong(data[11].toString()));
			if (data[12] != null)
				fmodel.setTrainingMonth(data[12].toString());
   
			fmodel.setTrainDetail(model);
			arraylist.add(fmodel);
		}
		obj.setList(arraylist);
		obj.setTotalCount(count);
		return obj;

	}

	private Long findGatherByPlanTime(String planTime) {
		String sql = "select t.training_gather_id from bp_j_training_gather t where to_char(t.training_month,'yyyy-mm') ='"
				+ planTime + "' ";
		Long gatherId = (bll.getSingal(sql) != null) ? Long.parseLong(bll
				.getSingal(sql).toString()) : 0l;
		return gatherId;
	}

	public void trainPlanGather(String ids, String planTime, String gatherBy,
			String enterpriseCode) {
		Date date = new Date();
		Long gatherId;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		if (this.findGatherByPlanTime(planTime) == 0l) {
			gatherId = bll.getMaxId("BP_J_TRAINING_GATHER",
					"TRAINING_GATHER_ID");
			BpJTrainingGather model = new BpJTrainingGather();
			model.setEnterpriseCode(enterpriseCode);
			model.setGatherBy(gatherBy);
			model.setGatherTime(date);
			model.setWorkflowStatus(0l);
			model.setTrainingGatherId(gatherId);
			try {
				model.setTrainingMonth(df.parse(planTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			model.setIsUse("Y");
			entityManager.persist(model);
		} else {
			gatherId = this.findGatherByPlanTime(planTime);
		}
		String sql = "update bp_j_training_main  t  set t.training_gather_id="
				+ gatherId + " where t.training_main_id in (" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public PageObject trainPlanGatherAproveList(String planTime,
			String entityIds, String enterpriseCode, int... rowStartIdxAndCount) {
		return null;

	}

	// --------------------------------add by drdu 091210-------------------------------------------------

	@SuppressWarnings("unchecked")
	public void update(List<Map> list,Long mainId,String workerCode,String month) throws ParseException {
		try { 
			Map<Long,Long[]> map = new HashMap<Long,Long[]>(); 
			for (Map data : list) {
				BpJTrainingDetail model = entityManager.find(BpJTrainingDetail.class, Long.parseLong(data.get("id").toString()))  ; 
				model.setFinishNumber(Long.parseLong(data.get("fn").toString())); 
				long leve = Long.parseLong(data.get("level").toString());
//				if(leve == 2)
//				{
					if(map.containsKey(model.getTrainingTypeId()))
					{
						Long[] cv =  map.get(model.getTrainingTypeId());
						cv[0] += model.getTrainingNumber();
						cv[1] += model.getFinishNumber();
					     
					}
					else {
						map.put(model.getTrainingTypeId(), new Long[]{model.getTrainingNumber(),model.getFinishNumber()});
					} 
					entityManager.persist(model);
					entityManager.flush();
					//entityManager.merge(model); 
				}
			//}

			BpJTrainingMain mainObj = entityManager.find(BpJTrainingMain.class, mainId);
			// modified by liuyi 20100505 
//			mainObj.setBackfillBy(workerCode);
//			mainObj.setBackfillDate(new Date());
//			mainObj.setBackfillWorkflowStatus(0L);
//			entityManager.merge(mainObj);
			SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
			String sql = 
				"update bp_j_training_main a\n" +
				"   set a.backfill_workflow_status = 0,\n" + 
				"       a.backfill_by              = '"+workerCode+"',\n" + 
				"       a.backfill_date            = to_date('"+sdfDay.format(new Date())+"', 'yyyy-mm-dd')\n" + 
				" where a.training_dep = '"+mainObj.getTrainingDep()+"'\n" + 
				"   and to_char(a.training_month, 'yyyy-mm') = '"+sdfMonth.format(mainObj.getTrainingMonth())+"'\n"+
				//add by sychen 20100518
				"   and a.backfill_workflow_status is null";
			bll.exeNativeSQL(sql);
			entityManager.flush();
			//add by drdu 20100613
			boolean isExist = isExistTrainingSumByMonth(month);
			if(isExist==false)
			{
				saveTrainingSumRecord(month);
			}else{
				String delSumSql = "delete bp_j_training_sum s\n" +
					" where s.training_month = to_date('"+month+"', 'yyyy-MM')";
				bll.exeNativeSQL(delSumSql);
				saveTrainingSumRecord(month);
			}
			
			//modify by drdu 20100611			
//			if(map != null && map.size()>0)
//			{
//				Iterator<Long> it = map.keySet().iterator(); 
//				while(it.hasNext())
//				{
//					Long key = it.next();
//					Long[] v = map.get(key);
//					BpJTrainingSum sumObj  = getTrainingSumBy(mainObj.getTrainingMonth().toString().substring(0,7),key,mainObj.getTrainingDep());
//					if(sumObj == null)
//					{
//						sumObj = new BpJTrainingSum();
//						sumObj.setTrainingMainId(bll.getMaxId("BP_J_TRAINING_SUM", "TRAINING_MAIN_ID"));
//						sumObj.setTrainingDep(mainObj.getTrainingDep());
//						sumObj.setTrainingMonth(mainObj.getTrainingMonth());
//						sumObj.setTrainingTypeId(key);
//						sumObj.setTrainingNumber(v[0]);
//						sumObj.setFinishNumber(v[1]);
//						sumObj.setIsUse("Y");
//						sumObj.setEnterpriseCode(mainObj.getEnterpriseCode());
//						entityManager.persist(sumObj);
//					} 
//					else {
//						sumObj.setTrainingNumber(v[0]);
//						sumObj.setFinishNumber(v[1]);
//						entityManager.merge(sumObj);
//					} 
//				}
//			}
		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	/**
	 * 在完成情况回填汇总页面，点查询按钮时先执行此方法
	 * 对培训计划完成情况统计BP_J_TRAINING_SUM表增加记录
	 * add by drdu 20100611
	 * @param month
	 */
	public void saveTrainingSumRecord(String month)
	{
		String insertSql = 
			"insert into BP_J_TRAINING_SUM(TRAINING_MAIN_ID,TRAINING_MONTH,TRAINING_DEP,TRAINING_TYPE_ID,TRAINING_NUMBER,FINISH_NUMBER,IS_USE,ENTERPRISE_CODE)\n" +
			"select ((select nvl(max(TRAINING_MAIN_ID),0) from BP_J_TRAINING_SUM)+ row_number() over( order by t.training_month ) ),\n" + 
			"       t.training_month,\n" + 
			"       t.training_dep,\n" + 
			"       t.training_type_id,\n" + 
			"       sum(t.training_number),\n" + 
			"       sum(t.finish_number),\n" + 
			"       t.is_use,\n" + 
			"       t.enterprise_code\n" + 
			"  from (select b.training_month,\n" + 
			"               b.training_dep,\n" + 
			"               a.training_type_id,\n" + 
			"               b.fill_by,\n" + 
			"               a.training_number,\n" + 
			"               a.finish_number,\n" + 
			"               a.is_use,\n" + 
			"               a.enterprise_code\n" + 
			"          from BP_J_TRAINING_DETAIL a, BP_J_TRAINING_MAIN b\n" + 
			"         where a.training_main_id = b.training_main_id\n" + 
			"           and a.is_use = 'Y'\n" + 
			"           and b.is_use = 'Y'\n" + 
			"           and a.training_level =\n" + 
			"               decode((select count(1)\n" + 
			"                        from BP_J_TRAINING_DETAIL aa, BP_J_TRAINING_MAIN bb\n" + 
			"                       where aa.training_main_id = bb.training_main_id\n" + 
			"                         and aa.training_type_id = a.training_type_id\n" + 
			"                         and bb.training_dep = b.training_dep\n" + 
			"                         and bb.fill_by = a.fill_by\n" + 
			"                         and aa.is_use = 'Y'\n" + 
			"                         and bb.is_use = 'Y'\n" + 
			"                         and bb.training_month = b.training_month),\n" + 
			"                      1,\n" + 
			"                      a.training_level,\n" + 
			"                      '2')\n" + 
			"           and b.training_month = to_date('"+month+"', 'yyyy-MM')) t\n" + 
			" group by t.training_month, t.training_dep, t.training_type_id,t.is_use, t.enterprise_code";

		bll.exeNativeSQL(insertSql);
	}


	/**
	 * 根据月份判断培训计划完成情况统计BP_J_TRAINING_SUM表是否已存在记录
	 * add by drdu 20100612
	 * @param month
	 * @param enterpriseCode
	 * @return
	 */
	private boolean isExistTrainingSumByMonth(String month)
	{
		String sumSql ="select count(1)\n" +
			"  from bp_j_training_sum s\n" + 
			" where s.is_use = 'Y'\n" + 
			"   and s.training_month = to_date('"+month+"', 'yyyy-MM')";

       int count=Integer.parseInt(bll.getSingal(sumSql).toString());
       if(count>0) 
    	   return true;
       else 
    	   return false;
	}
	
	/**
	 * 回填汇总上报动作前的保存方法
	 * @param month
	 * @param trainingMainId
	 */
	public void addSumApproveRecord(String month,Long trainingMainId)
	{
		BpJTrainingSum model = entityManager.find(BpJTrainingSum.class,trainingMainId);
		BpJTrainingSumApproval sumApprovalObj = getTrainingSumApproveIdBy(month);
		if (sumApprovalObj == null) {
			sumApprovalObj = new BpJTrainingSumApproval();
			sumApprovalObj.setApprovalId(bll.getMaxId("BP_J_TRAINING_SUM_APPROVAL", "approval_id"));
			sumApprovalObj.setIsUse("Y");
			sumApprovalObj.setWorkflowStatus(0L);
			sumApprovalObj.setEnterpriseCode(model.getEnterpriseCode());

			entityManager.merge(sumApprovalObj);
		}
		if(model.getApprovalId()==null)
		{
			model.setApprovalId(sumApprovalObj.getApprovalId());
			entityManager.merge(model);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateBackGather(List<Map> list, String month,Long trainingMainId) {
		try {
			BpJTrainingSumApproval sumApprovalObj = getTrainingSumApproveIdBy(month);
			if (sumApprovalObj == null) {
				BpJTrainingSum model = entityManager.find(BpJTrainingSum.class,trainingMainId);
				sumApprovalObj = new BpJTrainingSumApproval();
				sumApprovalObj.setApprovalId(bll.getMaxId("BP_J_TRAINING_SUM_APPROVAL", "approval_id"));
				sumApprovalObj.setIsUse("Y");
				sumApprovalObj.setWorkflowStatus(0L);
				sumApprovalObj.setEnterpriseCode(model.getEnterpriseCode());

				entityManager.merge(sumApprovalObj);
			}
			for (Map data : list) {
				BpJTrainingSum model = entityManager.find(BpJTrainingSum.class,Long.parseLong(data.get("id").toString()));
				model.setFinishNumber(Long.parseLong(data.get("fn").toString()));
				model.setTrainingNumber(Long.parseLong(data.get("tn").toString()));
				entityManager.merge(model);

				model.setApprovalId(sumApprovalObj.getApprovalId());
				entityManager.merge(model);
			}

		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public BpJTrainingSumApproval getTrainingSumApproveIdBy(String month) {
		String sqlString = "select p.*\n" +
			"  from bp_j_training_sum a, BP_J_TRAINING_SUM_APPROVAL p\n" + 
			" where a.approval_id = p.approval_id\n" + 
			"   and a.training_month = to_date('"+month+"','yyyy_MM')\n" + 
			"   and a.is_use = 'Y'";

		List<BpJTrainingSumApproval> list = bll.queryByNativeSQL(sqlString,BpJTrainingSumApproval.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public BpJTrainingSum getTrainingSumBy(String month,Long typeId,String deptCode)
	{
		String sumSql = "select t.*\n" +
		"  from bp_j_training_sum t\n" + 
		" where t.training_month = to_date('"+month+"', 'yyyy_MM')\n" + 
		"   and t.training_type_id = "+typeId+"\n" + 
		"   and t.training_dep = '"+deptCode+"'\n" + 
		"   and t.is_use = 'Y'"; 
		List<BpJTrainingSum> list = bll.queryByNativeSQL(sumSql, BpJTrainingSum.class);
		if(list != null && list.size()>0)
		{
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public BpJTrainingSumForm getTrainingSumIdForAdd(String month)
	{
		BpJTrainingSumForm sumModel = null;
		String sqlString = "select d.training_main_id,\n" +
			"                        d.approval_id,\n" + 
			"                        d.training_month\n" + 
			"                   from bp_j_training_sum d, BP_J_TRAINING_MAIN m\n" + 
			"                  where d.training_month = to_date('"+month+"', 'yyyy-MM')\n" + 
			"                    and d.is_use = 'Y'\n" + 
			"                    and m.is_use = 'Y'\n" + 
			"                    and d.training_month = m.training_month\n" + 
			"                    and d.training_dep = m.training_dep\n" + 
			"                    and m.backfill_workflow_status = 2";

		List<Object[]> list = bll.queryByNativeSQL(sqlString);
		if (list != null && list.size() > 0) {
			sumModel = new BpJTrainingSumForm();
			Object[] f = list.get(0);
			if(f[0] != null)
				sumModel.setTrainingMainId(Long.parseLong(f[0].toString()));
			if(f[1] != null)
				sumModel.setApprovalId(Long.parseLong(f[1].toString()));
			if(f[2] != null)
				sumModel.setTrainingMonth(f[2].toString());
		}
		return sumModel;
	}
	
	

	@SuppressWarnings("unchecked")
	public PageObject findTrainPlanBackfillList(String enterpriseCode,
			String planDept, String planDate,String report_By,String flag,String entryIds,int... rowStartIdxAndCount) {
		PageObject obj = new PageObject();

		String sql = "SELECT A.TRAINING_MAIN_ID,\n"
				+ "       A.TRAINING_MONTH,\n"
				+ "       A.TRAINING_DEP,\n"
				+ "       getdeptname(A.TRAINING_DEP),\n"
				+ "       A.BACKFILL_WORKFLOW_NO,\n"
				+ "       A.BACKFILL_WORKFLOW_STATUS,\n"
				+ "       getworkername(A.BACKFILL_BY),\n"
				+ "       A.BACKFILL_DATE,\n"
				+ "       B.TRAINING_DETAIL_ID,\n"
				+ "       B.TRAINING_NAME,\n"
				+ "       B.TRAINING_TYPE_ID,\n"
				+ "       (SELECT C.TRAINING_TYPE_NAME\n"
				+ "          FROM BP_C_TRAINING_TYPE C\n"
				+ "         WHERE C.TRAINING_TYPE_ID = B.TRAINING_TYPE_ID\n"
				+ "           AND C.IS_USE = 'Y'),\n"
				+ "       B.TRAINING_LEVEL,\n"
				+ "       B.TRAINING_NUMBER,\n"
				+ "       B.FINISH_NUMBER,\n"
				+ "       B.TRAINING_HOURS,\n"
				+ "       B.CHARGE_BY,\n"
				+ "       A.BACKFILL_BY\n"
				// modified by liuyi 20100505 不需要汇总审批后才能进行回填
//				+ "  FROM BP_J_TRAINING_MAIN A, BP_J_TRAINING_DETAIL B, BP_J_TRAINING_GATHER G\n"
				+ "  FROM BP_J_TRAINING_MAIN A, BP_J_TRAINING_DETAIL B\n"
				+ " WHERE B.TRAINING_MAIN_ID = A.TRAINING_MAIN_ID\n"
				//+ "   AND A.TRAINING_GATHER_ID = G.TRAINING_GATHER_ID\n"
				+ "   AND A.TRAINING_MONTH = to_date('" + planDate
				+ "', 'yyyy-MM')\n" 
//				+" and A.report_By = " + report_By
//				+ "   AND A.TRAINING_DEP = '" + planDept+ "'\n" 
//				+ "   AND G.WORKFLOW_STATUS = '2'\n"
				+ "   AND A.WORKFLOW_STATUS = '2'\n"
				+ "   AND A.IS_USE = 'Y'\n" + "   AND B.IS_USE = 'Y'\n"
				//+ "   AND G.IS_USE = 'Y'\n"
				+ "   AND A.ENTERPRISE_CODE = '"+enterpriseCode+"'\n"
				+ "   AND B.ENTERPRISE_CODE = '"+enterpriseCode+"'\n";
				//+ "   AND G.ENTERPRISE_CODE = '"+enterpriseCode+"'";

		if(flag != null && !flag.equals(""))
		{
			if(flag.equals("Y"))
			{
				//sql+="   and A.backfill_workflow_status = 1\n";
				sql+=" and a.BACKFILL_WORKFLOW_NO in (" + entryIds + ")";
				
			}
			if(flag.equals("N"))
			{
				sql+=" AND (A.BACKFILL_WORKFLOW_STATUS =0 or A.BACKFILL_WORKFLOW_STATUS =3 or A.BACKFILL_WORKFLOW_STATUS is null)\n" +
					"and A.report_By = " + report_By+"";//完成情况填写上报时需要填写人过滤 modify by wpzhu 20100602
			}
		}
		
		//部门培训计划回填查询页面看其他部门
		if(flag!=null && flag.equals("dept")){
			
		}else{
			sql+="  AND A.TRAINING_DEP = '" + planDept+ "'\n";
		}
//		sql = sql +  "order by A.report_By,B.TRAINING_TYPE_ID,B.TRAINING_LEVEL,B.TRAINING_DETAIL_ID"; modify by ywliu 20100607
		sql = sql +  "order by A.TRAINING_DEP,A.Backfill_By,B.TRAINING_TYPE_ID";
//		System.out.println("the sql"+sql);
		String sqlcount = "select count(1) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				BpJTrainingBackfillForm form = new BpJTrainingBackfillForm();
				Object[] data = (Object[]) it.next();
				form.setTrainingMainId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					form.setTrainingMonth(data[1].toString());
				if (data[2] != null)
					form.setTrainingDep(data[2].toString());
				if (data[3] != null)
					form.setTrainingDepName(data[3].toString());
				if (data[4] != null)
					form.setBackfillWorkflowNo(Long.parseLong(data[4].toString()));
				if (data[5] != null)
					form.setBackfillWorkflowStatus(Long.parseLong(data[5].toString()));
				if (data[6] != null)
					form.setBackfillName(data[6].toString());
				if (data[7] != null)
					form.setBackfillDate(data[7].toString());
				if (data[8] != null)
					form.setTrainingDetailId(Long.parseLong(data[8].toString()));
				if (data[9] != null)
					form.setTrainingName(data[9].toString());
				if (data[10] != null)
					form.setTrainingTypeId(Long.parseLong(data[10].toString()));
				if (data[11] != null)
					form.setTrainingTypeName(data[11].toString());
				if (data[12] != null)
					form.setTrainingLevel(Long.parseLong(data[12].toString()));
				if (data[13] != null)
					form.setTrainingNumber(Long.parseLong(data[13].toString()));
				if (data[14] != null)
					form.setFinishNumber(Long.parseLong(data[14].toString()));
				if (data[15] != null)
					form.setTrainingHours(Double.parseDouble(data[15].toString()));
				if (data[16] != null)
					form.setChargeName(data[16].toString());
				if(data[17] != null)
					form.setBackfillBy(data[17].toString());
				
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlcount).toString());
		obj.setTotalCount(totalCount);
		obj.setList(arrlist);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public BpJTrainingTypeForm getBpTrainMainBackList(String planTime,
			String planDept) {
		BpJTrainingTypeForm model = null;
		String sql = "select getworkername(b.BACKFILL_BY) BACKFILL_BY,\n" +
			"       to_char(b.BACKFILL_DATE, 'yyyy-MM') BACKFILL_DATE,\n" + 
			"       decode(b.backfill_workflow_status,\n" + 
			"              0,\n" + 
			"              '未上报',\n" + 
			"              1,\n" + 
			"              '审批中',\n" + 
			"              2,\n" + 
			"              '审批已通过',\n" + 
			"              3,\n" + 
			"              '审批退回',\n" + 
			"              4,\n" + 
			"              '部门培训员已汇总') backfill_workflow_status,\n" + 
			"       getdeptname(b.training_dep)\n" + 
			"  from BP_J_TRAINING_MAIN b\n" + 
			" where b.training_dep = '"+planDept+"'\n" + 
			"   and to_char(b.training_month, 'yyyy-mm') = '"+planTime+"'";

		List<Object[]> list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			model = new BpJTrainingTypeForm();
			Object[] f = list.get(0);
			if (f[0] != null)
				model.setReportBy(f[0].toString());
			if (f[1] != null)
				model.setReportTime(f[1].toString());
			if (f[2] != null)
				model.setWorkflowStatus(f[2].toString());
			if (f[3] != null)
				model.setDeptName(f[3].toString());
		}  
		return model;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findPlanBackGatherList(String enterpriseCode,String month,String deptName,String flag, int... rowStartIdxAndCount)
	{
		PageObject obj = new PageObject();
		
		String sql = "select a.training_main_id,\n" +
			"       a.approval_id,\n" + 
			"       a.training_month,\n" + 
			"       a.training_dep,\n" + 
			"       getdeptname(a.training_dep),\n" + 
			"       a.training_type_id,\n" + 
			"       (select b.training_type_name\n" + 
			"          from BP_C_TRAINING_TYPE b\n" + 
			"         where b.training_type_id = a.training_type_id\n" + 
			"           and b.is_use = 'Y'),\n" + 
			"       p.workflow_status,\n" + 
			"       a.training_number,\n" + 
			"       a.finish_number,\n" + 
			"       p.workflow_no\n" + 
			"  from bp_j_training_sum a, BP_J_TRAINING_SUM_APPROVAL p\n" + 
			" where a.training_month = to_date('"+month+"', 'yyyy-MM')\n" + 
			"   and getdeptname(a.training_dep) = '"+deptName+"'\n" + 
			"   and a.approval_id = p.approval_id(+)\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and p.is_use(+) = 'Y'\n" + 
			"   and (p.workflow_status = 0 or p.workflow_status is null)\n" + 
			"   and p.enterprise_code(+) = '"+enterpriseCode+"'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'";
		
		String sqlcount = "select count(1) from (" + sql + ")";
		
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				BpJTrainingSumForm form = new BpJTrainingSumForm();
				Object[] data = (Object[]) it.next();
					form.setTrainingMainId(Long.parseLong(data[0].toString()));
					if(data[1] != null)
						form.setApprovalId(Long.parseLong(data[1].toString()));
					if(data[2] != null)
						form.setTrainingMonth(data[2].toString());
					if(data[3] != null)
						form.setTrainingDep(data[3].toString());
					if(data[4] != null)
						form.setTrainingDepName(data[4].toString());
					if(data[5] != null)
						form.setTrainingTypeId(Long.parseLong(data[5].toString()));
					if(data[6] != null)
						form.setTrainingTypeName(data[6].toString());
					if(data[7] != null)
						form.setWorkflowStatus(Long.parseLong(data[7].toString()));
					if(data[8] != null)
						form.setTrainingNumber(Long.parseLong(data[8].toString()));
					if(data[9] != null)
						form.setFinishNumber(Long.parseLong(data[9].toString()));
					if(data[10] != null)
						form.setWorkflowNo(Long.parseLong(data[10].toString()));
					arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlcount).toString());
		obj.setTotalCount(totalCount);
		obj.setList(arrlist);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public BpJTrainingSumApproval getBpTrainBackGatherList(String planTime) {
		BpJTrainingSumApproval model = null;
		String sql = "select distinct a.approval_id,\n" +
			"         a.workflow_no,\n" + 
			"          a.workflow_status\n" + 
			"           from bp_j_training_sum_approval a,\n" + 
			"            bp_j_training_sum s\n" + 
			"          where a.approval_id = s.approval_id\n" + 
			"            and a.is_use = 'Y'\n" + 
			"            and s.training_month = to_date('"+planTime+"', 'yyyy-MM')";
		List<Object[]> list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			model = new BpJTrainingSumApproval();
			Object[] f = list.get(0);
			if (f[0] != null)
				model.setApprovalId(Long.parseLong(f[0].toString()));
			if (f[1] != null)
				model.setWorkflowNo(Long.parseLong(f[1].toString()));
			if (f[2] != null)
				model.setWorkflowStatus(Long.parseLong(f[2].toString()));
		}  
		return model;
	}
	
	@SuppressWarnings("unchecked")
	public List findTraingDept(String month)
	{
		if(month!= null)
		{
		String sql= "select distinct s.training_dep, getdeptname(s.training_dep)\n" +
			"  from BP_J_TRAINING_SUM s, BP_J_TRAINING_MAIN m\n" + 
			" where s.is_use = 'Y'\n" + 
			"   and s.training_month = m.training_month\n" + 
			"   and s.training_dep = m.training_dep\n" + 
			"   and s.training_month = to_date('"+month+"', 'yyyy-mm')\n" + 
			"   and m.backfill_workflow_status = 2";

		List list=bll.queryByNativeSQL(sql);
		return list;
		
		}else {
			return null;
		}
	}
	
	public void backGatherReturnSelectMethod(String month,String deptCode)
	{
		String sqlString = "update BP_J_TRAINING_MAIN m\n" +
			"   set m.backfill_workflow_status = 3, m.backfill_workflow_no=null\n" + 
			" where m.training_month = to_date('"+month+"', 'yyyy_mm')\n" + 
			"   and m.training_dep in('"+deptCode+"')\n"+
			"   and m.is_use = 'Y'";
		bll.exeNativeSQL(sqlString);
	}
	
	
	@SuppressWarnings("unchecked")
	public void updateDeptGather(List<Map> list)
	{
		try{
			for (Map data : list) {
				BpJTrainingDetail model = entityManager.find(BpJTrainingDetail.class,Long.parseLong(data.get("id").toString()));
				model.setTrainingTypeId(Long.parseLong(data.get("trainingTypeId").toString()));
				
				if( data.get("trainingName") != null)
				{
					model.setTrainingName(data.get("trainingName").toString());
				}else{
					model.setTrainingName("");
				}
				model.setTrainingNumber(Long.parseLong(data.get("trainingNumber").toString()));
				model.setChargeBy(data.get("chargeBy").toString());
				
				
				if( data.get("trainingHours") != null)
				{
					model.setTrainingHours(Double.parseDouble(
							(data.get("trainingHours").toString())));
				}else{
					model.setTrainingHours(0d);
				}
				entityManager.merge(model);
			}
		}catch (RuntimeException e) {
			throw e;
		}
	}
	
// -----------------------------------------end ----------------------------------------------------
	
	public String getCompleteDescription(String month) {
		String temp= null;
		String sql = 
			"select distinct b.workflow_no from BP_J_TRAINING_SUM a,BP_J_TRAINING_SUM_APPROVAL b\n" +
			"where a.approval_id=b.approval_id\n" + 
			"and to_char(a.training_month,'yyyy-mm')='"+ month+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			temp = obj.toString();
		return temp;

	}
	public List<BpTrainPlanApproveForm> getGatherApprovelist(String month){
		String temp = month.substring(0, 4)+"-"+month.substring(4, 6); 
		String entryId = this.getCompleteDescription(temp);
		if(entryId == null)
			entryId ="";
		List list = new ArrayList();
		String sql = "SELECT t.*,getworkername(t.caller)" 
	 		+ " FROM wf_j_historyoperation t"
			+ " WHERE t.entry_id = '"
			+ entryId
			+ "'"
			+ "AND t.step_id in (4,5)"
			+ " AND opinion_time > "
			+ "(SELECT MAX(opinion_time)"
			+ "FROM wf_j_historyoperation t"
			+ " WHERE t.entry_id = '"
			+ entryId
			+ "'"
			+ "AND t.step_id = '2')"
			+ " ORDER BY t.opinion_time ";
		list = bll.queryByNativeSQL(sql);
		List<BpTrainPlanApproveForm> arraylist = new ArrayList<BpTrainPlanApproveForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			BpTrainPlanApproveForm model = new BpTrainPlanApproveForm();
			if (data[0] != null) {
				model.setId(Long.parseLong(data[0].toString()));
			}
			if (data[3] != null) {
				model.setStepName(data[3].toString());
			}
//			if(data[6] !=null)
//				model.setCaller(data[6].toString());
			if (data[8] != null) {
				model.setOpinion(data[8].toString());
			}
			if (data[9] != null) {
				model.setOpinionTime(data[9].toString());
			}
			if (data[10] != null) {
				model.setCaller(data[10].toString());
			}
			arraylist.add(model);
		}
		return arraylist;
	}

	/**
	 *  add by liuyi 20100427 
	 * 部门培训计划查询 只为该查询页面用 
	 * @param planTime
	 * @param planType
	 * @param planDept
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getDeptTrainPlanQueryList(String planTime,
			String planType, String planDept, String enterpriseCode, int... rowStartIdxAndCount) {
		String strWhere = " and a.enterprise_code='" + enterpriseCode + "'\n"
				+ " and b.enterprise_code='" + enterpriseCode + "'";
		if (planDept != null && !planDept.equals("")) {
			strWhere += "and a.training_dep ='" + planDept + "'\n";
		}
		if (planTime != null && !planTime.equals("")) {
			strWhere += " and to_char(a.training_month,'yyyy-mm')='" + planTime
					+ "'\n";
		}

		if (planType != null && !planType.equals("")) {
			strWhere += "and b.training_type_id ='" + planType + "'";
		}
		return this.findAllList(strWhere, rowStartIdxAndCount);
	}

	/**
	 * add by liuyi 
	 * 查询部门表中为一级部门的数据
	 */
	public PageObject getManageDeptList(String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.* from hr_c_dept a where a.dept_level='1'and a.is_use='Y' and a.enterprise_code='"+enterpriseCode+"'"; //update by sychen 20100902
//		String sql = "select a.* from hr_c_dept a where a.dept_level='1'and a.is_use='U' and a.enterprise_code='"+enterpriseCode+"'";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List<HrCDept> list = bll.queryByNativeSQL(sql, HrCDept.class, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}

	/**
	 * add by liuyi 20100427 获取计划汇总的工作流
	 * @param month
	 * @return
	 */
	public String getPlanGatherDesc(String month) {
		String temp = null;
		String sql = "select distinct t.workflow_no from bp_j_training_gather t where t.is_use='Y' and to_char(t.training_month,'yyyy-mm') ='"
				+ month + "'";
		Object obj = bll.getSingal(sql);
		if (obj != null)
			temp = obj.toString();
		return temp;
	}

	/**
	 * 判断一部门在某一月份下是否能审批
	 * add by liuyi 20100429 
	 * flag  update by sychen 20100518
	 * @param planDept
	 * @param planTime
	 * @param enterpriseCode
	 * @return
	 */
	public String judgeDeptCanApprove(String planDept, String planTime,
			String enterpriseCode,String flag) {
		if (planDept != null && planTime != null) {
			
			String sql = "select distinct c.dept_name,b.chs_name from bp_j_training_main a,hr_j_emp_info b,hr_c_dept c\n"
					+ "where a.fill_by = b.emp_code\n"
					+ "and b.dept_id=c.dept_id\n"
//					+ "and a.workflow_status=0\n"
					+ "and a.is_use='Y'\n"
					+ "and a.enterprise_code='"
					+ enterpriseCode
					+ "'\n"
					+ "and a.training_dep='"
					+ planDept
					+ "'\n"
					+ "and to_char(a.training_month,'yyyy-mm') ='"
					+ planTime
					+ "'";
			//add by sychen 20100518
			if(flag!=null && flag.equals("finish")){

				sql+= "and a.backfill_workflow_status=0\n";
			}
			else{
				sql+= "and a.workflow_status=0\n";
			}
			//add end 
			String str = null;
			List list = bll.queryByNativeSQL(sql);
			if (list != null && list.size() > 0) {
				Object[] d1 = (Object[])list.get(0);
				if(d1[0] != null)
					str = d1[0].toString();
				else
					str = "";
				
				if(d1[1] != null)
					str += d1[1].toString() + "数据未上报!";
				else 
					str += "填写数据未上报!";
				
				for (int i = 1; i < list.size(); i++) {
					Object[] di = (Object[])list.get(i);
					if(di[0] != null)
						str += "<br>" + di[0].toString();
					else
						str += "<br>";
					
					if(di[1] != null)
						str +=  di[1].toString() + "填写数据未上报!";
					else 
						str += "填写数据未上报!";
				}
			}
			return str;

		} else {
			return "未知错误！";
		}

	}
		
}
