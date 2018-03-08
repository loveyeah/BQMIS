/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCTimeWork;
import power.ejb.administration.AdCTimeWorkD;
import power.ejb.administration.AdCTimeWorkDFacadeRemote;
import power.ejb.administration.form.RegularWorkArrangeInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
/**
 * 定期工作安排方法体.
 * 
 * @author liugonglei
 */
/**
 * 定期工作信息检索
 */
@Stateless
public class RegularWorkArrangeFacade implements RegularWorkArrangeFacadeRemote {
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    @EJB(beanName = "AdCTimeWorkDFacade")
    AdCTimeWorkDFacadeRemote adcDTimeRemote;
    @PersistenceContext
    private EntityManager entityManager;
    
    @SuppressWarnings({ "deprecation", "unchecked" })
    public PageObject findRegularWorkInfo(String strWorkTypeCode,String strEnterPriseCode, int start,
            int limit) throws Exception{
          LogUtil.log("EJB:定期工作信息检索开始。", Level.INFO, null);
        try
        {
            // 新规PageObject
            PageObject result = new PageObject();
            // 检索数据sql文
            String sql = "SELECT " + "A.ID " 
                    + ",A.WORKITEM_CODE "
                    + ",A.WORKITEM_NAME " 
                    + ",B.SUB_WORKTYPE_CODE "
                    + ",TO_CHAR(A.START_TIME,'YYYY-MM-DD HH24:mi') " 
                    + ",A.IF_WEEKEND "
                    + ",A.WORKRANGE_TYPE "
                    + ",A.WORK_EXPLAIN "
                    + ",B.SUB_WORKTYPE_NAME " 
                    + ",TO_CHAR(A.UPDATE_TIME,'YYYY-MM-DD HH24:mi:SS') "
                    + "FROM  " 
                    + "AD_C_TIMEWORK A "
                    + "LEFT JOIN "
                    + "AD_C_WORKTYPE B " 
                    + "ON "
                    + "A.SUB_WORKTYPE_CODE = B.SUB_WORKTYPE_CODE "
                    + "AND B.ENTERPRISE_CODE = ? "
                    + "WHERE "
                    + "A.WORKTYPE_CODE = ? "
                    + "AND A.ENTERPRISE_CODE = ? "
                    + "AND A.IS_USE = ? ";
            LogUtil.log("EJB:SQL=" +sql, Level.INFO, null);
            // 检索总条数sql文
            String countSql = "SELECT " 
                    + "COUNT(A.ID)  " 
                    + "FROM  " 
                    + "AD_C_TIMEWORK A "
                    + "LEFT JOIN "
                    + "AD_C_WORKTYPE B " 
                    + "ON "
                    + "A.SUB_WORKTYPE_CODE = B.SUB_WORKTYPE_CODE "
                    + "AND B.ENTERPRISE_CODE = ? "
                    + "WHERE "
                    + "A.WORKTYPE_CODE = ? "
                    + "AND A.ENTERPRISE_CODE = ? "
                    + "AND A.IS_USE = ? ";
            // 取得数据
            Object[] params = {strEnterPriseCode,strWorkTypeCode,strEnterPriseCode,"Y"};
            List list = bll.queryByNativeSQL(sql,params,start,limit);
            List<RegularWorkArrangeInfo> arrayList = new ArrayList();
            // 判断是否取到了数据
            if(list != null &&list.size()>0){
                Iterator it = list.iterator();
                // 迭代循环将数据放入ArrayList
                while (it.hasNext()){
                    RegularWorkArrangeInfo REgularWAinfo = new RegularWorkArrangeInfo();
                    Object[] listData = (Object[]) it.next();
                    if(null!=listData[0]){
                        // 设定id
                        REgularWAinfo.setId(listData[0].toString());
                    }
                    if(null != listData[1]){
                        // 设定工作项目code
                        REgularWAinfo.setWorkItemCode(listData[1].toString());
                    }
                    if(null != listData[2]){
                        // 设定工作项目名称
                        REgularWAinfo.setWorkItemName(listData[2].toString());
                    }
                    if(null != listData[3]){
                        // 设定子工作类型code
                        REgularWAinfo.setSubWorkTypeCode(listData[3].toString());
                    }
                    if(null != listData[4]){
                        // 设定取得的开始时间
                        REgularWAinfo.setStartTime(listData[4].toString());
                        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd hh:mm");  
                       
                            // format开始时间
                            try {
								REgularWAinfo.setNewStartTime(dateformat.parse(listData[4].toString()));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								throw e;
							}
                    }
                    if(null != listData[5]){
                        // 设定节假日是否工作
                        REgularWAinfo.setIfWeekEnd(listData[5].toString());
                    }
                    if(null != listData[6]){
                        // 设定工作周期类型
                        REgularWAinfo.setWorkRangeType(listData[6].toString());
                    }
                    if(null != listData[7]){
                        // 设定工作说明
                        REgularWAinfo.setWorkExplain(listData[7].toString());
                    }
                    if(null != listData[8]){
                        // 设定子工作类型名称
                        REgularWAinfo.setSubWorkTypeName(listData[8].toString());
                    }
                    if (null != listData[9]) {
                    	AdCTimeWork entity = findAdcTimeWork(new Long(
								listData[0].toString()));
						REgularWAinfo.setUpdateTime(entity.getUpdateTime().getTime());

					}
                    arrayList.add(REgularWAinfo);
                }
            }
            // 取得总条数
            Object countList = bll.getSingal(countSql,params);
            result.setList(arrayList);
            if (null != countList) {
				result.setTotalCount(new Long(countList.toString()));
			}
            LogUtil.log("EJB:定期工作信息检索正常结束。", Level.INFO, null);
            // 返回数据
            return result;
        }catch (Exception e) {
        	LogUtil.log("EJB:定期工作信息检索异常结束", Level.SEVERE, e);
        	throw new SQLException();
		}
        
    }
    /**
     * 定期工作详细信息检索
     * @throws SQLException 
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public PageObject findCycleParInfo(String workItemCode) throws SQLException{
        LogUtil.log("EJB:定期工作详细信息检索开始。", Level.INFO, null);
        try {
            // 新规Pageobject
            PageObject result = new PageObject();
            // 查询数据sql文
            String sql = "SELECT " 
                + "ID, " 
                + "RANGE_NUMBER, " 
                + "MEMO, "
                + "IS_USE, "
                + "UPDATE_USER, "
                + "UPDATE_TIME, "
                + "WORKITEM_CODE " 
                + "FROM " 
                + "AD_C_TIMEWORKD  " 
                + "WHERE "
                + "WORKITEM_CODE = ? "
                + "AND IS_USE = ? " 
                + "ORDER BY RANGE_NUMBER";
            LogUtil.log("EJB:SQL=" +sql, Level.INFO, null);
            // 设定参数
            Object[] params = {workItemCode,"Y"};
            // 取得数据
            List list = bll.queryByNativeSQL(sql, params,AdCTimeWorkD.class);
            result.setList(list);
            LogUtil.log("EJB:定期工作详细信息检索正常结束。", Level.INFO, null);
            // 返回数据
            return result;
        } catch (Exception er){
            LogUtil.log("EJB:定期工作详细信息检索异常结束。", Level.SEVERE, er);
            throw new SQLException();
        }
    }
    /**
     * 取得定期工作维护信息
     * @throws SQLException 
     */
    public AdCTimeWork findAdcTimeWork(Long lngId) throws SQLException {
        LogUtil.log("EJB:取得定期工作维护信息开始。", Level.INFO, null);
        try {
            // 取得数据
            AdCTimeWork adCTWork = entityManager.find(AdCTimeWork.class, lngId);
            LogUtil.log("EJB:取得定期工作维护信息正常结束。", Level.INFO, null);
            // 返回数据
            return adCTWork;
        } catch (Exception er) {
            LogUtil.log("EJB:取得定期工作维护信息异常结束。", Level.SEVERE, er);
            throw new SQLException();
        }
    }
    /**
     * 取得定期工作明细信息
     * @param strWorkItemCode
     * @return
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public PageObject findAdCTimeWorkD(String strWorkItemCode) throws SQLException {
        LogUtil.log("EJB:取得定期工作明细信息List开始。", Level.INFO, null);
        try {
            // 取得数据sql文
            String sql = "SELECT " 
                    + "ID " 
                    + ",WORKITEM_CODE "
                    + ",RANGE_NUMBER " 
                    + ",MEMO " 
                    + ",IS_USE "
                    + ",UPDATE_USER " 
                    + ",UPDATE_TIME " 
                    + "FROM "
                    + "AD_C_TIMEWORKD " 
                    + "WHERE " 
                    + "WORKITEM_CODE = ? ";
            LogUtil.log("EJB:SQL=" +sql, Level.INFO, null);
            Object[] params = {strWorkItemCode};
            // 取得数据
            List<AdCTimeWorkD> adCTWork = bll.queryByNativeSQL(sql, params,
					AdCTimeWorkD.class);
			PageObject pbj = new PageObject();
            pbj.setList(adCTWork);
            LogUtil.log("EJB:取得定期工作明细信息List正常结束。", Level.INFO, null);
            // 返回数据
            return pbj;
        } catch (Exception er) {
            LogUtil.log("EJB:取得定期工作明细信息List异常结束。", Level.SEVERE, er);
            throw new SQLException();
        }
    }
    /**
     * 取得新的定期工作明细id
     * @throws SQLException 
     */
    public Long getNewAdCTimeWorkDId() throws SQLException {
        LogUtil.log("EJB:取得新的定期工作明细id开始。", Level.INFO, null);
        try {
            // 取得id
            Long lngId = bll.getMaxId("AD_C_TIMEWORKD", "id");
            LogUtil.log("EJB:取得新的定期工作明细id正常结束。", Level.INFO, null);
            // 返回id
            return lngId;
        } catch (Exception er) {
            LogUtil.log("EJB:取得新的定期工作明细id异常结束。", Level.SEVERE, er);
            throw new SQLException();
        }
    }
    /**
     * 取得新的定期工作维护id
     * @throws SQLException 
     */
    public Long getNewAdCTimeWorkId() throws SQLException {
        LogUtil.log("EJB:取得新的定期工作维护id开始。", Level.INFO, null);
        try {
            // 取得id
            Long lngId = bll.getMaxId("AD_C_TIMEWORK", "id");
            LogUtil.log("EJB:取得新的定期工作维护id正常结束。", Level.INFO, null);
            // 返回id
            return lngId;
        } catch (Exception er) {
            LogUtil.log("Get 取得新的定期工作维护id异常结束。",Level.SEVERE, er);
            throw new SQLException();
        }
    }
    /**
     * 更新定期工作明细
     * @throws SQLException
     */
	public void update(AdCTimeWorkD adcTWDEntity, String strUpdateTime)
			throws SQLException,DataFormatException{
		LogUtil.log("EJB:更新定期工作明细开始。", Level.INFO, null);
		try {
			AdCTimeWorkD newAdcTWDEntity = adcDTimeRemote.findById(adcTWDEntity
					.getId());
			DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strNewUpdateTime = dateF.format(
					newAdcTWDEntity.getUpdateTime()).substring(0, 19);
			// 排他处理
			if(!strUpdateTime.equals(strNewUpdateTime)){
				throw new DataFormatException();
			}
			adcTWDEntity.setUpdateTime(new Date());
			// 更新处理
			adcDTimeRemote.update(adcTWDEntity);
			LogUtil.log("EJB:更新定期工作明细正常结束。", Level.INFO, null);
		} catch(DataFormatException e){
			LogUtil.log("EJB:更新定期工作明细异常结束。", Level.SEVERE, e);
			throw e;
		}
		catch (Exception e) {
			LogUtil.log("EJB:更新定期工作明细异常结束。", Level.SEVERE, e);
			throw new SQLException();
		}

	}
}