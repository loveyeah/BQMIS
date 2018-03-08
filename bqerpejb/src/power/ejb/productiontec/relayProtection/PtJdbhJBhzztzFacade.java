package power.ejb.productiontec.relayProtection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.productiontec.relayProtection.form.ProtectEquForm;
import power.ejb.productiontec.relayProtection.form.ProtectedDevicesForm;

/**
 *继电保护装置台帐
 *@author liuyi 090716
 */
@Stateless
public class PtJdbhJBhzztzFacade implements PtJdbhJBhzztzFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条继电保护装置台帐记录
	 */
	public PtJdbhJBhzztz save(PtJdbhJBhzztz entity) {
		LogUtil.log("saving PtJdbhJBhzztz instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_J_BHZZTZ t\n"
				+ "where t.EQU_CODE = '" + entity.getEquCode() + "'";
				if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
				{
					return null;
				}
				entity.setDeviceId(bll.getMaxId("PT_JDBH_J_BHZZTZ", "DEVICE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}

	/**
	 * 删除一条继电保护装置台帐记录
	 */
	public void delete(PtJdbhJBhzztz entity) {
		LogUtil.log("deleting PtJdbhJBhzztz instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJdbhJBhzztz.class, entity
					.getDeviceId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 删除一条或多条继电保护装置台帐记录
	 */
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_JDBH_J_BHZZTZ a\n"
	    + " where a.DEVICE_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
       
       // 删除继电保护装置动作情况表中的相关数据
       String sqlPro = "delete from  "+
		"PT_JDBH_J_BHZZDZQK b\n"
	    + " where b.DEVICE_ID in (" + ids
	   + ")\n" ;
      bll.exeNativeSQL(sqlPro);
      
      
      // 删除保护装置对应保护类型表中的相关数据
      String sqlTf = "delete from  "+
		"PT_JDBH_J_ZZDYLX b\n"
	    + " where b.DEVICE_ID in (" + ids
	   + ")\n" ;
     bll.exeNativeSQL(sqlTf);
	}

	/**
	 * 更新一条继电保护装置台帐记录
	 */
	public PtJdbhJBhzztz update(PtJdbhJBhzztz entity) {
		LogUtil.log("updating PtJdbhJBhzztz instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_J_BHZZTZ t\n"
				+ "where t.EQU_CODE = '" + entity.getEquCode() + "'"
				+ "and t.DEVICE_ID !=" + entity.getDeviceId();
				if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
				{
					return null;
				}
			PtJdbhJBhzztz result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条继电保护装置台帐记录
	 */
	public PtJdbhJBhzztz findById(Long id) {
		LogUtil.log("finding PtJdbhJBhzztz instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhJBhzztz instance = entityManager
					.find(PtJdbhJBhzztz.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询继电保护装置列表
	 */
	public PageObject findAll(String name,String enterpriseCode,int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = "select a.DEVICE_ID,\n"
			+ "       a.PROTECTED_DEVICE_ID,\n"
			+ "       a.EQU_CODE, \n"
			+ "       a.VOLTAGE, \n"
			+ "       a.DEVICE_TYPE, \n"
			+ "       a.SIZE_TYPE, \n"
			+ "       a.SIZES, \n"
			+ "       a.MANUFACTURER, \n"
			+ "       a.OUT_FACTORY_DATE, \n"
			+ "       a.OUT_FACTORY_NO, \n"
			+ "       a.INSTALL_PLACE, \n"
			+ "       a.TEST_CYCLE, \n"
			+ "       a.CHARGE_BY, \n"
			+ "       a.MEMO, \n"
			+ "       a.ENTERPRISE_CODE, \n"
			+ "       getequnamebycode(a.equ_code), \n"
			+ "		getequnamebycode(b.equ_code), \n"		
			+ "       to_char(a.OUT_FACTORY_DATE,'yyyy-MM-dd'), \n"
			+ "       getworkername(a.CHARGE_BY) \n"
			+ "  from PT_JDBH_J_BHZZTZ a, PT_JDBH_J_BBHSBTZ b \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n"
			+ " and b.enterprise_code ='" + enterpriseCode + "' \n"
			+ " and a.PROTECTED_DEVICE_ID =b.PROTECTED_DEVICE_ID \n";
		
		
		String sqlCount = "select count(a.DEVICE_ID)\n"
			+ "  from PT_JDBH_J_BHZZTZ a, PT_JDBH_J_BBHSBTZ b \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n"
			+ " and b.enterprise_code ='" + enterpriseCode + "' \n"
			+ " and a.PROTECTED_DEVICE_ID =b.PROTECTED_DEVICE_ID \n";
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and GETEQUNAMEBYCODE(a.equ_code) like '%" + name + "%' \n";
			sqlCount = sqlCount + " and GETEQUNAMEBYCODE(a.equ_code) like '%" + name + "%' \n";
		}

		sql = sql + " order by a.DEVICE_ID \n";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJdbhJBhzztz ptjd = new PtJdbhJBhzztz();
				ProtectEquForm form = new ProtectEquForm();
				Object []data = (Object[])it.next();
				ptjd.setDeviceId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					ptjd.setProtectedDeviceId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					ptjd.setEquCode(data[2].toString());
				if(data[3] != null)
					ptjd.setVoltage(data[3].toString());
				if(data[4] != null)
					ptjd.setDeviceType(data[4].toString());
				if(data[5] != null)
					ptjd.setSizeType(data[5].toString());
				if(data[6] != null)
					ptjd.setSizes(data[6].toString());
				if(data[7] != null)
					ptjd.setManufacturer(data[7].toString());
				if(data[8] != null)
					try {
						ptjd.setOutFactoryDate(sbf.parse(data[8].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				if(data[9] != null)
					ptjd.setOutFactoryNo(data[9].toString());
				if(data[10] != null)
					ptjd.setInstallPlace(data[10].toString());
				if(data[11] != null)
					ptjd.setTestCycle(Long.parseLong(data[11].toString()));
				if(data[12] != null)
					ptjd.setChargeBy(data[12].toString());
				if(data[13] != null)
					ptjd.setMemo(data[13].toString());
				if(data[14] != null)
					ptjd.setEnterpriseCode(data[14].toString());
				if(data[15] != null)
					form.setEquName(data[15].toString());
				if(data[16] != null)
					form.setProtectedDeviceName(data[16].toString());
				if(data[17] != null)
					form.setOutFactoryDate(data[17].toString());
				if(data[18] != null)
					form.setChargeName(data[18].toString());
					
				form.setPtjd(ptjd);
				arrlist.add(form);
			 }
		  }
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

}