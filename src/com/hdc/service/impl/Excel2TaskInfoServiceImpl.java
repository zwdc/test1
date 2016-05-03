package com.hdc.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdc.entity.FeedbackFrequency;
import com.hdc.entity.Message;
import com.hdc.entity.TaskInfo;
import com.hdc.entity.TaskSource;
import com.hdc.service.IBaseService;
import com.hdc.service.IExcel2TaskInfoService;
import com.hdc.service.IGroupService;

import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;

/**
 * 将excel导入到TaskInfo表  Service
 * @author qdj
 *
 */
@Service
public class Excel2TaskInfoServiceImpl implements IExcel2TaskInfoService{
	@Autowired
	private IBaseService<TaskInfo> baseService;
	@Autowired
	private IGroupService groupService;
	private StringBuffer hostGroupId;
	
	public Message readXls(String Path) {
		Message message=new Message();
		message.setStatus(true);//默认为真
		Workbook rwb=null;
		try {
			rwb = Workbook.getWorkbook(new File(Path));
		} catch (BiffException e1) {
			// TODO Auto-generated catch block
			message.setMessage("获取Excel文件失败");;
			return message;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			message.setMessage("获取Excel文件失败");;
			return message;
		}
		Sheet rs=rwb.getSheet(0);//取第一个sheet
        int rows=rs.getRows();//得到所有的行
		TaskInfo taskInfo = null;
		List<TaskInfo> list = new ArrayList<TaskInfo>();
		// 循环工作表Sheet
		// 循环行Row
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone zone = TimeZone.getTimeZone("GMT");
		sdf.setTimeZone(zone);
		for (int rowNum = 0; rowNum <rows; rowNum++) {	
			taskInfo = new TaskInfo();
			CellType titleType=rs.getCell(0,rowNum).getType();//任务内容
			CellType createType=rs.getCell(1,rowNum).getType();//生成任务时间
			CellType endType = rs.getCell(2,rowNum).getType();//任务办结时限	
			CellType claimType = rs.getCell(3,rowNum).getType();//任务签收时限
			CellType hostType = rs.getCell(4,rowNum).getType();//牵头单位
			CellType assistantType = rs.getCell(5,rowNum).getType();//协办单位
			CellType urgencyType =rs.getCell(6,rowNum).getType();//缓急程度
			CellType fb_frequencyType = rs.getCell(7,rowNum).getType();//反馈频度
			CellType task_sourceType = rs.getCell(8,rowNum).getType();//任务来源
			
			if (titleType!=CellType.EMPTY && titleType!=CellType.ERROR) {
				taskInfo.setTitle(rs.getCell(0,rowNum).getContents());
			}else{
				message.setMessage("第"+rowNum+"行,【任务内容】字段格式不对");
				message.setStatus(false);
				break;
			}
			if (createType==CellType.DATE &&createType!=CellType.EMPTY && createType!=CellType.ERROR) {
				DateCell dc = (DateCell)rs.getCell(1,rowNum);
				Date date=dc.getDate();  
				taskInfo.setCreateTaskDate(date);
			}else{
				message.setMessage("第"+rowNum+"行,【任务发布时间】字段格式不对");
				message.setStatus(false);
				break;
			}
			if (endType==CellType.DATE&&endType!=CellType.EMPTY && endType!=CellType.ERROR) {
				DateCell dc = (DateCell)rs.getCell(2,rowNum);
				Date date=dc.getDate(); 
				taskInfo.setEndTaskDate(date);
			}else{
				message.setMessage("第"+rowNum+"行,【任务截止时间】字段格式不对");
				message.setStatus(false);
				break;
			}
			if (claimType==CellType.DATE&&claimType!=CellType.EMPTY && claimType!=CellType.ERROR) {
				DateCell dc = (DateCell)rs.getCell(3,rowNum);
				Date date=dc.getDate(); 
				taskInfo.setClaimLimitDate(date);
			}else{
				message.setMessage("第"+rowNum+"行,【任务签收截止时间】字段格式不对");
				message.setStatus(false);
				break;
			}
			if (hostType!=CellType.EMPTY && hostType!=CellType.ERROR) {
					String hostGroupStr=String.valueOf(rs.getCell(4,rowNum).getContents());
					String[] hostGroupName=hostGroupStr.split(",");
					hostGroupId =new StringBuffer();;
					Map<String,Object> map=new HashMap<String, Object>();
					for(int i=0;i<hostGroupName.length;i++){
						map.put("name", hostGroupName[i]);
						try {
							Integer groupId=this.groupService.getGroupByName(map).getId();
							if(i!=0){
								hostGroupId.append(",");	
								hostGroupId.append(groupId.toString());
							}else{
								hostGroupId.append(groupId.toString());
							}		
						} catch (Exception e) {
							// TODO Auto-generated catch block
							message.setMessage("第"+rowNum+"行,获取第"+i+"个牵头单位ID出错");
							message.setStatus(false);
							break;
						}
					}
					taskInfo.setHostGroup(hostGroupId.toString());
			}else{
					message.setMessage("第"+rowNum+"行,【牵头部门】字段格式不对");
					message.setStatus(false);
					break;
			}
			if (assistantType!=CellType.EMPTY && assistantType!=CellType.ERROR) {
				taskInfo.setAssistantGroup(rs.getCell(5,rowNum).getContents());
			}else{
				message.setMessage("第"+rowNum+"行,【协办单位】字段格式不对");
				message.setStatus(false);
				break;
			}
			if (urgencyType!=CellType.EMPTY && urgencyType!=CellType.ERROR) {
				taskInfo.setUrgency(Integer.valueOf(rs.getCell(6,rowNum).getContents()));
			}else{
				message.setMessage("第"+rowNum+"行,【急缓程度】字段格式不对");
				message.setStatus(false);
				break;
				}
			if (fb_frequencyType!=CellType.EMPTY && fb_frequencyType!=CellType.ERROR) {
				FeedbackFrequency fb=new FeedbackFrequency();
				fb.setId(Integer.valueOf(rs.getCell(7,rowNum).getContents()));
				taskInfo.setFbFrequency(fb);
			}else{
				message.setMessage("第"+rowNum+"行,【反馈频度】字段格式不对");
				message.setStatus(false);
				break;
			}
			if (task_sourceType!=CellType.EMPTY && fb_frequencyType!=CellType.ERROR) {
				TaskSource ts=new TaskSource();
				ts.setId(Integer.valueOf(rs.getCell(8,rowNum).getContents()));
				taskInfo.setTaskSource(ts);
			}else{
				message.setMessage("第"+rowNum+"行,【任务来源】字段格式不对");
				message.setStatus(false);
				break;
			}
			taskInfo.setStatus("WAITING_FOR_APPROVAL");
			list.add(taskInfo);
		}
		if(message.getStatus()==true){
			message.setTitle("提示");
			message.setMessage("Excel导入成功！");
			message.setData(list);
		}
		return message;
	}
	
}
