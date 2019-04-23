<%@page import="com.model2.mvc.common.Page"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.model2.mvc.service.domain.Purchase"%>

<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%
	Page pageInfo = (Page)request.getAttribute("pageInfo");
	List<Purchase> list = (List<Purchase>)request.getAttribute("list"); 	
%>


<html>
<head>
<title>구매 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	function fncGetPurchaseList(currentPage) {
		document.detailForm.currentPage.value = currentPage;
		document.detailForm.menu.value =  <%="\""+request.getParameter("menu")+"\""%>;

		document.detailForm.submit();
	}
	
	function fncUpdatePurchaseCode(currentPage,tranNo){
		var URI = "/updateTranCode.do?page=" + <%=pageInfo.getCurrentPage() %>+ "&tranNo=" 
				+ tranNo + "&tranCode=3"
				
		locate.href = URI;
	}
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<form name="detailForm" action="/listPurchase.do" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">구매 목록조회</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<td colspan="11">전체 <%=pageInfo.getTotalCount() %> 건수, 현재 <%=pageInfo.getCurrentPage() %> 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">회원ID</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">회원명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">전화번호</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">배송현황</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">정보수정</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>

	
	<%for(int i = 0; i < list.size(); i++){ %>
	<tr class="ct_list_pop">
		<td align="center">
			<a href="/getPurchase.do?tranNo=<%=list.get(i).getTranNo()%>"><%=i+1 %></a>
		</td>
		<td></td>
		<td align="left">
			<a href="/getUser.do?userId=<%=list.get(i).getBuyer().getUserId() %>"><%=list.get(i).getBuyer().getUserId() %></a>
		</td>
		<td></td>
		<td align="left"><%=list.get(i).getReceiverName() %></td>
		<td></td>
		<td align="left"><%=list.get(i).getReceiverPhone() %></td>
		<td></td>
		<td align="left">현재
				<%if(list.get(i).getTranCode().equals("1")){ %>
					배송 준비
				<%}else if(list.get(i).getTranCode().equals("2")){ %>
					배송중 
				<%}else if(list.get(i).getTranCode().equals("3")){ %>
					거래 완료
				<%} %>
				상태 입니다.</td>
		<td></td>
		<%if(list.get(i).getTranCode().equals("2")){ %>
		<td align="left">
			<a href="javascript:funUpdatePurchaseCode(<%=pageInfo.getCurrentPage() %>,<%=list.get(i).getTranNo()%>)">수취확인</a>
		</td>
		<%} %>
	</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	<%} %>
	
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
	<tr>
		<td align="center">
		 <input type="hidden" id="currentPage" name="currentPage" value=""/>
		 <input type="hidden" id="menu" name="menu" value=""/>
		 <%if(pageInfo.getBeginUnitPage() != 1){ %>
			<a href="javascript:fncGetPurchaseList(<%=pageInfo.getBeginUnitPage()-1%>);"> ◁ </a>
		<%}else{%> ◁ <%}%>
		
		<%for(int i = pageInfo.getBeginUnitPage(); i <= pageInfo.getEndUnitPage(); i++){%>
			<%if(i != pageInfo.getCurrentPage()){%>
				<a href="javascript:fncGetPurchaseList(<%=i%>);"><%=i%>	</a>
			<%}else{%><font style="font-weight: bold"><%=i%></font><%}%>
		<%}%>
		
		<%if(pageInfo.getEndUnitPage() != pageInfo.getMaxPage()){ %>
			<a href="javascript:fncGetPurchaseList(<%=pageInfo.getEndUnitPage()+1%>);">	▷ </a>
		<%}else{%>	▷ <%}%>
		
		</td>
	</tr>
</table>

<!--  페이지 Navigator 끝 -->
</form>

</div>

</body>
</html>