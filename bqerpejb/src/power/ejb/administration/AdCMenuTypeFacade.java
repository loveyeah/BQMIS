/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.ejb.administration;


import java.sql.SQLException;
import java.text.DecimalFormat;
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
import power.ejb.administration.form.LeaderAuditInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 
 * 
 *  菜谱类型维护
 * @author zhaomingjian
 */
@Stateless
public class AdCMenuTypeFacade implements AdCMenuTypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 菜谱类型保存
	 * @param entity 菜谱类型实体
	 * 
	 *  @throws RuntimeException 
	 *             
	 */
	public void save(AdCMenuType entity) {
		LogUtil.log("saving AdCMenuType instance", Level.INFO, null);
		try {
		    Long id = bll.getMaxId("AD_C_MENU_TYPE", "id");;
		    Long menu = bll.getMaxId("AD_C_MENU_TYPE", "MENUTYPE_CODE");
		    entity.setId(id);
		    if(menu.toString().length() <= 1){
		    	 entity.setMenutypeCode("0"+menu.toString());
		    }else{
		    	entity.setMenutypeCode(menu.toString());	
		    }
		    
		    entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 菜谱类型删除.
	 * 
	 * @param entity
	 *            AdCMenuType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdCMenuType entity) {
		LogUtil.log("deleting AdCMenuType instance", Level.INFO, null);
		try {
			
			entity = entityManager.getReference(AdCMenuType.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 菜谱类别逻辑删除
	 * @param  entity 菜谱类别
	 */
  public void logicDelete(AdCMenuType entity) throws SQLException {
		LogUtil.log("EJB: 菜谱类型删除开始", Level.INFO, null);
		try {
			AdCMenuType objDelEntity = this.findById(entity.getId());
			objDelEntity.setUpdateUser(entity.getUpdateUser());
			objDelEntity.setIsUse("N");
			objDelEntity.setUpdateTime(new Date());
			entityManager.persist(objDelEntity);

			LogUtil.log("EJB :菜谱类型删除正常结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("EJB: 菜谱类型删除开始失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	/**
	 * 菜谱类型维护更新
	 * 
	 * @param entity
	 *            菜谱类型
	 * @return AdCMenuType
	 * @throws RuntimeException
	 */
	public AdCMenuType update(AdCMenuType entity) {
		LogUtil.log("updating AdCMenuType instance", Level.INFO, null);
		try {
			entity.setUpdateTime(new Date());
			AdCMenuType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AdCMenuType findById(Long id) {
		LogUtil.log("finding AdCMenuType instance with id: " + id, Level.INFO,
				null);
		try {
			AdCMenuType instance = entityManager.find(AdCMenuType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdCMenuType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCMenuType property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCMenuType> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdCMenuType> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdCMenuType instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdCMenuType model where model."
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
	 * Find all AdCMenuType entities.
	 * 
	 * @return List<AdCMenuType> all AdCMenuType entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdCMenuType> findAll() {
		LogUtil.log("finding all AdCMenuType instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdCMenuType model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
     * 查询菜谱类别信息
     * 
     * @param 
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
	public  PageObject findMenutype(String strEnterpriseCode,final int... rowStartIdxAndCount) {
    	LogUtil.log("finding all AdCMenuType instances", Level.INFO, null);
		try {
			LogUtil.log("EJB :查询菜谱类别信息查询开始", Level.INFO, null);
            PageObject result = null;
            // 查询sql
            String sql = "SELECT  " 
            	            + " T.*  "
            	            + " FROM AD_C_MENU_TYPE T  " 
                            + " WHERE   T.IS_USE = 'Y' "
                            + " AND T.ENTERPRISE_CODE =  '"+strEnterpriseCode+"' "
                            + " ORDER BY T.ID ";
       String sqlCount = "SELECT " 
            	            + " COUNT(1)  "
				            + " FROM AD_C_MENU_TYPE T  "
				            + " WHERE   T.IS_USE = 'Y' " 
				            + "AND T.ENTERPRISE_CODE ='"+strEnterpriseCode+"'";
       
            List<AdCMenuType> list=bll.queryByNativeSQL(sql, AdCMenuType.class, rowStartIdxAndCount);
            Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
            result = new PageObject();
			result.setList(list);
			result.setTotalCount(totalCount);
			LogUtil.log("EJB :sql = "+ sql, Level.INFO, null);
			LogUtil.log("EJB :查询菜谱类别信息查询结束", Level.INFO, null);
			return result;		
		
		} catch (RuntimeException e) { 
			LogUtil.log("EJB :查询菜谱类别信息查询失败", Level.SEVERE, null);
			throw e;
        }
    }

	/**
     * 查询值长审核订餐信息
     * 
     * @param strWorkTypeCode 订餐类别
	 * @param strSubWorkTypeCode 订餐日期
	 * @param strEnterpriseCode 企业代码
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
	public  PageObject findWatchAduitInfo(String strMenuType, String strMenuDate, String strEnterpriseCode) {
    	LogUtil.log("CHA", Level.INFO, null);

		// 数字输出格式化
		String patternNumber = "###,###,###,###,##0.00";
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
        PageObject pobj = new PageObject();
		Object[] params = new Object[12];
        // 查询sql
        String sql = "SELECT F.CHS_NAME, " +
        		"G.DEPT_NAME, " +
        		"D.MENU_NAME, " +
        		"C.MENUTYPE_NAME, " +
        		"B.MENU_AMOUNT, " +
        		"B.MENU_PRICE, " +
        		"B.MEMO, " +
        		"DECODE(A.MENU_TYPE,'1','早餐','2','中餐','3','晚餐','4','宵夜'), " +
        		"TO_CHAR(A.MENU_DATE, 'yyyy-mm-dd hh24:mi:ss'), " +
        		"A.INSERTDATE, " +
        		"DECODE(A.PLACE,'1','餐厅','2','工作地') " +
	            " FROM AD_J_USER_MENU A, " +
	            		"AD_J_USER_SUB B, " +
	            		"AD_C_MENU_TYPE C, " +
	            		"AD_C_MENU_WH D, " +
	            		"HR_J_EMP_INFO F, " +
	            		"HR_C_DEPT G " + 
                " WHERE  A.IS_USE = ? AND " +
                "B.IS_USE = ? AND " +
                "C.IS_USE = ? AND " +
                "D.IS_USE = ? AND " +
                "A.MENU_DATE = to_date(?,'yyyy-mm-dd') AND " +
                "A.MENU_TYPE = ? AND " +
                "A.M_ID = B.M_ID AND " +
                "B.MENU_CODE = D.MENU_CODE AND " +
                "D.MENUTYPE_CODE = C.MENUTYPE_CODE AND " +
                "A.INSERTBY = F.EMP_CODE AND " +
                "F.DEPT_ID = G.DEPT_ID AND " +
                "A.MENU_INFO = ? AND " +
                "A.ENTERPRISE_CODE = ? AND " +
                "C.ENTERPRISE_CODE = ? AND " +
                "D.ENTERPRISE_CODE = ? AND " +
                "F.ENTERPRISE_CODE = ? AND " +
                "G.ENTERPRISE_CODE = ? "  + 
               " ORDER BY  A.INSERTDATE ";
        params[0] = 'Y';
        params[1] = 'Y';
        params[2] = 'Y';
        params[3] = 'Y';
        params[4] = strMenuDate;
        params[5] = strMenuType;
        // modify by liuyi 090925 值长审核中打印的数据，是订餐装填为2的待审核状态
//        params[6] = '1';
        params[6] = '2';
        params[7] = strEnterpriseCode;
        params[8] = strEnterpriseCode;
        params[9] = strEnterpriseCode;
        params[10] = strEnterpriseCode;
        params[11] = strEnterpriseCode;
        List<LeaderAuditInfo> list=bll.queryByNativeSQL(sql, params);
        List<LeaderAuditInfo> lstLeader = new ArrayList<LeaderAuditInfo>();
        if (list != null) {
			Iterator it = list.iterator();
			while (it.hasNext()) { 
				Object[] data = (Object[]) it.next();
				LeaderAuditInfo  leaderAudit = new LeaderAuditInfo();
				// 订餐人
				if (data[0] != null) {
					leaderAudit.setWorkerName(data[0].toString());
				}
				// 所属部门
				if (data[1] != null) {
					leaderAudit.setDepName(data[1].toString());
				}
				// 菜谱名称
				if (data[2] != null) {
					leaderAudit.setMenuName(data[2].toString());
				}
				// 菜谱类别
				if (data[3] != null) {
					leaderAudit.setMenuTypeName(data[3].toString());
				}
				// 份数
				if (data[4] != null) {
					leaderAudit.setMenuAmount(data[4].toString());
				}
				// 单价
				if (data[5] != null) {
					leaderAudit.setMenuPrice(dfNumber.format(data[5]));
				}
				// 备注
				if (data[6] != null) {
					leaderAudit.setMemo(data[6].toString());
				}
				// 订餐类别
				if (data[7] != null) {
					leaderAudit.setMenuType(data[7].toString());
				}
				// 订餐类别
				if (data[10] != null) {
					leaderAudit.setPlace(data[10].toString());
				}
				lstLeader.add(leaderAudit);
			}
		}
		if(lstLeader.size()>0)
		{
			// 符合条件的定期工作维护
			pobj.setList(lstLeader);
			// 符合条件的定期工作维护的总数 
			pobj.setTotalCount(Long.parseLong(lstLeader.size() + ""));
		}	
		return pobj;			
    }
}