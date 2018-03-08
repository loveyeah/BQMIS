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
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdCMenuWh.
 * 
 * @see power.ejb.administration.AdCMenuWh
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdCMenuWhFacade implements AdCMenuWhFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 *增加菜谱维护信息
	 * @author Li Chensheng
	 */
	public void save(AdCMenuWh entity) {
		LogUtil.log("EJB:增加菜谱维护信息开始。", Level.INFO, null);
		try {
			Long id = bll.getMaxId("AD_C_MENU_WH", "id");
			entity.setId(id);
			entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("EJB:增加菜谱维护信息成功", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:增加菜谱维护信息失败", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * Delete a persistent AdCMenuWh entity.
	 * 
	 * @param entity
	 *            AdCMenuWh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdCMenuWh entity) {
		LogUtil.log("deleting AdCMenuWh instance", Level.INFO, null);
		try {
			entity = entityManager
					.getReference(AdCMenuWh.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * @author Li Chensheng
	 * 更新菜谱维护信息
	 */
	public AdCMenuWh update(AdCMenuWh entity) throws DataChangeException, SQLException{
		LogUtil.log("EJB：更新菜谱维护信息开始。", Level.INFO, null);
		try {
			AdCMenuWh adcMenWh = findById(entity.getId());
			if (adcMenWh.getUpdateTime().compareTo(entity.getUpdateTime()) == 0) {
				entity.setUpdateTime(new Date());
				AdCMenuWh result = entityManager.merge(entity);				
				LogUtil.log("EJB：更新菜谱维护信息成功。", Level.INFO, null);
				return result;
			} else {
				throw new DataChangeException("发生排他异常");
			}
			
		} catch (DataChangeException e) {
			LogUtil.log("EJB：发生排他异常", Level.SEVERE, null);
			throw e;
		} catch (Exception e) {
			LogUtil.log("EJB：更新菜谱维护信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	public AdCMenuWh findById(Long id) {
		LogUtil.log("finding AdCMenuWh instance with id: " + id, Level.INFO,
				null);
		try {
			AdCMenuWh instance = entityManager.find(AdCMenuWh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdCMenuWh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCMenuWh property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCMenuWh> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdCMenuWh> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdCMenuWh instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdCMenuWh model where model."
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
	 * 根据菜谱类别得到菜谱信息
	 * 
	 * @param menutypeCode 菜谱类别
	 * 
	 * @return 所有的菜谱
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAllMenu(String menutypeCode, final int... rowStartIdxAndCount) {
		PageObject objResult = new PageObject();
		
		try {
			// 取得菜谱类别sql
			String strSql = "SELECT"
						  + " ID,"
						  + " MENU_CODE,"
						  + " MENU_NAME,"
						  + " MENU_PRICE,"
						  + " RETRIEVE_CODE,"
						  + " MENUTYPE_CODE "
						  + "FROM"
						  + " AD_C_MENU_WH "
						  + "WHERE"
						  + " IS_USE = 'Y' AND"
						  + " MENUTYPE_CODE = ?";
			Object[] params = new Object[1];
			params[0] = menutypeCode;
			
			// 取得菜谱类别数目sql
			String strSqlCount   = "SELECT COUNT(ID)"
								 + "FROM"
								 + " AD_C_MENU_WH "
								 + "WHERE"
								 + " IS_USE = 'Y' AND"
								 + " MENUTYPE_CODE = ?";
			
			LogUtil.log("EJB:得到菜谱信息数据开始。", Level.INFO, null);
			LogUtil.log("SQL：" + strSql, Level.INFO, null);
			
			// 查询的数据
			List lstQuery = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			// 封装的数据
			List<AdCMenuWh> arrQuery = new ArrayList<AdCMenuWh>();

			LogUtil.log("EJB:得到菜谱信息数据正常结束", Level.INFO, null);

			// 格式转换
			if ((lstQuery != null) && (lstQuery.size() > 0)) {
				Iterator it = lstQuery.iterator();
				while (it.hasNext()) {
					AdCMenuWh model = new AdCMenuWh();
					Object[] data = (Object[]) it.next();
					model.setId(Long.parseLong(data[0].toString()));
					// 菜谱编码
					if (data[1] != null)
						model.setMenuCode(data[1].toString());
					// 菜谱名称
					if (data[2] != null)
						model.setMenuName(data[2].toString());
					// 价格
					if (data[3] != null)
						model.setMenuPrice(Double.parseDouble(data[3]
								.toString()));
					// 检索码
					if (data[4] != null)
						model.setRetrieveCode(data[4].toString());
					// 类别编码
					if (data[5] != null)
						model.setMenutypeCode(data[5].toString());
					arrQuery.add(model);
				}
				if (arrQuery.size() > 0) {
					Long totalCount = Long.parseLong(bll.getSingal(strSqlCount,
							params).toString());
					objResult.setList(arrQuery);
					objResult.setTotalCount(totalCount);
				}
			} else {
				Long lngZero = new Long(0);
				objResult.setTotalCount(lngZero);
			}
		} catch (NumberFormatException e) {
			LogUtil.log("EJB:得到菜谱信息数据失败", Level.SEVERE, e);
		}
		return objResult;
	}
	/**
     * 查询菜谱维护信息
     * @author Li Chensheng
     * @param 
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
	public  PageObject findMenu(String menuTypeCode,String enterpriseCode,
			final int... rowStartIdxAndCount)throws SQLException{
    	LogUtil.log("EJB:查询菜谱维护信息开始。", Level.INFO, null);
		try {
            PageObject  result = new PageObject();
         // SQL语句参数
			Object[] params = new Object[3];
			params[0] = "Y";
			params[1] = menuTypeCode;
			params[2] = enterpriseCode;
            // 查询sql
            String sql = "SELECT"
            	         + " T.ID,"
            	         + " T.MENU_CODE,"
            	         + " T.MENU_NAME,"	
            	         + " T.MENU_MEMO,"
            	         + " T.MENU_PRICE,"
            	         + " T.RETRIEVE_CODE,"
            	         + " T.IS_USE,"
            	         + " T.MENUTYPE_CODE,"
            	         + " T.UPDATE_USER,"
            	         + " T.UPDATE_TIME,"
            	         + " T.ENTERPRISE_CODE"
            	         + " FROM AD_C_MENU_WH T"
            	         + " WHERE T.IS_USE = ? "
            	         + " AND T.MENUTYPE_CODE = ? "
            	         + " AND T.ENTERPRISE_CODE = ? ";

            String sqlCount = "SELECT" 
            	            + " COUNT(1)"
				            + " FROM AD_C_MENU_WH T"
                            + " WHERE T.IS_USE = ? " 
                            + " AND T.MENUTYPE_CODE = ? " 
                            + " AND T.ENTERPRISE_CODE = ? ";
            LogUtil.log("SQL：" + sql, Level.INFO, null);
            List<AdCMenuWh> list=bll.queryByNativeSQL(sql,params,AdCMenuWh.class,rowStartIdxAndCount);
            Long totalCount=Long.parseLong(bll.getSingal(sqlCount,params).toString());           
			result.setList(list);
			result.setTotalCount(totalCount);
			LogUtil.log("EJB:查询菜谱维护信息结束。", Level.INFO, null);
			return result;			
		} catch (Exception e) {
			throw new SQLException();
        }
    }
    /**
	 *  删除一条菜谱维护信息
	 * @author Li Chensheng
	 * @throws JSONException
	 * 
	 */
    public void delete(Long id,String workerCode,String updateTime)throws DataChangeException, SQLException{
    	LogUtil.log("EJB：删除菜谱维护信息开始。", Level.INFO, null);
		try {
			AdCMenuWh adcMenWh = findById(id);
			String testLastmodifiedDate = adcMenWh.getUpdateTime().toString()
                       .substring(0, 19);
			String lastmodifiedDate = updateTime.substring(
					0, 10)
					+ " " + updateTime.substring(11);
			if (!testLastmodifiedDate.equals(lastmodifiedDate)){
				throw new DataChangeException("发生排他异常");
			}
            adcMenWh.setUpdateUser(workerCode);
            adcMenWh.setUpdateTime(new Date());
            adcMenWh.setIsUse("N");
            entityManager.merge(adcMenWh);
            LogUtil.log("删除成功", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("EJB：发生排他异常", Level.SEVERE, null);
			throw e;
		} catch (Exception e) {
			LogUtil.log("EJB：删除菜谱维护信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
    }


}