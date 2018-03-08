package power.ejb.workticket.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketContentKey;
import power.ejb.workticket.RunCWorkticketContentKeyFacadeRemote;

public class RunCWorkticketContentKeyFacadeRemoteTest {
	protected static RunCWorkticketContentKeyFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunCWorkticketContentKeyFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCWorkticketContentKeyFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException {
		RunCWorkticketContentKey model= new RunCWorkticketContentKey();
		model.setEnterpriseCode("hfdc");
		model.setContentKeyName("关键词4");
		model.setModifyBy("999999");
		model.setKeyType("1");
		RunCWorkticketContentKey entity= remote.save(model);
		System.out.print(entity.getContentKeyId()); 
	}

	@Test
	public void testDelete() throws CodeRepeatException {
		remote.delete(Long.parseLong("1"));
	}

	@Test
	public void testDeleteMulti() {
		remote.deleteMulti("1,2");
	}

	@Test
	public void testUpdate() throws CodeRepeatException {
		RunCWorkticketContentKey model= remote.findById(Long.valueOf(1));
		model.setEnterpriseCode("hfdc");
		model.setContentKeyName("关键词2");
		model.setModifyBy("999999");
		RunCWorkticketContentKey entity=remote.update(model);
		System.out.print(entity.getContentKeyName());
	}

	@Test
	public void testFindById() {
		RunCWorkticketContentKey model=remote.findById(Long.parseLong("1"));
	}

	@Test
	public void testFindAll() {
		PageObject result=remote.findAllWithComm("1", "hfdc","1");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}

}
