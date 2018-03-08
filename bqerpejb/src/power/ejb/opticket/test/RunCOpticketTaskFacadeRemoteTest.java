package power.ejb.opticket.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.RunCOpticketTask;
import power.ejb.opticket.RunCOpticketTaskFacadeRemote;

public class RunCOpticketTaskFacadeRemoteTest {
	protected static RunCOpticketTaskFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunCOpticketTaskFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunCOpticketTaskFacade");
	}

	@Test
	public void testSave() {
		RunCOpticketTask entity=new RunCOpticketTask();
		entity.setDisplayNo(Long.valueOf(1));
		entity.setEnterpriseCode("hefdc");
		entity.setIsMain("Y");
		entity.setModifyBy("999999");
		entity.setOperateTaskCode("123");
		entity.setParentOperateTaskId(Long.valueOf(0));
		entity.setOperateTaskName("abc");
		entity.setOperateTaskExplain("cdf");
		RunCOpticketTask model=remote.save(entity);
		System.out.print(model.getOperateTaskId());
	}

	@Test
	public void testDelete() {
		RunCOpticketTask entity=remote.findById(Long.valueOf(1));
		System.out.print(remote.findAll("hefdc").size()+"*****");
		remote.delete(entity);
		System.out.print(remote.findAll("hefdc").size());
	}

	@Test
	public void testUpdate() {
		RunCOpticketTask entity=remote.findById(Long.valueOf(1));
		entity.setOperateTaskName("xiugai");
		System.out.print(remote.update(entity).getOperateTaskName());
	}

	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByParentOperateTaskId() {
		List list=remote.findByParentOperateTaskId("hefdc",Long.valueOf(0));
		System.out.print(list.size());
	}

}
