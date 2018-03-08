package power.web.manage.plan.action;



import java.text.ParseException;
import java.util.Date;


import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.manage.plan.BpJPlanForeword;
import power.ejb.manage.plan.BpJPlanForewordFacadeRemote;
import power.ejb.manage.plan.form.PlanForewordForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class BeforeWorkPlanAction extends AbstractAction{
		private BpJPlanForeword baseInfo;
		/*计划月份*/
		private String planTime;
		private BpJPlanForewordFacadeRemote remote;
		
		
		public BeforeWorkPlanAction(){
			remote = (BpJPlanForewordFacadeRemote)factory.getFacadeRemote("BpJPlanForewordFacade");
		}
		/**
		 * 根据月份获取一条计划前言记录
		 * 
		 * 
		 * @throws JSONException
		 * @throws ParseException 
		 */
		public void getBeforeWorkPlanInfo() throws JSONException, ParseException{
			PlanForewordForm form = remote.findModelByPlantime(planTime, employee.getEnterpriseCode());
			System.out.println(JSONUtil.serialize(form));
			write(JSONUtil.serialize(form));
		}
		/**
		 * 
		 * 新增一条计划前言
		 * @throws ParseException 
		 * @throws Exception
		 */
		public void addBeforeWorkPlan() throws ParseException{
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM");
			baseInfo.setEnterpriseCode(employee.getEnterpriseCode());
			baseInfo.setEditBy(employee.getWorkerCode());
			baseInfo.setPlanTime(formatter.parse(planTime));
			BpJPlanForeword model = remote.save(baseInfo);
			if(model == null){
				write("{failure:true,msg:'数据库已经有了该月的数据!'}");
			}else{
				String time = formatter.format(model.getPlanTime());
				write("{success:true,msg:'增加成功！',plt : '"+time+"',status : "+model.getPlanStatus()+"}");
            }
		}
		/**
		 * 
		 * 更新一条记录
		 * @throws ParseException
		 */
		public void updateBeforeWorkPlan() throws ParseException{
			java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
			BpJPlanForeword entity = remote.findById(formatter.parse(planTime));
			entity.setEditBy(baseInfo.getEditBy());
			entity.setEditDate(baseInfo.getEditDate());
			entity.setPlanForeword(baseInfo.getPlanForeword());
			entity.setPlanStatus(baseInfo.getPlanStatus());
			entity.setPlanTime(formatter.parse(planTime));
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			BpJPlanForeword model = remote.update(entity);
			if(model != null){
				String time = formatter.format(model.getPlanTime());
				write("{success:true,msg:'修改成功!',plt : '"+time+"',status : "+model.getPlanStatus()+"}");
			}else
				write("{failure : true,msg :'修改失败！'}");
			
		}
		/**
		 * 前言定稿
		 * 
		 * @throws ParseException
		 */
		public void defineBeforeWorkPlan() throws ParseException{
		   java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
			BpJPlanForeword entity = remote.findById(formatter.parse(planTime));
			entity.setPlanStatus(1L);
			BpJPlanForeword model = remote.update(entity);
			if(model != null)
				write("{success:true,msg:'定稿成功!',status : "+model.getPlanStatus()+"}");
			else
				write("{failure : true,msg :'定稿失败！'}");
			
		}
		/**
		 * 前言发布
		 * 
		 * @throws ParseException
		 */
		public void editBeforeWorkPlan() throws ParseException{
			java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
			BpJPlanForeword entity = remote.findById(formatter.parse(planTime));
			entity.setPlanStatus(2L);
			BpJPlanForeword model = remote.update(entity);
			if(model != null)
				write("{success:true,msg:'发布成功!',status : "+model.getPlanStatus()+"}");
			else
				write("{failure : true,msg :'发布失败！'}");
		}
		/**
		 * 
		 * 删除一条记录
		 * @throws ParseException 
		 */
		public void deleteBeforeWorkPlan() throws ParseException{
			java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
			BpJPlanForeword entity = remote.findById(formatter.parse(planTime));
			if(remote.delete(entity))
				write("{success : true,msg : '删除成功！'}");
			else
				write("{success : false,msg : '删除失败！'}");
			
		}
		public BpJPlanForeword getBaseInfo() {
			return baseInfo;
		}
		public void setBaseInfo(BpJPlanForeword baseInfo) {
			this.baseInfo = baseInfo;
		}
		public String getPlanTime() {
			return planTime;
		}
		public void setPlanTime(String planTime) {
			this.planTime = planTime;
		}
}
