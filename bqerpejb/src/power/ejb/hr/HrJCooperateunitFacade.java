/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.hr;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * 协作单位维护Facade
 *
 * @author zhaozhijie
 */
@Stateless
public class HrJCooperateunitFacade implements HrJCooperateunitFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**是否使用*/
	private String IS_USE_Y = "Y";

	/**
	 * 新增协作单位维护
	 *
	 * @param entity 新增协作单位维护实体
	 * @throws SQLException
	 */
	public void save(HrJCooperateunit entity) throws SQLException {
		LogUtil.log("EJB:新增协作单位维护开始", Level.INFO, null);
		try {
			// 设置新增协作单位ID
			entity.setCooperateUnitId(bll.getMaxId("HR_J_COOPERATEUNIT", "COOPERATE_UNIT_ID"));
			entityManager.persist(entity);
			LogUtil.log("EJB:新增协作单位维护结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:新增协作单位维护失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent HrJCooperateunit entity.
	 *
	 * @param entity
	 *            HrJCooperateunit entity to delete
	 * @throws SQLException
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJCooperateunit entity) {
		LogUtil.log("deleting HrJCooperateunit instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJCooperateunit.class, entity
					.getCooperateUnitId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新协作单位维护
	 *
	 * @param entity 更新协作单位维护实体
	 * @return 协作单位维护实体
	 * @throws SQLException
	 */
	public HrJCooperateunit update(HrJCooperateunit entity) throws SQLException {
		LogUtil.log("EJB:更新协作单位维护开始", Level.INFO, null);
		try {
			HrJCooperateunit result = entityManager.merge(entity);
			LogUtil.log("EJB:更新协作单位维护结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:更新协作单位维护失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	public HrJCooperateunit findById(Long id) {
		LogUtil.log("finding HrJCooperateunit instance with id: " + id,
				Level.INFO, null);
		try {
			HrJCooperateunit instance = entityManager.find(
					HrJCooperateunit.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJCooperateunit entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrJCooperateunit property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJCooperateunit> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJCooperateunit> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJCooperateunit instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJCooperateunit model where model."
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
	 * Find all HrJCooperateunit entities.
	 *
	 * @return List<HrJCooperateunit> all HrJCooperateunit entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJCooperateunit> findAll() {
		LogUtil.log("finding all HrJCooperateunit instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJCooperateunit model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询所有协作单位维护信息
	 * @param strEnterpriseCode 企业代码
	 * @return 所有协作单位维护信息
	 * @throws SQLException 
	 * @throws ParseException 
	 * @author zhaozhijie
	 */
	@SuppressWarnings("unchecked")
	public PageObject getCooperateUnit(String strEnterpriseCode,final int... rowStartIdxAndCount)
	    throws SQLException {
		LogUtil.log("EJB:协作单位维护查询正常开始", Level.INFO, null);
		try {
			PageObject pobj = new PageObject();
			// 查询sql
			StringBuffer sbd = new StringBuffer();
			sbd.append("SELECT ");
			sbd.append("A.COOPERATE_UNIT_ID, ");
			sbd.append("A.COOPERATE_UNIT, ");
			sbd.append("A.ORDER_BY ");
			sbd.append("FROM HR_J_COOPERATEUNIT A ");
			sbd.append("WHERE A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");
			
			Object[] params = new Object[2];
			params[0] = strEnterpriseCode;
			params[1] = IS_USE_Y;
			// 记录数sql
			StringBuffer sbdCount = new StringBuffer();
			sbdCount.append("SELECT ");
			sbdCount.append("COUNT(A.COOPERATE_UNIT_ID) ");
			sbdCount.append("FROM HR_J_COOPERATEUNIT A ");
			sbdCount.append("WHERE A.ENTERPRISE_CODE = ? ");
			sbdCount.append("AND A.IS_USE = ? ");
			// sql执行
			List<HrJCooperateunit> list = bll.queryByNativeSQL(sbd.toString(), params, rowStartIdxAndCount);
			LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
			// 记录数
			Long totalCount = Long.parseLong(bll.getSingal(sbdCount.toString(), params)
					.toString());
			LogUtil.log("EJB: SQL =" + sbdCount.toString(), Level.INFO, null);
			List<HrJCooperateunit> arrList = new ArrayList<HrJCooperateunit>();
			if (list !=null) {
				Iterator it = list.iterator();
				while(it.hasNext()) {
					HrJCooperateunit hrJCooperateunit = new HrJCooperateunit();
					Object[] data = (Object[]) it.next();
					// 协作单位ID
					if(data[0] != null) {
						hrJCooperateunit.setCooperateUnitId(Long.parseLong(data[0].toString()));
					}
					// 协作单位名称
					if (data[1] != null) {
						hrJCooperateunit.setCooperateUnit(data[1].toString());
					}
					// 显示顺序
					if (data[2] != null) {
						hrJCooperateunit.setOrderBy(Long.parseLong(data[2].toString()));
					}
					arrList.add(hrJCooperateunit);
				}
			}
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:协作单位维护查询正常结束", Level.INFO, null);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:协作单位维护查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	/**
	 * 查找所有协作单位
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllCooperateunits(String enterpriseCode){
		try{
			String sql = "SELECT C.COOPERATE_UNIT_ID,C.COOPERATE_UNIT " +
					"  FROM HR_J_COOPERATEUNIT C" +
					" WHERE C.IS_USE = ? AND C.ENTERPRISE_CODE = ?";
			LogUtil.log("所有协作单位id和名称开始。", Level.INFO, null);
			List list = bll.queryByNativeSQL(sql,new Object[]{"Y",enterpriseCode});
			List<DrpCommBeanInfo> arraylist = new ArrayList<DrpCommBeanInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DrpCommBeanInfo model = new DrpCommBeanInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setText(data[1].toString());
				}
				arraylist.add(model);
			}
			PageObject result = new PageObject();
			result.setList(arraylist);
			LogUtil.log("查找所有协作单位id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有协作单位id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}
	/**
     * 
     */
    public PageObject getCooperateUnitIDAndName(String strEnterpriseCode){
    	try{
    		LogUtil.log("EJB:获取协作单位ID与名称开始", Level.INFO, null);
    		
    		PageObject result = new PageObject();
    		List lstParams = new ArrayList();
    		lstParams.add("Y");
    		lstParams.add(strEnterpriseCode);
    		//sql文
    		String strSql = "SELECT COOPERATE_UNIT_ID,COOPERATE_UNIT FROM HR_J_COOPERATEUNIT WHERE IS_USE = ? AND ENTERPRISE_CODE = ? ";
    		//query
    		List list = bll.queryByNativeSQL(strSql,lstParams.toArray());
    		
    		List<HrJCooperateunit> arrList = new ArrayList<HrJCooperateunit>();
    		Iterator it = list.iterator();
    		HrJCooperateunit unit = new HrJCooperateunit();
    		unit.setCooperateUnitId(0l);
    		unit.setCooperateUnit("");
    		arrList.add(unit);
    		while(it.hasNext()){
    			Object[] data = (Object[])it.next();
    			HrJCooperateunit info = new HrJCooperateunit();
    			//协作企业ID
    			info.setCooperateUnitId(Long.parseLong(data[0].toString()));
    			if((data[1] != null) && !"".equals(data[1])){
    				info.setCooperateUnit(data[1].toString()); 
    			}else{
    				info.setCooperateUnit("");
    			}
    			arrList.add(info);
    		}
    		
    		result.setList(arrList);
    		result.setTotalCount(1l);
    		
    		LogUtil.log("EJB: 获取协作单位ID与名称结束。", Level.INFO, null);
    		return result;
    	}catch(RuntimeException re){
    		LogUtil.log("EJB:获取协作单位ID与名称失败", Level.SEVERE, re);
			throw re;
    	}
    	
    }
}