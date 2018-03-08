package power.ejb.equ.workbill;

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
 * Facade for entity EquJWoer.
 * 
 * @see power.ejb.equ.workbill.EquJWoer
 * @author slTang
 */
@Stateless
public class EquJWoerFacade implements EquJWoerFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(EquJWoer entity) {
		try {
			if (entity.getId() == null) {
				entity.setId(bll.getMaxId("EQU_J_WOER", "ID"));
			}
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(EquJWoer entity) {
		try {
			entity = entityManager.getReference(EquJWoer.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EquJWoer update(EquJWoer entity) {
		try {
			EquJWoer result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EquJWoer findById(Long id) {
		try {
			EquJWoer instance = entityManager.find(EquJWoer.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<EquJWoer> findByWoCode(String woCode, String enterprisecode) {
		try {
			String sql = "select * from equ_j_woer where wo_code='" + woCode
					+ "' and ENTERPRISECODE='" + enterprisecode + "'";
			return bll.queryByNativeSQL(sql, EquJWoer.class);
		} catch (RuntimeException re) {
			throw re;
		}
	}
}