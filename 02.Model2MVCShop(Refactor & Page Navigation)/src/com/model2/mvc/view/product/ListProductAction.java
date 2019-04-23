package com.model2.mvc.view.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.CommonUtil;
import com.model2.mvc.common.util.HttpUtil;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class ListProductAction extends Action {

	public ListProductAction() {
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Search search = new Search();

		search.setSearchCondition(request.getParameter("searchCondition"));
		if (!(CommonUtil.null2str(request.getParameter("searchKeyword")).equals(""))) {
			if (request.getMethod().equals("GET")) {
				search.setSearchKeyword(HttpUtil.convertKo(request.getParameter("searchKeyword")));
			} else {
				search.setSearchKeyword(request.getParameter("searchKeyword"));
			}
		}

		int currentPage = 1;
		if (request.getParameter("page") != null) {
			currentPage = Integer.parseInt(request.getParameter("page"));
		} else if (request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		search.setCurrentPage(currentPage);

		int pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
		search.setPageSize(pageSize);

		ProductService service = new ProductServiceImpl();
		Map<String, Object> map = service.getProductList(search);

		Page pageInfo = new Page(currentPage, ((Integer) map.get("count")).intValue(),
				Integer.parseInt(getServletContext().getInitParameter("pageUnit")), pageSize);
		System.out.println(pageInfo);

		// M,V ¿¬°á
		request.setAttribute("list", map.get("list"));
		request.setAttribute("search", search);
		request.setAttribute("pageInfo", pageInfo);

		return "forward:/product/listProduct.jsp";
	}

}
