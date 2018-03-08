package power.ejb.workticket.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.workticket.form.WorkticketDanger;
import power.ejb.workticket.form.WorkticketDangerForPrint;

@Stateless
public class WorkticketDangerPrintImpl implements WorkticketDangerPrint{
	@PersistenceContext
	private EntityManager entityManager; 
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	private RunJWorkticketDangerFacadeRemote dangerRemote;
	protected RunJWorkticketsFacadeRemote baseRemote;
	protected RunJWorkticketContentFacadeRemote contentRemote;
	public WorkticketDangerPrintImpl()
	{
		baseRemote=(RunJWorkticketsFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJWorkticketsFacade");
		dangerRemote=(RunJWorkticketDangerFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJWorkticketDangerFacade");
		contentRemote=(RunJWorkticketContentFacadeRemote)Ejb3Factory.getInstance()
		.getFacadeRemote("RunJWorkticketContentFacade");
	}
	
	@SuppressWarnings("unchecked")
	public WorkticketDangerForPrint getDangerInfo(String workticketNo)
	{
		WorkticketDangerForPrint model=new WorkticketDangerForPrint();
		
		// modify BY ywliu 09/04/30 t.workticket_staus_id
		String sql=
			"select t.workticket_no,nvl(GETWORKERNAME(t.charge_by),t.charge_by) chargeName,\n" +
			"t.danger_condition,\n" + 
			"(select GETWORKERNAME(a.approve_by) from run_j_worktickethis a\n" + 
			"where a.workticket_no='"+workticketNo+"' and a.approve_status=3 and a.id=(select max(id) from run_j_worktickethis where  approve_status=3 and workticket_no='"+workticketNo+"')) approveman,\n" + 
			"(select a.approve_text from run_j_worktickethis a\n" + 
			" where a.workticket_no='"+workticketNo+"' and a.approve_status=3 and a.id=(select max(id) from run_j_worktickethis where  approve_status=3 and workticket_no='"+workticketNo+"')) approvetext,\n" + 
			" t.workticket_staus_id\n " +
			" from run_j_worktickets t\n" + 
			"where t.workticket_no like '"+workticketNo+"'\n" + 
			"and t.is_use='Y'";
		List list=bll.queryByNativeSQL(sql);
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Object[] data=(Object[])it.next();
			if(data[0]!=null)
			{
				model.setWorkticketNo(data[0].toString());
			}
			if(data[1]!=null)
			{
				model.setChargeBy(data[1].toString());
			}
			if(data[2]!=null)
			{
				model.setDangerContent(data[2].toString());
			}
			if(data[3]!=null)
			{
				model.setSignBy(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setSignText(data[4].toString());
			}
			if(data[5]!=null)
			{
				model.setWorkticketStausId(data[5].toString());
			}
		}
		//危险点内容改为工作票的工作内容 modify by fyyang 090420
		model.setDangerContent(contentRemote.getWorkticketContent(workticketNo));
		List<WorkticketDanger> dangerList=dangerRemote.findDangerList(workticketNo).getList();
		
		List<WorkticketDanger> myList=new ArrayList();
		if(dangerList!=null)
		{
				for(int i=0;i<dangerList.size();i++)
				{
					 boolean check=false;
					  if(myList!=null)
					  {
						 
						  for(int j=0;j<myList.size();j++)
						  {
							  if(dangerList.get(i).getDangerId().equals(myList.get(j).getDangerId()))
							  {
								  String myMeasure=myList.get(j).getDangerMeasure();
								  if(myMeasure.equals(""))
								  {
									  myMeasure=dangerList.get(i).getDangerMeasure();
								  }
								  else
								  {
									  myMeasure += "<br>"+dangerList.get(i).getDangerMeasure();
								  }
								  myList.get(j).setDangerMeasure(myMeasure);
								  check=true;
								  break;
							  }
						  }
					  }
					  
					  if(!check)
					  {
						  WorkticketDanger entity=dangerList.get(i);
						  myList.add(entity);
					  }
					  
					
				}
		}
		
		//-------------------危险点查询处理---------------
		model.setDanger(myList);
		
		return model;
		
	}
	
	
	
	
}
