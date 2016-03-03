package com.hdc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 督察接收
 * @author zhao
 *
 */
@Controller
@RequestMapping("/superviserReveive")
public class SuperviseReceiveController {

	/**
	 * 跳转到督察接收
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public ModelAndView toSuperviseReceive() {
		ModelAndView mv = new ModelAndView("superviseReceive/list_receive");
		return mv;
	}
	
	
	
}
