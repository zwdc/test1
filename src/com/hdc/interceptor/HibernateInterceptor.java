package com.hdc.interceptor;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.hdc.entity.BaseCommonEntity;
import com.hdc.entity.BaseServiceEntity;
import com.hdc.entity.User;
import com.hdc.util.UserUtil;

/**
 * hibernate 拦截器，记录用户的操作
 * @author ZML
 *
 */
public class HibernateInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1120733347460916412L;

    /**
     * entity - POJO对象
     * id - POJO对象的主键
     * state - POJO对象的每一个属性所组成的集合(除了ID)
     * propertyNames - POJO对象的每一个属性名字组成的集合(除了ID)
     * types - POJO对象的每一个属性类型所对应的Hibernate类型组成的集合(除了ID)
     */
	@Override
	public boolean onFlushDirty(Object entity,Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames,Type[] types) throws CallbackException {
	    
        //记录更新时间、更新人
        if (entity instanceof BaseServiceEntity || entity instanceof BaseCommonEntity) {
        	boolean updateDateFlag = false;
        	boolean updateUserIdFlag = false;
        	for (int i = 0, length = propertyNames.length; i < length; i++) {
        		if ( propertyNames[i].equalsIgnoreCase("updateDate") && !updateDateFlag ) {
        			updateDateFlag = true;
					Date updateDate = new Date();
					currentState[i] = updateDate;
					if(entity instanceof BaseServiceEntity) {
						((BaseServiceEntity) entity).setUpdateDate(updateDate);
					} else if(entity instanceof BaseCommonEntity) {
						((BaseCommonEntity) entity).setUpdateDate(updateDate);
					}
				} else if ( propertyNames[i].equalsIgnoreCase("updateUserId") && !updateUserIdFlag ) {
					updateUserIdFlag = true;
					User user = UserUtil.getUserFromSession();
					if (user != null) {
						currentState[i] = user.getId();
						if(entity instanceof BaseServiceEntity) {
							((BaseServiceEntity) entity).setUpdateUserId(user.getId());
						} else if(entity instanceof BaseCommonEntity) {
							((BaseCommonEntity) entity).setUpdateUserId(user.getId());
						}
					}
				}
        		if(updateDateFlag && updateUserIdFlag){
        			break;
        		}
        	}
        	return updateDateFlag && updateUserIdFlag;
        }
        return false;	//如果返回值是false，Hibernate会产生一条Update SQL语句将拦截器的操作结果取消。
    }
	
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] currentState, String[] propertyNames, Type[] types) {
		
		//记录添加时间、添加人、isDelete、数据所属公司、部门、职位
        if (entity instanceof BaseServiceEntity || entity instanceof BaseCommonEntity) {
        	boolean createDateFlag = false;
        	boolean createUserIdFlag = false;
        	boolean isDeleteFlag = false;
        	
        	User user = UserUtil.getUserFromSession();
        	for (int i = 0, length = propertyNames.length; i < length; i++) {
        		if ( propertyNames[i].equalsIgnoreCase("createDate") && !createDateFlag ) {
        			createDateFlag = true;
					Date createDate = new Date();
					currentState[i] = createDate;
					if(entity instanceof BaseServiceEntity) {
						((BaseServiceEntity) entity).setCreateDate(createDate);
					} else if(entity instanceof BaseCommonEntity) {
						((BaseCommonEntity) entity).setCreateDate(createDate);
					}
				} else if ( propertyNames[i].equalsIgnoreCase("createUserId") && !createUserIdFlag ) {
					createUserIdFlag = true;
					if (user != null) {
						currentState[i] = user.getId();
						if(entity instanceof BaseServiceEntity) {
							((BaseServiceEntity) entity).setCreateUserId(user.getId());
						} else if(entity instanceof BaseCommonEntity) {
							((BaseCommonEntity) entity).setCreateUserId(user.getId());
						}
					}
				} else if ( propertyNames[i].equalsIgnoreCase("isDelete") && !isDeleteFlag ) {
					isDeleteFlag = true;
					currentState[i] = 0;
					if(entity instanceof BaseServiceEntity) {
						((BaseServiceEntity) entity).setIsDelete(0);
					} else if(entity instanceof BaseCommonEntity) {
						((BaseCommonEntity) entity).setIsDelete(0);
					}
				}
        		
        		if(createDateFlag && createUserIdFlag && isDeleteFlag){
        			return true;
        		}
        	}
        }
        return false;
	}
	
}
