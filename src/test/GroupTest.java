package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hdc.entity.Group;
import com.hdc.service.IGroupService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class GroupTest {
	
	@Autowired
	private IGroupService groupService;
	
    @Test
	public void groupTest() throws Exception{
		Group group = new Group();
		group.setName("督察处");
		group.setType("SUPERVISE_DEPARTMENT");
		
		Group group2 = new Group();
		group2.setName("承办单位");
		group2.setType("UNDERTAKER_DEPARTMENT");
		
		Group group3 = new Group();
		group3.setName("督察专员");
		group3.setType("SUPERVISOR_DEPARTMENT");
		
		Group group4 = new Group();
		group4.setName("系统管理");
		group4.setType("ADMIN_DEPARTMENT");
		
		
		this.groupService.doAdd(group);
		this.groupService.doAdd(group2);
		this.groupService.doAdd(group3);
		this.groupService.doAdd(group4);
		
		/*List<Group> list = this.groupService.getGroupList();
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).getName(), "采购组");*/
		
	}
}
