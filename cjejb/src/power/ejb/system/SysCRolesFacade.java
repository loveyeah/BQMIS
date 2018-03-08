package power.ejb.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.system.form.RoleForm;

@Stateless
public class SysCRolesFacade implements SysCRolesFacadeRemote {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "SysJUrFacade")
	protected SysJUrFacadeRemote urRemote;
	@EJB(beanName = "SysJRrsFacade")
	protected SysJRrsFacadeRemote frRemote;

	public SysCRoles save(SysCRoles entity) throws CodeRepeatException {
		LogUtil.log("saving SysCRoles instance", Level.INFO, null);
		try {
			if (checkNameSameForAdd(entity.getRoleName(), entity.getIsUse())) {
				throw new CodeRepeatException("角色名称已经存在!");
			} else {

				if (entity.getRoleId() == null) {
					entity.setRoleId(Long.parseLong(bll.getMaxId("SYS_C_ROLES",
							"role_id").toString()));
				}
				entityManager.persist(entity);
				return entity;
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCRoles saveRF(SysCRoles entity, String fileIds, SysJRrs rolefile)
			throws CodeRepeatException {
		LogUtil.log("saving SysCRoles And SYSJRrs instance", Level.INFO, null);
		try {
			if (checkNameSameForAdd(entity.getRoleName(), entity.getIsUse())) {
				throw new CodeRepeatException("角色名称已经存在!");
			} else {
				if (entity.getRoleId() == null) {
					entity.setRoleId(Long.parseLong(bll.getMaxId("SYS_C_ROLES",
							"role_id").toString()));
				}
				entityManager.persist(entity);
				if (fileIds != null && fileIds.length() > 0) {
					long _count=bll.getMaxId("sys_j_rrs", "id");
					String[] fileId_array = fileIds.split(",");
					for(int k=0;k<fileId_array.length;k++){
						rolefile.setFileId(Long.parseLong(fileId_array[k]));
						rolefile.setRoleId(entity.getRoleId());
						rolefile.setId(_count+k);
						frRemote.save(rolefile);
					}
//					for (String fileId : fileId_array) {
//						rolefile.setFileId(Long.parseLong(fileId));
//						rolefile.setRoleId(entity.getRoleId());
//						frRemote.save(rolefile);
//					}

				}
				return entity;
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	private boolean checkNameSameForAdd(String roleName, String isUse) {
		String sql = "select count(1) from sys_c_roles t where t.role_name=? and t.is_use=?";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { roleName, isUse }).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}

	private boolean checkNameSameForUpdate(String roleName, String isUse,
			Long roleId) {
		String sql = "select count(1) from sys_c_roles t where t.role_name=? and t.is_use=? and t.role_id<>?";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { roleName, isUse, roleId }).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}

	public void delete(SysCRoles entity) {
		LogUtil.log("deleting SysCRoles instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SysCRoles.class, entity
					.getRoleId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCRoles update(SysCRoles entity) throws CodeRepeatException {
		LogUtil.log("updating SysCRoles instance", Level.INFO, null);
		try {
			if (checkNameSameForUpdate(entity.getRoleName(), entity.getIsUse(),
					entity.getRoleId())) {
				throw new CodeRepeatException("角色名称已经存在!");
			}
			SysCRoles result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCRoles findById(Long id) {
		LogUtil.log("finding SysCRoles instance with id: " + id, Level.INFO,
				null);
		try {
			SysCRoles instance = entityManager.find(SysCRoles.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SysCRoles> findAll(String roleType, String roleName,
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all SysCRoles instances", Level.INFO, null);
		try {
			final String queryString = "select model from SysCRoles model where model.roleType like ? and model.roleName like ?";
			Query query = entityManager.createQuery(queryString);
			query.setParameter(1, roleType);
			query.setParameter(2, roleName);
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

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void grantRoleUsers(List<SysJUr> roleUsers) {
		for (int i = 0; i < roleUsers.size(); i++) {
			urRemote.save(roleUsers.get(i));
			if (i % 30 == 0) {
				entityManager.flush();
			}
		}
	}

	public void revokeRoleUsers(String roleUserIds) {
		String sql = "delete from sys_j_ur t where t.id in(" + roleUserIds
				+ ")";
		bll.exeNativeSQL(sql);
	}

	@SuppressWarnings("unchecked")
	public List findUsersByRole(Long roleId) {
		String sql = "select a.id, b.worker_id, b.worker_code, b.worker_name\n"
				+ "  from sys_j_ur a, sys_c_ul b\n"
				+ " where a.worker_id = b.worker_id\n"
				+ "   and a.worker_id = " + roleId + "\n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'";
		return bll.queryByNativeSQL(sql);
	}

	public void grantRoleFiles(List<SysJRrs> roleFiles) {
		for (int i = 0; i < roleFiles.size(); i++) {
			frRemote.save(roleFiles.get(i));
			if (i % 30 == 0) {
				entityManager.flush();
			}
		}
	}

	public void revokeRoleFiles(String roleFileIds) {
		String sql = "delete from sys_j_rrs t where t.id in(" + roleFileIds
				+ ")";
		bll.exeNativeSQL(sql);
	}

	public List findByisUse(String isUse, int... rowStartIdxAndCount) {
		LogUtil.log("find all SysCRole by " + isUse, Level.INFO, null);
		try {
			final String sql = "select model from SysCRoles model where model.isUse='"
					+ isUse + "'";
			Query query = entityManager.createQuery(sql);
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

	public PageObject findByProperty(String propertyName, String propertyValue,
			int... rowStartIdxAndCount) {
		try {
			String sql = "";
			String sqlCount = "";
			PageObject result = new PageObject();
			// if(propertyValue.equals("%")){
			// sql="select r.* from sys_c_roles r where
			// r.role_id||r."+propertyName+" like '%'"+" and r.is_use='Y' order
			// by r.line";
			// sqlCount="select count(1) from sys_c_roles r where
			// r.role_id||r."+propertyName+" like '%'"+" and r.is_use='Y'";
			// }
			// else
			{
				sql = "select\n" + "r.role_id,\n" + "r.line,\n"
						+ "r.role_name,\n" + "r.memo,\n" + "r.is_use,\n"
						+ "r.enterprise_code,\n" + "getworkername(r.modify_by),\n"
						+ "to_char(r.modify_date,'yyyy-mm-dd'),\n"
						+ "r.role_type\n"
						+ "from sys_c_roles r where r.role_id||r."
						+ propertyName + " like '%" + propertyValue + "%'"
						+ " and r.is_use='Y' order by r.line,r.role_id";
				sqlCount = "select count(1) from sys_c_roles r where r.role_id||r."
						+ propertyName
						+ " like '%"
						+ propertyValue
						+ "%'"
						+ " and r.is_use='Y'";
			}
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] ob = (Object[]) it.next();
				RoleForm bmi=new RoleForm();
				bmi.setRoleId(Long.parseLong(ob[0].toString()));
				if (ob[1] != null)
					bmi.setLine(Long.parseLong(ob[1].toString()));
				if (ob[2] != null)
					bmi.setRoleName(ob[2].toString());
				if (ob[3] != null)
					bmi.setMemo(ob[3].toString());
				if (ob[4] != null)
					bmi.setIsUse(ob[4].toString());
				if (ob[5] != null)
					bmi.setEnterpriseCode(ob[5].toString());
				if (ob[6] != null)
					bmi.setModifyBy(ob[6].toString());
				if (ob[7] != null)
					bmi.setModifyDate(ob[7].toString());
				if(ob[8] != null)
					bmi.setRoleType(ob[8].toString());
				arrlist.add(bmi);
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(arrlist);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SysCRoles> findRolesBywId(Long workerId) {
		try {
			final String sql = "select r.* from sys_c_roles r ,sys_j_ur ur,sys_c_ul u where r.role_id=ur.role_id and u.worker_id=ur.worker_id and u.worker_id="
					+ workerId
					+ " and ur.is_use='Y' and r.is_use='Y' and u.is_use='Y'";
			List<SysCRoles> list = bll.queryByNativeSQL(sql, SysCRoles.class);
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("find SysCRoles failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SysCRoles> findRolesByfileId(Long fileId) {
		try {
			final String sql = "select r.*\n"
					+ "  from sys_c_roles r, sys_j_rrs rr\n"
					+ " where rr.role_id = r.role_id\n"
					+ "   and rr.file_id = " + fileId + "\n"
					+ "   and rr.is_use = 'Y'\n" + "   and r.is_use = 'Y'";
			List<SysCRoles> list = bll.queryByNativeSQL(sql, SysCRoles.class);
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("find SysCRoles failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SysCRoles> findByRoleName(String roleName) {
		String sql = "select * from sys_c_roles where is_use='Y' and role_name like '%"
				+ roleName + "%'";
		return bll.queryByNativeSQL(sql, SysCRoles.class);
	}
}