<%@ page import="com.model2.mvc.common.util.CommonUtil"%>
<%@ page import="com.model2.mvc.common.Page"%>
<%@ page import="com.model2.mvc.service.domain.User"%>
<%@ page import="com.model2.mvc.service.domain.Product"%>
<%@ page import="com.model2.mvc.common.Search"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page language="java"%>
<%@ page contentType="text/html; charset=EUC-KR"%>
<%@ page pageEncoding="EUC-KR"%>

<%
	Search search = (Search)request.getAttribute("search");
	Page pageInfo = (Page)request.getAttribute("pageInfo");
	System.out.println("jspSearchVO : " + search);

	
	List<Product> list = (List<Product>)request.getAttribute("list");
	User user = (User)session.getAttribute("user");
	String menu = request.getParameter("menu");
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>상품 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
function fncGetProductList(currentPage){
	document.detailForm.currentPage.value = currentPage;
	document.detailForm.menu.value =  <%="\""+request.getParameter("menu")+"\""%>;
	
	//검색 조건 Validation Check
	if(document.detailForm.searchCondition.value != 1){
		if(isNaN(document.detailForm.searchKeyword.value)){
			alert("숫자만 가능합니다.")
			return;
		}
	}

	document.detailForm.submit();
}
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm" action="<%=request.getParameter("menu").equals("search")?"/listProduct.do?menu=search":"/listProduct.do?menu=manage" %>" method="post" onsubmit="return false">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">
					
						<%if(menu.equals("search")) {%>
							상품 목록조회
						<%}else if(menu.equals("manage")){ %>
							상품 관리
						<%} %>
					
					</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		
		<td align="right">
			<select name="searchCondition" class="ct_input_g" style="width:80px" id="searchCondition">
				<option value="0" <%if(search.getSearchCondition()!=null&&search.getSearchCondition().equals("0")){%>selected="selected"<%} %>>상품번호</option>
				<option value="1" <%if(search.getSearchCondition()!=null&&search.getSearchCondition().equals("1")){%>selected="selected"<%} %>>상품명</option>
				<option value="2" <%if(search.getSearchCondition()!=null&&search.getSearchCondition().equals("2")){%>selected="selected"<%} %>>상품가격</option>
			</select>
			<input type="text" name="searchKeyword" id="searchKeyword" onkeypress="if( event.keyCode==13 ){javascript:fncGetProductList(<%=pageInfo.getCurrentPage() %>);}" class="ct_input_g" style="width:200px; height:19px" value="<%if(!CommonUtil.null2str(search.getSearchKeyword()).equals("")){ %><%=search.getSearchKeyword()%><%} %>"/>
		</td>
	
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetProductList(<%=pageInfo.getCurrentPage()%>);">검색</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" >전체 <%=pageInfo.getTotalCount()%> 건수, 현재 <%=pageInfo.getCurrentPage()%> 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">가격</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">등록일</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b">현재상태</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
		
	<%for(int i=0;i<list.size();i++){%>
	<tr class="ct_list_pop">
	
		<td align="center"><%=i+1%></td>
		<td></td>
				<td align="left"><a href="/getProduct.do?prodNo=<%=list.get(i).getProdNo()%>&menu=<%=menu%>"><%=list.get(i).getProdName()%></a></td>
		<td></td>
		<td align="left"><%=list.get(i).getPrice()%></td>
		<td></td>
		<td align="left"><%=list.get(i).getRegDate()%></td>
		<td></td>
		<td align="left">
		
		<%if(list.get(i).getProTranCode()!=null){%>
			<%if(user != null && user.getRole().equals("admin")){%>
				<%if(list.get(i).getProTranCode().equals("1")){%>배송준비중&nbsp;<%if (menu.equals("manage")){ %><a href="/updateTranCodeByProd.do?page=<%=search.getCurrentPage() %>&prodNo=<%=list.get(i).getProdNo()%>&tranCode=2<%if(search.getSearchCondition() != null){%>&searchCondition=<%=search.getSearchCondition() %>&searchKeyword=<%=CommonUtil.null2str(search.getSearchKeyword()) %><%}%>">제품발송</a><%}}%>
				<%if(list.get(i).getProTranCode().equals("2")){%>배송중 <%}%>
				<%if(list.get(i).getProTranCode().equals("3")){%>거래완료 <%}%>
			<%}else {%>
				재고없음
			<%} %>
		<%}else{ %>
			판매중
		<%} %>
		
		</td>	
	</tr>	
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>	
	<%} %>
	
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="center">
		<input type="hidden" id="currentPage" name="currentPage" value=""/>
		<input type="hidden" id="menu" name="menu" value=""/>
		<%if(pageInfo.getBeginUnitPage() != 1){ %>
			<a href="javascript:fncGetProductList(<%=pageInfo.getBeginUnitPage()-1%>);"> ◁ </a>
		<%}else{%> ◁ <%}%>
		
		<%for(int i = pageInfo.getBeginUnitPage(); i <= pageInfo.getEndUnitPage(); i++){%>
			<%if(i != search.getCurrentPage()){%>
				<a href="javascript:fncGetProductList(<%=i%>);"><%=i%>	</a>
			<%}else{%><font style="font-weight: bold"><%=i%></font><%}%>
		<%}%>
		
		<%if(pageInfo.getEndUnitPage() != pageInfo.getMaxPage()){ %>
			<a href="javascript:fncGetProductList(<%=pageInfo.getEndUnitPage()+1%>);">	▷ </a>
		<%}else{%>	▷ <%}%>
		
		</td>
	</tr>
</table>
<!--  페이지 Navigator 끝 -->

</form>

</div>
</body>
</html>
