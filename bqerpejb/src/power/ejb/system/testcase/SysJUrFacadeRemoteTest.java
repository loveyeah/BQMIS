package power.ejb.system.testcase;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.system.SysJUr;
import power.ejb.system.SysJUrFacadeRemote;

public class SysJUrFacadeRemoteTest {
	protected static SysJUrFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(SysJUrFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("SysJUrFacade");
	}

	@Test
	public void testSave() {
		SysJUr entity=new SysJUr();
		entity.setEnterpriseCode("hfdc");
		entity.setRoleId(Long.valueOf(30));
		entity.setWorkerId(Long.valueOf(20));
		entity.setModifyBy("123");
		remote.save(entity);
		System.out.print(entity.getId());
	}

	@Test
	public void testDelete() {
		SysJUr model=remote.findURByURId(Long.valueOf(30), Long.valueOf(20),false);
		remote.delete(model);
		System.out.print(model.getRoleId());
	}

}
