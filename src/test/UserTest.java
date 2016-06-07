package test;

import java.io.Serializable;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hdc.dao.IJdbcDao;
import com.hdc.entity.Role;
import com.hdc.entity.User;
import com.hdc.service.IBaseService;
import com.hdc.service.IRoleService;
import com.hdc.service.IUserService;
import com.hdc.service.impl.PasswordHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class UserTest {
	
	@Autowired
	private IJdbcDao jdbcDao;
	
    @Autowired
    private PasswordHelper passwordHelper;
    
    @Autowired 
	private IUserService userService;
    
	@Autowired 
	private IBaseService<User> baseService;
	
	@Autowired
	private IRoleService roleService;
	
    @Test
	public void userTest() throws Exception{
//    	Group group = this.groupService.getGroupById("41");
//    	assertEquals( group.getName(), "采购组" );
//    	assertEquals( group.getType(), "procurement" );
//    	Company company = this.companyService.getCompanyById(1);
    	
//    	Role role = this.roleService.getRoleById("1");
		User user1 = new User();
		user1.setName("admin");
		user1.setPasswd("123");
		this.passwordHelper.encryptPassword(user1);
		user1.setRole(new Role(6));
		user1.setRegisterDate(new Date());
		Serializable id1 = this.userService.doAdd(user1, false);
		
		/*for(int i=0; i<10; i++){
			User user = new User();
			user.setName("user"+i);
			u ser.setPasswd("123");
			user.setRegisterDate(new Date());
			user.setIsDelete(0);
//			user.setGroup(group);
			Serializable id = this.userService.doAdd(user, false);
		}*/
		
		
		//修改密码时判断，原密码是否输入正确
/*		User u = this.userService.getUserById((Integer) id);
		String oldPass = u.getPasswd();
		
		u.setPasswd("123");
		this.passwordHelper.encryptPassword(u);
		
        String newPass = u.getPasswd(); 
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("name", "user3");
    	List<User> list = this.baseService.findByWhere("User", params);
    	assertEquals(list.size(), 1);
    	
    	User user = this.baseService.findUnique("User", params);
    	assertEquals(user.getName(), "user3");*/
	}
}
