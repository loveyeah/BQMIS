/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJOutreg;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;

/**
 * 物质出门登记
 * 
 * @see power.ejb.administration.AdJOutreg
 * @author sufeiyu
 */
@Stateless
public class AdJOutregFacade implements AdJOutregFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 单位*/
	@EJB(beanName = "BpCMeasureUnitFacade")
	BpCMeasureUnitFacadeRemote unitRemote;

	/**	
	 * 物质出门登记保存
	 * @param entity   
	 * @throws RuntimeException
	 * @author daichunlin
	 * @throws SQLException 
	 */
	public void save(AdJOutreg entity) throws SQLException {
		LogUtil.log("物资出门登记保存正常开始。" , Level.INFO, null);
		try {
			// 序号的取得
			Long id = bll.getMaxId("AD_J_OUTREG", "id");
			entity.setId(id);
			// 单据状态
			entity.setDcmStatus("0");
			// 更新时间
			entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("物资出门登记保存正常结束。" , Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("物资出门登记保存异常结束。" , Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**	
	 * 物质出门登记删除
	 * @param id
	 * @param entity   
	 * @throws RuntimeException
	 * @author daichunlin
	 */
	public void delete(Long id, String strEmployee) {
		try {
			String strSql = "UPDATE AD_J_OUTREG T"
				    + " SET T.UPDATE_USER = '"
					+ strEmployee 
					+ "' "
					+ ",   T.UPDATE_TIME = SYSDATE \n"
					+ ",   T.IS_USE = 'N' \n"
					+ "WHERE T.ID =" + id;

			LogUtil.log("物资出门登记删除正常开始。 SQL=" + strSql, Level.INFO, null);
			bll.exeNativeSQL(strSql);
			LogUtil.log("物资出门登记删除正常结束。" , Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("物资出门登记删除异常结束。" , Level.SEVERE, null);
			throw re;
		}
	}
	
	/**
	 * 上报时修改单据状态信息
	 * @param updater 更新者
	 * @param id 序号
	 * @author daichunlin
	 * @throws DataChangeException 
	 * @throws SQLException 
	 */
	public void updateState(String updater, Long id, String dteRequest)
	throws DataChangeException, SQLException{
		try {
			AdJOutreg oldAdJOutreg = new AdJOutreg();
			oldAdJOutreg = this.findById(id);
			String lastTime = oldAdJOutreg.getUpdateTime().toString().substring(
					0, 19);
			if (dteRequest.equals(lastTime)) {
				String strSql = "UPDATE AD_J_OUTREG T"
				    + " SET T.UPDATE_USER = '"
					+ updater 
					+ "' "
					+ ",   T.UPDATE_TIME = SYSDATE "
					+ ",   T.DCM_STATUS = '1' "
					+ "WHERE T.ID = "
					+ id;
				
				LogUtil.log("EJB:上报时修改单据状态信息开始。 SQL=" + strSql, Level.INFO, null);
				bll.exeNativeSQL(strSql);
				LogUtil.log("EJB:上报时修改单据状态信息结束。" , Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch (Exception re) {
			LogUtil.log("EJB:上报时修改单据状态信息失败", Level.SEVERE, re);
			throw new SQLException("");
		}	
	}
	
	/**	
	 * 物质出门登记更新
	 * @param entity   
	 * @throws RuntimeException
	 * @author daichunlin
	 * @throws DataChangeException 
	 * @throws SQLException 
	 */
	public void update(AdJOutreg entity,String lastmodifiedDate) 
	      throws DataChangeException, SQLException {
		LogUtil.log("EJB:物资出门登记更新正常开始。 " , Level.INFO, null);
		try {
			AdJOutreg oldAdJOutreg = new AdJOutreg();
			oldAdJOutreg = this.findById(entity.getId());
			String strDate = oldAdJOutreg.getUpdateTime().toString().substring(
					0, 19);
			if (lastmodifiedDate.equals(strDate)) {
				entity.setUpdateTime(new Date());
				entityManager.merge(entity);
				LogUtil.log("EJB:物资出门登记更新正常结束。 ", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}			
		} catch (DataChangeException ue) {
			LogUtil.log("EJB:来访人员登记表更新数据失败", Level.SEVERE, ue);
			throw ue;
		} catch (Exception e) {
			LogUtil.log("EJB:来访人员登记表更新数据失败", Level.SEVERE, e);
			throw new SQLException("");
		}
	}

	/**
	 * Find all AdJOutreg entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJOutreg property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJOutreg> found by query
	 */
	public AdJOutreg findById(Long id) {
		LogUtil.log("finding AdJOutreg instance with id: " + id, Level.INFO,
				null);
		try {
			AdJOutreg instance = entityManager.find(AdJOutreg.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJOutreg entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJOutreg property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJOutreg> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJOutreg> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJOutreg instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJOutreg model where model."
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
	 * Find all AdJOutreg entities.
	 * 
	 * @return List<AdJOutreg> all AdJOutreg entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJOutreg> findAll() {
		LogUtil.log("finding all AdJOutreg instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJOutreg model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询取得出门物资的详细
	 * @param rowStartIdxAndCount 
	 * @return PageObject
	 * @author daichunlin
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject getInfoList(String strEnterpriseCode, final int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("物资出门登记检索正常开始。", Level.INFO, null);
		try {
			PageObject pobj = new PageObject();
			// 查询sql
			String strSql = "SELECT* "
				    + " FROM"
				    + " AD_J_OUTREG A"
					+ " WHERE"
					+ " A.IS_USE = ? AND"				
					+ " (A.DCM_STATUS = '0' OR A.DCM_STATUS = '3') AND"
					+ " A.ENTERPRISE_CODE = ? "	;
			// 参数
			Object[] params = new Object[2];
            params[0] = 'Y';
            params[1] = strEnterpriseCode;
           
			LogUtil.log("物资出门登记检索正常开始。SQL：" + strSql, Level.INFO, null);
			List<AdJOutreg> list = bll.queryByNativeSQL(strSql, params, AdJOutreg.class,
					rowStartIdxAndCount);
			
			String sqlCount = "SELECT COUNT(A.ID) "
				    + " FROM"
					+ " AD_J_OUTREG A"
					+ " WHERE"
					+ " A.IS_USE = ? AND"				
					+ " (A.DCM_STATUS = '0' OR A.DCM_STATUS = '3') AND"
					+ " A.ENTERPRISE_CODE = ? "	;
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount,params).toString());

			pobj.setList(list);
			pobj.setTotalCount(totalCount);	
			LogUtil.log("物资出门登记检索正常结束", Level.INFO, null);
			return pobj;			
		} catch (Exception e) {
			LogUtil.log("物资出门登记检索异常结束", Level.SEVERE, e);
			throw new SQLException("");
		}
	}

	/**
	 * 查询取得出门物资的详细
	 * 
	 * @param dteStartDate 开始时间
	 * @param dteEndDate 结束时间
	 * @param strFirm    所在单位
	 * @param strDcmStatus  上报状态
	 * 
	 * @return PageObject
	 * @author sufeiyu
	 */
	@SuppressWarnings("unchecked")
	public PageObject getData(String strEnterpriseCode, java.sql.Date dteStartDate,
			java.sql.Date dteEndDate, String strFirm, String strDcmStatus,
			int... rowStartIdxAndCount) {
		PageObject objResult = new PageObject();
		
		try {
			String sql  = "SELECT" 
				        + "* " 
    				    + "FROM"
						+ "  AD_J_OUTREG A " 
						+ "WHERE"
						+  " A.ENTERPRISE_CODE = ? AND"
						+ "  A.IS_USE = ?" ;
			
			String sqlCount = "SELECT COUNT(A.ID)" 
							+ "FROM"
							+ "  AD_J_OUTREG A " 
							+ "WHERE"
							+  " A.ENTERPRISE_CODE = ? AND"
							+ "  A.IS_USE = ?" ;
			Object[] params = new Object[2];
			params[0] = strEnterpriseCode;
			params[1] = "Y";
			
			if (dteStartDate != null) {
				sql += " AND   (TO_DATE('"
					+ dteStartDate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')) <= A.OUT_DATE";
				
				sqlCount += " AND   (TO_DATE('"
						 + dteStartDate
						 + "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')) <= A.OUT_DATE";
			}
			
			if (dteEndDate != null) {
				sql += " AND   (TO_DATE('"
					+ dteEndDate
					+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')) >= A.OUT_DATE";
				
				sqlCount += " AND   (TO_DATE('"
						 + dteEndDate
						 + "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')) >= A.OUT_DATE";
			}

			if (!strFirm.equals("")) {
				sql += " AND  FIRM LIKE \'%" + strFirm + "%\'";
				sqlCount += " AND  FIRM LIKE \'%" + strFirm + "%\'";
			}

			if (!strDcmStatus.equals("")) {
				sql += " AND A.DCM_STATUS = '" + strDcmStatus + "'";
				sqlCount += " AND A.DCM_STATUS = '" + strDcmStatus + "'";
			}
            
			LogUtil.log("EJB:取得物资出门数据开始。", Level.INFO, null);
			LogUtil.log("SQL：" + sql, Level.INFO, null);
			
			// 查询的数据
			List<AdJOutreg> lstQuery = bll.queryByNativeSQL(sql, params, AdJOutreg.class, rowStartIdxAndCount);
			Long totalCount = Long
			.parseLong(bll.getSingal(sqlCount, params).toString());

			objResult.setList(lstQuery);
			objResult.setTotalCount(totalCount);	
			
			LogUtil.log("EJB:取得物资出门数据正常结束", Level.INFO, null);
		} catch (NumberFormatException e) {
			LogUtil.log("EJB:取得物资出门数据失败", Level.SEVERE, e);
		}

		return objResult;
	}

	/**
	 * 取得单位数量
	 * @param unit 单位Id
	 * @return 单位名称
	 * @author sufeiyu
	 */
	public String getUintById(String unit) {
		String unitName = "";
		// 单位名称
		BpCMeasureUnit objUnit;
		try {
			objUnit = unitRemote.findById(Long.parseLong(unit));
		} catch (NumberFormatException e) {
			return unitName;
		}
		if (objUnit == null || objUnit.getUnitName() == null
				|| objUnit.getUnitName().trim() == "") {
			unitName = "";
		} else {
            unitName = objUnit.getUnitName();
		}
		return unitName;
	}
}