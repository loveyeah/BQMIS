package power.web.productiontec.dependability.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxJSjyb;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxJSjybFacadeRemote;
import power.ejb.productiontec.dependabilityAnalysis.form.PtKkxJSjybForm;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class MonthlyDependItemComputationAction extends AbstractAction {

	private PtKkxJSjybFacadeRemote remote;
	private int start;
	private int limit;

	public MonthlyDependItemComputationAction() {
		remote = (PtKkxJSjybFacadeRemote) factory
				.getFacadeRemote("PtKkxJSjybFacade");
	}

	public void getMonthInput() throws JSONException {
		String month = request.getParameter("argFuzzy");

		PageObject object = remote.getMonthInput(month, employee
				.getEnterpriseCode(), start, limit);
		write(JSONUtil.serialize(object));
	}

	public void computeAndSave() throws JSONException {
		String month = request.getParameter("argFuzzy");
		List<PtKkxJSjybForm> list = remote.getcomputelist(month, employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(list));
	}

	public void update() {
		String month = request.getParameter("argFuzzy");
		List<PtKkxJSjybForm> list = remote.getcomputelist(month, employee
				.getEnterpriseCode());

	}

	public void findAllBlockMonthly() throws JSONException {
		String month = request.getParameter("month");
		PageObject object = remote.findAll(month,employee.getEnterpriseCode(), start,
				limit);
		write(JSONUtil.serialize(object));
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	/**
	 * 批量修改可靠性数据月报数据
	 * @throws JSONException 
	 */
	public void monthlyResultsModify() throws JSONException
	{
		String modifyRecords = request.getParameter("modifyRecords");
		if(modifyRecords != null && !(modifyRecords.equals("")))
		{
			List<PtKkxJSjyb> list = this.parseContent(modifyRecords);
			remote.modifyRecords(list);
			write("{success:true}");
		}
	}
	
	public List<PtKkxJSjyb> parseContent(String modifyRecords) throws JSONException
	{
		List<PtKkxJSjyb> result = new ArrayList<PtKkxJSjyb>();
		Object obj = JSONUtil.deserialize(modifyRecords);
		List list = (List)obj;
		int len = list.size();
		for(int i = 0; i <= len -1; i++)
		{
			Map map = (Map)list.get(i);
			PtKkxJSjyb pk = this.parseModel(map);
			result.add(pk);
		}
		return result;
	}
	
	public PtKkxJSjyb parseModel(Map map)
	{
		PtKkxJSjyb temp = new PtKkxJSjyb();
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM");
		
		Object kkxybId = map.get("kkxybId");
		if(kkxybId != null && !(kkxybId.equals("")))
			temp.setKkxybId(Long.parseLong(kkxybId.toString()));
		Object month = map.get("month");
		if(month != null && !(month.equals("")))
			try {
				temp.setMonth(sbf.parse(month.toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		Object blockCode  = map.get("blockCode");
		if(blockCode != null && !(blockCode.equals("")))
				temp.setBlockCode(blockCode.toString());
			
		Object fdl = map.get("fdl");
		if(fdl != null && !(fdl.equals("")))
			temp.setFdl(Double.parseDouble(fdl.toString()));
		
		Object uth = map.get("uth");
		if(uth != null && !(uth.equals("")))
			temp.setUth(Double.parseDouble(uth.toString()));
		
		Object ph = map.get("ph");
		if(ph != null && !(ph.equals("")))
			temp.setPh(Double.parseDouble(ph.toString()));
		
		Object undh = map.get("undh");
		if(undh != null && !(undh.equals("")))
			temp.setUndh(Double.parseDouble(undh.toString()));
		
		Object sh = map.get("sh");
		if(sh != null && !(sh.equals("")))
			temp.setSh(Double.parseDouble(sh.toString()));
		
		Object rh = map.get("rh");
		if(rh != null && !(rh.equals("")))
			temp.setRh(Double.parseDouble(rh.toString()));
		
		Object pot = map.get("pot");
		if(pot != null && !(pot.equals("")))
			temp.setPot(Double.parseDouble(pot.toString()));
		
		Object poh = map.get("poh");
		if(poh != null && !(poh.equals("")))
			temp.setPoh(Double.parseDouble(poh.toString()));
		
		Object uot = map.get("uot");
		if(uot != null && !(uot.equals("")))
			temp.setUot(Double.parseDouble(uot.toString()));
		
		Object uoh = map.get("uoh");
		if(uoh != null && !(uoh.equals("")))
			temp.setUoh(Double.parseDouble(uoh.toString()));
		
		Object fot = map.get("fot");
		if(fot != null && !(fot.equals("")))
			temp.setFot(Double.parseDouble(fot.toString()));
		
		Object foh = map.get("foh");
		if(foh != null && !(foh.equals("")))
			temp.setFoh(Double.parseDouble(foh.toString()));
		
		Object for1 = map.get("for1");
		if(for1 != null && !(for1.equals("")))
			temp.setFor1(Double.parseDouble(for1.toString()));
		
		Object eaf = map.get("eaf");
		if(eaf != null && !(eaf.equals("")))
			temp.setEaf(Double.parseDouble(eaf.toString()));
		
		Object exr = map.get("exr");
		if(exr != null && !(exr.equals("")))
			temp.setExr(Double.parseDouble(exr.toString()));
		
		Object pof = map.get("pof");
		if(pof != null && !(pof.equals("")))
			temp.setPof(Double.parseDouble(pof.toString()));
		
		Object uof = map.get("uof");
		if(uof != null && !(uof.equals("")))
			temp.setUof(Double.parseDouble(uof.toString()));
		
		Object fof = map.get("fof");
		if(fof != null && !(fof.equals("")))
			temp.setFof(Double.parseDouble(fof.toString()));
		
		Object af = map.get("af");
		if(af != null && !(af.equals("")))
			temp.setAf(Double.parseDouble(af.toString()));
		
		Object sf = map.get("sf");
		if(sf != null && !(sf.equals("")))
			temp.setSf(Double.parseDouble(sf.toString()));
		
		Object udf = map.get("udf");
		if(udf != null && !(udf.equals("")))
			temp.setUdf(Double.parseDouble(udf.toString()));
		
		Object utf = map.get("utf");
		if(utf != null && !(utf.equals("")))
			temp.setUtf(Double.parseDouble(utf.toString()));
		
		Object uor = map.get("uor");
		if(uor != null && !(uor.equals("")))
			temp.setUor(Double.parseDouble(uor.toString()));
		
		Object foor = map.get("foor");
		if(foor != null && !(foor.equals("")))
			temp.setFoor(Double.parseDouble(foor.toString()));
		
		Object mttpo = map.get("mttpo");
		if(mttpo != null && !(mttpo.equals("")))
			temp.setMttpo(Double.parseDouble(mttpo.toString()));
		
		Object mttuo = map.get("mttuo");
		if(mttuo != null && !(mttuo.equals("")))
			temp.setMttuo(Double.parseDouble(mttuo.toString()));
		
		Object cah = map.get("cah");
		if(cah != null && !(cah.equals("")))
			temp.setCah(Double.parseDouble(cah.toString()));
		
		Object mtbf = map.get("mtbf");
		if(mtbf != null && !(mtbf.equals("")))
			temp.setMtbf(Double.parseDouble(mtbf.toString()));
		
		Object eundh = map.get("eundh");
		if(eundh != null && !(eundh.equals("")))
			temp.setEundh(Double.parseDouble(eundh.toString()));
		
		temp.setEnterpriseCode(employee.getEnterpriseCode());
		
		return temp;
			
	}
}
