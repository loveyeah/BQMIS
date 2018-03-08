package power.ejb.workticket.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;

public class RunCWorkticketTypeFacadeRemoteTest {

	protected static RunCWorkticketTypeFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCWorkticketTypeFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCWorkticketTypeFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException {
		RunCWorkticketType entity=new RunCWorkticketType();
		entity.setEnterpriseCode("hfdc");
		entity.setModifyBy("999999");
		entity.setWorkticketTypeCode("1");
		entity.setWorkticketTypeName("test1");
		remote.save(entity);
	}

	@Test
	public void testDelete() throws CodeRepeatException {
		remote.delete(1l);
	}
	
	@Test
	public void testDeleteMulti()
	{
	  remote.deleteMulti("1,2");
	}

	@Test
	public void testUpdate() throws CodeRepeatException {
		RunCWorkticketType entity=remote.findById(1l);
		entity.setModifyBy("999999");
		entity.setWorkticketTypeCode("2");
		entity.setWorkticketTypeName("test2");
		remote.update(entity);
	}

	@Test
	public void testFindAll() {
		PageObject result=remote.findAll("hfdc");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}

}
