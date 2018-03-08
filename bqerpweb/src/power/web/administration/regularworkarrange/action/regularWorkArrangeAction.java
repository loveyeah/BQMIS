/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.regularworkarrange.action;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.CodeConstants;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCTimeWork;
import power.ejb.administration.AdCTimeWorkD;
import power.ejb.administration.AdCTimeWorkDFacadeRemote;
import power.ejb.administration.AdCTimeWorkFacadeRemote;
import power.ejb.administration.business.RegularWorkArrangeFacadeRemote;
import power.ejb.administration.comm.ADCommonFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.administration.comm.ComAdCRight;
import power.ejb.administration.form.RegularWorkArrangeInfo;
import power.ejb.hr.LogUtil;

/**
 * 定期工作安排
 * 
 * @author liugonglei
 */

public class regularWorkArrangeAction extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /** 定义接口 */
    /**
     * 定期工作安排接口
     */
    RegularWorkArrangeFacadeRemote remote;
    
    /**
     * 共通接口
     */
    ADCommonFacadeRemote adRemote;
    CodeCommonFacadeRemote codeCommonRemote;
    /**
     * 定期工作维护接口
     */
    AdCTimeWorkFacadeRemote adcTimeRemote;
    /**
     * 定期工作明细接口
     */
    AdCTimeWorkDFacadeRemote adcDTimeRemote;
    /**
     * entity
     */
    RegularWorkArrangeInfo entity;
    String strRec;
    /**需要更新和插入的定期工作明细**/
    public String[] records;
    /**需要删除的定期工作明细**/
    public String[] deletePars;
    public Date[] deleteTime;
    /**开始页**/
    public Long start;
    /**页容量**/
    public Long limit;
    /**周末是否工作*/
    public String ifWeekEnd;
    /**类别*/
    public String subWorktypeCode;
    /**工作是否进行*/
    public String ifFlag;
    /**周期类别*/
    public String workRangeType;
    /**定期工作维护id*/
    public String strId;
    /**项目code*/
    public String strWorkItemCode;
    /**更新时间*/
    public String strUpdateTime;
    /**开始时间*/
    public String strStartTime;
    /**标志常量*/
    private String FLAG1= "{success:true,msg:'1'}";

    /**
     * 构造函数
     */
    public regularWorkArrangeAction() {
        // TODO Auto-generated constructor stub
        remote = (RegularWorkArrangeFacadeRemote) factory
                .getFacadeRemote("RegularWorkArrangeFacade");
        adRemote = (ADCommonFacadeRemote) factory
                .getFacadeRemote("ADCommonFacade");
        adcTimeRemote = (AdCTimeWorkFacadeRemote) factory
                .getFacadeRemote("AdCTimeWorkFacade");
        adcDTimeRemote = (AdCTimeWorkDFacadeRemote) factory
                .getFacadeRemote("AdCTimeWorkDFacade");
        codeCommonRemote = (CodeCommonFacadeRemote) factory
                .getFacadeRemote("CodeCommonFacade");
    }
    /**
     * 定期工作信息检索
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void findRegularWorkInfo() throws Exception{
        LogUtil.log("Action：定期工作信息检索开始", Level.INFO, null);
       try{
        // 开始页
    	   int intStart = Integer.parseInt(start.toString());
		// 页容量
    	   int intLimit = Integer.parseInt(limit.toString());
        //员工id
        String strUserID = employee.getWorkerCode();
        //取得工作权限
        PageObject workKind = null;
		try {
			workKind = adRemote.getUserRight(strUserID,employee.getEnterpriseCode());
		} catch (SQLException e1) {
			write(Constants.SQL_FAILURE);
		}
        List<ComAdCRight> list = workKind.getList();
        String strWorkTypeCode = "";
        if (list.size() > 0) {
            ComAdCRight adRight = (ComAdCRight) list.get(0);
            //工作类别code
            strWorkTypeCode = adRight.getWorktypeCode();
        }
        //查询数据
        PageObject obj = null;
        String strEnterPriseCode = employee.getEnterpriseCode();
        try {
            obj = remote.findRegularWorkInfo(strWorkTypeCode,strEnterPriseCode, intStart,
                    intLimit);
        } catch (SQLException e) {
            write (Constants.SQL_FAILURE);
            LogUtil.log("Action：定期工作信息检索异常结束", Level.INFO, null);
        }
        LogUtil.log("Action：定期工作信息检索正常结束", Level.INFO, null);
        //返回页面数据
        write(JSONUtil.serialize(obj));
       }catch(JSONException er){
    	   write(Constants.DATA_FAILURE);
       }
    }
    /**
     * 定期工作详细信息检索
     * @throws JSONException
     */
    public void findCycleParInfo() throws JSONException {
        LogUtil.log("Action：定期工作详细信息检索开始", Level.INFO, null);
        //取得数据
        PageObject obj = null;
		try {
			obj = remote.findCycleParInfo(strWorkItemCode);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action：定期工作详细信息检索异常结束", Level.INFO, null);
		}
        LogUtil.log("Action：定期工作详细信息检索正常结束", Level.INFO, null);
        //返回页面数据
        write(JSONUtil.serialize(obj));
    }
    /**
     * 删除定期工作信息
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void delRegularWork() {
        LogUtil.log("Action：删除定期工作信息数据开始", Level.INFO, null);
        try {
            Map map = (Map) JSONUtil.deserialize(strRec);
            //取得定期工作维护id
            Long lngId = Long.parseLong(map.get("id").toString());
            //取得工作项目code
            String strWorkItemCode = getStringToString(map.get("workItemCode"));
            //取得上次更新时间
            Long lngUpdateTime = (Long)map.get("updateTime");
            //回滚事务取得
            UserTransaction tx = (UserTransaction) new InitialContext()
            .lookup("java:comp/UserTransaction");
            try{
                //回滚事物开始
                tx.begin();
                //删除定期工作明细数据
                delAdcTimeWorkD(strWorkItemCode);
                //删除定期工作维护数据
                delAdcTimeWork(lngId, lngUpdateTime);
                //回滚事物commit
                tx.commit();
            }catch(SQLException e) {
                //回滚事物rollback
                tx.rollback();
                LogUtil.log("Action：删除定期工作信息数据异常终了", Level.SEVERE, e);
                write(Constants.SQL_FAILURE);
            } catch (DataFormatException e){
            	//回滚事物rollback
                tx.rollback();
                LogUtil.log("Action：删除定期工作信息数据异常终了", Level.SEVERE, e);
                write(Constants.DATA_USING);
            } catch (JSONException e){
            	//回滚事物rollback
                tx.rollback();
                LogUtil.log("Action：删除定期工作信息数据异常终了", Level.SEVERE, e);
                write(Constants.DATA_FAILURE);
            }
            LogUtil.log("Action：删除定期工作信息数据正常终了", Level.INFO, null);
            //返回页面数据
            write(FLAG1);
        } catch (Exception e){
            LogUtil.log("Action：删除定期工作信息数据异常终了", Level.SEVERE, e);
            write(Constants.DATA_FAILURE);
        }
    }
    /**
     * 删除定期工作明细数据
     * @param strWorkItemCode
     * @throws JSONException
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
	public void delAdcTimeWorkD(String strWorkItemCode)throws JSONException, SQLException{
        LogUtil.log("Action：删除定期工作明细数据开始", Level.INFO, null);
        try {
            //取得定期工作明细数据
        	PageObject pbj = remote.findAdCTimeWorkD(strWorkItemCode);
            List<AdCTimeWorkD> list = pbj.getList();
            //循环逻辑删除数据库中的数据
            if(null != list && list.size()>0){
                for(int i = 0;i<list.size();i++){
                    AdCTimeWorkD entity = list.get(i);
                    entity.setIsUse(Constants.IS_USE_N);
                    entity.setUpdateTime(new Date());
                    entity.setUpdateUser(employee.getWorkerCode());
                    //数据逻辑删除
						adcDTimeRemote.update(entity);
                }
            }
            LogUtil.log("Action：删除定期工作明细数据正常终了", Level.INFO, null);
        }catch (RuntimeException e){
            LogUtil.log("Action：删除定期工作明细数据异常终了", Level.SEVERE, e);
            throw e;
        } catch (SQLException e) {
        	   LogUtil.log("Action：删除定期工作明细数据异常终了", Level.SEVERE, e);
			throw e;
		}
    }
    /**
     * 删除定期工作维护数据
     * @param lngId
     * @param lngUpdateTime
     * @throws SQLException 
     * @throws DataFormatException 
     * @throws Exception 
     */
    public void delAdcTimeWork(Long lngId, Long lngUpdateTime)
            throws JSONException, SQLException, DataFormatException {
        LogUtil.log("Action：删除一条定期工作维护数据开始", Level.INFO, null);
        try {
            //取得定期工作维护
            AdCTimeWork entity = remote.findAdcTimeWork(lngId);
            //设定更新者code
            entity.setUpdateUser(employee.getWorkerCode());
                //是定是否使用为N
                entity.setIsUse(CodeConstants.USE_FLG_N);
                // 数据逻辑删除
				adcTimeRemote.update(entity, lngUpdateTime);
            LogUtil.log("Action：删除一条定期工作维护数据正常结束", Level.INFO, null);
        } catch (RuntimeException e) {
            LogUtil.log("Action：删除一条定期工作维护数据异常结束", Level.SEVERE, null);
            throw e;
        } catch (SQLException e) {
        	LogUtil.log("Action：删除一条定期工作维护数据异常结束", Level.SEVERE, null);
			throw e;
		} catch (DataFormatException e) {
			LogUtil.log("Action：删除一条定期工作维护数据异常结束", Level.SEVERE, null);
			throw e;
		}
    }
    /**
     * 保存数据
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public void saveWorkParrange() throws JSONException {
        LogUtil.log("Action：保存定期工作明细数据开始", Level.INFO, null);
        try {
            // 回滚事务取得
            UserTransaction tx = (UserTransaction) new InitialContext()
                    .lookup("java:comp/UserTransaction");
            try {
                // 回滚事务开始
                tx.begin();
                //判断是更新还是插入                
                if(null != strId && strId != ""){
                    // 更新定期工作维护表
                    updateAdcTimeWork();
                } else {
                    // 插入定期工作维护表
                    addAdcTimeWork();
                }
                if ("" != deletePars[0] && deletePars.length > 0) {
                    // 循环删除定期工作明细数据
                    for (int i = 0; i < deletePars.length; i++) {
                        // 取得定期工作明细id
                        String strId = deletePars[i];
                        String strUpdateTime = null;
                        DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strUpdate = null;
                        if(deleteTime[i] != null){
                        strUpdate = dateF.format(deleteTime[i]);
                        strUpdateTime = strUpdate.substring(0,19);
                        }
                        // 删除定期工作明细数据
                        deleteAdcTimeWorkD(new Long(strId),strUpdateTime);
                    }
                }
                int intFlag = 0;
                Long lngId = new Long(0);
                if ("" != records[0] && records.length > 0) {
                    // 循环增加或更新定期工作安排数据
                    for (int i = 0; i < records.length; i++) {
                        Map map = (Map) JSONUtil.deserialize(records[i]);
                        if (map.get("id") != null) {
                            // 更新定期工作安排数据
                            updateAdcTimeWorkD(map);
                        } else {
                            
                            // 取得定期工作明细id
                            if (intFlag == 0){
                                lngId = remote.getNewAdCTimeWorkDId();
                                intFlag++;
                            } else {
                                lngId = lngId  + new Long(1);
                            }
                            // 增加定期工作安排数据
                            addAdCTimeWorkD(map,lngId);
                            
                        }
                    }
                }
                // 回滚事务commit
                tx.commit();
                // 返回画面数据
                write(FLAG1);
            } catch (SQLException e) {
                // 回滚事务rollback
                tx.rollback();
                LogUtil.log("Action：保存定期工作安排数据异常结束", Level.SEVERE, e);
                write(Constants.SQL_FAILURE);
            } catch (DataFormatException e){
            	tx.rollback();
            	LogUtil.log("Action：保存定期工作安排数据异常结束", Level.SEVERE, e);
            	write(Constants.DATA_USING);
            } catch (ParseException e){
            	tx.rollback();
            	LogUtil.log("Action：保存定期工作安排数据异常结束", Level.SEVERE, e);
            	write(Constants.DATA_FAILURE);
            } catch(JSONException e){
            	tx.rollback();
            	LogUtil.log("Action：保存定期工作安排数据异常结束", Level.SEVERE, e);
            	write(Constants.DATA_FAILURE);
            }
            LogUtil.log("Action：保存定期工作安排数据正常结束", Level.INFO, null);
        } catch (Exception e) {
            LogUtil.log("Action：保存定期工作安排数据异常结束", Level.SEVERE, e);
            write(Constants.DATA_FAILURE);
        }
    }
    /**
     * 保存时插入定期工作维护数据
     * @throws SQLException 
     * @throws ParseException 
     */
    @SuppressWarnings("unchecked")
	public void addAdcTimeWork() throws SQLException, ParseException{
        LogUtil.log("Action：插入定期工作维护数据开始", Level.INFO, null);
        try {
            // 新规entity
            AdCTimeWork adcTWEntity = new AdCTimeWork();
            // 取得并设定id
            adcTWEntity.setId(remote.getNewAdCTimeWorkId());
            // 设定节假日是否工作
            adcTWEntity.setIfWeekend(ifWeekEnd);
            // 设定工作说明
            adcTWEntity.setWorkExplain(this.entity.getWorkExplain());
            // 设定是否使用
            adcTWEntity.setIsUse(CodeConstants.USE_FLG_Y);
            adcTWEntity.setEnterPriseCode(employee.getEnterpriseCode());
            // 设定开始时间
            DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if(null != strStartTime && !"".equals(strStartTime.trim())){
				adcTWEntity.setStartTime(dateF.parse(strStartTime));
            } else {
            	adcTWEntity.setStartTime(null);
            }
            // 设定工作项目名称
            adcTWEntity.setWorkitemName(this.entity.getWorkItemName());
            // 取得工作项目code
            String strWorkitemCode = codeCommonRemote.getWorkItemCode();
            // 设定工作项目code
            adcTWEntity.setWorkitemCode(strWorkitemCode);
          //员工id
            String strUserID = employee.getWorkerCode();
            //取得工作权限
            PageObject workKind = adRemote.getUserRight(strUserID,employee.getEnterpriseCode());
            List<ComAdCRight> list = workKind.getList();
            String strWorkTypeCode = "";
            if (list.size() > 0) {
                ComAdCRight adRight = (ComAdCRight) list.get(0);
                //工作类别code
                strWorkTypeCode = adRight.getWorktypeCode();
            }
            // 设定工作类型
            adcTWEntity.setWorktypeCode(strWorkTypeCode);
            // 设定子工作类型code
            adcTWEntity.setSubWorktypeCode(subWorktypeCode);
            // 设定工作周期类型
            adcTWEntity.setWorkrangeType(workRangeType);
            // 设定更新时间
            adcTWEntity.setUpdateTime(new Date());
            // 设定更新者code
            adcTWEntity.setUpdateUser(employee.getWorkerCode());
            // 插入数据
				adcTimeRemote.save(adcTWEntity);
            LogUtil.log("Action：插入定期工作维护数据正常终了", Level.INFO, null);
        }catch (RuntimeException e){
            LogUtil.log("Action：插入定期工作维护数据异常终了", Level.SEVERE, e);
            throw e;
        }catch (SQLException e) {
        	LogUtil.log("Action：插入定期工作维护数据异常终了", Level.SEVERE, e);
			throw e;
		} catch (ParseException e) {
			LogUtil.log("Action：插入定期工作维护数据异常终了", Level.SEVERE, e);
			throw e;
		}
    }
    /**
     * 保存时更新定期维护明细
     * @throws JSONException
     * @throws SQLException 
     * @throws ParseException 
     * @throws DataFormatException 
     */
    public void updateAdcTimeWork() throws JSONException, SQLException, ParseException, DataFormatException{
        LogUtil.log("Action：更新定期工作维护数据开始", Level.INFO, null);
        try {
            // 取得entity
            AdCTimeWork adcTWEntity = adcTimeRemote.findById(new Long(strId));
            // 设定工作项目名称
            adcTWEntity.setWorkitemName(this.entity.getWorkItemName());
            // 设定子工作类型code
            adcTWEntity.setSubWorktypeCode(subWorktypeCode);
            // 设定工作周期类型
            adcTWEntity.setWorkrangeType(workRangeType);
            // 设定开始时间
            DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (null != strStartTime && !"".equals(strStartTime.trim())) {
				adcTWEntity.setStartTime(dateF.parse(strStartTime));
			} else {
				adcTWEntity.setStartTime(null);
			}
            // 设定工作说明
            adcTWEntity.setWorkExplain(this.entity.getWorkExplain());
            // 设定节假日是否工作
            adcTWEntity.setIfWeekend(ifWeekEnd);
            // 设定更新时间
            adcTWEntity.setUpdateTime(new Date());
            // 设定更新者code
            adcTWEntity.setUpdateUser(employee.getWorkerCode());
            // 更新数据
            	Long lngUpdateTime = new Long(0);
            	if(null != strUpdateTime){
            		lngUpdateTime = new Long(strUpdateTime);
            	}
				adcTimeRemote.update(adcTWEntity,new Long(lngUpdateTime));
			
            LogUtil.log("Action：更新定期工作维护数据正常终了", Level.INFO, null);
        } catch (RuntimeException e){
            LogUtil.log("Action：更新定期工作维护数据异常终了", Level.SEVERE, e);
            throw e;
        }catch (ParseException e) {
        	LogUtil.log("Action：更新定期工作维护数据异常终了", Level.SEVERE, e);
        	throw e;
		}catch (SQLException e) {
			 LogUtil.log("Action：更新定期工作维护数据异常终了", Level.SEVERE, e);
			throw e;
		} catch (DataFormatException e) {
			 LogUtil.log("Action：更新定期工作维护数据异常终了", Level.SEVERE, e);
			throw e;
		}
    }
    /**
     * 保存时删除定期工作明细数据
     * @param lngId
     * @throws JSONException
     * @throws SQLException 
     * @throws DataFormatException 
     */
    public void deleteAdcTimeWorkD(Long lngId, String strUpdateTime)throws JSONException, SQLException, DataFormatException{
        LogUtil.log("Action：删除定期工作明细数据开始", Level.INFO, null);
        try {
            // 取得entity
            AdCTimeWorkD adcTWDEntity = adcDTimeRemote.findById(lngId);
            // 设定是否使用
            adcTWDEntity.setIsUse(CodeConstants.USE_FLG_N);
            // 设定更新者code
            adcTWDEntity.setUpdateUser(employee.getWorkerCode());
            // 删除数据
            remote.update(adcTWDEntity, strUpdateTime);
            LogUtil.log("Action：删除定期工作明细数据正常终了", Level.INFO, null);
        }catch (DataFormatException e){
        	LogUtil.log("Action：删除定期工作明细数据异常终了", Level.SEVERE, e);
            throw e;
        }
        catch (RuntimeException e){
            LogUtil.log("Action：删除定期工作明细数据异常终了", Level.SEVERE, e);
            throw e;
        }  catch (SQLException e) {
        	 LogUtil.log("Action：删除定期工作明细数据异常终了", Level.SEVERE, e);
			throw e;
		}
    }
    /**
     * 更新定期工作明细
     * @param map
     * @throws JSONException
     * @throws SQLException 
     * @throws DataFormatException 
     */
    @SuppressWarnings("unchecked")
    public void updateAdcTimeWorkD(Map map)throws JSONException, SQLException, DataFormatException{
        LogUtil.log("Action：更新定期工作明细数据开始", Level.INFO, null);
        try {
			// 取得entity
			AdCTimeWorkD adcTWDEntity = adcDTimeRemote.findById(new Long(map
					.get("id").toString()));
			// 设定工作周期号
			if (null != map.get("rangeNumber")) {
				adcTWDEntity.setRangeNumber(new Long(map.get("rangeNumber")
						.toString()));
			}
			// 设定备注
			if (null != map.get("memo")) {
				adcTWDEntity.setMemo(map.get("memo").toString());
			}
			String strUpdate = map.get("updateTime").toString();
			String strUpdateTime = strUpdate.substring(0, 10) + " "
					+ strUpdate.substring(11, 19);
			// 设定更新者
			adcTWDEntity.setUpdateUser(employee.getWorkerCode());
			// 更新数据
			remote.update(adcTWDEntity, strUpdateTime);
			LogUtil.log("Action：更新定期工作明细数据终了", Level.INFO, null);
		} catch (DataFormatException e){
			LogUtil.log("Action：更新定期工作明细数据异常终了", Level.SEVERE, e);
            throw e;
		}
		catch (RuntimeException e){
            LogUtil.log("Action：更新定期工作明细数据异常终了", Level.SEVERE, e);
            throw e;
        }catch (SQLException e) {
        	LogUtil.log("Action：更新定期工作明细数据异常终了", Level.SEVERE, e);
			throw e;
		}
    }
    /**
     * 保存时插入定期工作明细
     * @param map
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public void addAdCTimeWorkD(Map map,long lngId) throws SQLException{
        LogUtil.log("Action：插入定期工作明细数据开始", Level.INFO, null);
        try{
            // 新规entity
            AdCTimeWorkD adcTWDEntity = new AdCTimeWorkD();
                // 设定id
                adcTWDEntity.setId(lngId);
            // 设定是否使用
            adcTWDEntity.setIsUse(CodeConstants.USE_FLG_Y);
            // 设定周期号
            if (null != map.get("rangeNumber") ||"".equals(map.get("rangeNumber")) ) {
				adcTWDEntity.setRangeNumber(new Long(map.get("rangeNumber")
						.toString()));
			}
            // 设定备注
            if (null != map.get("memo")) {
				adcTWDEntity.setMemo(map.get("memo").toString());
			}
            // 根据工作项目code判断是新规项目还是更新
            if(null != strWorkItemCode){
                // 设定工作项目code（更新）
                adcTWDEntity.setWorkitemCode(strWorkItemCode);
            } else {
                // 取得工作项目code
                String strWorkitemCode = codeCommonRemote.getWorkItemCode();
                // 设定工作项目code（新规）
                adcTWDEntity.setWorkitemCode(strWorkitemCode);
            }
            // 设定更新时间
            adcTWDEntity.setUpdateTime(new Date());
            // 设定更新者code
            adcTWDEntity.setUpdateUser(employee.getWorkerCode());
            // 插入数据
				adcDTimeRemote.save(adcTWDEntity);
            LogUtil.log("Action：插入定期工作明细数据结束", Level.INFO, null);
        } catch (RuntimeException e){
            LogUtil.log("Action：插入定期工作明细数据异常终了", Level.SEVERE, e);
            throw e;
        }catch (SQLException e) {
        	LogUtil.log("Action：插入定期工作明细数据异常终了", Level.SEVERE, e);
			throw e;
		}
    }
    /**
     * object转换为string
     * @param obj
     * @return
     */
    private String getStringToString(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return "";
        }
    }
    /**
     * @return the entity
     */
    public RegularWorkArrangeInfo getEntity() {
        return entity;
    }
    /**
     * @param entity the entity to set
     */
    public void setEntity(RegularWorkArrangeInfo entity) {
        this.entity = entity;
    }
    /**
     * @return the strRec
     */
    public String getStrRec() {
        return strRec;
    }
    /**
     * @param strRec the strRec to set
     */
    public void setStrRec(String strRec) {
        this.strRec = strRec;
    }
    /**
     * @return the records
     */
    public String[] getRecords() {
        return records;
    }
    /**
     * @param records the records to set
     */
    public void setRecords(String[] records) {
        this.records = records;
    }
    /**
     * @return the deletePars
     */
    public String[] getDeletePars() {
        return deletePars;
    }
    /**
     * @param deletePars the deletePars to set
     */
    public void setDeletePars(String[] deletePars) {
        this.deletePars = deletePars;
    }
    /**
     * @return the ifWeekEnd
     */
    public String getIfWeekEnd() {
        return ifWeekEnd;
    }
    /**
     * @param ifWeekEnd the ifWeekEnd to set
     */
    public void setIfWeekEnd(String ifWeekEnd) {
        this.ifWeekEnd = ifWeekEnd;
    }
    /**
     * @return the workKind
     */
    public String getSubWorktypeCode() {
        return subWorktypeCode;
    }
    /**
     * @param workKind the workKind to set
     */
    public void setSubWorktypeCode(String subWorktypeCode) {
        this.subWorktypeCode = subWorktypeCode;
    }
    /**
     * @return the ifFlag
     */
    public String getIfFlag() {
        return ifFlag;
    }
    /**
     * @param ifFlag the ifFlag to set
     */
    public void setIfFlag(String ifFlag) {
        this.ifFlag = ifFlag;
    }
    /**
     * @return the workRangeType
     */
    public String getWorkRangeType() {
        return workRangeType;
    }
    /**
     * @param workRangeType the workRangeType to set
     */
    public void setWorkRangeType(String workRangeType) {
        this.workRangeType = workRangeType;
    }
    /**
     * @return the strId
     */
    public String getStrId() {
        return strId;
    }
    /**
     * @param strId the strId to set
     */
    public void setStrId(String strId) {
        this.strId = strId;
    }
    /**
     * @return the strWorkItemCode
     */
    public String getStrWorkItemCode() {
        return strWorkItemCode;
    }
    /**
     * @param strWorkItemCode the strWorkItemCode to set
     */
    public void setStrWorkItemCode(String strWorkItemCode) {
        this.strWorkItemCode = strWorkItemCode;
    }
	/**
	 * @return the strUpdateTime
	 */
	public final String getStrUpdateTime() {
		return strUpdateTime;
	}
	/**
	 * @param strUpdateTime the strUpdateTime to set
	 */
	public final void setStrUpdateTime(String strUpdateTime) {
		this.strUpdateTime = strUpdateTime;
	}
	/**
	 * @return the strStartTime
	 */
	public final String getStrStartTime() {
		return strStartTime;
	}
	/**
	 * @param strStartTime the strStartTime to set
	 */
	public final void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}
	/**
	 * @return the deleteTime
	 */
	public Date[] getDeleteTime() {
		return deleteTime;
	}
	/**
	 * @param deleteTime the deleteTime to set
	 */
	public void setDeleteTime(Date[] deleteTime) {
		this.deleteTime = deleteTime;
	}
}
