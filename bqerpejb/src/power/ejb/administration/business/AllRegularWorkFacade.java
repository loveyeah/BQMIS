/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.AllRegularWorkInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class AllRegularWorkFacade implements AllRegularWorkFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**是否使用*/
	private String IS_USE_Y = "Y";
	/**
	 * 全部定期工作
	 * 	
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAllRegularWorkList() {
		LogUtil.log("EJB:全部定期工作查询正常开始", Level.INFO, null);
		try {
			PageObject pobj = new PageObject();
			// 查询sql
			String sql = "SELECT "				  
			           + " A.WORKITEM_CODE,  "
					   + " A.WORKTYPE_CODE,  "
					   + " A.SUB_WORKTYPE_CODE,  "
					   + " A.WORKRANGE_TYPE,  "
					   + " B.RANGE_NUMBER,  "
					   + " A.START_TIME,  "
					   + " A.WORK_EXPLAIN  "
					   + " FROM  "
					   + " AD_C_TIMEWORK A, "
					   + " AD_C_TIMEWORKD B  "
					   + " WHERE  "
					   + " A.IS_USE = ? AND "
					   + " B.IS_USE = ? AND "					
					   + " A.WORKITEM_CODE = B.WORKITEM_CODE AND "
					   + " A.START_TIME <= SYSDATE  ";
			
			// 查询参数数量
			int paramsCnt = 2;			
			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = IS_USE_Y;
			params[i++] = IS_USE_Y;	
			
			List<AllRegularWorkInfo> list = bll.queryByNativeSQL(sql, params);
			String sqlCount = "SELECT count(A.ID) "
				            + " FROM "
					        + " AD_C_TIMEWORK A, "
					        + " AD_C_TIMEWORKD B "
					        + " WHERE "
					        + " A.IS_USE = 'Y' AND "
					        + "B.IS_USE = 'Y' AND "
					        + "A.WORKITEM_CODE = B.WORKITEM_CODE AND "
					        + "A.START_TIME <= SYSDATE  ";
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount, params).toString());

			List<AllRegularWorkInfo> arrlist = new ArrayList<AllRegularWorkInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				AllRegularWorkInfo materialInfo = new AllRegularWorkInfo();
				Object[] data = (Object[]) it.next();
				// 工作项目编码
				if (null != data[0])
					materialInfo.setWorkItemCode(data[0].toString());
				// 工作类别编码
				if (null != data[1])
					materialInfo.setWorkTypeCode(data[1].toString());
				// 子类别编码
				if (null != data[2])
					materialInfo.setSubWorkTypeCode(data[2].toString());
				// 周期类别
				if (null != data[3]) {
					materialInfo.setWorkRangeType(data[3].toString());
				}
				// 周期号
				if (null != data[4])
					materialInfo.setRangeNumber(data[4].toString());
				// 开始时间
				if (null != data[5])
					materialInfo.setStartTime(data[5].toString());
				// 工作说明
				if (null != data[6])
					materialInfo.setWorkExplain(data[6].toString());				
				arrlist.add(materialInfo);
			}
			pobj.setList(arrlist);
			pobj.setTotalCount(totalCount);	
			LogUtil.log("EJB:全部定期工作查询正常结束", Level.INFO, null);
			return pobj;			
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}

}
