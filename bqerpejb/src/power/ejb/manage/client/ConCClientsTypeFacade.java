package power.ejb.manage.client;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class ConCClientsTypeFacade implements ConCClientsTypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public ConCClientsType save(ConCClientsType entity)
			throws CodeRepeatException {
		LogUtil.log("saving ConCClientsType instance", Level.INFO, null);
		try {
			if (!this.checkName(entity.getTypeName(),entity.getEnterpriseCode())) {
				entity.setTypeId(bll.getMaxId("CON_C_CLIENTS_TYPE", "type_id"));
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("类型名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql = "delete CON_C_CLIENTS_TYPE b where b.type_id in(" + ids
				+ ")";
		bll.exeNativeSQL(sql);
	}

	public ConCClientsType update(ConCClientsType entity)
			throws CodeRepeatException {
		LogUtil.log("updating ConCClientsType instance", Level.INFO, null);
		try {
			if (!this.checkName(entity.getTypeName(),entity.getEnterpriseCode(), entity.getTypeId())) {
				ConCClientsType result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("类型名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConCClientsType findById(Long id) {
		LogUtil.log("finding ConCClientsType instance with id: " + id,
				Level.INFO, null);
		try {
			ConCClientsType instance = entityManager.find(
					ConCClientsType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String typeName,String enterpriseCode, final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			if (typeName == null || "".equals(typeName)) {
				typeName = "%";
			}
			String sqlCount = "select count(*) from CON_C_CLIENTS_TYPE t where t.type_name like '%"+typeName+"%'  and t.enterprise_code='"+enterpriseCode+"'"
							+ " or t.type_id like '" +typeName+"'" ;
			Long totalCount = Long.valueOf(bll.getSingal(sqlCount).toString());
			result.setTotalCount(totalCount);
			if (totalCount > 0) {
				String sql = "select * from CON_C_CLIENTS_TYPE t where t.type_name like '%"
						+ typeName + "%' and t.enterprise_code='"+enterpriseCode+"'"+ " or t.type_id like '" +typeName+"'" ;;
				List<ConCClientsType> list = bll.queryByNativeSQL(sql,
						ConCClientsType.class, rowStartIdxAndCount);
				result.setList(list);
			}
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 判断名称是否重复
	 * 
	 * @param typeName
	 * @param typeId
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean checkName(String typeName,String enterpriseCode, Long... typeId) {
		boolean isSame = false;
		String sql = "select count(*) from CON_C_CLIENTS_TYPE t\n"
				+ "where t.type_name = '" + typeName + "' and t.enterprise_code='"+enterpriseCode+"'";

		if (typeId != null && typeId.length > 0) {
			sql += "  and t.type_id <> " + typeId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

}