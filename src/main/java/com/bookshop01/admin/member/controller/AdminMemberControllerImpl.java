package com.bookshop01.admin.member.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.admin.member.service.AdminMemberService;
import com.bookshop01.common.base.BaseController;
import com.bookshop01.member.vo.MemberVO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller("adminMemberController")
@RequestMapping(value="/admin/member")
public class AdminMemberControllerImpl extends BaseController  implements AdminMemberController{
	@Autowired
	private AdminMemberService adminMemberService;
	@RequestMapping(value="/adminMemberMain.do")
	public ModelAndView adminGoodsMain( HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		ModelAndView mav = new ModelAndView(viewName);
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
		Calendar time = Calendar.getInstance();
		time.add(Calendar.YEAR, -5);
		String before = format1.format(time.getTime());
		time.add(Calendar.YEAR, +5);
		String now = format1.format(time.getTime());
		HashMap<String,Object> condMap=new HashMap<String,Object>();
		condMap.put("beginDate",before);
		String chapter, pageNum = null;
		condMap.put("endDate",now);
		chapter = request.getParameter("chapter");
		pageNum = request.getParameter("pageNum");
		if(chapter== null) {
			chapter = "1";
		}
		
		if(pageNum== null) {
			pageNum = "1";
		}
		
		condMap.put("chapter", chapter);
		condMap.put("pageNum", pageNum);
//		System.out.println(before+" / "+now);
		ArrayList<MemberVO> member_list=adminMemberService.listMember(condMap);
		mav.addObject("member_list",member_list);
		
		return mav;	
	}
	
	@RequestMapping(value="/adminMemberMain2.do",produces="application/JSON;charset=UTF-8", method={RequestMethod.POST,RequestMethod.GET})
	public ResponseEntity<Object> adminGoodsMain2(@RequestParam Map<String,String>dateMap, HttpServletRequest request, HttpServletResponse response) throws Exception{
		ResponseEntity<Object> resEntity = null;

		try { ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dateMap);
				String chapter = dateMap.get("chapter");
				String pageNum = dateMap.get("pageNum");
				String[] getDatum = json.split(",");
				String[] begin = getDatum[0].split(":");
				String[] end = getDatum[1].split(":");
				String beginDate = begin[1].replaceAll("\"","").replaceAll(" ","");
				String endDate = end[1].replaceAll("}","").replaceAll("\"","").replaceAll(" ","");

				HashMap<String,Object> condMap=new HashMap<String,Object>();
				condMap.put("beginDate",beginDate);
				condMap.put("endDate",endDate);

				if(chapter== null) {
					chapter = "1";
				}

				condMap.put("chapter",chapter);
				if(pageNum== null) {
					pageNum = "1";
				}
				condMap.put("pageNum",pageNum);
				String search_type = dateMap.get("search_type");
				String search_word = dateMap.get("search_word");
				condMap.put("search_type", search_type);
				condMap.put("search_word", search_word);

				List<MemberVO> member_list=adminMemberService.listMember(condMap);

				HashMap<String, Object> condMap1=new HashMap<String, Object>();
				condMap1.put("member_list",member_list);
				condMap1.put("pageNum",pageNum);
				condMap1.put("chapter",chapter);
				condMap1.put("search_type", search_type);
				condMap1.put("search_word", search_word);
				resEntity = new ResponseEntity<Object>(condMap1,HttpStatus.OK);
		} catch(Exception e) {
			resEntity = new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
			e.printStackTrace();
		}
		return resEntity;

	}
	
	
	@RequestMapping(value="/memberDetail.do" ,method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView memberDetail(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		String member_id=request.getParameter("member_id");
		MemberVO member_info=adminMemberService.memberDetail(member_id);
		mav.addObject("member_info",member_info);
		return mav;
	}
	
	@RequestMapping(value="/modifyMemberInfo.do" ,method={RequestMethod.POST,RequestMethod.GET})
	public void modifyMemberInfo(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		HashMap<String,String> memberMap=new HashMap<String,String>();
		String val[]=null;
		PrintWriter pw=response.getWriter();
		String member_id=request.getParameter("member_id");
		String mod_type=request.getParameter("mod_type");
		String value =request.getParameter("value");
		if(mod_type.equals("member_birth")){
			val=value.split(",");
			memberMap.put("member_birth_y",val[0]);
			memberMap.put("member_birth_m",val[1]);
			memberMap.put("member_birth_d",val[2]);
			memberMap.put("member_birth_gn",val[3]);
		}else if(mod_type.equals("tel")){
			val=value.split(",");
			memberMap.put("tel1",val[0]);
			memberMap.put("tel2",val[1]);
			memberMap.put("tel3",val[2]);
			
		}else if(mod_type.equals("hp")){
			val=value.split(",");
			memberMap.put("hp1",val[0]);
			memberMap.put("hp2",val[1]);
			memberMap.put("hp3",val[2]);
			memberMap.put("smssts_yn", val[3]);
		}else if(mod_type.equals("email")){
			val=value.split(",");
			memberMap.put("email1",val[0]);
			memberMap.put("email2",val[1]);
			memberMap.put("emailsts_yn", val[2]);
		}else if(mod_type.equals("address")){
			val=value.split(",");
			memberMap.put("zipcode",val[0]);
			memberMap.put("roadAddress",val[1]);
			memberMap.put("jibunAddress", val[2]);
			memberMap.put("namujiAddress", val[3]);
		}
		
		memberMap.put("member_id", member_id);
		
		adminMemberService.modifyMemberInfo(memberMap);
		pw.print("mod_success");
		pw.close();		
		
	}
	
	@RequestMapping(value="/deleteMember.do" ,method={RequestMethod.POST})
	public ModelAndView deleteMember(HttpServletRequest request, HttpServletResponse response)  throws Exception {
		ModelAndView mav = new ModelAndView();
		HashMap<String,String> memberMap=new HashMap<String,String>();
		String member_id=request.getParameter("member_id");
		String del_yn=request.getParameter("del_yn");
		memberMap.put("del_yn", del_yn);
		memberMap.put("member_id", member_id);
		
		adminMemberService.modifyMemberInfo(memberMap);
		mav.setViewName("redirect:/admin/member/adminMemberMain.do");
		return mav;
		
	}
		
}
