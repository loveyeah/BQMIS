package power.ejb.manage.client;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity ConCClientsCharacter.
 * 
 * @see power.ejb.manage.client.ConCClientsCharacter
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConCClientsCharacterFacade implements
		ConCClientsCharacterFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public ConCClientsCharacter save(ConCClientsCharacter entity)
			throws CodeRepeatException {
		LogUtil.log("saving ConCClientsCharacter instance", Level.INFO, null);
		try {
			if (!this.checkName(entity.getCharacterName(),entity.getEnterpriseCode())) {
				entity.setCharacterId(bll.getMaxId("con_c_clients_character",
						"character_id"));
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("公司性质名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql = "delete con_c_clients_character b where b.character_id in(" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public ConCClientsCharacter update(ConCClientsCharacter entity)
			throws CodeRepeatException {
		LogUtil.log("updating ConCClientsCharacter instance", Level.INFO, null);
		try {
			if (!this.checkName(entity.getCharacterName(),entity.getEnterpriseCode(), entity
					.getCharacterId())) {
				ConCClientsCharacter result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("公司性质名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConCClientsCharacter findById(Long id) {
		LogUtil.log("finding ConCClientsCharacter instance with id: " + id,
				Level.INFO, null);
		try {
			ConCClientsCharacter instance = entityManager.find(
					ConCClientsCharacter.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String characterName,String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			if(characterName == null || "".equals(characterName)) {
				characterName = "%";
			}
			String sqlCount = "select count(*) from CON_C_CLIENTS_CHARACTER t where t.CHARACTER_NAME like '%"+characterName+"%' and t.enterprise_code='"+enterpriseCode+"'"
							+ " or t.CHARACTER_ID like '"+ characterName + "'";
			Long totalCount = Long.valueOf(bll.getSingal(sqlCount).toString());
			result.setTotalCount(totalCount);
			if(totalCount > 0 ) {
				String sql = "select * from CON_C_CLIENTS_CHARACTER t where t.CHARACTER_NAME like '%"+characterName+"%' and t.enterprise_code='"+enterpriseCode+"'"
							+ " or t.CHARACTER_ID like '"+ characterName + "'";
				List<ConCClientsCharacter> list = bll.queryByNativeSQL(sql, ConCClientsCharacter.class, rowStartIdxAndCount);
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
	 * @param characterName
	 * @param characterId
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean checkName(String characterName,String enterpriseCode, Long... characterId) {
		boolean isSame = false;
		String sql = "select count(*) from con_c_clients_character t\n"
				+ "where t.character_name = '" + characterName + "' and t.enterprise_code='"+enterpriseCode+"'";

		if (characterId != null && characterId.length > 0) {
			sql += "  and t.character_id <> " + characterId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

}