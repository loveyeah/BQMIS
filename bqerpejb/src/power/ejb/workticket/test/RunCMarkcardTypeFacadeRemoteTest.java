package power.ejb.workticket.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCMarkcardType;
import power.ejb.workticket.RunCMarkcardTypeFacadeRemote;

public class RunCMarkcardTypeFacadeRemoteTest {
   protected static RunCMarkcardTypeFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCMarkcardTypeFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCMarkcardTypeFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException {
		RunCMarkcardType model=new RunCMarkcardType();
		model.setEnterpriseCode("hfdc");
		//model.setMarkcardTypeId(1l);
		model.setMarkcardTypeName("abc");
		model.setModifyBy("999999");
		remote.save(model);
	}

	@Test
	public void testDelete() throws CodeRepeatException {
		Long id=2l;
		remote.delete(id);
	}

	@Test
	public void testDeleteMulti() {
	  String ids="1,2";
	  remote.deleteMulti(ids);
	}

	@Test
	public void testUpdate() throws CodeRepeatException {
		RunCMarkcardType model=remote.findById(2l);
		model.setEnterpriseCode("hfdc");
		//model.setMarkcardTypeId(1l);
		model.setMarkcardTypeName("ttt");
		model.setModifyBy("999999");
		remote.update(model);
	}

	@Test
	public void testFindAll() {
		PageObject result=remote.findAll("hfdc", "");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}

}
