package power.ejb.manage.stat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity BpCReportNew.
 * 
 * @see power.ejb.manage.plan.BpCReportNew
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCReportNewFacade implements BpCReportNewFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * 保存报表名称信息
	 * 
	 * @param entity
	 */
	public void save(BpCReportNew entity) {
		try {
			if (entity.getReportCode() == null) {
				entity.setReportCode(dll.getMaxId("BP_C__REPORT_NEW",
						"REPORT_CODE"));
			}
			entityManager.persist(entity);
//			dll.exeNativeSQL(createStartData(entity.getReportCode(), entity
//					.getReportType(), entity.getEnterpriseCode()));
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 批量增加录入报表数据
	 * 
	 * @param addList
	 *            报表数据
	 */
	public void save(List<BpCReportNew> addList) {
		if (addList != null && addList.size() > 0) {
			Long reportCode = dll.getMaxId("BP_C__REPORT_NEW", "REPORT_CODE");
			int i = 0;
			for (BpCReportNew entity : addList) {
				entity.setReportCode(reportCode + (i++));
				this.save(entity);
			}
		}
	}

	/**
	 * 删除一条录入报表数据
	 * 
	 * @param entity
	 */
	public void delete(BpCReportNew entity) {
		try {
			entity = entityManager.getReference(BpCReportNew.class, entity
					.getReportCode());
			entityManager.remove(entity);
			String sql = "DELETE FROM BP_C__REPORT_NEW t\n"
					+ " WHERE t.REPORT_CODE = '" + entity.getReportCode()
					+ "'\n" + "   AND t.enterprise_code = '"
					+ entity.getEnterpriseCode() + "'";
			dll.exeNativeSQL(sql);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 批量删除录入报表数据
	 * 
	 * @param ids
	 * @return true 删除成功 false 删除不成功
	 */
	public boolean delete(String ids) {
		try {
			String[] temp1 = ids.split(",");

			for (String i : temp1) {
				BpCReportNew entity = new BpCReportNew();
				entity = this.findById(Long.parseLong(i));
				this.delete(entity);
			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * 批量更新报表数据
	 * 
	 * @param updateList
	 *            报表数据
	 */
	public void update(List<BpCReportNew> updateList) {
		try {
			for (BpCReportNew data : updateList) {
				String sql = "update BP_C__REPORT_NEW t\n"
						+ "set t.REPORT_CODE='" + data.getReportCode() + "',"
						+ " t.REPORT_NAME='" + data.getReportName() + "',"
						+ " t.REPORT_TYPE='" + data.getReportType() + "',"
//						+ " t.TIME_DELAY='" + data.getTimeDelay() + "',"
//						+ " t.TIME_UNIT='" + data.getTimeUnit() + "',"
						+ " t.DISPLAY_NO=" + data.getDisplayNo() + " \n"
						+ "where t.REPORT_CODE='" + data.getReportCode()
						+ "'\n";
				dll.exeNativeSQL(sql);
			}
		} catch (RuntimeException e) {
			throw e;
		}

	}

	/**
	 * 通过报表编码查询对应的报表信息
	 * 
	 * @param id
	 *            报表编码
	 * @return BpCReportNew 报表信息
	 */
	public BpCReportNew findById(Long id) {
		try {
			BpCReportNew instance = entityManager.find(BpCReportNew.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	//add by wpzhu 20100830
	public PageObject findreportName(String reportType,String reportCode,String enterpriseCode,String workerCode,int... rowStartIdxAndCount)//add by wpzhu 20100830
	{

		try {
			PageObject result = new PageObject();
			String sql = null;
			String sqlCount = "SELECT count(*)\n"
					+ "  	FROM BP_C__REPORT_NEW t\n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode+ "'\n";
		if(reportType !=null && !"".equals(reportType)){
			sqlCount +=" and t.report_type ='"+reportType+"'"
				+" and (t.report_code || '_zb' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"+workerCode+"') or\n" 
				+" t.report_code || '_zb' not in (select r.code from JXL_REPORTS_RIGHT r)\n" + 
				" )";
			sqlCount+="and t.report_code   in("+reportCode+")";

		}
//					+ " ORDER BY t.display_no\n";
			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			if (count > 0) {
				 sql = "SELECT t.*\n" + "  	FROM BP_C__REPORT_NEW t\n"
						+ "    WHERE  t.enterprise_code = '" + enterpriseCode+ "'\n";
				if(reportType !=null && !"".equals(reportType)){
					sql +=" and t.report_type ='"+reportType+"'"
						+" and (t.report_code || '_zb' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"+workerCode+"') or\n" 
						+" t.report_code || '_zb' not in (select r.code from JXL_REPORTS_RIGHT r)\n" + 
						" )";
					sql+="and t.report_code  in("+reportCode+")";
				}
				sql+= " ORDER BY t.display_no\n";
				List<BpCReportNew> list = dll.queryByNativeSQL(sql,
						BpCReportNew.class, rowStartIdxAndCount);
				result.setList(list);
				result.setTotalCount(count);
			}
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	
		
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String reportType,String enterpriseCode,String workerCode,int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = null;
			String sqlCount = "SELECT count(*)\n"
					+ "  	FROM BP_C__REPORT_NEW t\n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode+ "'\n";
		if(reportType !=null && !"".equals(reportType)){
			sqlCount +=" and t.report_type ='"+reportType+"'"
				+" and (t.report_code || '_zb' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"+workerCode+"') or\n" 
				+" t.report_code || '_zb' not in (select r.code from JXL_REPORTS_RIGHT r)\n" + 
				" )";
			sqlCount+="and t.report_code  not in(36,37,38,39)";//modify by wpzhu 新维护的两个报表不需要查询到

		}
//					+ " ORDER BY t.display_no\n";
			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			if (count > 0) {
				 sql = "SELECT t.*\n" + "  	FROM BP_C__REPORT_NEW t\n"
						+ "    WHERE  t.enterprise_code = '" + enterpriseCode+ "'\n";
				if(reportType !=null && !"".equals(reportType)){
					sql +=" and t.report_type ='"+reportType+"'"
						+" and (t.report_code || '_zb' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"+workerCode+"') or\n" 
						+" t.report_code || '_zb' not in (select r.code from JXL_REPORTS_RIGHT r)\n" + 
						" )";
					sql+="and t.report_code  not in(36,37,38,39)";//modify by  wpzhu 20100830
				}
				sql+= " ORDER BY t.display_no\n";
				List<BpCReportNew> list = dll.queryByNativeSQL(sql,
						BpCReportNew.class, rowStartIdxAndCount);
				result.setList(list);
				result.setTotalCount(count);
			}
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 判断报表名称是否重复
	 * 
	 * @param reportName
	 *            报表名称
	 * @return Long类型 大于0 表示名称重复 不大于0 表示名称不重复
	 */
	public Long checkReportName(String reportName) {
		String sql = "select count(1) from BP_C__REPORT_NEW t"
				+ " where t.REPORT_NAME= '" + reportName + "'";
		Long count = Long.valueOf(dll.getSingal(sql).toString());
		return count;
	}

	/**
	 * 获取报表时间类型初始值SQL
	 */
	public String createStartData(Long reportCode, String timeType,
			String enterpriseCode) {
		String strSQL = "";
		try {
			switch (Integer.parseInt(timeType)) {
			// 时数据
			case 1:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP";
				for (int i = 1; i < 24; i++) {
					strSQL += " SELECT '" + reportCode + "'," + i + ",'1','"
							+ timeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + reportCode + "',24,'1','" + timeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			// 日数据
			case 3:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP";
				for (int i = 1; i < 31; i++) {
					strSQL += " SELECT '" + reportCode + "'," + i + ",'1','"
							+ timeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + reportCode + "',31,'1','" + timeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			case 4:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP";
				for (int i = 1; i < 12; i++) {
					strSQL += " SELECT '" + reportCode + "'," + i + ",'1','"
							+ timeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + reportCode + "',12,'1','" + timeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			case 5:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP";
				for (int i = 1; i < 4; i++) {
					strSQL += " SELECT '" + reportCode + "'," + i + ",'1','"
							+ timeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + reportCode + "',4,'1','" + timeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			case 6:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP\n" + "VALUES\n"
						+ "  ('" + reportCode + "',\n" + "   1,\n"
						+ "   '1',\n" + "   \n" + "   '" + timeType + "','1','"
						+ enterpriseCode + "')";

				break;
			default:
				break;
			}
		} catch (Exception e) {

		}
		return strSQL;
	}
	/**
	 * 燃料运行班上煤量汇总查询 
	 * @param date 月度
	 * @param enterpriseCode
	 * @return PageObject
	 * add by kzhang 20100906
	 */
	public PageObject finReportListByMon(String date, String enterpriseCode) {
		String Sql="select t. data_value,to_char(t.data_date,'yyyy-mm-dd'),t.zhibie " +
				"from bp_j_stat_fztz t " +
				"where to_char(t.data_date, 'yyyy-mm') = '"+date+"' " +
					//	"and t.enterprise_code='"+enterpriseCode+"' " +
				"and t.item_code in ('smlhyb', 'smlxwb', 'smlswb', 'smlqyb') " +
				//"and t.item_code in ('#2fdl(hyb)','#12fdfh(qyb)','#11fdfh(xwb)','#1fdfh(swb)') " +//测试用
				"order by t.data_date,t.zhibie";
	System.out.println(Sql);
		List list=dll.queryByNativeSQL(Sql);
		int count=list.size();
		//List reportList=new ArrayList();
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM");
		Calendar calendar =Calendar.getInstance();
		Date Mon = new Date();
		  try {
			  Mon = sdfInput.parse(date);
		  } catch (ParseException e) {
		   e.printStackTrace();
		  }
		calendar.clear();
		calendar.setTime(Mon);
		//calendar.set(Mon.getYear(),Mon.getMonth(),1);
		int maxDays=calendar.getActualMaximum(calendar.DAY_OF_MONTH);
		List reportList=new ArrayList();
		Object[] objects=new Object[3];
		Object[] ListItem=new Object[6];
		for (int i = 0; i < maxDays; i++) {
			ListItem=new Object[6];
			ListItem[0]=i+1;
			for (int j = 0; j < count; j++) {
				objects=(Object[])list.get(j);
				if (objects==null) {
					continue;
				}
				String day=objects[1].toString().substring(8);
//				day=day.substring(8);
				if (Integer.parseInt(day)==(i+1)&&objects[2]!=null&&objects[0]!=null) {
					if ("一值".equals(objects[2].toString())) {
						ListItem[1]=objects[0].toString();
					}
					if ("二值".equals(objects[2].toString())) {
						ListItem[2]=objects[0].toString();
					}
					if ("三值".equals(objects[2].toString())) {
						ListItem[3]=objects[0].toString();
					}
					if ("四值".equals(objects[2].toString())) {
						ListItem[4]=objects[0].toString();
					}
					if ("五值".equals(objects[2].toString())) {
						ListItem[5]=objects[0].toString();
					}
				}
			}
			reportList.add(ListItem);
		}
		PageObject pg=new PageObject();
		pg.setList(reportList);
		pg.setTotalCount(Long.parseLong(maxDays+""));
		return pg;
	}

}