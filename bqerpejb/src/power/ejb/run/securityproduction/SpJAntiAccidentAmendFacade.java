package power.ejb.run.securityproduction;

import java.text.SimpleDateFormat;
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

import com.sun.org.apache.bcel.internal.generic.NEW;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.run.securityproduction.form.AmendForm;

/**
 * 25项反措整改计划表
 * @author liuyi 090917
 */
@Stateless
public class SpJAntiAccidentAmendFacade implements
		SpJAntiAccidentAmendFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条25项反措整改计划表记录
	 */
	public void save(SpJAntiAccidentAmend entity) {
		LogUtil.log("saving SpJAntiAccidentAmend instance", Level.INFO, null);
		try {
			entity.setAmendId(bll.getMaxId("SP_J_ANTI_ACCIDENT_AMEND", "AMEND_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条25项反措整改计划表记录
	 */
	public void delete(SpJAntiAccidentAmend entity) {
		LogUtil.log("deleting SpJAntiAccidentAmend instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SpJAntiAccidentAmend.class,
					entity.getAmendId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条25项反措整改计划表记录
	 */
	public void delete(String ids)
	{
		String sql = "update SP_J_ANTI_ACCIDENT_AMEND a set a.is_use = 'N' \n"
			+ "where a.AMEND_ID in (" + ids + ") \n";
		bll.exeNativeSQL(sql);
	}
	/**
	 * 更新一条25项反措整改计划表记录
	 */
	public SpJAntiAccidentAmend update(SpJAntiAccidentAmend entity) {
		LogUtil.log("updating SpJAntiAccidentAmend instance", Level.INFO, null);
		try {
			SpJAntiAccidentAmend result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJAntiAccidentAmend findById(Long id) {
		LogUtil.log("finding SpJAntiAccidentAmend instance with id: " + id,
				Level.INFO, null);
		try {
			SpJAntiAccidentAmend instance = entityManager.find(
					SpJAntiAccidentAmend.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	

	

	@SuppressWarnings("unchecked")
	public PageObject findAllCheckupPlan(Long checkupId, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.amend_id, \n"
			+ "a.checkup_id, \n"
			+ "a.exist_problem, \n"
			+ "a.amend_measure, \n"
			+ "a.before_amend_measure, \n"
			+ "a.charge_dept, \n"
			+ "a.charge_by, \n"
			+ "a.supervise_dept, \n"
			+ "a.supervise_by, \n"
			+ "a.no_amend_reason, \n"
			+ "a.problem_kind, \n"
			+ "a.modify_by, \n"
			+ "getdeptname(a.charge_dept), \n"
			+ "getworkername(a.charge_by), \n"
			+ "getdeptname(a.supervise_dept), \n"
			+ "getworkername(a.supervise_by), \n"
			+ "getworkername(a.modify_by), \n"
			+ "to_char(a.plan_finish_date,'yyyy-MM-dd'), \n"
			+ "to_char(a.amend_finish_date,'yyyy-MM-dd'), \n"
			+ "to_char(a.modify_date,'yyyy-MM-dd'), \n"
			+ "c.special_code, \n"
			+ "getspecialname(c.special_code) \n"
			+ "from SP_J_ANTI_ACCIDENT_AMEND a,SP_J_ANTI_ACCIDENT_CHECKUP b,SP_J_ANTI_ACCIDENT c \n"
			+ "where a.is_use='Y' \n"
			+ "and a.enterprise_code='" + enterpriseCode + "' \n"
			+ "and b.is_use='Y' \n"
			+ "and b.enterprise_code='" + enterpriseCode + "' \n"
			+ "and c.is_use='Y' \n"
			+ "and c.enterprise_code='" + enterpriseCode + "' \n"
			+ "and a.checkup_id=b.checkup_id \n"
			+ "and b.measure_code=c.measure_code \n";
		if(checkupId != null)
			sql += "and a.checkup_id=" + checkupId;
		String sqlCount = "select count(*) from (" + sql + ")";
		List<AmendForm> arrlist = new ArrayList<AmendForm>();
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				AmendForm form = new AmendForm();
				SpJAntiAccidentAmend amend = new SpJAntiAccidentAmend();
				Object[] data = (Object[])it.next();
				if(data[0] != null)
					amend.setAmendId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					amend.setCheckupId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					amend.setExistProblem(data[2].toString());
				if(data[3] != null)
					amend.setAmendMeasure(data[3].toString());
				if(data[4] != null)
					amend.setBeforeAmendMeasure(data[4].toString());
				if(data[5] != null)
					amend.setChargeDept(data[5].toString());
				if(data[6] != null)
					amend.setChargeBy(data[6].toString());
				if(data[7] != null)
					amend.setSuperviseDept(data[7].toString());
				if(data[8] != null)
					amend.setSuperviseBy(data[8].toString());
				if(data[9] != null)
					amend.setNoAmendReason(data[9].toString());
				if(data[10] != null)
					amend.setProblemKind(data[10].toString());
				if(data[11] != null)
					amend.setModifyBy(data[11].toString());
				if(data[12] != null)
					form.setChargeDeptName(data[12].toString());
				if(data[13] != null)
					form.setChargeName(data[13].toString());
				if(data[14] != null)
					form.setSuperviseDeptName(data[14].toString());
				if(data[15] != null)
					form.setSuperviseName(data[15].toString());
				if(data[16] != null)
					form.setModifyName(data[16].toString());
				if(data[17] != null)
					form.setPlanFinishDate(data[17].toString());
				if(data[18] != null)
					form.setAmendFinishDate(data[18].toString());
				if(data[19] != null)
					form.setModifyDate(data[19].toString());
				if(data[20] != null)
					form.setSpecialCode(data[20].toString());
				if(data[21] != null)
					form.setSpecialName(data[21].toString());
				String nowDate = sbf.format(new Date());
				if(form.getAmendFinishDate() != null && !form.getAmendFinishDate().equals(""))
				{
					System.out.println(form.getPlanFinishDate());
					System.out.println(form.getAmendFinishDate());
					System.out.println("aaaaaaaa " + form.getPlanFinishDate().compareTo(form.getAmendFinishDate()));
					if(form.getPlanFinishDate().compareTo(form.getAmendFinishDate()) > 0)
						form.setProType("整改完成");
				}
				else 
				{
					if(form.getPlanFinishDate().compareTo(nowDate) > 0)
						form.setProType("整改之中");
					else 
						form.setProType("逾期未完");
				}
				form.setAmend(amend);
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

}