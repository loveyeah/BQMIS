package power.ejb.workticket.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCMarkcardTypeFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketActors;
import power.ejb.workticket.business.RunJWorkticketContent;
import power.ejb.workticket.business.RunJWorkticketFireContent;
import power.ejb.workticket.business.RunJWorkticketSafety;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.WorkticketManager;

public class WorkticketManagerTest {

	protected static WorkticketManager remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(WorkticketManager)Ejb3Factory.getInstance().getFacadeRemote("WorkticketManagerImpl");
	}

	@Test
	public void testCreateWorkticket() {
		RunJWorktickets entity=new RunJWorktickets();
		entity.setEntryBy("999999");
		entity.setEquAttributeCode("1");
		entity.setConditionName("test");
		entity.setPermissionDept("1");
		entity.setEnterpriseCode("hfdc");
		entity.setWorkticketTypeCode("4");
		entity.setRefWorkticketNo("S1200811X0001");
		entity.setRepairSpecailCode("TEST");
		remote.createWorkticket(entity, "S","","","999999","");
		
	}

	@Test
	public void testUpdateWorkticketInfoByOld() {
		remote.updateWorkticketInfoByOld("S1200811X0001", "S1200811D0001", "hfdc", "888888","");
		
	}

	@Test
	public void testUpdateWorkticket() {
		RunJWorktickets entity=remote.findWorkticketByNo("S4200812T0003");
		entity.setEntryBy("888888");
		entity.setEquAttributeCode("2");
		entity.setConditionName("test");
		entity.setPermissionDept("2");
		entity.setEnterpriseCode("hfdc");
		entity.setRefWorkticketNo("S1200811D0001");
		remote.updateWorkticket(entity,"","");
	}

	@Test
	public void testDeleteWorkticket() {
		
		remote.deleteWorkticket("S4200812T0003");
		
	}

	@Test
	public void testFindWorkticketContentList() {
	
		
		PageObject result=remote.findWorkticketContentList("hfdc", "S1200811X0002");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}

	@Test
	public void testFindWorkticketContenById() {
		fail("尚未实现"); // TODO
	}

//	@Test
//	public void testAddWorkticketContent() {
//		RunJWorkticketContent entity=new RunJWorkticketContent();
//		entity.setAttributeCode("001");
//		entity.setBackKeyDesc("backa1");
//		entity.setBackKeyId(10l);
//		entity.setCreateBy("999999");
//		entity.setEnterpriseCode("hfdc");
//		entity.setEquName("#1机组a");
//		entity.setFlagId(1l);
//		entity.setFrontKeyDesc("fronta");
//		entity.setFrontKeyId(2l);
//		entity.setLine(1l);
//		entity.setLocationId(2l);
//		entity.setLocationName("locationa");
//		entity.setWorkticketNo("S1200811X0001");
//		entity.setWorktypeId(2l);
//		entity.setWorktypeName("worktype");
//		remote.addWorkticketContent(entity);
//		
//	}

//	@Test
//	public void testUpdateWorkticketContent() {
//		RunJWorkticketContent entity=remote.findWorkticketContenById(50l);
//		entity.setBackKeyDesc("ttt");
//		entity.setEquName("bb");
//		remote.updateWorkticketContent(entity);
//		
//	}

	@Test
	public void testDeleteWorkticketContent() {
		remote.deleteWorkticketContent(52l);
	}

	@Test
	public void testFindSafetyDetailList() {
		remote.findSafetyDetailList("hfdc", "S1200811Q0001", "11");
	}

	@Test
	public void testFindSafetyDetailById() {
		remote.findSafetyDetailById(10l);
	}

//	@Test
//	public void testAddWorkticketSafetyDetail() {
//		RunJWorkticketSafety entity=new RunJWorkticketSafety();
//		entity.setAttributeCode("1");
//		entity.setBackKeyId(8l);
//		entity.setBackKeyword("back");
//		entity.setCreateBy("999999");
//		entity.setEnterpriseCode("hfdc");
//		entity.setEquName("#1机组");
//		entity.setFlagId(5l);
//		entity.setFrontKeyId(6l);
//		entity.setFrontKeyword("front");
//		entity.setOperationOrder(2l);
//		entity.setSafetyCode("41");
//		entity.setWorkticketNo("S4200812T0003");
//		remote.addWorkticketSafetyDetail(entity);
//		
//	}

//	@Test
//	public void testUpdateWorkticketSafetyDetail() {
//		RunJWorkticketSafety entity=remote.findSafetyDetailById(10l);
//		entity.setBackKeyword("ccc");
//		entity.setEquName("equ");
//		remote.updateWorkticketSafetyDetail(entity);
//	}

//	@Test
//	public void testDeleteWorkticketSafetyDetail() {
//		remote.deleteWorkticketSafetyDetail(10l);
//	}
	
	//---------------------------------------------------------
	
	@Test
	public void testAddWorkticketMember() throws CodeRepeatException {
		RunJWorkticketActors entity=new RunJWorkticketActors();
		entity.setActorCode("02");
		entity.setActorName("test2");
		entity.setActorDept("01");
		entity.setEnterpriseCode("hfdc");
		entity.setActorType(1l);
		entity.setWorkticketNo("S1200811X0001");
		remote.addWorkticketMember(entity);
	}
	@Test
	public void testDeleteWorkticketMember() throws CodeRepeatException {
		remote.deleteWorkticketMember(5l);
	}
	
	@Test
	public void testDeleteMultiMember() {
		remote.deleteMultiMember("2,4");
	}
	
	@Test
	public void testFindAllActors() {
		PageObject result=remote.findAllActors("hfdc", "S1200811X0001");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	
	@Test
	public void testGetSafetyContent() {
	 String content=remote.getSafetyContent("hfdc", "S1200811X0002","1a");
	 System.out.println(content);
	}
	
	
	@Test
	public void testFindSafetyContentList()
	{
		PageObject result=remote.findSafetyContentList("hfdc", "S1200811X0002","1");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	
	@Test 
	public void testSaveWorkticketFireContent() throws CodeRepeatException
	{
		RunJWorkticketFireContent entity=new RunJWorkticketFireContent();
		entity.setEnterpriseCode("hfdc");
		entity.setFirecontentId(1l);
		entity.setModifyBy("99999");
		entity.setWorkticketNo("S4200811Q0001");
		entity.setOrderBy(1l);
		remote.saveWorkticketFireContent(entity);
	}
	
	@Test 
	public void testDeleteWorkticketFireContent()
	{
		remote.deleteWorkticketFireContent(1l);
	}
	
	@Test 
	public void testDeleteMultiFireContent()
	{
		remote.deleteMultiFireContent("3,2");
	}
	@Test 
	public void testFindFireContentList()
	{
		PageObject result=remote.findFireContentList("hfdc", "S4200811Q0001");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	
//	@Test 
//	public void testUpdateFireSafety()
//	{
//		remote.updateFireSafety("S4200812t0001", "4d");
//	}
	
	

	

}
