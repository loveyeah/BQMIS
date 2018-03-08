package power.ejb.productiontec.insulation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import power.ejb.productiontec.insulation.form.DeviceTryForm;
import power.ejb.productiontec.insulation.form.PjjTryForm;

/**
 * @author liuyi 090706
 */
@Stateless
public class PtJyjdJSbtzlhFacade implements PtJyjdJSbtzlhFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 增加一条绝缘设备信息
	 */
	public PtJyjdJSbtzlh save(PtJyjdJSbtzlh entity) {
		LogUtil.log("saving PtJyjdJSbtzlh instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JYJD_J_SBTZLH t\n"
				+ "where t.DEVICE_NAME = '" + entity.getDeviceName() + "'";
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			else
			{
				entity.setDeviceId(bll.getMaxId("PT_JYJD_J_SBTZLH", "DEVICE_ID"));
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			}
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}

	/**
	 * 删除一条记录
	 */
	public void delete(PtJyjdJSbtzlh entity) {
		LogUtil.log("deleting PtJyjdJSbtzlh instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJyjdJSbtzlh.class, entity
					.getDeviceId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条绝缘设备信息
	 */
	public PtJyjdJSbtzlh update(PtJyjdJSbtzlh entity) {
		LogUtil.log("updating PtJyjdJSbtzlh instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JYJD_J_SBTZLH t\n"
				+ "where t.DEVICE_NAME = '" + entity.getDeviceName() + "'"
				+ " and t.DEVICE_ID !=" + entity.getDeviceId();
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			PtJyjdJSbtzlh result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过Id查一条记录
	 * 
	 */
	public PtJyjdJSbtzlh findById(Long id) {
		LogUtil.log("finding PtJyjdJSbtzlh instance with id: " + id,
				Level.INFO, null);
		try {
			PtJyjdJSbtzlh instance = entityManager
					.find(PtJyjdJSbtzlh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_JYJD_J_SBTZLH a\n"
	    + " where a.DEVICE_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
		
	}
	
	public PageObject findAll(String name, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.DEVICE_ID,\n"
			+ "       a.DEVICE_NAME,\n"
			+ "       a.TEST_CYCLE,\n"
			+ "       a.FACTORY,\n"
			+ "       a.SIZES,\n"
			+ "       a.USER_RANGE,\n"
			+ "       a.VOLTAGE,\n"
			+ "       a.MEMO,\n"
			+ "       a.ENTERPRISE_CODE \n"
			+ "  from PT_JYJD_J_SBTZLH a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		String sqlCount = "select count(a.DEVICE_ID)\n"
			+ "  from PT_JYJD_J_SBTZLH a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and a.DEVICE_NAME like '%" + name + "%' \n";
			sqlCount = sqlCount + " and a.DEVICE_NAME like '%" + name + "%' \n";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJyjdJSbtzlh pjj = new PtJyjdJSbtzlh();
				Object []data = (Object[])it.next();
				pjj.setDeviceId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					pjj.setDeviceName(data[1].toString());
				if(data[2] != null)
					pjj.setTestCycle(Long.parseLong(data[2].toString()));
				if(data[3] != null)
					pjj.setFactory(data[3].toString());
				if(data[4] != null)
					pjj.setSizes(data[4].toString());
				if(data[5] != null)
					pjj.setUserRange(data[5].toString());
				if(data[6] != null)
					pjj.setVoltage(data[6].toString());
				if(data[7] != null)
					pjj.setMemo(data[7].toString());
				if(data[8] != null)
					pjj.setEnterpriseCode(data[8].toString());
				
				arrlist.add(pjj);
			 }
		  }
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
	}
	
	
	public PageObject findDeviceTryAll(String name, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.DEVICE_ID,\n"
			+ "       a.DEVICE_NAME,\n"
			+ "       a.TEST_CYCLE,\n"
			+ "       a.FACTORY,\n"
			+ "       a.SIZES,\n"
			+ "       a.USER_RANGE,\n"
			+ "       a.VOLTAGE,\n"
			+ "       a.MEMO,\n"
			+ "       a.ENTERPRISE_CODE, \n"
			+ "       to_char(b.NEXT_DATE, 'yyyy-MM-dd'),\n"
			+ "       b.OPERATE_BY,\n"
			+ "       getworkername(b.OPERATE_BY),\n"
			+ "       to_char(b.OPERATE_DATE, 'yyyy-MM-dd')\n"
			+ "  from PT_JYJD_J_SBTZLH a, PT_JYJD_J_SBYSJHLH b \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n"
			+ "  and a.DEVICE_ID = b.DEVICE_ID(+) \n";
		
		
		String sqlCount = "select count(a.DEVICE_ID)\n"
			+ "  from PT_JYJD_J_SBTZLH a,PT_JYJD_J_SBYSJHLH b \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n"
			+ "  and a.DEVICE_ID = b.DEVICE_ID(+) \n";
		
		
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and a.DEVICE_NAME like '%" + name + "%' \n";
			sqlCount = sqlCount + " and a.DEVICE_NAME like '%" + name + "%' \n";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJyjdJSbtzlh pjj = new PtJyjdJSbtzlh();
				DeviceTryForm info = new DeviceTryForm();
				Object []data = (Object[])it.next();
				pjj.setDeviceId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					pjj.setDeviceName(data[1].toString());
				if(data[2] != null)
					pjj.setTestCycle(Long.parseLong(data[2].toString()));
				if(data[3] != null)
					pjj.setFactory(data[3].toString());
				if(data[4] != null)
					pjj.setSizes(data[4].toString());
				if(data[5] != null)
					pjj.setUserRange(data[5].toString());
				if(data[6] != null)
					pjj.setVoltage(data[6].toString());
				if(data[7] != null)
					pjj.setMemo(data[7].toString());
				if(data[8] != null)
					pjj.setEnterpriseCode(data[8].toString());
				if(data[9] != null)
					info.setNextDate(data[9].toString());
				if(data[10] != null)
					info.setOperateBy(data[10].toString());
				if(data[11] !=null)
					info.setOperateName(data[11].toString());
				if(data[12] !=null)
					info.setOperateDate(data[12].toString());
				info.setPjj(pjj);
				arrlist.add(info);
			 }
		  }
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
		
	}
	
	
	public boolean editDeviceTryInfo(Long id, String operateBy,
			String operateDate, String memo,int testCycle) {
		PtJyjdJSbysjhlh pjjy = new PtJyjdJSbysjhlh();
		String sql = "select a.JYSBYSJH_ID,\n"
			+ "       a.DEVICE_ID,\n"
			+ "       a.NEXT_DATE,\n"
			+ "       a.OPERATE_BY,\n"
			+ "       a.OPERATE_DATE \n"
			+ "  from PT_JYJD_J_SBYSJHLH a \n"
			+ " where a.DEVICE_ID ='" + id + "' \n";
		List list=bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar calendar = new GregorianCalendar();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				Object []data = (Object[])it.next();
				pjjy.setJysbysjhId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					pjjy.setDeviceId(Long.parseLong(data[1].toString()));
				if(data[2] != null)
					try {
						pjjy.setNextDate(sdf.parse(data[2].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				if(data[3] != null)
					pjjy.setOperateBy(data[3].toString());
				if(data[4] != null)
					try {
						pjjy.setOperateDate(sdf.parse(data[4].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
			 }
			pjjy.setOperateBy(operateBy);
			
			try {
				Date date = sdf.parse(operateDate);
				pjjy.setOperateDate(date);
				int year = Integer.parseInt(operateDate.substring(0, 4));
				int month = Integer.parseInt(operateDate.substring(5,7));
				int day = Integer.parseInt(operateDate.substring(8));
				calendar.set(year,month-1,day);
				calendar.add(Calendar.MONTH,testCycle);
				pjjy.setNextDate(calendar.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 修改
			entityManager.merge(pjjy);
		  }
		else
		{
			pjjy.setJysbysjhId(bll.getMaxId("PT_JYJD_J_SBYSJHLH", "JYSBYSJH_ID"));
			pjjy.setDeviceId(id);
			pjjy.setOperateBy(operateBy);
			
			try {
				Date date = sdf.parse(operateDate);
				pjjy.setOperateDate(date);
				int year = Integer.parseInt(operateDate.substring(0, 4));
				int month = Integer.parseInt(operateDate.substring(5,7));
				int day = Integer.parseInt(operateDate.substring(8));
				calendar.set(year,month-1,day);
				calendar.add(Calendar.MONTH,testCycle);
				pjjy.setNextDate(calendar.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 新增
			entityManager.persist(pjjy);
		}
		
		
		PtJyjdJSbtzlh pjj = findById(id);
		pjj.setMemo(memo);
		// 修改
		PtJyjdJSbtzlh result = entityManager.merge(pjj);
		if(result == null)
			return false;
		return true;
	}

}