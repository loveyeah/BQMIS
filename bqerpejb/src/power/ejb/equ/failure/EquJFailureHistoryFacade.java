package power.ejb.equ.failure;

import java.util.ArrayList;
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
 * Facade for entity EquJFailureHistory.
 * 
 * @see power.ejb.equ.failure.EquJFailureHistory
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJFailureHistoryFacade implements EquJFailureHistoryFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved EquJFailureHistory
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquJFailureHistory entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJFailureHistory entity) {
		LogUtil.log("saving EquJFailureHistory instance", Level.INFO, null);
		try {
			if(entity.getId() == null || "".equals(entity.getId()))
			{
				entity.setId(Long.parseLong(bll.getMaxId("equ_j_failure_history", "id")
					.toString()));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent EquJFailureHistory entity.
	 * 
	 * @param entity
	 *            EquJFailureHistory entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJFailureHistory entity) {
		LogUtil.log("deleting EquJFailureHistory instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquJFailureHistory.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquJFailureHistory entity and return it or a
	 * copy of it to the sender. A copy of the EquJFailureHistory entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJFailureHistory entity to update
	 * @return EquJFailureHistory the persisted EquJFailureHistory entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJFailureHistory update(EquJFailureHistory entity) {
		LogUtil.log("updating EquJFailureHistory instance", Level.INFO, null);
		try {
			EquJFailureHistory result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJFailureHistory findById(Long id) {
		LogUtil.log("finding EquJFailureHistory instance with id: " + id,
				Level.INFO, null);
		try {
			EquJFailureHistory instance = entityManager.find(
					EquJFailureHistory.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJFailureHistory entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJFailureHistory property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<EquJFailureHistory> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquJFailureHistory> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding EquJFailureHistory instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJFailureHistory model where model."
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

	/**
	 * Find all EquJFailureHistory entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJFailureHistory> all EquJFailureHistory entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquJFailureHistory> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquJFailureHistory instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from EquJFailureHistory model";
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
	public EquFailuresHisInfo findApplyType(String code,String type,String enterprisecode)
	{
		String sql="select r.id,\n" +
		"       r.failure_code,\n" + 
		"       r.approve_type,\n" + 
		"       to_char(r.approve_time,'yyyy-mm-dd hh24:mi:ss') approve_time,\n" + 
		"       r.approve_opinion,\n" + 
		"       r.approve_people,\n" + 
		"       getworkername(r.approve_people) name,\n" + 
		"       r.arbitrate_type,\n" + 
		"       r.await_type,\n" +
		"       r.charge_man,\n" + 
		"       getworkername(r.charge_man) charge_name,\n" +
		"       to_char(r.delay_date,'yyyy-mm-dd hh24:mi:ss') delay_date\n" + 
		"  from equ_j_failure_history r\n" + 
		" where r.failure_code = '"+code+"'\n" + 
		"   and r.isuse = 'Y'\n" +
		"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
		"   and r.approve_type = '"+type+"'\n" + 
		" order by r.id desc";
		List list=bll.queryByNativeSQL(sql);
		if(list.size() > 0)
		{
			EquFailuresHisInfo model=new EquFailuresHisInfo();
			Object[] obj = (Object[]) list.get(0);
			if (obj[0] != null) {
				model.setId(Long.parseLong(obj[0].toString()));
			}
			if(obj[1] != null)
			{
				model.setFailureCode(obj[1].toString());
			}
			if(obj[2] != null)
			{
				model.setApproveType(obj[2].toString());
			}
			if(obj[3] != null)
			{
				model.setApproveTime(obj[3].toString());
			}
			if(obj[4] != null)
			{
				model.setApproveOpinion(obj[4].toString());
			}
			if(obj[5] != null)
			{
				model.setApprovePeople(obj[5].toString());
			}
			if(obj[6] != null)
			{
				model.setApprovePeopleName(obj[6].toString());
			}
			if(obj[7] != null)
			{
				model.setArbitrateType(obj[7].toString());
			}
			if(obj[8] != null)
			{
				model.setAwaitType(obj[8].toString());
			}
			if(obj[9] != null)
			{
				model.setChargeMan(obj[9].toString());
			}
			else
			{
				model.setChargeMan("");
			}
			if(obj[10] != null)
			{
				model.setChargeManName(obj[10].toString());
			}
			else
			{
				model.setChargeManName("");
			}
			if(obj[11] != null)
			{
				model.setDelayDate(obj[11].toString());
			}
			else
			{
				model.setDelayDate("");
			}
			return model;
		}
		else
		{
			return null;
		}
	}
	public String  findAwaitCount(String code,String enterprisecode)
	{
		String sql="select count(*)\n" +
			"  from equ_j_failure_history r\n" + 
			" where r.failure_code = '"+code+"'\n" + 
			"   and r.approve_type in (5, 19, 22)\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'";
		String count=bll.getSingal(sql).toString();
		return count;
	}
	public String convert(Object o)
	{
		if(o != null)
		{
			return o.toString();
		}
		else
		{
			return " ";
		}
	}
	public List<EquFailuresHisInfo> findApproveList(String failurecode,String enterprisecode)
	{
		String sql=
			"select r.id,\n" +
			"       r.failure_code,\n" + 
			"       r.approve_type,\n" + 
			"       decode(r.approve_type,\n" + 
			"              '1',\n" + 
			"              '消缺',\n" + 
			"              '7',\n" + 
			"              '申请仲裁',\n" + 
			"              '2',\n" + 
			"              '仲裁',\n" + 
//			"              '3',\n" + 
//			"              '仲裁',\n" + 
			"              '8',\n" + 
			"              '仲裁',\n" + 
			"              '11',\n" + 
			"              '申请延期待处理',\n" + 
			"              '12',\n" + 
			"              '点检延期待处理',\n" + 
			"              '13',\n" + 
			"              '点检延期待处理',\n" + 
			"              '15',\n" + 
			"              '点检延期待处理退回',\n" + 
			"              '16',\n" + 
			"              '设备部延期待处理退回',\n" + 
			"              '17',\n" + 
			"              '运行延期待处理退回',\n" + 
			"              '5',\n" + 
			"              '点检已处理',\n" + 
			"              '9',\n" + 
			"              '验收退回',\n" + 
			"              '14',\n" + 
			"              '点检验收',\n" + 
			"              '4',\n" + 
			"              '运行验收',\n" + 
			"              '18',\n" + 
			"              '缺陷确认',\n" + 
			"              '6',\n" + 
			"              '作废',\n" + 
			"              '19',\n" + 
			"              '设备部主任已处理',\n" + 
			"              '20',\n" + 
			"              '设备部主任延期待处理',\n" + 
			"              '21',\n" + 
			"              '运行延期待处理',\n" + 
			"              '22',\n" + 
			"              '总工待处理',\n" + 
			"              '23',\n" + 
			"              '总工延期待处理退回',\n" + 
			"              '10',\n" + 
			"              '退回') approve_type_name,\n" + 
			"       decode(r.approve_type,\n" + 
			"              '1',\n" + 
			"              '消缺信息',\n" + 
			"              '7',\n" + 
			"              '仲裁信息',\n" + 
			"              '2',\n" + 
			"              '仲裁信息',\n" + 
//			"              '3',\n" + 
//			"              '仲裁信息',\n" + 
			"              '8',\n" + 
			"              '仲裁信息',\n" + 
			"              '11',\n" + 
			"              '延期待处理信息',\n" + 
			"              '12',\n" + 
			"              '延期待处理信息',\n" + 
			"              '13',\n" + 
			"              '延期待处理信息',\n" + 
			"              '15',\n" + 
			"              '延期待处理信息',\n" + 
			"              '16',\n" + 
			"              '延期待处理信息',\n" + 
			"              '17',\n" + 
			"              '延期待处理信息',\n" + 
			"              '5',\n" + 
			"              '延期待处理信息',\n" + 
			"              '19',\n" + 
			"              '待处理信息',\n" + 
			"              '20',\n" + 
			"              '延期待处理信息',\n" + 
			"              '21',\n" + 
			"              '延期待处理信息',\n" + 
			"              '22',\n" + 
			"              '延期待处理信息',\n" + 
			"              '23',\n" + 
			"              '延期待处理信息',\n" + 
			"              '9',\n" + 
			"              '验收信息',\n" + 
			"              '14',\n" + 
			"              '验收信息',\n" + 
			"              '4',\n" + 
			"              '验收信息',\n" + 
			"              '6',\n" + 
			"              '仲裁信息',\n" + 
			"              '18',\n" + 
			"              '确认信息',\n" + 
			"              '10',\n" + 
			"              '仲裁信息') group_name,\n" + 
			"       to_char(r.approve_time, 'yyyy-mm-dd hh24:mi:ss') approve_time,\n" + 
			"       r.approve_opinion,\n" + 
			"       r.approve_people,\n" + 
			"       getworkername(r.approve_people) approve_people_name,\n" + 
			"       decode(r.arbitrate_type,'1','管辖专业仲裁','2','检修部门仲裁','3','验收仲裁','4','其它','5','类别仲裁'),\n" + 
			"       r.arbitrate_dept,\n" + 
			"       getdeptname(r.arbitrate_dept) arbitrate_dept_name,\n" + 
			"       r.arbitrate_profession,\n" + 
			"       getspecialname(r.arbitrate_profession) arbitrate_profession_name,\n" + 
			"       decode(r.check_arbitrate_type,'1','已消除可验收','2','未消除继续消缺'),\n" + 
			"       (select t.failuretype_name from equ_c_failure_type t where t.failuretype_code=r.arbitrate_kind and t.is_use='Y' and t.enterprise_code='"+enterprisecode+"') arbitrate_kind,\n" + 
			"       decode(r.await_type,'1','无备品','2','等待停机处理'),\n" + 
			"       to_char(r.delay_date, 'yyyy-mm-dd hh24:mi:ss') delay_date,\n" + 
			"       to_char(r.awaitappo_date, 'yyyy-mm-dd hh24:mi:ss') awaitappo_date,\n" + 
			"       decode(r.eliminate_type,'1','正常消缺','2','低谷消缺','3','降负荷消缺','4','停机消缺'),\n" + 
			"       r.eliminate_class,\n" + 
			"       getdeptname(r.eliminate_class) eliminate_class_name,\n" + 
			"       decode(r.tackle_result,'1','正常','2','不正常'),\n" + 
			"       r.charge_man,\n" + 
			"       getworkername(r.charge_man) charge_man_name,\n" + 
			"       r.approve_remark,\n" + 
			"       r.charger_leader,\n" + 
			"       getworkername(r.charger_leader) charger_leader_name,\n" + 
			"       r.check_man,\n" + 
			"       getworkername(r.check_man) check_man_name,\n" + 
			"       decode(r.check_quality,'1','合格','2','不合格')\n" + 
			"  from equ_j_failure_history r\n" + 
			" where r.failure_code = '"+failurecode+"'\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and r.isuse = 'Y'\n" + 
			"   and r.approve_type != '0'\n" + 
			" order by r.id";
		List list=bll.queryByNativeSQL(sql);
		List<EquFailuresHisInfo> querylist=new ArrayList();
		if(list.size() > 0)
		{
			for(int i=0;i<list.size();i++)
			{
				EquFailuresHisInfo model=new EquFailuresHisInfo();
				Object[] obj = (Object[]) list.get(i);
				if (obj[0] != null) {
					model.setId(Long.parseLong(obj[0].toString()));
				}
				model.setFailureCode(convert(obj[1]));
				model.setApproveType(convert(obj[2]));
				model.setApproveTypeName(convert(obj[3]));
				model.setGroupName(convert(obj[4]));
				model.setApproveTime(convert(obj[5]));
				model.setApproveOpinion(convert(obj[6]));
				model.setApprovePeople(convert(obj[7]));
				model.setApprovePeopleName(convert(obj[8]));
				model.setArbitrateType(convert(obj[9]));
				model.setArbitrateDept(convert(obj[10]));
				model.setArbitrateDeptName(convert(obj[11]));
				model.setArbitrateProfession(convert(obj[12]));
				model.setArbitrateProfessionName(convert(obj[13]));
				model.setCheckArbitrateType(convert(obj[14]));
				model.setArbitrateKind(convert(obj[15]));
				model.setAwaitType(convert(obj[16]));
				model.setDelayDate(convert(obj[17]));
				model.setAwaitappoDate(convert(obj[18]));
				model.setEliminateType(convert(obj[19]));
				model.setEliminateClass(convert(obj[20]));
				model.setEliminateClassName(convert(obj[21]));
				model.setTackleResult(convert(obj[22]));
				model.setChargeMan(convert(obj[23]));
				model.setChargeManName(convert(obj[24]));
				model.setApproveRemark(convert(obj[25]));
				model.setChargerLeader(convert(obj[26]));
				model.setChargerLeaderName(convert(obj[27]));
				model.setCheckMan(convert(obj[28]));
				model.setCheckManName(convert(obj[29]));
				model.setCheckQuality(convert(obj[30]));
				querylist.add(model);
			}
		}
		return querylist;
	}
}