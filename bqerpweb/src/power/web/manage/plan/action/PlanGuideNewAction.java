package power.web.manage.plan.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.manage.plan.BpJPlanGuideNew;
import power.ejb.manage.plan.BpJPlanGuideNewFacadeRemote;
import power.ejb.manage.plan.form.BpJPlanGuideNewForm;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class PlanGuideNewAction extends AbstractAction{
		private BpJPlanGuideNew guideNew;
		private BpJPlanGuideNewFacadeRemote remote;
		private String guideCode;
		public PlanGuideNewAction(){
			remote = (BpJPlanGuideNewFacadeRemote)factory.getFacadeRemote("BpJPlanGuideNewFacade");
		}
		
		/**
		 * 新增厂部临时计划
		 * @throws Exception
		 */
		public void addPlanNews() throws Exception{
			String planTime = request.getParameter("planTime");
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM"); 
			guideNew.setEnterpriseCode(employee.getEnterpriseCode());
			guideNew.setPlanTime(dateFm.parse(planTime));
			BpJPlanGuideNew model = remote.save(guideNew);
			write("{success:true,msg:'增加成功!',guideId:'"+model.getGuideId()+"'}");
		}
		/**
		 * 更新厂部临时计划
		 * @throws ParseException
		 */
		public void updatePlanNews() throws ParseException{
			BpJPlanGuideNew entity = remote.findById(guideCode);
			String planTime = request.getParameter("planTime");
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM"); 
			entity.setPlanTime(dateFm.parse(planTime));
//			entity.setEditBy(guideNew.getEditBy());
//			entity.setEditDate(guideNew.getEditDate());
//			entity.setGuideContent(guideNew.getGuideContent());
//			entity.setReferDepcode(guideNew.getReferDepcode());
//			entity.setMainDepcode(guideNew.getMainDepcode());
//			entity.setOtherDepcode(guideNew.getOtherDepcode());
			entity.setIfComplete(guideNew.getIfComplete());
			entity.setCompleteDesc(guideNew.getCompleteDesc());
			BpJPlanGuideNew model =	remote.update(entity);
			write("{success:true,msg:'修改成功!',guideId:'"+model.getGuideId()+"'}");
			
		}
		/**
		 * 
		 * 删除一条记录
		 */
		public void deletePlanNews(){
			BpJPlanGuideNew entity = remote.findById(guideCode);
			remote.delete(entity);
			write("{success : true,msg : '删除成功！'}");
			
		}
		/**
		 * 厂部临时计划列表
		 * @throws JSONException 
		 */
		public void getPlanNewsList() throws JSONException{
			String month = request.getParameter("queryString");
			List <BpJPlanGuideNewForm> guideNew = remote.findAll(month);
			write(JSONUtil.serialize(guideNew));
		}

		public BpJPlanGuideNew getGuideNew() {
			return guideNew;
		}

		public void setGuideNew(BpJPlanGuideNew guideNew) {
			this.guideNew = guideNew;
		}

		public String getGuideCode() {
			return guideCode;
		}

		public void setGuideCode(String guideCode) {
			this.guideCode = guideCode;
		}

}