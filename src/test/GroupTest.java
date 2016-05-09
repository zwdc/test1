package test;



import  org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class GroupTest {
	
    @Test
	public void groupTest() throws Exception{
    	
		/*Group group = new Group();
		group.setName("督察处");
		group.setType("SUPERVISE_DEPARTMENT");
		
		Group group2 = new Group();
		group2.setName("承办单位1");
		group2.setType("UNDERTAKER1_DEPARTMENT");
		
		Group group5 = new Group();
		group5.setName("承办单位2");
		group5.setType("UNDERTAKER2_DEPARTMENT");
		
		Group group6 = new Group();
		group6.setName("承办单位3");
		group6.setType("UNDERTAKER3_DEPARTMENT");
		
		Group group3 = new Group();
		group3.setName("督察专员");
		group3.setType("SUPERVISOR_DEPARTMENT");
		
		Group group4 = new Group();
		group4.setName("系统管理");
		group4.setType("ADMIN_DEPARTMENT");*/
		
		
		/*List<Group> list = this.groupService.getGroupList();
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).getName(), "采购组");*/
		
    	//解决冲突2222
	}
}
