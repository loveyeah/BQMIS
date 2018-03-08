package power.ejb.hr.reward;

import java.util.ArrayList;
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

/**
 * Facade for entity HrJRewardApprove.
 * 
 * @see power.ejb.hr.reward.HrJRewardApprove
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJRewardApproveFacade implements HrJRewardApproveFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(HrJRewardApprove entity) {
		LogUtil.log("saving HrJRewardApprove instance", Level.INFO, null);
		try {
			entity.setApproveId(bll.getMaxId("HR_J_REWARD_APPROVE", "APPROVE_ID"));
			
			entityManager.persist(entity);
			entityManager.flush();
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(HrJRewardApprove entity) {
		LogUtil.log("deleting HrJRewardApprove instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJRewardApprove.class, entity
					.getApproveId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJRewardApprove update(HrJRewardApprove entity) {
		LogUtil.log("updating HrJRewardApprove instance", Level.INFO, null);
		try {
			HrJRewardApprove result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJRewardApprove findById(Long id) {
		LogUtil.log("finding HrJRewardApprove instance with id: " + id,
				Level.INFO, null);
		try {
			HrJRewardApprove instance = entityManager.find(
					HrJRewardApprove.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HrJRewardApprove> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJRewardApprove instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJRewardApprove model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HrJRewardApprove> findAll(Long deptId, Long workerId) {
		//LogUtil.log("finding all HrJRewardApprove instances", Level.INFO, null);
		try {
			List<HrJRewardApprove> resultList=new ArrayList<HrJRewardApprove>();
			
//			String roleSql = "SELECT COUNT(*)\n" +
//					"  FROM sys_j_ur      a,\n" + 
//					"       sys_c_ul      b,\n" + 
//					"       hr_j_emp_info c\n" + 
//					" WHERE a.WORKER_ID = b.worker_id\n" + 
//					"   AND a.ROLE_ID IN\n" + 
//					"       (SELECT DISTINCT e.role_id\n" + 
//					"          FROM sys_c_fls d,\n" + 
//					"               sys_j_rrs e\n" + 
//					"         WHERE d.file_id = e.file_id\n" + 
//					"           AND d.file_addr IN\n" + 
//					"               ('hr/reward/monthAward/business/rewardProvide/mainApprove/monthRewardProvide.jsp',\n" + 
//					"                'hr/reward/monthAward/business/rewardProvide/deptAdminApprove/deptAdminList.jsp',\n" + 
//					"                'hr/reward/monthAward/business/rewardProvide/shfitAdminApprove/shfitAdminList.jsp',\n" + 
//					"                'hr/reward/bigReward/bigRewardApprove/bigRewardApprove.jsp',\n" + 
//					"                'hr/reward/bigReward/deptAdminApprove/deptAdminList.jsp',\n" + 
//					"                'hr/reward/bigReward/shfitAdminApprove/shfitAdminList.jsp'))\n" + 
//					"   AND b.worker_code = c.emp_code\n" + 
//					"   AND a.is_use = 'Y'\n" + 
//					"   AND c.emp_id = '"+workerId+"'";
//
//			int roleId = Integer.parseInt(bll.getSingal(roleSql).toString());
//		
//			if(roleId >0 ) {
//				String sql = "select * from hr_j_reward_approve t where t.DEPT_ID='"+deptId+"'  and t.flag in ('1','2')";
//				resultList = bll.queryByNativeSQL(sql, HrJRewardApprove.class);
//				if(resultList == null || resultList.size()==0) {
//					 sql = "select * from hr_j_reward_approve t where t.DEPT_ID is null and t.flag in ('1','2')";
//					 resultList = bll.queryByNativeSQL(sql, HrJRewardApprove.class);
//				}
//				
//			}
//			//-------------------add by fyyang 20100715--大奖月奖上报------------------------------
//			 String sql=
//				 "select t2.* from\n" +
//				 " ( SELECT distinct d.file_addr\n" + 
//				 "          FROM sys_c_fls d,\n" + 
//				 "               sys_j_rrs e,\n" + 
//				 "               sys_j_ur a,\n" + 
//				 "               sys_c_ul b,\n" + 
//				 "               hr_j_emp_info c\n" + 
//				 "\n" + 
//				 "         WHERE d.file_id = e.file_id\n" + 
//				 "           and e.role_id= a.role_id\n" + 
//				 "           and a.worker_id=b.worker_id\n" + 
//				 "           and b.worker_code=c.emp_code\n" + 
//				 "           and c.emp_id="+workerId+") t1,hr_j_reward_approve t2\n" + 
//				 "           where t1.file_addr=t2.flow_list_url\n" + 
//				 "           and t2.flag in ('3','4')\n" + 
//				// "  and (t2.dept_id = '"+deptId+"'  or\n" +
//				 "and (t2.dept_id = decode(trim(t2.flow_list_url),\n" +
//				 "                           'hr/reward/monthAward/approve/manager/rewardGrantManager.jsp',\n" + 
//				 "                           t2.dept_id,\n" + 
//				 "                           'hr/reward/bigReward/bigRewardReport/approve/manager/bigRewardGrantManager.jsp',\n" + 
//				 "                           t2.dept_id,\n" + 
//				 "                           "+deptId+") or \n"+
//				 "      t2.dept_id  in\n" + 
//				 "       (select t.dept_id\n" + 
//				 "           from hr_c_dept t\n" + 
//				 "          where t.dept_level = 1\n" + 
//				 "            and rownum = 1\n" + 
//				 "          start with t.dept_id ="+deptId+" \n" + 
//				 "         connect by prior t.pdept_id = t.dept_id))";
//
//               List<HrJRewardApprove> reportList=bll.queryByNativeSQL(sql, HrJRewardApprove.class);
//               if(reportList!=null&&reportList.size()>0)
//               {
//            	   if(resultList!=null&&resultList.size()>0)
//            	   {
//            		   for(HrJRewardApprove model: reportList )
//            		   {
//            			   resultList.add(model);
//            		   }
//            	   }
//            	   else 
//            		   {
//            		   resultList=reportList;
//            		   }
//            	   
//            	   return resultList;
//               }
//			//---------------------------------------------------------------------------
			
		//modify by fyyang 20100728	
			String sql=
				"select t2.*\n" +
				"  from (SELECT distinct d.file_addr\n" + 
				"          FROM sys_c_fls     d,\n" + 
				"               sys_j_rrs     e,\n" + 
				"               sys_j_ur      a,\n" + 
				"               sys_c_ul      b,\n" + 
				"               hr_j_emp_info c\n" + 
				"\n" + 
				"         WHERE d.file_id = e.file_id\n" + 
				"           and e.role_id = a.role_id\n" + 
				"           and a.worker_id = b.worker_id\n" + 
				"           and b.worker_code = c.emp_code\n" + 
				"          and d.is_use='Y'\n" +
				"          and e.is_use='Y'\n" + 
				"          and a.is_use='Y'\n" + 
				"          and b.is_use='Y'  \n"+
				"          and a.role_id <> 58 \n "+  //add by fyyang 20100802 厂方管理员角色不需要事务提醒
				"           and c.emp_id = "+workerId+") t1,\n" + 
				"       hr_j_reward_approve t2\n" + 
				" where t1.file_addr = t2.flow_list_url\n" + 
				"   and t2.flag in ('3', '4')\n" + 
				"   and (t2.dept_id = decode(trim(t2.flow_list_url),\n" + 
				"                            'hr/reward/monthAward/approve/manager/rewardGrantManager.jsp',\n" + 
				"                            t2.dept_id,\n" + 
				"                            'hr/reward/bigReward/bigRewardReport/approve/manager/bigRewardGrantManager.jsp',\n" + 
				"                            t2.dept_id,\n" + 
				"                            "+deptId+") or\n" + 
				"\n" + 
				"      t2.dept_id =\n" + 
				"(SELECT t.dept_id\n" +
				"  FROM hr_c_dept t\n" + 
				" where t.dept_level = 1\n" + 
				"   and rownum = 1\n" + 
				" START WITH t.dept_id = "+deptId+"\n" + 
				"CONNECT BY PRIOR t.pdept_id = t.dept_id) \n"+
				")\n" + 
				"union (select t2.*\n" + 
				"         from (SELECT distinct d.file_addr\n" + 
				"                 FROM sys_c_fls     d,\n" + 
				"                      sys_j_rrs     e,\n" + 
				"                      sys_j_ur      a,\n" + 
				"                      sys_c_ul      b,\n" + 
				"                      hr_j_emp_info c\n" + 
				"\n" + 
				"                WHERE d.file_id = e.file_id\n" + 
				"                  and e.role_id = a.role_id\n" + 
				"                  and a.worker_id = b.worker_id\n" + 
				"                  and b.worker_code = c.emp_code\n" + 
				"          and d.is_use='Y'\n" +
				"          and e.is_use='Y'\n" + 
				"          and a.is_use='Y'\n" + 
				"          and b.is_use='Y'  \n"+
				"          and  a.role_id <>58\n"+ //add by fyyang 20100802 厂方管理员角色不需要事务提醒 
				"                  and c.emp_id = "+workerId+") t1,\n" + 
				"              hr_j_reward_approve t2\n" + 
				"        where t1.file_addr = t2.flow_list_url\n" + 
				"          and t2.flag in ('1', '2')\n" + 
				"          and (t2.dept_id is null or t2.dept_id = "+deptId+" or\n" + 
				"              t2.dept_id = decode(t2.flow_list_url,\n" + 
				"                                   'hr/reward/bigReward/shfitAdminApprove/shfitAdminList.jsp',\n" + 
				"                                   "+deptId+",\n" + 
				"                                   'hr/reward/monthAward/business/rewardProvide/shfitAdminApprove/shfitAdminList.jsp',\n" + 
				"                                   "+deptId+",\n" + 
				"(SELECT t.dept_id\n" +
				"  FROM hr_c_dept t\n" + 
				" where t.dept_level = 1\n" + 
				"   and rownum = 1\n" + 
				" START WITH t.dept_id = "+deptId+"\n" + 
				"CONNECT BY PRIOR t.pdept_id = t.dept_id) \n"+
				"                                   )))";

			 resultList=bll.queryByNativeSQL(sql, HrJRewardApprove.class);
			return resultList;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
//	public String getNextSetpRolesTelephone(List<HrJRewardApprove> approveList,HrJRewardApprove approve) {
//		String tels = "";
//		String telSql = "";
//		List telList = null;
//		if(approve != null) {
//			telSql = "SELECT DISTINCT c.mobile_phone\n" +
//				"  FROM sys_j_ur      a,\n" + 
//				"       sys_c_ul      b,\n" + 
//				"       hr_j_emp_info c\n" + 
//				" WHERE a.WORKER_ID = b.worker_id\n" + 
//				"   AND a.ROLE_ID IN\n" + 
//				"       (SELECT DISTINCT e.role_id\n" + 
//				"          FROM sys_c_fls d,\n" + 
//				"               sys_j_rrs e\n" + 
//				"         WHERE d.file_id = e.file_id\n" + 
//				"			AND d.is_use = 'Y'\n" +
//				"			AND e.is_use = 'Y'\n" +
//				"           AND d.file_addr = '"+approve.getFlowListUrl()+"')\n" + 
//				"   AND b.worker_code = c.emp_code\n" + 
//				"   AND a.is_use = 'Y'" +
//				"   AND b.is_use = 'Y'" +
//				"   AND c.is_use = 'Y'" +
//				"   AND c.emp_state = 'U'" +
//				"	AND c.mobile_phone is not null";
//			telList = bll.queryByNativeSQL(telSql);
//		} else if(approveList != null && approveList.size() >0) {
//			String deptIds = "";
//			String url = approveList.get(0).getFlowListUrl();
//			for(int i=0;i<approveList.size();i++) {
//				HrJRewardApprove entity = approveList.get(i);
//				if("".equals(deptIds)) {
//					deptIds += entity.getDeptId();
//				} else {
//					deptIds +=","+entity.getDeptId();
//				}
//			}
//			telSql = "SELECT DISTINCT c.mobile_phone\n" +
//				"  FROM sys_j_ur      a,\n" + 
//				"       sys_c_ul      b,\n" + 
//				"       hr_j_emp_info c\n" + 
//				" WHERE a.WORKER_ID = b.worker_id\n" + 
//				"   AND a.ROLE_ID IN\n" + 
//				"       (SELECT DISTINCT e.role_id\n" + 
//				"          FROM sys_c_fls d,\n" + 
//				"               sys_j_rrs e\n" + 
//				"         WHERE d.file_id = e.file_id\n" + 
//				"			AND d.is_use = 'Y'\n" +
//				"			AND e.is_use = 'Y'\n" +
//				"           AND d.file_addr = '"+url+"')\n" + 
//				"   AND b.worker_code = c.emp_code\n" + 
//				"   AND a.is_use = 'Y'" +
//				"   AND c.is_use = 'Y'" +
//				"   AND c.emp_state = 'U'" +
//				"	AND c.mobile_phone is not null" +
//				"	AND (select t.dept_id from hr_c_dept t where t.dept_code= GETFirstLevelBYID(c.dept_id)) in ("+deptIds+")";
//			telList = bll.queryByNativeSQL(telSql);
//		}
//		if(telList != null && telList.size()>0) {
//			Iterator it = telList.iterator();
//			while (it.hasNext()) {
//				String data = (String) it.next();
//				if(data != null) {
//					if("".equals(tels)) {
//						tels += data.toString();
//					} else {
//						tels +=","+data.toString();
//					}
//				}
//			}
//		}
//		return tels;
//	}
	
	/** 大奖月奖发放   */
	public String getNextSetpRolesTelephone(String url,String deptIds)
	{
		String tels = "";
		String sql=
			"SELECT DISTINCT c.mobile_phone\n" +
			"  FROM sys_j_ur a, sys_c_ul b, hr_j_emp_info c\n" + 
			" WHERE a.WORKER_ID = b.worker_id\n" + 
			"and a.is_use='Y' and b.is_use='Y' and c.is_use='Y' \n"+
			"   AND a.ROLE_ID IN (SELECT DISTINCT e.role_id\n" + 
			"                       FROM sys_c_fls d, sys_j_rrs e\n" + 
			"                      WHERE d.file_id = e.file_id\n" + 
			"   and d.is_use='Y' and e.is_use='Y' \n"+
			"   and e.role_id<>58\n"+ //add by fyyang 0802 厂方管理员不需要短信通知
			"                        AND d.file_addr = '"+url+"')\n" + 
			"   AND b.worker_code = c.emp_code\n" + 
			"   AND a.is_use = 'Y'\n" + 
			"   AND c.mobile_phone is not null\n";
		if(deptIds!=null&&!deptIds.equals("")&&!url.equals("hr/reward/monthAward/business/rewardProvide/mainApprove/monthRewardProvide.jsp")&&!url.equals("hr/reward/bigReward/bigRewardApprove/bigRewardApprove.jsp"))
		{
			if(url.equals("hr/reward/monthAward/business/rewardProvide/shfitAdminApprove/shfitAdminList.jsp")
					||url.equals("hr/reward/bigReward/shfitAdminApprove/shfitAdminList.jsp"))
			{
				sql+=" and c.dept_id in ("+deptIds+")";
			}else
			{
			sql+="   and \n" + 
			"       (select t.dept_id\n" + 
			"           from hr_c_dept t\n" + 
			"           where t.dept_level=1\n" +
			"                and rownum=1 \n"+
			"          start with t.dept_id = c.dept_id\n" + 
			"         connect by prior t.pdept_id = t.dept_id) in ("+deptIds+")";
			}
		}
	   List	telList = bll.queryByNativeSQL(sql);
	   if(telList != null && telList.size()>0) {
			Iterator it = telList.iterator();
			while (it.hasNext()) {
				String data = (String) it.next();
				if(data != null) {
					if("".equals(tels)) {
						tels += data.toString();
					} else {
						tels +=","+data.toString();
					}
				}
			}
		}
			return tels;
	}
	/**
	 * add by fyyang 20100715
	 *  大奖月奖发放上报
	 */
	public String getNextSetpRolesTelForReport(String url,Long deptId) {
		String tels = "";
		String sql=
			"SELECT DISTINCT c.mobile_phone\n" +
			"  FROM sys_j_ur a, sys_c_ul b, hr_j_emp_info c\n" + 
			" WHERE a.WORKER_ID = b.worker_id\n" + 
			"and a.is_use='Y' and b.is_use='Y' and c.is_use='Y' \n"+
			"   AND a.ROLE_ID IN (SELECT DISTINCT e.role_id\n" + 
			"                       FROM sys_c_fls d, sys_j_rrs e\n" + 
			"                      WHERE d.file_id = e.file_id\n" + 
			"   and d.is_use='Y' and e.is_use='Y' \n"+
			"   and e.role_id<>58 \n"+   //add by fyyang 0802 厂方管理员不需要短信通知
			"                        AND d.file_addr = '"+url+"')\n" + 
			"   AND b.worker_code = c.emp_code\n" + 
			"   AND a.is_use = 'Y'\n" + 
			"   AND c.mobile_phone is not null\n";
		if(!url.equals("hr/reward/monthAward/approve/manager/rewardGrantManager.jsp")&&!url.equals("hr/reward/bigReward/bigRewardReport/approve/manager/bigRewardGrantManager.jsp"))
		{
			sql+="   and (c.dept_id = '"+deptId+"' or\n" + 
			"      "+deptId+" =\n" + 
			"       (select t.dept_id\n" + 
			"           from hr_c_dept t\n" + 
			"           where t.dept_level=1\n" +
			"                and rownum=1 \n"+
			"          start with t.dept_id = c.dept_id\n" + 
			"         connect by prior t.pdept_id = t.dept_id))";
		}
	   List	telList = bll.queryByNativeSQL(sql);
	   if(telList != null && telList.size()>0) {
			Iterator it = telList.iterator();
			while (it.hasNext()) {
				String data = (String) it.next();
				if(data != null) {
					if("".equals(tels)) {
						tels += data.toString();
					} else {
						tels +=","+data.toString();
					}
				}
			}
		}
	   System.out.println(tels);
			return tels;
	}
	
	/**
	 * add by fyyang 20100715
	 */
	@SuppressWarnings("unchecked")
	public List<HrJRewardApprove> findListByDetailId(String ids,String flag)
	{

		String sql="select * from hr_j_reward_approve t\n" +
		" where t.detail_id in ("+ids+")\n" + 
		" and t.flag='"+flag+"'";
		List<HrJRewardApprove> list=bll.queryByNativeSQL(sql, HrJRewardApprove.class);
		return list;

	}
	
	

}