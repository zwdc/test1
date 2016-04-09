package com.hdc.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hdc.entity.FeedbackFrequency;
import com.hdc.entity.FeedbackRecord;
import com.hdc.entity.Page;
import com.hdc.entity.Parameter;
import com.hdc.service.IBaseService;
import com.hdc.service.IFeedbackRecordService;
import com.hdc.service.IProcessService;
import com.hdc.util.BeanUtils;
import com.hdc.util.Constants;
import com.hdc.util.Constants.FeedbackStatus;
import com.hdc.util.upload.FileUploadUtils;

@Service
public class FeedbackRecordServiceImpl implements IFeedbackRecordService {

	@Autowired
	private IBaseService<FeedbackRecord> baseService;
	
	@Autowired
	private IProcessService processService;
	
	@Override
	public List<FeedbackRecord> getListPage(Parameter param,
			Page<FeedbackRecord> page) throws Exception {
		return this.baseService.findListPage("FeedbackRecord", param, null, page, true);
	}
	
	@Override
	public List<FeedbackRecord> getAllList() throws Exception {
		String hql = "from FeedbackRecord where isDelete = 0 order by createDate desc";
		return this.baseService.find(hql);
	}
	
	@Override
	public FeedbackRecord findById(Integer id) throws Exception {
		return this.baseService.getBean(FeedbackRecord.class, id);
	}

	@Override
	public List<FeedbackRecord> findByTaskId(Integer id) throws Exception {
		String hql = "from FeedbackRecord where taskInfo.id = " + id +" order by createDate ASC";
		return this.baseService.find(hql);
	}

	@Override
	public Serializable doAdd(FeedbackRecord feedback) throws Exception {
		return this.baseService.add(feedback);
	}

	@Override
	public void doUpdate(FeedbackRecord feedback) throws Exception {
		this.baseService.update(feedback);
	}

	@Override
	public List<FeedbackRecord> findByDate(Date beginDate, Date endDate)
			throws Exception {
		String hql = "from FeedbackRecord where createDate between :begin and :end";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("begin", beginDate);
		params.put("end", endDate);
		return this.baseService.find(hql);
	}

	@Override
	public void doCompleteTask(FeedbackRecord feedback, String taskId,
			MultipartFile file, HttpServletRequest request) throws Exception {
		Integer id = feedback.getId();
		if(id == null) {
			if(!BeanUtils.isBlank(file)) {
				/*String filePath = FileUploadUtils.upload(request, file, Constants.FILE_PATH);
				feedback.setFilePath(filePath);
				feedback.setFileName(file.getOriginalFilename());
				feedback.setUploadDate(new Date());*/
			}
			feedback.setStatus(FeedbackStatus.FEEDBACKING.toString());
			feedback.setIsDelay(0);		//是否迟报，得根据时间判断
			this.baseService.add(feedback);
			
		} else {
			this.baseService.update(feedback);
		}
		
		//如果有提示的代办任务，则完成任务。
		if(StringUtils.isNotBlank(taskId)) {
			this.processService.complete(taskId, null, null);
		}
		
	}

}
