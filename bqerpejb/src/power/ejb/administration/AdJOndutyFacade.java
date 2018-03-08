/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.administration;

import java.sql.SQLException;
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

import power.ear.comm.ejb.PageObject;
import power.ear.comm.DataChangeException;
import power.ejb.administration.form.OndutyInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJOnduty.
 * 
 * @see power.ejb.administration.AdJOnduty
 * @author sufeiyu
 */
@Stateless
public class AdJOndutyFacade implements AdJOndutyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 值班记事保存
	 * 
	 * @param entity
	 *            值班保存entity
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJOnduty entity) throws SQLException {
		LogUtil.log("EJB：值班记事数据增加操作开始", Level.INFO, null);
		try {
			if (entity.getId() == null) {
				entity.setId(bll.getMaxId("AD_J_ONDUTY",
						"ID"));
			}
			entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("EJB：值班记事数据增加操作正常结束", Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("EJB：值班记事数据增加异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * 删除一条值班记事数据
	 * 
	 * @param entity
	 *            值班记事entity
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String strEmployee, Long noteId, String strUpdateTime)
			throws DataChangeException, SQLException {
		try {
			AdJOnduty objOld = new AdJOnduty();
			objOld = this.findById(noteId);
			String strLastmodifiedDate = objOld.getUpdateTime().toString().substring(0, 19);
			if (strLastmodifiedDate.equals(strUpdateTime.substring(0, 19))) {
				String sql = "UPDATE AD_J_ONDUTY T " 
					       + "SET T.UPDATE_USER = ?"
						   + ", T.UPDATE_TIME = sysdate "
						   + ", T.IS_USE = ? " 
						   + "WHERE T.ID = ?"; 
				Object[] params = new Object[3];
				params[0] = strEmployee;
				params[1] = "N";
				params[2] = noteId;		  
				LogUtil.log("EJB：值班记事数据删除开始。SQL：", Level.INFO, null);
				LogUtil.log(sql, Level.INFO, null);
				bll.exeNativeSQL(sql, params);
				LogUtil.log("EJB：值班记事数据删除操作正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch(DataChangeException e){
			LogUtil.log("EJB：值班记事数据删除异常结束", Level.SEVERE, e);
			throw new DataChangeException("");
		} catch (Exception re) {
			LogUtil.log("EJB：值班记事数据删除异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * 更新值班记事entity
	 * 
	 * @param entity
	 *            值班记事entity
	 * @return 值班记事Object对象 
	 * @throws DataChangeException 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public void update(AdJOnduty entity, String strUpdateTime)
			throws DataChangeException, SQLException {
		LogUtil.log("EJB：值班记事数据更新操作开始", Level.INFO, null);
		try {
			AdJOnduty objOld = new AdJOnduty();
			objOld = this.findById(entity.getId());
			String strLastmodifiedDate = objOld.getUpdateTime().toString().substring(0,19);

			if (strLastmodifiedDate.equals(strUpdateTime.substring(0, 19))) {
				entity.setUpdateTime(new Date());
				entityManager.merge(entity);
				LogUtil.log("EJB：值班记事数据更新操作正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch(DataChangeException e){
			LogUtil.log("EJB：值班记事数据更新操作异常结束", Level.SEVERE, e);
			throw new DataChangeException("");
		} catch (Exception re) {
			LogUtil.log("EJB：值班记事数据更新操作异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	public AdJOnduty findById(Long id) {
		LogUtil.log("finding AdJOnduty instance with id: "+ id, Level.INFO,
				null);
		try {
			AdJOnduty instance = entityManager.find(AdJOnduty.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJOnduty entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJOnduty property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJOnduty> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJOnduty> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJOnduty instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJOnduty model where model."     
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJOnduty entities.
	 * 
	 * @return List<AdJOnduty> all AdJOnduty entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJOnduty> findAll() {
		LogUtil.log("finding all AdJOnduty instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJOnduty model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 取得值班记事的详细信息
	 * @param worktypeCode 工作类别
	 * @return PageObject 值班记事信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getRecord(String strEnterpriseCode, String worktypeCode, final int... rowStartIdxAndCount) {
		PageObject objResult = new PageObject();
		
		try {
			String strSql = "SELECT"
					      + " A.ID,"
					      + " TO_CHAR(A.REG_TIME,'YYYY-MM-DD HH24:MI'),"
					      + " A.REG_TEXT,"
					      + " A.SUB_WORKTYPE_CODE,"
					      + " B.SUB_WORKTYPE_NAME,"
					      + " A.DUTY_TYPE,"
					      + " C.DUTY_TYPE_NAME,"
					      + " A.DUTYMAN,"
					      + " TO_CHAR(A.UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS'),"
					      + " A.WORKTYPE_CODE "
					      + "FROM"
					      + " AD_J_ONDUTY A"
					      + " LEFT JOIN"
					      + " AD_C_WORKTYPE B"
					      + " ON"
					      + " B.ENTERPRISE_CODE = ? AND"
					      + " A.SUB_WORKTYPE_CODE = B.SUB_WORKTYPE_CODE "
					      + " LEFT JOIN" 
					      + " AD_C_DUTYTYPE C"
					      + " ON"
					      + " C.ENTERPRISE_CODE = ? AND"
					      + " A.DUTY_TYPE = C.DUTY_TYPE "
					      + "WHERE"
					      + " A.IS_USE = ?"
					      + " AND"
					      + " TO_CHAR(A.REG_TIME,'YYYY-MM-DD')="   
					      + " TO_CHAR(SYSDATE,'YYYY-MM-DD')"
					      + " AND"
					      +  " A.ENTERPRISE_CODE = ? AND"
						  +  " A.WORKTYPE_CODE = ?";
			Object[] params = new Object[5];
			params[0] = strEnterpriseCode;
			params[1] = strEnterpriseCode;
			params[2] = "Y";
			params[3] = strEnterpriseCode;
			params[4] = worktypeCode;
			
			String strSqlCount    = "SELECT COUNT(A.ID)"
								  + "FROM"
							      + " AD_J_ONDUTY A"
							      + " LEFT JOIN"
							      + " AD_C_WORKTYPE B"
							      + " ON"
							      + " B.ENTERPRISE_CODE = ? AND"
							      + " A.SUB_WORKTYPE_CODE = B.SUB_WORKTYPE_CODE "
							      + " LEFT JOIN" 
							      + " AD_C_DUTYTYPE C"
							      + " ON"
							      + " C.ENTERPRISE_CODE = ? AND"
							      + " A.DUTY_TYPE = C.DUTY_TYPE "
							      + "WHERE"
							      + " A.IS_USE = ?"
							      + " AND"
							      + " TO_CHAR(A.REG_TIME,'YYYY-MM-DD')="   
							      + " TO_CHAR(SYSDATE,'YYYY-MM-DD')"
							      + " AND"
							      +  " A.ENTERPRISE_CODE = ? AND"
								  +  " A.WORKTYPE_CODE = ?";
			
			LogUtil.log("EJB：查询值班记事开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
			List lstQuery = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<OndutyInfo> arrQuery = new ArrayList<OndutyInfo>();
			LogUtil.log("EJB：查询值班记事正常结束", Level.INFO, null);
			
			if (lstQuery != null && lstQuery.size() > 0) {
				AdJOnduty test = new AdJOnduty();
				Iterator it = lstQuery.iterator();
				while (it.hasNext()) {
					OndutyInfo model = new OndutyInfo();
					Object[] data = (Object[]) it.next();
					model.setNoteId(Long.parseLong(data[0].toString()));
					// 值班记事时间
					if (data[1] != null)
						model.setRegTime(data[1].toString());
					// 值班记事内容
					if (data[2] != null)
						model.setRegText(data[2].toString());
					// 子工作类别code
					if (data[3] != null)
						model.setSubWorktypeCode(data[3].toString());
					// 子工作类别名
					if (data[4] != null)
						model.setSubWorktypeName(data[4].toString());
					// 值别code
					if (data[5] != null)
						model.setDutyType(data[5].toString());
					// 值别名
					if (data[6] != null)
						model.setDutyTypeName(data[6].toString());
					// 值班人
					if (data[7] != null)
						model.setDutyman(data[7].toString());
					// 修改时间
					if (data[8] != null)
						test = this.findById(model.getNoteId());
					    model.setUpdateTime(test.getUpdateTime().toString());
					// 工作类别code
					if (data[9] != null)
						model.setWorktypeCode(data[9].toString());
					arrQuery.add(model);
					model = null;
				}
				if (arrQuery.size() > 0) {
					Long totalCount = Long.parseLong(bll.getSingal(strSqlCount, params)
							.toString());
					objResult.setList(arrQuery);
					objResult.setTotalCount(totalCount);
				}  
			} else {
				Long lngZero = new Long(0);
				objResult.setTotalCount(lngZero);
			}
			    LogUtil.log("EJB:查询值班人员正常结束", Level.INFO, null);
				return objResult;
			} catch (NumberFormatException e) {
				LogUtil.log("EJB:查询值班人员异常结束", Level.SEVERE, e);
				throw e;
			}
		}
	}