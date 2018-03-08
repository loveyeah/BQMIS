/**
　* Copyright ustcsoft.com
　* All right reserved.
*/
package power.ejb.administration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.QuestReaderInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 抄送人对象
 * 
 * @author sufeiyu
 */
@Stateless
public class AdJOutQuestReaderFacade implements AdJOutQuestReaderFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 增加一条签报申请抄送人对象
	 * 
	 * @param entity 签报申请抄送人对象
	 *            
	 * @throws SQLException 
	 * @throws RuntimeException
	 *            
	 */
	public void save(QuestReaderInfo entity) throws SQLException {
		LogUtil.log("EJB：签报申请抄送人数据增加操作开始", Level.INFO, null);
		try {
			if (entity.getIdReader() == null) {
				entity.setIdReader(bll.getMaxId("AD_J_OUT_QUEST_READER", "ID"));
			}
			String strSql   = "INSERT INTO AD_J_OUT_QUEST_READER ("
							+ " ID,"
							+ " APPLY_ID,"
							+ " READ_MAN,"
							+ " UPDATE_USER,"
							+ " UPDATE_TIME,"
							+ " IS_USE"
							+ " )"
							+ " VALUES"
							+ "  ("
							+ " ?,"
							+ " ?,"
							+ " ?,"
							+ " ?,"
							+ " sysdate,"
							+ " ?"
							+ " )";
            Object[] params = new Object[5];
            params[0] = entity.getIdReader();
            params[1] = entity.getApplyIdReader();
            params[2] = entity.getReadManReader();
            params[3] = entity.getUpdateUserReader();
            params[4] = "Y";
            
            LogUtil.log("EJB:签报申请抄送人数据增加操作开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
            bll.exeNativeSQL(strSql, params);
			LogUtil.log("EJB：签报申请抄送人数据增加操作正常结束", Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("EJB：签报申请抄送人数据增加异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * Delete a persistent AdJOutQuestReader entity.
	 * 
	 * @param entity
	 *            AdJOutQuestReader entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJOutQuestReader entity) {
		LogUtil.log("deleting AdJOutQuestReader instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJOutQuestReader.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新签报申请抄送人表
	 * 
	 * @param entity  签报申请抄送人对象 
	 * @param strUpdateTime 上次修改时间
	 *            
	 * @throws DataChangeException 
	 * @throws SQLException 
	 * @throws Exception
	 *           
	 */
	public void update(QuestReaderInfo entity, String strUpdateTime) throws DataChangeException, SQLException {
		LogUtil.log("EJB：签报申请抄送人数据更新操作开始", Level.INFO, null);
		try {
			QuestReaderInfo objOld = new QuestReaderInfo();
			objOld = this.findByPhyId(entity.getIdReader());
			String strLastmodifiedDate = objOld.getUpdateTimeReader().toString().substring(0,19);
			if (strLastmodifiedDate.equals(strUpdateTime.substring(0, 19))) {
				String strSql   = "UPDATE"
								+ " AD_J_OUT_QUEST_READER "
								+ "SET"
								+ " APPLY_ID = ?,"
								+ " READ_MAN = ?,"
								+ " UPDATE_USER = ?,"
								+ " UPDATE_TIME = sysdate,"
								+ " IS_USE = ? "
								+ "WHERE"
								+ " IS_USE = ? AND"
								+ " ID = ?";
				Object[] params = new Object[6];
				params[0] = entity.getApplyIdReader();
				params[1] = entity.getReadManReader();
				params[2] = entity.getUpdateUserReader();
				params[3] = entity.getIsUseReader();
				params[4] = "Y";
				params[5] = entity.getIdReader();
				
				LogUtil.log("EJB:签报申请抄送人数据更新操作开始。SQL：", Level.INFO, null);
				LogUtil.log(strSql, Level.INFO, null);
				bll.exeNativeSQL(strSql, params);
				LogUtil.log("EJB：签报申请抄送人数据更新操作正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch (DataChangeException e) {
			throw new DataChangeException("");
		}catch (Exception re) {
			LogUtil.log("EJB：签报申请抄送人数据更新操作异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findById(String strApply) throws SQLException {
		PageObject objResult = new PageObject();
		try {
			QuestReaderInfo objReaderInfo = new QuestReaderInfo();
			String strSql   = "SELECT"
							+ " ID,"
							+ " APPLY_ID,"
							+ " READ_MAN,"
							+ " UPDATE_USER,"
							+ " TO_CHAR(UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS'),"
							+ " IS_USE "
							+ "FROM"
							+ " AD_J_OUT_QUEST_READER "
							+ "WHERE"
							+ " IS_USE = ? AND"
							+ " APPLY_ID =?";
			
			String strSqlCount  = "SELECT (ID) "
								+ "FROM"
								+ " AD_J_OUT_QUEST_READER "
								+ "WHERE"
								+ " IS_USE = ? AND"
								+ " APPLY_ID =?";
			
			Object[] params = new Object[2];
			params[0] = "Y";
			params[1] = strApply;
			
			LogUtil.log("EJB:签报申请抄送人数据根据ID查询操作开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
			List lstReaderList = bll.queryByNativeSQL(strSql, params);
            List<QuestReaderInfo> arrQuery = new ArrayList<QuestReaderInfo>();
			
			if (lstReaderList != null && lstReaderList.size() > 0) {
				Iterator it = lstReaderList.iterator();
				while (it.hasNext()) {
					objReaderInfo = new QuestReaderInfo();
					Object[] data = (Object[]) it.next();
					objReaderInfo.setIdReader(Long
							.parseLong(data[0].toString()));
					if (data[1] != null) {
						objReaderInfo.setApplyIdReader(data[1].toString());
					}
					if (data[2] != null) {
						objReaderInfo.setReadManReader(data[2].toString());
					}
					if (data[3] != null) {
						objReaderInfo.setUpdateUserReader(data[3].toString());
					}
					if (data[4] != null) {
						objReaderInfo.setUpdateTimeReader(data[4].toString());
					}
					if (data[5] != null) {
						objReaderInfo.setIsUseReader(data[5].toString());
					}
					arrQuery.add(objReaderInfo);
				}
				if (arrQuery.size() > 0) {
					Long totalCount = Long.parseLong(bll.getSingal(strSqlCount,
							params).toString());
					objResult.setList(arrQuery);
					objResult.setTotalCount(totalCount);
				}
			}  else {
			Long lngZero = new Long(0);
			objResult.setTotalCount(lngZero);
			}
		    LogUtil.log("EJB：签报申请抄送人数据根据ID查询操作正常结束", Level.INFO, null);
			return objResult;
	    }  catch (Exception e) {
			LogUtil.log("EJB：签报申请抄送人数据根据ID查询操作异常结束", Level.SEVERE, e);
			throw new SQLException("");
		}
	}

	/**
	 * Find all AdJOutQuestReader entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJOutQuestReader property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJOutQuestReader> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJOutQuestReader> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJOutQuestReader instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJOutQuestReader model where model."
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
	 * Find all AdJOutQuestReader entities.
	 * 
	 * @return List<AdJOutQuestReader> all AdJOutQuestReader entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJOutQuestReader> findAll() {
		LogUtil
				.log("finding all AdJOutQuestReader instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from AdJOutQuestReader model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
@SuppressWarnings("unchecked")
public QuestReaderInfo findByPhyId(Long lngReaderId) throws SQLException {
		
		try {
			QuestReaderInfo objReaderInfo = new QuestReaderInfo();
			String strSql   = "SELECT"
							+ " ID,"
							+ " APPLY_ID,"
							+ " READ_MAN,"
							+ " UPDATE_USER,"
							+ " TO_CHAR(UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS'),"
							+ " IS_USE "
							+ "FROM"
							+ " AD_J_OUT_QUEST_READER "
							+ "WHERE"
							+ " IS_USE = ? AND"
							+ " ID =?";
			
			Object[] params = new Object[2];
			params[0] = "Y";
			params[1] = lngReaderId;
			
			LogUtil.log("EJB：签报申请抄送人数据根据物理ID查询操作开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
			List lstReaderList = bll.queryByNativeSQL(strSql, params);
			Iterator it = lstReaderList.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				objReaderInfo.setIdReader(Long.parseLong(data[0].toString()));
				if (data[1] != null) {
					objReaderInfo.setApplyIdReader(data[1].toString());
				}
				if (data[2] != null) {
					objReaderInfo.setReadManReader(data[2].toString());
				}
				if (data[3] != null) {
					objReaderInfo.setUpdateUserReader(data[3].toString());
				}
				if (data[4] != null) {
					objReaderInfo.setUpdateTimeReader(data[4].toString());
				}
				if (data[5] != null) {
					objReaderInfo.setIsUseReader(data[5].toString());
				}
			}
			LogUtil.log("EJB：签报申请抄送人数据根据物理ID查询操作正常结束", Level.INFO, null);
			return objReaderInfo;
			
		} catch (Exception e) {
			LogUtil.log("EJB：签报申请抄送人数据根据物理ID查询操作异常结束", Level.SEVERE, e);
			throw new SQLException("");
		}
	}

	/**
	 *  取得全部签报申请抄送人
	 *  @param applyId 申请单号
	 *  
	 *  @return 所有抄送人
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAllQuestReader(String lngApplyId, final int... rowStartIdxAndCount) {
			PageObject objResult = new PageObject();
			
			try {
				// 取得所有的申报申请的sql
				String strSql   = "SELECT"
								+ " A.ID,"
								+ " A.APPLY_ID,"
								+ " A.READ_MAN,"
								+ " B.CHS_NAME,"
								+ " TO_CHAR(A.UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS') "
								+ "FROM"
								+ " AD_J_OUT_QUEST_READER A,"
								+ " HR_J_EMP_INFO B "
								+ "WHERE"
								+ " A.READ_MAN = B.EMP_CODE AND"
								+ " A.IS_USE = ? AND"
								+ " A.APPLY_ID = ?";
				Object[] params = new Object[2];
				params[0] = "Y";
				params[1] = lngApplyId;
				
				// 取得所有的申报申请数目的sql
				String strSqlCount  = "SELECT COUNT(A.ID)"
									+ "FROM"
									+ " AD_J_OUT_QUEST_READER A,"
									+ " HR_J_EMP_INFO B "
									+ "WHERE"
									+ " A.READ_MAN = B.EMP_CODE AND"
									+ " A.IS_USE = ? AND"
									+ " A.APPLY_ID = ?";
				
				LogUtil.log("EJB:查询申报申请开始。SQL：", Level.INFO, null);
				LogUtil.log(strSql, Level.INFO, null);
				List lstQuery = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
				List<QuestReaderInfo> arrQuery = new ArrayList<QuestReaderInfo>();
				
				if (lstQuery != null && lstQuery.size() > 0) {
					Iterator it = lstQuery.iterator();
					while (it.hasNext()) {
						QuestReaderInfo model = new QuestReaderInfo();
						Object[] data = (Object[]) it.next();
						model.setIdReader(Long.parseLong(data[0].toString()));
						// 申请单号
						if (data[1] != null)
							model.setApplyIdReader(data[1].toString());
						// 抄送人员code
						if (data[2] != null)
							model.setReadManReader(data[2].toString());
						// 抄送人员名
						if (data[3] != null)
							model.setReadManNameReader(data[3].toString());
						// 修改时间
						if (data[4] != null)
							model.setUpdateTimeReader(data[4].toString());
						
						arrQuery.add(model);
					}
					if (arrQuery.size() > 0) {
						Long totalCount = Long.parseLong(bll.getSingal(strSqlCount, params)
								.toString());
						objResult.setList(arrQuery);
						objResult.setTotalCount(totalCount);
					}
				}  else {
					Long lngZero = new Long(0);
					objResult.setTotalCount(lngZero);
				}
			    LogUtil.log("EJB:查询申报申请正常结束", Level.INFO, null);
	    }  catch (NumberFormatException e) {
		    LogUtil.log("EJB:查询申报申请异常结束", Level.SEVERE, e);
		}
			return objResult;
    }
}
	
	
