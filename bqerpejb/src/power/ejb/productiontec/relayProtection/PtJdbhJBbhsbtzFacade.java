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
import power.ejb.productiontec.relayProtection.form.ProtectedDevicesForm;

/**
 * 被保护设备记录台帐
 */
@Stateless
public class PtJdbhJBbhsbtzFacade implements PtJdbhJBbhsbtzFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 *新增一条被保护设备记录
	 */
	public PtJdbhJBbhsbtz save(PtJdbhJBbhsbtz entity) {
		LogUtil.log("saving PtJdbhJBbhsbtz instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_J_BBHSBTZ t\n"
			+ "where t.EQU_CODE = '" + entity.getEquCode() + "'";
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			entity.setProtectedDeviceId(bll.getMaxId("PT_JDBH_J_BBHSBTZ", "PROTECTED_DEVICE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}

	/**
	 * 删除一条被保护设备记录
	 */
	public void delete(PtJdbhJBbhsbtz entity) {
		LogUtil.log("deleting PtJdbhJBbhsbtz instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJdbhJBbhsbtz.class, entity
					.getProtectedDeviceId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 删除一条或多条继电保护类型记录
	 */
	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_JDBH_J_BBHSBTZ a\n"
	    + " where a.PROTECTED_DEVICE_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
       
       // 删除继电保护装置台帐表中的相关数据
       String sqlPro = "delete from  "+
		"PT_JDBH_J_BHZZTZ b\n"
	    + " where b.PROTECTED_DEVICE_ID in (" + ids
	   + ")\n" ;
      bll.exeNativeSQL(sqlPro);
	}

	/**
	 * 更新一条被保护设备记录
	 */
	public PtJdbhJBbhsbtz update(PtJdbhJBbhsbtz entity) {
		LogUtil.log("updating PtJdbhJBbhsbtz instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_J_BBHSBTZ t\n"
			+ "where t.EQU_CODE = '" + entity.getEquCode() + "'"
			+ "and t.PROTECTED_DEVICE_ID !=" + entity.getProtectedDeviceId();
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			PtJdbhJBbhsbtz result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条被保护设备记录
	 */
	public PtJdbhJBbhsbtz findById(Long id) {
		LogUtil.log("finding PtJdbhJBbhsbtz instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhJBbhsbtz instance = entityManager.find(PtJdbhJBbhsbtz.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询被保护设备列表
	 */
	public PageObject findAll(String name,String enterpriseCode,int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = "select a.PROTECTED_DEVICE_ID,\n"
			+ "       a.equ_code,\n"
			+ "       a.EQU_LEVEL, \n"
			+ "       a.VOLTAGE, \n"
			+ "       a.INSTALL_PLACE, \n"
			+ "       a.MANUFACTURER, \n"
			+ "       a.SIZES, \n"
			+ "       a.OUT_FACTORY_NO, \n"
			+ "       a.OUT_FACTORY_DATE, \n"
			+ "       a.CHARGE_MAN, \n"
			+ "       a.MEMO, \n"
			+ "       a.ENTERPRISE_CODE, \n"
			+ "       getequnamebycode(a.equ_code), \n"
			+ "       to_char(a.OUT_FACTORY_DATE,'yyyy-MM-dd'), \n"
			+ "       getworkername(a.CHARGE_MAN) \n"
			+ "  from PT_JDBH_J_BBHSBTZ a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		String sqlCount = "select count(a.PROTECTED_DEVICE_ID)\n"
			+ "  from PT_JDBH_J_BBHSBTZ a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and GETEQUNAMEBYCODE(a.equ_code) like '%" + name + "%' \n";
			sqlCount = sqlCount + " and GETEQUNAMEBYCODE(a.equ_code) like '%" + name + "%' \n";
		}

		sql = sql + " order by a.PROTECTED_DEVICE_ID \n";
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		SimpleDateFormat sbFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJdbhJBbhsbtz pjjb = new PtJdbhJBbhsbtz();
				ProtectedDevicesForm form = new ProtectedDevicesForm();
				Object []data = (Object[])it.next();
				pjjb.setProtectedDeviceId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					pjjb.setEquCode(data[1].toString());
				if(data[2] != null)
					pjjb.setEquLevel(data[2].toString());
				if(data[3] != null)
					pjjb.setVoltage(data[3].toString());
				if(data[4] != null)
					pjjb.setInstallPlace(data[4].toString());
				if(data[5] != null)
					pjjb.setManufacturer(data[5].toString());
				if(data[6] != null)
					pjjb.setSizes(data[6].toString());
				if(data[7] != null)
					pjjb.setOutFactoryNo(data[7].toString());
				if(data[8] != null)
					try {
						pjjb.setOutFactoryDate(sbFormat.parse(data[8].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				if(data[9] != null)
					pjjb.setChargeMan(data[9].toString());
				if(data[10] != null)
					pjjb.setMemo(data[10].toString());
				if(data[11] != null)
					pjjb.setEnterpriseCode(data[11].toString());
				if(data[12] != null)
					form.setEquName(data[12].toString());
				if(data[13] != null)
					form.setOutFactoryDate(data[13].toString());
				if(data[14] != null)
					form.setChargeName(data[14].toString());
					
				form.setPjjb(pjjb);
				arrlist.add(form);
			 }
		  }
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

	

}