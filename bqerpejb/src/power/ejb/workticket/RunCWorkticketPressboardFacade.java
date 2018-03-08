package power.ejb.workticket;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class RunCWorkticketPressboardFacade implements
		RunCWorkticketPressboardFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCWorkticketPressboard save(RunCWorkticketPressboard entity)
			throws CodeRepeatException {
		try {
			if (!this.checkCodeSameForAddOrUpdate(entity.getEnterpriseCode(),
					entity.getPressboardCode())
					&& !this.checkNameSameForAddOrUpdate(entity
							.getEnterpriseCode(), entity.getPressboardName())) {
				if (entity.getPressboardId() == null) {
					entity.setPressboardId(bll.getMaxId(
							"RUN_C_WORKTICKET_PRESSBOARD", "pressboard_id"));
				}
				entity.setIsUse("Y");
				entity.setModifyDate(new Date());
				entityManager.persist(entity);
				return entity;
			} else {
				throw new CodeRepeatException("压板编号或者名称重复！");
			}

		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(RunCWorkticketPressboard entity) throws CodeRepeatException{
		entity.setIsUse("N");
		this.update(entity);
	}

	public RunCWorkticketPressboard update(RunCWorkticketPressboard entity) throws CodeRepeatException{
		try {
			if (!this.checkCodeSameForAddOrUpdate(entity.getEnterpriseCode(),
					entity.getPressboardCode(),entity.getPressboardId())
					&& !this.checkNameSameForAddOrUpdate(entity
							.getEnterpriseCode(), entity.getPressboardName(),entity.getPressboardId())) {
				RunCWorkticketPressboard result = entityManager.merge(entity);
				return result;
			}
			else{
				throw new CodeRepeatException("压板编码或者名称重复！");
			}
			
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketPressboard findById(Long id) {
		LogUtil.log("finding RunCWorkticketPressboard instance with id: " + id,
				Level.INFO, null);
		try {
			RunCWorkticketPressboard instance = entityManager.find(
					RunCWorkticketPressboard.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<RunCWorkticketPressboard> findByParentPressboardId(Long parentPressboardId) {
		String sql="select * from RUN_C_WORKTICKET_PRESSBOARD t where t.parent_pressboard_id=? and t.is_use='Y'";
		return bll.queryByNativeSQL(sql, new Object[]{parentPressboardId}, RunCWorkticketPressboard.class);
	}

	private boolean checkCodeSameForAddOrUpdate(String enterpriseCode,
			String pressbordCode, Long... pressbordId) {
		boolean isSame = false;
		int count = 0;
		String sql = "select count(1) from RUN_C_WORKTICKET_PRESSBOARD t where t.is_use='Y' and t.enterprise_code=? and t.pressboard_code=?";
		if (pressbordId != null && pressbordId.length>0) {
			sql += " and t.pressboard_id<>?";
			count = Integer.parseInt(bll.getSingal(
					sql,
					new Object[] { enterpriseCode, pressbordCode,
							pressbordId[0] }).toString());
		} else {
			count = Integer.parseInt(bll.getSingal(
					sql,
					new Object[] { enterpriseCode, pressbordCode}).toString());
		}
		if (count == 0)
			isSame = false;
		else
			isSame = true;
		return isSame;
	}

	private boolean checkNameSameForAddOrUpdate(String enterpriseCode,
			String pressbordName, Long... pressbordId) {
		boolean isSame = false;
		int count = 0;
		String sql = "select count(1) from RUN_C_WORKTICKET_PRESSBOARD t where t.is_use='Y' and t.enterprise_code=? and t.pressboard_name=?";
		if (pressbordId != null && pressbordId.length >0) {
			sql += " and t.pressboard_id<>?";
			count = Integer.parseInt(bll.getSingal(
					sql,
					new Object[] { enterpriseCode, pressbordName,
							pressbordId[0] }).toString());
		} else {
			count = Integer.parseInt(bll.getSingal(
					sql,
					new Object[] { enterpriseCode, pressbordName}).toString());
		}
		if (count == 0)
			isSame = false;
		else
			isSame = true;
		return isSame;
	}

}