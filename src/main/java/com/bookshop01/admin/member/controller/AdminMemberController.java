package com.bookshop01.admin.member.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

public interface AdminMemberController {
	public ModelAndView adminGoodsMain(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ResponseEntity<Object> adminGoodsMain2(@RequestBody Map<String,String>dateMapdateMap, HttpServletRequest request, HttpServletResponse response)  throws Exception;	
	public ModelAndView memberDetail(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public void modifyMemberInfo(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView deleteMember(HttpServletRequest request, HttpServletResponse response)  throws Exception;
}
