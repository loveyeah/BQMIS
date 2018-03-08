package power.web.workticketbq.approve.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketMarkcardFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketSafety;
import power.ejb.workticket.business.RunJWorkticketSafetyFacadeRemote;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.business.WorkticketManager;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

public class BqApproveSafetyAction extends AbstractAction{
	 private RunJWorkticketsFacadeRemote workticketsRemote;
	  /** 挂牌数据remote*/
   private RunCWorkticketMarkcardFacadeRemote markcardRemote;
   /**安措明细记录remote*/
   private RunJWorkticketSafetyFacadeRemote safetyRemote;
   /** 安措remote */
   private WorkticketManager  measureRemote;
   
   public BqApproveSafetyAction()
   {
   	 // 工作票remote
       workticketsRemote = (RunJWorkticketsFacadeRemote)factory.getFacadeRemote("RunJWorkticketsFacade");
      
   	 // 挂牌数据remote
       markcardRemote = (RunCWorkticketMarkcardFacadeRemote)factory
               .getFacadeRemote("RunCWorkticketMarkcardFacade");
       // 安措remote
       measureRemote = (WorkticketManager)factory.getFacadeRemote("WorkticketManagerImpl");
       // 安措明细记录remote
       safetyRemote = (RunJWorkticketSafetyFacadeRemote)factory.getFacadeRemote("RunJWorkticketSafetyFacade");
   }
	
	  /**
    * 挂牌编号获得
    * @throws JSONException
    */
   public void getMarkcardList() throws JSONException{
       // 检索条件
       String strFuzzy = request.getParameter("fuzzy");
       // 没有检索条件则检索全部
       if(strFuzzy == null || strFuzzy.length() < 1){
           strFuzzy = Constants.ALL_DATA;
       }
       try{
           // 标示牌类型ID
           String strMarkcardTypeID = request.getParameter("markcardTypeID");
           Long lngMarkcardTypeID = Long.parseLong(strMarkcardTypeID);
           // 开始行号
           String strStart = request.getParameter("start");
           // 行数
           String strLimit = request.getParameter("limit");
           int start = Integer.parseInt(strStart);
           int limit = Integer.parseInt(strLimit);
           PageObject obj = markcardRemote.findAll(Constants.ENTERPRISE_CODE, lngMarkcardTypeID, strFuzzy,start,limit);
           write(JSONUtil.serialize(obj));
       }catch(Exception e){
           write("{success:true,msg:'未知错误'}");
       }

   }
   
   /**
    * 更新工作票安全措施明细记录
    * 
    */
   @SuppressWarnings("unchecked")
	public void updateWorkticketSafetyRecord() throws JSONException
   {
   	String str=request.getParameter("data");
		   Object obj = JSONUtil.deserialize(str);
		   List<Map> list = (List<Map>)obj;
		   for(Map data : list)
		   {
			   String strId = ((Map) ((Map) data)).get("id").toString();
		       String strExeState = ((Map) ((Map)data)).get("safetyExecuteCode").toString(); 
		       String strMarkcardCode = ((Map) ((Map)data)).get("markcardCode").toString(); 
		       RunJWorkticketSafety  entity = safetyRemote.findById(Long.parseLong(strId));
		       if(entity != null){
		            // 执行状态
		            entity.setSafetyExecuteCode(strExeState);
		            // 挂牌编号
		            if(strMarkcardCode != null && strMarkcardCode.length() > 0){
		                entity.setMarkcardCode(strMarkcardCode);
		               
		            }
		            
		            entity.setSafetyExecuteBy(employee.getWorkerCode());
	                entity.setSafetyExecuteDate(new Date());
		            try{
		                // 更新
		                safetyRemote.exeSafety(entity);
		               
		                write(Constants.MODIFY_SUCCESS);
		            }catch(Exception e){
		                write(Constants.MODIFY_FAILURE);
		            }
		        }else {
		            write(Constants.MODIFY_FAILURE);
		        }
		   }
		   
   }
   
   /**
    * 安措内容列表查询
    *modify by fyyang 
    * @throws JSONException
    */
   public void getSafetyContent() throws JSONException {
       // 工作票编号 参数
       String strWorkticketNo = request.getParameter("workticketNo");
       if(strWorkticketNo == null){
           strWorkticketNo ="";
       }
       // 查询
       String workticketTypeCode=request.getParameter("workticketTypeCode");
       PageObject result = measureRemote.findSafetyContentList(
               Constants.ENTERPRISE_CODE, strWorkticketNo,workticketTypeCode);
       // 返回
       write(JSONUtil.serialize(result));
   }
   
   /**
    * 打印安措执行卡操作
    */
   public void printMeasureExeCard(){

       // 工作票编号
       String strWorkticketNo = request.getParameter("workticketNo");
       // 获得要修改的记录
       RunJWorktickets entity = workticketsRemote.findById(strWorkticketNo);
       // 查询不到指定的记录则返回
       if(entity == null){
            write("{success:true,msg:'操作失败:未找到要打印的数据'}");
            return;
       }
       // printFlag 置1
       entity.setPrintFlag("1");
       try{
           // 更新
           workticketsRemote.update(entity);
           write("{success:true,msg:'操作成功！'}");
       } catch (Exception e) {
           write("{success:true,msg:'操作失败！'}");
       }
   }
}
