package power.ejb.workticket.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;


import power.ejb.workticket.RunCWorktickSafety;
import power.ejb.workticket.RunCWorktickSafetyFacadeRemote;
import power.ejb.workticket.form.WorticketSafety;


public class RunCWorktickSafetyFacadeRemoteTest {
	protected static RunCWorktickSafetyFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCWorktickSafetyFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("RunCWorktickSafetyFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException {
		
		RunCWorktickSafety model=new RunCWorktickSafety();
		//model.setSafetyId(2l);
		model.setEnterpriseCode("hfdc");
		model.setSafetyType("N");
		model.setMarkcardTypeId(1l);
		model.setModifyBy("999999");
		model.setOrderBy(1l);
		model.setSafetyCode("11");
		model.setSafetyDesc("test2");
	    model.setWorkticketTypeCode("1");
		RunCWorktickSafety entity=   remote.save(model);
        System.out.println(entity.getSafetyId());

	}

	@Test
	public void testDelete() throws CodeRepeatException {
		Long safetyId=1l;
		remote.delete(safetyId);
		
	}
	@Test
	public void  testDeleteMulti()
	{
		String ids="1,2";
		remote.deleteMulti(ids);
	}

	@Test
	public void testUpdate() throws CodeRepeatException {
		RunCWorktickSafety model=remote.findById(4l);
		if(model!=null)
		{
	//	model.setIsRunAdd("Y");
	    model.setSafetyType("Y");
		model.setMarkcardTypeId(1l);
		model.setModifyBy("999999");
		model.setOrderBy(1l);
		model.setSafetyCode("12");
		model.setSafetyDesc("test12");
		model= remote.update(model);
		System.out.println(model.getSafetyId());
		}
		
	}

	@Test
	public void testFindAll() throws ParseException {
		PageObject result=remote.findAll("1", "hfdc"); 
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}

}
