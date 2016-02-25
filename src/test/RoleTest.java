package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hdc.entity.Role;
import com.hdc.service.IRoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class RoleTest {

	@Autowired
	private IRoleService roleService;
	
	@Test
	public void roleTest() throws Exception{
		 
		Role role1 = new Role();
		role1.setName("督察处");
		role1.setType("SUPERVISE");
		
		Role role2 = new Role();
		role2.setName("督察专员");
		role2.setType("SUPERVISOR");
		
		Role role3 = new Role();
		role3.setName("承办单位1");
		role3.setType("UNDERTAKER1");
		
		Role role5 = new Role();
		role5.setName("承办单位2");
		role5.setType("UNDERTAKER2");
		
		Role role6 = new Role();
		role6.setName("承办单位3");
		role6.setType("UNDERTAKER3");
		
		Role role4 = new Role();
		role4.setName("系统管理");
		role4.setType("admin");
		
		this.roleService.doAdd(role4);
		this.roleService.doAdd(role1);
		this.roleService.doAdd(role2);
		this.roleService.doAdd(role3);
		this.roleService.doAdd(role5);
		this.roleService.doAdd(role6);
	}
}
