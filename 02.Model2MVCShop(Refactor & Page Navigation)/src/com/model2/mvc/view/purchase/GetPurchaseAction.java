package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class GetPurchaseAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurchaseService service = new PurchaseServiceImpl();
		
		Purchase purchase = service.getPurchase(Integer.parseInt(request.getParameter("tranNo")));
		request.setAttribute("purchase", purchase);
		System.out.println("getPurchaseAction�� getTranCode�� : "+purchase.getTranCode());
		
		return "forward:/purchase/getPurchase.jsp";
	}
}
