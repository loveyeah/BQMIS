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
import power.ejb.productiontec.insulation.form.PjjTryForm;
import power.ejb.run.securityproduction.SpJSpecialoperators;
import power.ejb.run.securityproduction.form.SpJSpecialoperatorsInfo;

/** 
 * @author liuyi 090706
 */
@Stateless
public class PtJyjdJYqybtzlhFacade implements PtJyjdJYqybtzlhFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 增加一条绝缘仪器仪表信息
	 */
	public PtJyjdJYqybtzlh save(PtJyjdJYqybtzlh entity) {
		LogUtil.log("saving PtJyjdJYqybtzlh instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JYJD_J_YQYBTZLH t\n"
				+ "where t.NAMES = '" + entity.getNames() + "'";
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			else
			{
				entity.setRegulatorId(bll.getMaxId("PT_JYJD_J_YQYBTZLH", "REGULATOR_ID"));
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
	public void delete(PtJyjdJYqybtzlh entity) {
		LogUtil.log("deleting PtJyjdJYqybtzlh instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJyjdJYqybtzlh.class, entity
					.getRegulatorId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条绝缘仪器仪表信息
	 */
	public PtJyjdJYqybtzlh update(PtJyjdJYqybtzlh entity) {
		LogUtil.log("updating PtJyjdJYqybtzlh instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JYJD_J_YQYBTZLH t\n"
				+ "where t.NAMES = '" + entity.getNames() + "'"
				+ " and t.REGULATOR_ID !=" + entity.getRegulatorId();
			if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
			{
				return null;
			}
			PtJyjdJYqybtzlh result = entityManager.merge(entity);
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
	public PtJyjdJYqybtzlh findById(Long id) {
		LogUtil.log("finding PtJyjdJYqybtzlh instance with id: " + id,
				Level.INFO, null);
		try {
			PtJyjdJYqybtzlh instance = entityManager.find(
					PtJyjdJYqybtzlh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	

	public void deleteMulti(String ids) {
		String sql = "delete from  "+
		"PT_JYJD_J_YQYBTZLH a\n"
	    + " where a.REGULATOR_ID in (" + ids
	   + ")\n" ;
       bll.exeNativeSQL(sql);
		
	}

	public PageObject findAll(String name, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.REGULATOR_ID,\n"
			+ "       a.NAMES,\n"
			+ "       a.REGULATOR_NO,\n"
			+ "       a.FACTORY,\n"
			+ "       a.SIZES,\n"
			+ "       a.USER_RANGE,\n"
			+ "       a.TEST_CYCLE,\n"
			+ "       a.MEMO,\n"
			+ "       a.ENTERPRISE_CODE \n"
			+ "  from PT_JYJD_J_YQYBTZLH a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		String sqlCount = "select count(a.REGULATOR_ID)\n"
			+ "  from PT_JYJD_J_YQYBTZLH a \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n";
		
		
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and a.NAMES like '%" + name + "%' \n";
			sqlCount = sqlCount + " and a.NAMES like '%" + name + "%' \n";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJyjdJYqybtzlh pjj = new PtJyjdJYqybtzlh();
//				SpJSpecialoperatorsInfo info = new SpJSpecialoperatorsInfo();
				Object []data = (Object[])it.next();
				pjj.setRegulatorId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					pjj.setNames(data[1].toString());
				if(data[2] != null)
					pjj.setRegulatorNo(data[2].toString());
				if(data[3] != null)
					pjj.setFactory(data[3].toString());
				if(data[4] != null)
					pjj.setSizes(data[4].toString());
				if(data[5] != null)
					pjj.setUserRange(data[5].toString());
				if(data[6] != null)
					pjj.setTestCycle(Long.parseLong((data[6].toString())));
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

	public PageObject findPjjTryAll(String name, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.REGULATOR_ID,\n"
			+ "       a.NAMES,\n"
			+ "       a.REGULATOR_NO,\n"
			+ "       a.FACTORY,\n"
			+ "       a.SIZES,\n"
			+ "       a.USER_RANGE,\n"
			+ "       a.TEST_CYCLE,\n"
			+ "       a.MEMO,\n"
			+ "       a.ENTERPRISE_CODE, \n"
			+ "       to_char(b.NEXT_DATE, 'yyyy-MM-dd'),\n"
			+ "       b.OPERATE_BY,\n"
			+ "       getworkername(b.OPERATE_BY),\n"
			+ "       to_char(b.OPERATE_DATE, 'yyyy-MM-dd')\n"
			+ "  from PT_JYJD_J_YQYBTZLH a, PT_JYJD_J_YQYBYSJH b \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n"
			+ "  and a.REGULATOR_ID = b.regulator_id(+) \n";
		
		
		String sqlCount = "select count(a.REGULATOR_ID)\n"
			+ "  from PT_JYJD_J_YQYBTZLH a,PT_JYJD_J_YQYBYSJH b \n"
			+ " where a.enterprise_code ='" + enterpriseCode + "' \n"
			+ "  and a.REGULATOR_ID = b.regulator_id(+) \n";
		
		
		if(name !=null && (!name.equals("")))
		{
			sql = sql + " and a.NAMES like '%" + name + "%' \n";
			sqlCount = sqlCount + " and a.NAMES like '%" + name + "%' \n";
		}
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				PtJyjdJYqybtzlh pjj = new PtJyjdJYqybtzlh();
				PjjTryForm info = new PjjTryForm();
				Object []data = (Object[])it.next();
				pjj.setRegulatorId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					pjj.setNames(data[1].toString());
				if(data[2] != null)
					pjj.setRegulatorNo(data[2].toString());
				if(data[3] != null)
					pjj.setFactory(data[3].toString());
				if(data[4] != null)
					pjj.setSizes(data[4].toString());
				if(data[5] != null)
					pjj.setUserRange(data[5].toString());
				if(data[6] != null)
					pjj.setTestCycle(Long.parseLong((data[6].toString())));
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

	public boolean editPjjTryInfo(Long id, String operateBy,
			String operateDate, String memo,int testCycle) {
		PtJyjdJYqybysjh pjjy = new PtJyjdJYqybysjh();
		String sql = "select a.YQYBYSJH_ID,\n"
			+ "       a.REGULATOR_ID,\n"
			+ "       a.NEXT_DATE,\n"
			+ "       a.OPERATE_BY,\n"
			+ "       a.OPERATE_DATE \n"
			+ "  from PT_JYJD_J_YQYBYSJH a \n"
			+ " where a.REGULATOR_ID ='" + id + "' \n";
		List list=bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar calendar = new GregorianCalendar();
		if (list != null && list.size() > 0) {
			while(it.hasNext()){
				Object []data = (Object[])it.next();
				pjjy.setYqybysjhId(Long.parseLong(data[0].toString()));
				if(data[1] != null)
					pjjy.setRegulatorId(Long.parseLong(data[1].toString()));
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
			pjjy.setYqybysjhId(bll.getMaxId("PT_JYJD_J_YQYBYSJH", "YQYBYSJH_ID"));
			pjjy.setRegulatorId(id);
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
		
		
		PtJyjdJYqybtzlh pjj = findById(id);
		pjj.setMemo(memo);
		// 修改
		PtJyjdJYqybtzlh result = entityManager.merge(pjj);
		if(result == null)
			return false;
		return true;
	}

}