/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 基础表维护Remote
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
@Stateless
public class BaseTableInfoFacade implements BaseTableInfoFacadeRemote {
	@PersistenceContext
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 是使用("Y") */
	private static String STR_IS_USE_Y = "Y";
	/** 表名1："加班类别表" */
	private static String STR_TABLENAME_1 = "加班类别表";
	/** 表名2："运行班类别表" */
	private static String STR_TABLENAME_2 = "运行班类别表";

	/**
	 * 获得基础表维护信息
	 * 
	 * @param tableName
	 *            基础表名称
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 * @throws SQLException
	 */
	@SuppressWarnings( { "unchecked", "unchecked" })
	public PageObject getBaseTableRecordList(String tableName,
			String enterpriseCode, int... rowStartIdxAndCount)
			throws SQLException {
		LogUtil.log("EJB:基础表维护(" + tableName + ")初始化开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		try {
			// 查询sql
			StringBuffer sql = new StringBuffer();
			// 查询总数
			StringBuffer sqlCount = new StringBuffer();
			// 查询表
			String fromTable = "";
			// 排序规则
			String strOrderBy = "";
			sql.append("SELECT ");
			sqlCount.append("SELECT ");
			if (STR_TABLENAME_1.equals(tableName)) {
				sql.append(" A.OVERTIME_TYPE_ID, ");
				sql.append(" A.OVERTIME_TYPE, ");
				sql.append(" A.IF_OVERTIME_FEE, ");
				sql.append(" A.OVERTIME_MARK ");
				sqlCount.append(" count(distinct A.OVERTIME_TYPE_ID) ");
				fromTable = " FROM HR_C_OVERTIME A ";
				strOrderBy = " ORDER BY A.OVERTIME_TYPE_ID ";
			} else if (STR_TABLENAME_2.equals(tableName)) {
				sql.append(" A.WORK_SHIFT_ID, ");
				sql.append(" A.WORK_SHIFT, ");
				sql.append(" A.WORK_SHIT_FEE, ");
				sql.append(" A.WORK_SHIFT_MARK ");
				sqlCount.append(" count(distinct A.WORK_SHIFT_ID) ");
				fromTable = " FROM HR_C_WORKSHIFT A ";
				strOrderBy = " ORDER BY A.WORK_SHIFT_ID ";
			}

			// where文
			StringBuffer sqlWhere = new StringBuffer();
			sqlWhere.append(" WHERE ");
			sqlWhere.append(" A.ENTERPRISE_CODE = ? ");
			sqlWhere.append(" AND A.IS_USE = ? ");
			// 查询参数数组
			Object[] params = new Object[2];
			int i = 0;
			params[i++] = enterpriseCode;
			params[i++] = STR_IS_USE_Y;
			sqlWhere.append(strOrderBy);
			sql.append(fromTable);
			sql.append(sqlWhere.toString());
			// 记录数
			sqlCount.append(fromTable);
			sqlCount.append(sqlWhere.toString());

			LogUtil.log("EJB: 基础表维护(" + tableName + ")初始化查询SQL ="
					+ sql.toString(), Level.INFO, null);
			List list = bll.queryByNativeSQL(sql.toString(), params,
					rowStartIdxAndCount);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount.toString(),
					params).toString());
			List<BaseTableInfo> arrList = new ArrayList<BaseTableInfo>();
			if (list != null) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					BaseTableInfo baseTableBeen = new BaseTableInfo();
					Object[] data = (Object[]) it.next();
					// id
					if (data[0] != null) {
						baseTableBeen.setRecordId(data[0].toString());
					}
					if (STR_TABLENAME_1.equals(tableName)) {
						// 加班类别
						if (data[1] != null) {
							baseTableBeen.setRecordOverTimeName(data[1]
									.toString());
						}
						// 是否发放费用
						if (data[2] != null) {
							baseTableBeen.setRecordIfOverTimeFee(data[2]
									.toString());
						}
					} else if (STR_TABLENAME_2.equals(tableName)) {
						// 运行班类别
						if (data[1] != null) {
							baseTableBeen
									.setRecordShiftName(data[1].toString());
						}
						// 津贴标准
						if (data[2] != null) {
							baseTableBeen.setRecordShiftFee(data[2].toString());
						}
					}
					// 考勤标志
					if (data[3] != null) {
						baseTableBeen.setRecordMark(data[3].toString());
					}
					arrList.add(baseTableBeen);
				}
			}
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:基础表维护(" + tableName + ")初始化结束", Level.INFO, null);
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:基础表维护(" + tableName + ")初始化失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
}
