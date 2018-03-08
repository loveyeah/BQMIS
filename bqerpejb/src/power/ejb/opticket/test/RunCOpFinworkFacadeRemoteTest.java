package power.ejb.opticket.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.RunCOpFinwork;
import power.ejb.opticket.RunCOpFinworkFacadeRemote;

public class RunCOpFinworkFacadeRemoteTest {
	static RunCOpFinworkFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCOpFinworkFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCOpFinworkFacade");
	}

	@Test
	public void testSave() {
		RunCOpFinwork entity=new RunCOpFinwork();
		entity.setDisplayNo(1L);
		entity.setEnterpriseCode("hfdc");
		entity.setFinishWorkName("123");
		entity.setModifyBy("999999");
		entity.setOperateTaskId(1L);
		try {
			remote.save(entity);
		} catch (CodeRepeatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDelete() {
		RunCOpFinwork entity=remote.findById(1L);
		try {
			remote.delete(entity);
		} catch (CodeRepeatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdate() {
		RunCOpFinwork entity=remote.findById(1L);
		entity.setFinishWorkName("cba");
		try {
			remote.update(entity);
		} catch (CodeRepeatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testFindFinworkByTask() {
		List list=remote.findFinworkByTask("hfdc", 1L);
		System.out.print(list.size());
	}

}
