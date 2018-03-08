package power.ejb.manage.plan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.plan.form.BpJPlanJobDepMainForm;

/**
 * Facade for entity BpJPlanJobDepDetail.
 * 
 * @see power.ejb.manage.plan.BpJPlanJobDepDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanJobDepDetailFacade implements
		BpJPlanJobDepDetailFacadeRemote {
	// property constants
	public static final String DEP_MAIN_ID = "depMainId";
	public static final String JOB_CONTENT = "jobContent";
	public static final String IF_COMPLETE = "ifComplete";
	public static final String COMPLETE_DESC = "completeDesc";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String COMPLETE_DATA = "completeData";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved BpJPlanJobDepDetail
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpJPlanJobDepDetail entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpJPlanJobDepDetail entity) {
		LogUtil.log("saving BpJPlanJobDepDetail instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpJPlanJobDepDetail entity.
	 * 
	 * @param entity
	 *            BpJPlanJobDepDetail entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanJobDepDetail entity) {
		LogUtil.log("deleting BpJPlanJobDepDetail instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJPlanJobDepDetail.class,
					entity.getJobId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<BpJPlanJobDepDetail> addList) {
		if (addList != null && addList.size() > 0) {
			int i = 0;
			Long idtemp = dll.getMaxId("bp_j_plan_job_dep_detail", "job_id");
			for (BpJPlanJobDepDetail entity : addList) {
				Long id = idtemp + i++;

				entity.setJobId(id);
				this.save(entity);
			}
		}
	}

	public boolean delete(String ids) {
		try {
			String[] temp1 = ids.split(";");

			for (String i : temp1) {
				BpJPlanJobDepDetail entity = new BpJPlanJobDepDetail();
				entity = this.findById(Long.parseLong(i));
				this.delete(entity);

			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved BpJPlanJobDepDetail entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanJobDepDetail entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanJobDepDetail entity to update
	 * @return BpJPlanJobDepDetail the persisted BpJPlanJobDepDetail entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanJobDepDetail update(BpJPlanJobDepDetail entity) {
		LogUtil.log("updating BpJPlanJobDepDetail instance", Level.INFO, null);
		try {
			BpJPlanJobDepDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void update(List<BpJPlanJobDepDetail> updateList) {
		if (updateList != null && updateList.size() > 0) {

			for (BpJPlanJobDepDetail entity : updateList) {

				this.update(entity);
			}
		}
	}

	public BpJPlanJobDepDetail findById(Long id) {
		LogUtil.log("finding BpJPlanJobDepDetail instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanJobDepDetail instance = entityManager.find(
					BpJPlanJobDepDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpJPlanJobDepDetail entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanJobDepDetail property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanJobDepDetail> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanJobDepDetail> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpJPlanJobDepDetail instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanJobDepDetail model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			System.out.println("dddd"+query.getResultList().toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	//update by sychen 20100630
	public PageObject findByDepMainId(String depMainId,String enterpriseCode,int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "SELECT d.job_id,\n" +
			"       d.dep_main_id,\n" + 
			"       d.job_content,\n" + 
			"       d.complete_data,\n" + 
			"       d.if_complete,\n" + 
			"       d.complete_desc,\n" + 
			"       d.order_by\n" + 
			"  FROM BP_J_PLAN_JOB_DEP_DETAIL d\n" + 
			" WHERE d.dep_main_id = ' "+depMainId+" '\n" + 
			"   AND d.enterprise_code = '"+enterpriseCode+"'\n" + 
			" ORDER BY to_number(d.order_by）";
		
		String sqlCount =	"SELECT count(*)\n" +
			"  FROM BP_J_PLAN_JOB_DEP_DETAIL d\n" + 
			" WHERE d.dep_main_id = '"+depMainId+"'\n" + 
			"   AND d.enterprise_code = '"+enterpriseCode+"'\n" + 
			" ORDER BY d.order_by";

			
		List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(dll.getSingal(sqlCount).toString()));
		return pg;
		
	}
	// update bjxu 091224
//	public List<BpJPlanJobDepDetail> findByDepMainId(Object depMainId) {
//		 try {
//			 String sql ="select * from BP_J_PLAN_JOB_DEP_DETAIL t where t.dep_main_id='"+depMainId+"' order by t.JOB_ID";
//			 List<BpJPlanJobDepDetail> list =new ArrayList<BpJPlanJobDepDetail>();
//			 if (depMainId !=null) {
//				 list = dll.queryByNativeSQL(sql, BpJPlanJobDepDetail.class); 
//			 }
//			 return list;
//		 } catch (RuntimeException re) {
//			 LogUtil.log("find by depmainid failed", Level.SEVERE, re);
//				throw re; 
//		 }
//		
//	}
	//update by sychen 20100630 end 
	@SuppressWarnings("unchecked")
	public PageObject findByDepMainIdApprove(String depMainId) throws Exception {
			 PageObject pg = new PageObject();
			 String sql=
				 "SELECT t.job_id,\n" +
				 "       t.dep_main_id,\n" + 
				 "       t.job_content,\n" + 
				 "       t.if_complete,\n" + 
				 "       t.complete_desc,\n" + 
				 "       t.complete_data,\n" + 
				 "       a.edit_depcode,\n" + 
				 "       getdeptname(a.edit_depcode) deptName,\n" + 
				 "       getworkername(a.edit_by) editbyname,\n" + 
				 "       t.charge_by,\n" + 
				 "       getworkername(t.charge_by) chargeName,\n" + 
				 "       to_char(a.edit_date, 'yyyy-mm-dd')edit_date,\n" + 
				 "       a.sign_status,\n" + 
				 "       t.order_by,\n" + 
				 "       (SELECT getdeptname(t.dept_code)\n" + 
				 "          FROM hr_c_dept t\n" + 
				 "         WHERE t.dept_level = 1\n" + 
				 "         AND rownum = 1\n" + 
				 "         START WITH t.dept_id = c.dept_id\n" + 
				 "        CONNECT BY PRIOR t.pdept_id = t.dept_id)level1DeptName,\n" + 
				 "       a.work_flow_no\n" + 
				 "  FROM BP_J_PLAN_JOB_DEP_DETAIL t,\n" + 
				 "       hr_c_dept               c,\n" +
				 "       Bp_j_Plan_Job_Dep_Main   a\n" + 
				 " WHERE t.dep_main_id = a.dep_main_id\n" + 
				 "   AND c.dept_code = a.edit_depcode\n" + 
				 "  AND a.dep_main_id in ("+ depMainId+ ")\n" + 
				 "  ORDER BY a.edit_depcode,t.job_id  ";
				List list = dll.queryByNativeSQL(sql);
				pg.setList(list);
				return pg;
	}

	public List<BpJPlanJobDepDetail> findByJobContent(Object jobContent) {
		return findByProperty(JOB_CONTENT, jobContent);
	}

	public List<BpJPlanJobDepDetail> findByIfComplete(Object ifComplete) {
		return findByProperty(IF_COMPLETE, ifComplete);
	}

	public List<BpJPlanJobDepDetail> findByCompleteDesc(Object completeDesc) {
		return findByProperty(COMPLETE_DESC, completeDesc);
	}

	public List<BpJPlanJobDepDetail> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all BpJPlanJobDepDetail entities.
	 * 
	 * @return List<BpJPlanJobDepDetail> all BpJPlanJobDepDetail entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanJobDepDetail> findAll() {
		LogUtil.log("finding all BpJPlanJobDepDetail instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from BpJPlanJobDepDetail model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	
//	public PageObject queryBpJPlanJobDepDetail(String planTime,
//			String editDepcode,String enterpriseCode, int start, int limit) {
	//update by sychen 20100414
	public PageObject queryBpJPlanJobDepDetail(String planTime,
			String editDepcode,String editBy, String flag,String enterpriseCode, int start, int limit) {
		PageObject object = new PageObject();
		String strWhere = "";
       // add by sychen 20100528
		String sqlString="SELECT s.dep_main_id,\n" +
			"       to_char(s.edit_date, 'yyyy-mm-dd') edit_date,\n" + 
			"       s.finish_work_flow_no,\n" + 
			"       s.finish_sign_status,\n" + 
			"       getworkername(s.edit_by) editbyname,\n" + 
			"       getdeptname(s.edit_depcode) deptname,\n" + 
			"       t.job_content,\n" + 
			"       t.complete_data,\n" + 
			"       t.if_complete,\n" + 
			"       t.complete_desc,\n" + 
			"       t.order_by,\n" + 
			"       (SELECT getdeptname(t.dept_code)\n" + 
			"          FROM hr_c_dept t\n" + 
			"         WHERE t.dept_level = 1\n" + 
			"           AND rownum = 1\n" + 
			"         START WITH t.dept_id = c.dept_id\n" + 
			"        CONNECT BY PRIOR t.pdept_id = t.dept_id) level1DeptName\n" + 
			"  FROM bp_j_plan_job_dep_detail t,\n" + 
			"       bp_j_plan_job_dep_main   s,\n" + 
			"       hr_c_dept                c\n" + 
			" WHERE s.dep_main_id = t.dep_main_id\n" + 
			"   AND c.dept_code = s.edit_depcode\n" + 
			"   AND s.enterprise_code = '" + enterpriseCode + "'\n";
        // add end ---------------
		
//		String sqlString = "select s.*,"
//				+ " getworkername(s.edit_by) editbyname,getdeptname(s.edit_depcode) deptname ,"
//				+ " t.job_content,t.complete_data,t.if_complete,t.complete_desc,t.order_by, \n"
//				+ " (SELECT getdeptname(t.dept_code)\n" + 
//						"          FROM hr_c_dept t\n" + 
//						"         WHERE t.dept_level = 1\n" +  
//						 "        AND rownum = 1\n" + 
//						"         START WITH t.dept_id = c.dept_id\n" + 
//						"        CONNECT BY PRIOR t.pdept_id = t.dept_id)level1DeptName\n"  
//				+ " from bp_j_plan_job_dep_detail t,"
//				+ "bp_j_plan_job_dep_main s, "
//				+"  hr_c_dept                c\n" 
//				+ " where  s.dep_main_id=t.dep_main_id "
//				+"   AND c.dept_code = s.edit_depcode\n" 
//				+ " and s.enterprise_code='" + enterpriseCode + "'";
		String sqlCount = "select count(*) from bp_j_plan_job_dep_detail t,"
				+ "bp_j_plan_job_dep_main s "
				+ " where  s.dep_main_id=t.dep_main_id";
		if (planTime != null && !planTime.equals("")) {
			strWhere += " and s.plan_time=" + "to_date('" + planTime
					+ "'||'-01','yyyy-MM-dd ') ";
		}
		//add by sychen 201110414
		if (flag != null && flag.equals("query")) {
			if (editDepcode != null && !editDepcode.equals("")) {
			  strWhere += " and substr(s.edit_depcode,1,"+editDepcode.length()+")='" + editDepcode + "'";
			}
//			if (editBy != null && !editBy.equals("")) {
//				strWhere += " and s.edit_by='" + editBy + "'";
//			}
		} 
		else if (editDepcode != null && !editDepcode.equals("")) {
			strWhere += " and s.edit_depcode='" + editDepcode + "'";
		}
		
		sqlString += strWhere;
		sqlString += " order by s.edit_date desc,t.job_id ";
		sqlCount += strWhere;
		List list = dll.queryByNativeSQL(sqlString, start, limit);
		BpJPlanJobDepMainForm model = new BpJPlanJobDepMainForm();
		BpJPlanJobDepMain baseInfo = new BpJPlanJobDepMain();

		Iterator it = list.iterator();

		List<BpJPlanJobDepMainForm> arraylist = new ArrayList<BpJPlanJobDepMainForm>();
		while (it.hasNext()) {
			model = new BpJPlanJobDepMainForm();
			baseInfo=new BpJPlanJobDepMain();
			Object[] data = (Object[]) it.next();
			// add by sychen 20100528
			if (data[0] != null) {
				baseInfo.setDepMainId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				model.setEditDate(data[1].toString());
			}
			if (data[2] != null) {
				baseInfo
						.setFinishWorkFlowNo(Long.parseLong(data[2].toString()));
			}
			if (data[3] != null) {
				baseInfo.setFinishSignStatus(Long.parseLong(data[3].toString()));
			}
			if (data[4] != null) {
				model.setEditByName(data[4].toString());
			}
			if (data[5] != null) {
				model.setEditDepName(data[5].toString());
			}
			if (data[6] != null) {
				model.setJobContent(data[6].toString());
			}
			if (data[7] != null) {
				model.setCompleteData(data[7].toString());
			}
			if (data[8] != null) {
				model.setIfComplete(data[8].toString());
			}
			if (data[9] != null) {
				model.setCompleteDesc(data[9].toString());
			}
			if (data[10] != null) {
				model.setOrderBy(data[10].toString());
			}
			if (data[11] != null) {
				model.setLevel1DeptName(data[11].toString());
			}
			//add end---------------
			
// if (data[0] != null) {
//				baseInfo.setDepMainId(Long.parseLong(data[0].toString()));
//			}
			// if(data[1]!=null){
			// baseInfo.setDepMainId(data[0].toString());
			// }
			// if(data[2]!=null){
			// model.setEditDate(data[0].toString());
			// }
//			if (data[3] != null) {
//				model.setEditDate(data[3].toString());
//			}
			// if(data[4]!=null){
			// baseInfo.setDepMainId(data[0].toString());
			// }
			// if(data[5]!=null){
			// baseInfo.setPlanStatus(Long.parseLong(data[5].toString()));
			// }
			// if(data[6]!=null){
			// baseInfo.setDepMainId(data[0].toString());
			// }
			// if(data[7]!=null){
			// baseInfo.setDepMainId(data[0].toString());
			// }
			// if(data[8]!=null){
			// baseInfo.setDepMainId(data[0].toString());
			// }
//			 if(data[9]!=null){
//			 baseInfo.setFinishWorkFlowNo(Long.parseLong(data[9].toString()));
//			 }
//			 if(data[10]!=null){
//				 baseInfo.setFinishSignStatus(Long.parseLong(data[10].toString()));
//				 }
//			
//			if (data[18] != null) {
//				model.setEditByName(data[18].toString());
//			}
//			if (data[19] != null) {
//				model.setEditDepName(data[19].toString());
//			}
//			if (data[20] != null) {
//				model.setJobContent(data[20].toString());
//			}if (data[21] != null) {
//				model.setCompleteData(data[21].toString());
//			}
//			if (data[22] != null) {
//				model.setIfComplete(data[22].toString());
//			}
//			if (data[23] != null) {
//				model.setCompleteDesc(data[23].toString());
//			}
//			if (data[24] != null) {
//				model.setOrderBy(data[24].toString());
//			}


			model.setBaseInfo(baseInfo);

			arraylist.add(model);
		}

		Long countLong = Long.parseLong(dll.getSingal(sqlCount).toString());
		object.setList(arraylist);
		object.setTotalCount(countLong);
		return object;
	}

	@SuppressWarnings("unchecked")
	public PageObject getBpJPlanJobDepDetailStat(String planTime,
			String enterpriseCode) {
		String sql = "SELECT getdeptname(s.edit_depcode), "
				+ " (select count(*) from bp_j_plan_job_dep_detail t "
				+ "	 where t.if_complete='0' and t.dep_main_id=s.dep_main_id),"
				+ " ( select count(*) from bp_j_plan_job_dep_detail t "
				+ " where t.if_complete='1' and t.dep_main_id=s.dep_main_id),"
				+ " (select count(*) from bp_j_plan_job_dep_detail t "
				+ " where t.if_complete='2' and t.dep_main_id=s.dep_main_id)"
				+ " FROM  bp_j_plan_job_dep_main  s " + " where  s.plan_time="
				+ "to_date('" + planTime + "'||'-01','yyyy-MM-dd ') "
				+ " and s.enterprise_code='" + enterpriseCode + "'"
				+ " order by s.edit_date ";
		String sqlCount = "select count(*) " + " from bp_j_plan_job_dep_main ";
		List list = dll.queryByNativeSQL(sql);
		BpJPlanJobDepMainForm model = new BpJPlanJobDepMainForm();

		Iterator it = list.iterator();
		PageObject object = new PageObject();
		List<BpJPlanJobDepMainForm> arraylist = new ArrayList<BpJPlanJobDepMainForm>();
		while (it.hasNext()) {

			Object[] data = (Object[]) it.next();
			model =new BpJPlanJobDepMainForm();
			if (data[0] != null) {
				model.setEditDepName(data[0].toString());
			}
			if (data[1] != null) {
				model.setUnfinished(Long.parseLong(data[1].toString()));
			}
			if (data[2] != null) {
				model.setPartfinished(Long.parseLong(data[2].toString()));
			}
			if (data[3] != null) {
				model.setFinishPlan(Long.parseLong(data[3].toString()));
			}

			arraylist.add(model);
		}

		Long totalCount = Long.parseLong(dll.getSingal(sqlCount).toString());
		object.setList(arraylist);
		object.setTotalCount(totalCount);
		return object;

	}

}