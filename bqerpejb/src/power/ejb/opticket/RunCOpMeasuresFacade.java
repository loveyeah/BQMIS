package power.ejb.opticket;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.opticket.bussiness.RunJOpMeasures;

/**
 * @author slTang
 */
@Stateless
public class RunCOpMeasuresFacade implements RunCOpMeasuresFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(RunCOpMeasures entity) throws CodeRepeatException {
		try {
			if (!this.checkNameForAdd(entity.getEnterpriseCode(), entity
					.getOperateTaskId(), entity.getDangerName())) {
				if (entity.getDangerId() == null) {
					entity.setDangerId(bll.getMaxId("RUN_C_OP_MEASURES",
							"danger_id"));

				}
				entity.setIsUse("Y");
				entity.setModifyDate(new Date());
				entityManager.persist(entity);
			} else {
				throw new CodeRepeatException("名称重复!");
			}
		} catch (RuntimeException re) {
			throw re;
		}
	}

	private boolean checkNameForAdd(String enterpriseCode, Long operateTaskId,
			String dangerName, Long... dangerId) {
		boolean isSame = true;
		String sql = "select count(1) from RUN_C_OP_MEASURES t where t.enterprise_code=? and t.danger_name=? and t.operate_task_id=? and t.is_use='Y'";
		String whereSql = "";
		if (dangerId != null && dangerId.length > 0) {
			whereSql += " and t.danger_id<>" + dangerId[0];
		}
		sql += whereSql;
		if (Integer.parseInt(bll.getSingal(sql,
				new Object[] { enterpriseCode, dangerName, operateTaskId })
				.toString()) > 0) {
			isSame = true;
		} else {
			isSame = false;
		}
		return isSame;
	}

	public void delete(RunCOpMeasures entity) {
		try {
			entity.setIsUse("N");
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunCOpMeasures update(RunCOpMeasures entity)
			throws CodeRepeatException {
		try {
			if (!this.checkNameForAdd(entity.getEnterpriseCode(), entity
					.getOperateTaskId(), entity.getDangerName(), entity
					.getDangerId())) {
				RunCOpMeasures result = entityManager.merge(entity);
				return result;
			} else {
				throw new CodeRepeatException("名称重复!");
			}

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunCOpMeasures findById(Long id) {
		LogUtil.log("finding RunCOpMeasures instance with id: " + id,
				Level.INFO, null);
		try {
			RunCOpMeasures instance = entityManager.find(RunCOpMeasures.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<RunCOpMeasures> findByTaskId(String enterpriseCode,
			String modifyBy, Long operateTaskId) {
		String sql = "select t.* from RUN_C_OP_MEASURES t";
		String whereSql = " where t.is_use='Y'";
		if (enterpriseCode != null) {
			whereSql += " and t.enterprise_code='" + enterpriseCode + "'";
		}
		if(modifyBy!=null){
			whereSql += " and t.modify_by=" + modifyBy;
		}
		if (operateTaskId != null) {
			whereSql += " and t.operate_task_id=" + operateTaskId;
		}
		sql += whereSql;
		sql += " order by t.DISPLAY_NO,t.DANGER_ID";
		return bll.queryByNativeSQL(sql, RunCOpMeasures.class);
	}

	private RunCOpMeasures updateModel(Map map) {
		RunCOpMeasures model = new RunCOpMeasures();
		Object dangerId = map.get("dangerId");
		Object operateTaskId = map.get("operateTaskId");
		Object dangerName = map.get("dangerName");
		Object measureContent = map.get("measureContent");
		Object displayNo = map.get("displayNo");
		Object memo = map.get("memo");
		if (dangerId == null || "".equals(dangerId.toString())) {
			;
		} else {
			model = this.findById(Long.parseLong(dangerId.toString()));
		}
		if (operateTaskId != null) {
			model.setOperateTaskId(Long.parseLong(operateTaskId.toString()));
		}
		if (dangerName != null) {
			model.setDangerName(dangerName.toString());
		}
		if (measureContent != null) {
			model.setMeasureContent(measureContent.toString());
		}
		if (displayNo != null) {
			model.setDisplayNo(Long.parseLong(displayNo.toString()));
		}
		if (memo != null) {
			model.setMemo(memo.toString());
		}
		return model;
	}

	public boolean saveAllOperat(List<Map> addList, List<Map> updateList,
			String delStr, String currentMan, String enterpriseCod)
			throws CodeRepeatException {
		boolean operatStatus = false;
		final int maxSize = 30;
		try {
			if (addList != null) {
				Long count = bll.getMaxId("RUN_C_OP_MEASURES", "danger_id");
				int totalAdd = addList.size();
				for (int i = 0; i < totalAdd; i++) {
					count++;
					Map map = addList.get(i);
					RunCOpMeasures op = this.updateModel(map);
					if (op.getDangerId() == null) {
						op.setDangerId(count);
						op.setModifyBy(currentMan);
						op.setEnterpriseCode(enterpriseCod);
						this.save(op);
						if (totalAdd > maxSize) {
							if ((i + 1) % maxSize == 0) {
								entityManager.flush();
							}
						}
					}
				}
			}
			if (updateList != null) {
				int totalUpdate = updateList.size();
				for (int i = 0; i < totalUpdate; i++) {
					Map mapUp = updateList.get(i);
					RunCOpMeasures opUp = this.updateModel(mapUp);
					if (opUp.getDangerId() != null) {
						opUp.setModifyBy(currentMan);
						this.update(opUp);
						if (totalUpdate > maxSize) {
							if ((i + 1) % maxSize == 0) {
								entityManager.flush();
							}
						}
					}
				}
			}
			if (delStr.length() > 1) {
				String[] ids = delStr.split(",");
				for (int i = 0; i < ids.length; i++) {
					RunCOpMeasures opDel = this
							.findById(Long.parseLong(ids[i]));
					this.delete(opDel);
					if (ids.length > maxSize) {
						if ((i + 1) % maxSize == 0) {
							entityManager.flush();
						}
					}
				}
			}
			operatStatus = true;
		} catch (CodeRepeatException e) {
			throw new CodeRepeatException("名称重复");
		}

		return operatStatus;

	}
}