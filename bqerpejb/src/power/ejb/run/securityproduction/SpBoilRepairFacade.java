package power.ejb.run.securityproduction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class SpBoilRepairFacade implements SpBoilRepairFacadeRemote {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	protected SpCBoilerFacadeRemote boiler;

	public SpBoilRepairFacade() {
		boiler = (SpCBoilerFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("SpCBoilerFacade");
	}
//update by ltong 20100428
	public PageObject getBoilRepair(String enterprise, String startTime,
			String endTime, String isMaint, String fillBy) {
		PageObject result = new PageObject();
		String sql = "select c.boiler_name," + "c.boiler_type,"
				+ "r.boiler_repair_id," + "r.boiler_id," + "r.task_source,"
				+ "r.repair_record," + "to_char(r.repair_time,'yyyy-MM-dd'),"
				+ "r.repair_by," + "getworkername( r.repair_by)\n"
				+ "from   SP_J_BOILER_REPAIR  r,SP_C_BOILER  c\n"
				+ "where c.boiler_id=r.boiler_id\n" + "and c.is_use='Y' "
				+ "and r.is_use='Y' " + "and c.enterprise_code='" + enterprise
				+ "' " + "and r.enterprise_code='" + enterprise + "'";

		if (startTime != null && !startTime.equals("")) {
			sql += "and  to_char(r.repair_time,'yyyy-MM-dd')>='" + startTime
					+ "'";
		}
		if (endTime != null && !endTime.equals("")) {
			sql += "and  to_char(r.repair_time,'yyyy-MM-dd')<='" + endTime
					+ "'";
		}
		//add by ltong
		if (isMaint != null && isMaint.equals("1")) {
			sql += " and r.fill_by='" + fillBy + "'\n";
		}
		// System.out.println("the sql"+sql);
		String sqlCount = "select count(1) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(count);

		return result;
	}

	public Long findBoilerID(String blockName, String type, String enterprise) {

		String sql = "select   c.boiler_id  from  SP_C_BOILER  c "
				+ "where  c.is_use='Y'  and c.enterprise_code='" + enterprise
				+ "'";
		if (!blockName.equals("") && blockName != null) {
			sql += " and c.boiler_name='" + blockName + "' ";
		}
		if (!type.equals("") && type != null) {
			sql += "and c.boiler_type='" + type + "'";
		}
		Long ID = Long.parseLong(bll.getSingal(sql).toString());
		if (ID.SIZE > 0) {
			return ID;
		} else {
			return 0l;
		}
	}

	public SpJBoilerRepair updateBoilerRepair(SpJBoilerRepair entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
		return entity;

	}

	public SpJBoilerRepair addBoilerRepair(SpJBoilerRepair entity, Long boilID) {
		Long boilRepairId = bll.getMaxId("SP_J_BOILER_REPAIR ",
				"boiler_repair_id");
		entity.setBoilerRepairId(boilRepairId);
		entity.setBoilerId(boilID);
		entity.setIsUse("Y");
		entityManager.persist(entity);
		entityManager.flush();
		return entity;

	}

	public void saveBoilRepair(List<SpJBoilerRepair> addList,
			List<SpJBoilerRepair> updateList, String enterpriseCode,
			String blockName, String type) {
		Long boilID = null;
		String[] blname = blockName.split(",");
		String[] Type = type.split(",");
		int i = 0;
		int j = 0;
		if (addList != null && addList.size() > 0) {
			for (SpJBoilerRepair entity : addList) {

				boilID = this.findBoilerID(blname[i], Type[i], enterpriseCode);
				i++;
				this.addBoilerRepair(entity, boilID);

			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (SpJBoilerRepair entity : updateList) {
				Long mainid = this.findBoilerID(blname[j], Type[j],
						enterpriseCode);
				j++;
				if (mainid != entity.getBoilerId()
						|| !mainid.equals(entity.getBoilerId())) {
					entity.setBoilerId(mainid);
					entityManager.merge(entity);
				}
				this.updateBoilerRepair(entity);

			}
		}

	}

	public void delBoilRepair(String ids) {
		String sql = "update SP_J_BOILER_REPAIR  r\n" + "set  r.is_use='N'\n"
				+ "where r.boiler_repair_id in (" + ids + ")";

		bll.exeNativeSQL(sql);

	}

	public SpJBoilerRepair findById(Long id)

	{
		LogUtil.log("finding SpJBoilerRepair instance with id: " + id,
				Level.INFO, null);
		try {
			SpJBoilerRepair instance = entityManager.find(
					SpJBoilerRepair.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getBoilerEquList(String enterprise, Long boilerId,
			String sDate, String eDate, int... rowStartIdxAndCount) {
		PageObject result = new PageObject();

		String sql = "select c.boiler_name,"
				+ "c.boiler_type,"
				+ "r.boiler_repair_id,"
				+ "r.boiler_id,"
				+ "decode(r.task_source,'1','A级检修','2','B级检修','3','C级检修','4','缺陷'),"
				+ "r.repair_record," + "to_char(r.repair_time,'yyyy-MM-dd'),"
				+ "r.repair_by," + "getworkername( r.repair_by)\n"
				+ "from   SP_J_BOILER_REPAIR  r,SP_C_BOILER  c\n"
				+ "where c.boiler_id=r.boiler_id\n" + "and c.is_use='Y' "
				+ "and r.is_use='Y' " + "and c.enterprise_code='" + enterprise
				+ "' " + "and r.enterprise_code='" + enterprise + "'";

		String strWhere = "";
		if (boilerId != null && !"".equals(boilerId) && boilerId != 0) {
			strWhere += "and r.boiler_id in (select t.boiler_id\n"
					+ "  from SP_C_BOILER t\n" + "  where t.is_use = 'Y'\n"
					+ "  start with t.boiler_id =" + boilerId + "\n"
					+ "  connect by prior t.boiler_id = t.f_boiler_id)";
		}
		if (sDate != null && !sDate.equals("")) {
			sql += "and  to_char(r.repair_time,'yyyy-MM-dd')>='" + sDate + "'";
		}
		if (eDate != null && !eDate.equals("")) {
			sql += "and  to_char(r.repair_time,'yyyy-MM-dd')<='" + eDate + "'";
		}

		String sqlCount = "select count(1)\n"
				+ "  from SP_J_BOILER_REPAIR r, SP_C_BOILER c\n"
				+ " where c.boiler_id = r.boiler_id\n"
				+ "   and c.is_use = 'Y'\n" + "   and r.is_use = 'Y'\n"
				+ "   and c.enterprise_code = '" + enterprise + "'\n"
				+ "   and r.enterprise_code = '" + enterprise + "'";

		sql = sql + strWhere;
		sqlCount = sqlCount + strWhere;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(count);
		return result;
	}

}