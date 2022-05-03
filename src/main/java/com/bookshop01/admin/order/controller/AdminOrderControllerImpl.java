package com.bookshop01.admin.order.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.admin.order.service.AdminOrderService;
import com.bookshop01.common.base.BaseController;
import com.bookshop01.order.vo.OrderVO;

@Controller("adminOrderController")
@RequestMapping(value="/admin/order")
public class AdminOrderControllerImpl extends BaseController  implements AdminOrderController{
	@Autowired
	private AdminOrderService adminOrderService;
	
	@Override
	@RequestMapping(value="/adminOrderMain.do" ,method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView adminOrderMain(@RequestParam Map<String, String> dateMap,
			                          HttpServletRequest request, HttpServletResponse response)  throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		System.out.println("aBcDE");
		String fixedSearchPeriod = dateMap.get("fixedSearchPeriod");
		String section = dateMap.get("section");
		String pageNum = dateMap.get("pageNum");
		HashMap<String,Object> condMap=new HashMap<String,Object>();
		if("dsearch".equals(fixedSearchPeriod) || "today".equals(fixedSearchPeriod) || "one_week".equals(fixedSearchPeriod) || "two_week".equals(fixedSearchPeriod) || "one_month".equals(fixedSearchPeriod) || "two_month".equals(fixedSearchPeriod) || "three_month".equals(fixedSearchPeriod) || "four_month".equals(fixedSearchPeriod)) {
			String beginDate = dateMap.get("beginDates");
			String endDate = dateMap.get("endDates");
			condMap.put("beginDate", beginDate);
			condMap.put("endDate", endDate);
			String beginDate1[]=beginDate.split("-");
			String endDate2[]=endDate.split("-");
			mav.addObject("beginYear",beginDate1[0]);
			mav.addObject("beginMonth",beginDate1[1]);
			mav.addObject("beginDay",beginDate1[2]);
			mav.addObject("endYear",endDate2[0]);
			mav.addObject("endMonth",endDate2[1]);
			mav.addObject("endDay",endDate2[2]);
		} else {
			String [] tempDate=calcSearchPeriod(null).split(",");
			String beginDate=tempDate[0];
			String endDate=tempDate[1];
			System.out.println("출력");
			condMap.put("beginDate", beginDate);
			condMap.put("endDate", endDate);
			String beginDate1[]=beginDate.split("-");
			String endDate2[]=endDate.split("-");
			mav.addObject("beginYear",beginDate1[0]);
			mav.addObject("beginMonth",beginDate1[1]);
			mav.addObject("beginDay",beginDate1[2]);
			mav.addObject("endYear",endDate2[0]);
			mav.addObject("endMonth",endDate2[1]);
			mav.addObject("endDay",endDate2[2]);
		}
		String search_type = dateMap.get("search_type");
		String search_word = dateMap.get("search_word");
		condMap.put("search_type", search_type);
		condMap.put("search_word", search_word);
		if(section== null) {
			section = "1";
		}
		condMap.put("section",section);
  		if(pageNum== null) {
			pageNum = "1";
		}
		condMap.put("pageNum",pageNum);
 		List<OrderVO> newOrderList=adminOrderService.listNewOrder(condMap);
		mav.addObject("newOrderList",newOrderList);
		

		
		mav.addObject("section", section);
		mav.addObject("pageNum", pageNum);
		return mav;
		
	}
	
	@Override
	@RequestMapping(value="/modifyDeliveryState.do" ,method={RequestMethod.POST})
	public ResponseEntity modifyDeliveryState(@RequestParam Map<String, String> deliveryMap, 
			                        HttpServletRequest request, HttpServletResponse response)  throws Exception {
		adminOrderService.modifyDeliveryState(deliveryMap);
		
		String message = null;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		message  = "mod_success";
		resEntity =new ResponseEntity(message, responseHeaders, HttpStatus.OK);
		return resEntity;
		
	}
	
	@Override
	@RequestMapping(value="/orderDetail.do" ,method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView orderDetail(@RequestParam("order_id2") int order_id, 
			                      HttpServletRequest request, HttpServletResponse response)  throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		Map orderMap =adminOrderService.orderDetail(order_id);
		mav.addObject("orderMap", orderMap);
		return mav;
	}
	
}
