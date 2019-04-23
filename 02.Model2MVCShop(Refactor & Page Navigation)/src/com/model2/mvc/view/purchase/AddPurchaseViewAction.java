package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class AddPurchaseViewAction extends Action{

	public AddPurchaseViewAction() {
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ProductService productService = new ProductServiceImpl();
		HttpSession session = request.getSession();
		
		
		Purchase purchase = new Purchase();
		Product product = productService.getProduct(Integer.parseInt(request.getParameter("prodNo")));
		User user = (User)session.getAttribute("user");
				
		//purchase ¼³Á¤
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		
		request.setAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
}
