package com.model2.mvc.view.purchase;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class ListPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurchaseService service = new PurchaseServiceImpl();
		User user = (User)request.getSession().getAttribute("user");


		int currentPage = 1;

		if (request.getParameter("page") != null) {
			currentPage = Integer.parseInt(request.getParameter("page"));
		} else if (request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		int pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));

		//DB 접속을 위한 search
		Search search = new Search();
		search.setCurrentPage(currentPage);
		search.setPageSize(pageSize);

		//DB에 접속하여 결과를 Map으로 가져옴
		Map<String, Object> map = service.getPurchaseList(search, user.getUserId());

		//pageView를 위한 객체
		Page pageInfo = new Page(currentPage, ((Integer) map.get("count")).intValue(),
				Integer.parseInt(getServletContext().getInitParameter("pageUnit")), pageSize);
		
		System.out.println("ListPurchaseAction-pageInfo : " + pageInfo);
		System.out.println("ListPurchaseAction-list.size() : " + ((List)map.get("list")).size());
		
		request.setAttribute("list", map.get("list"));
		request.setAttribute("pageInfo", pageInfo);

		return "forward:/purchase/listPurchase.jsp";
	}
}
