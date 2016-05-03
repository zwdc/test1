package com.hdc.service;

import java.io.IOException;

import com.hdc.entity.Message;

public interface IExcel2TaskInfoService {
	public Message readXls(String Path) throws IOException ;
}
