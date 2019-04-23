package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
public class PurchaseDAO {

	public PurchaseDAO() {
	}

	// 구매 정보 상세조회
	public Purchase findPurchase(int tranNo) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "SELECT" + 
				" u.*, p.*, t.*" + 
				" from users u, product p, transaction t" + 
				" where t.prod_no = p.prod_no" + 
				" AND u.user_id = t.buyer_id" + 
				" AND t.tran_no = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);

		ResultSet rs = stmt.executeQuery();
		Purchase purchase = null;
		if (rs.next()) {
			User user = new User();
			user = new User();
			user.setUserId(rs.getString("user_id"));
			user.setUserName(rs.getString("user_name"));
			user.setPassword(rs.getString("password"));
			user.setRole(rs.getString("role"));
			user.setPhone(rs.getString("cell_phone"));
			user.setAddr(rs.getString("addr"));
			user.setEmail(rs.getString("email"));
			user.setRegDate(rs.getDate("reg_date"));
			
			Product product = new Product();
			product = new Product();
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(rs.getInt("price"));
			product.setFileName(rs.getString("image_file"));
			product.setRegDate(rs.getDate("reg_date"));
			product.setProTranCode(rs.getString("tran_status_code"));
			
			purchase = new Purchase();
			purchase.setTranNo(tranNo);
			purchase.setPurchaseProd(product);
			purchase.setBuyer(user);
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDlvyAddr(rs.getString("demailaddr"));// dlvy_addr
			purchase.setDlvyRequest(rs.getString("dlvy_request"));
			purchase.setTranCode(rs.getString("tran_status_code"));
			purchase.setDlvyDate((rs.getString("dlvy_date") != null) ? rs.getString("dlvy_date").substring(0, 10)
					: rs.getString("dlvy_date"));
			purchase.setOrderDate(rs.getDate("order_data"));// order_date
		}

		stmt.close();
		rs.close();
		con.close();

		return purchase;
	}

	public Purchase findPurchase2(int prodNo) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "SELECT" + 
				" u.*, p.*, t.*" + 
				" from users u, product p, transaction t" + 
				" where t.prod_no = p.prod_no" + 
				" AND u.user_id = t.buyer_id" + 
				" AND p.prod_no = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();
		Purchase purchase = null;
		if (rs.next()) {
			User user = new User();
			user = new User();
			user.setUserId(rs.getString("user_id"));
			user.setUserName(rs.getString("user_name"));
			user.setPassword(rs.getString("password"));
			user.setRole(rs.getString("role"));
			user.setPhone(rs.getString("cell_phone"));
			user.setAddr(rs.getString("addr"));
			user.setEmail(rs.getString("email"));
			user.setRegDate(rs.getDate("reg_date"));
			
			Product product = new Product();
			product = new Product();
			product.setProdNo(prodNo);
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(rs.getInt("price"));
			product.setFileName(rs.getString("image_file"));
			product.setRegDate(rs.getDate("reg_date"));
			product.setProTranCode(rs.getString("tran_status_code"));
			
			purchase = new Purchase();
			purchase.setTranNo(rs.getInt("tran_no"));
			purchase.setPurchaseProd(product);
			purchase.setBuyer(user);
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDlvyAddr(rs.getString("demailaddr"));// dlvy_addr
			purchase.setDlvyRequest(rs.getString("dlvy_request"));
			purchase.setTranCode(rs.getString("tran_status_code"));
			purchase.setDlvyDate((rs.getString("dlvy_date") != null) ? rs.getString("dlvy_date").substring(0, 10)
					: rs.getString("dlvy_date"));
			purchase.setOrderDate(rs.getDate("order_data"));// order_date
		}

		stmt.close();
		rs.close();
		con.close();

		return purchase;
	}

	// 구매 목록 조회
	public HashMap<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
		System.out.println("::getPurchaseList 시작");
		// 1.DBUtil을 이용하여 DB연결
		Connection con = DBUtil.getConnection();

		// 2.DB에 보낼 DML 작성 및 값 설정
		String sql = "SELECT" + 
				" u.*, p.*, t.*" + 
				" from users u, product p, transaction t" + 
				" where t.prod_no = p.prod_no" + 
				" AND u.user_id = t.buyer_id" +
				" AND buyer_id = '" + buyerId + "'";

		// 3. 총 Row수 구함
		int totalUnit = getTotalCount(sql);
		System.out.println("구매 목록 총 개수 : " + totalUnit);

		// 4. 반환할 map생성 및 총 Row수를 저장
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("count", new Integer(totalUnit));

		// 5. search의 값에 따라 가져올 정보의 수를 변경
		// 가져올 정보의 시작점이 리스트의 최대값을 넘어가면 1페이지 출력
		if (((search.getCurrentPage() - 1) * search.getPageSize() + 1) > totalUnit) {
			search.setCurrentPage(1);
		}
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		// 6. ResultSet의 Product를 list에 저장
		List<Purchase> list = new ArrayList<Purchase>();
		Purchase purchase = null;
		while (rs.next()) {
			User user = new User();
			user = new User();
			user.setUserId(rs.getString("user_id"));
			user.setUserName(rs.getString("user_name"));
			user.setPassword(rs.getString("password"));
			user.setRole(rs.getString("role"));
			user.setPhone(rs.getString("cell_phone"));
			user.setAddr(rs.getString("addr"));
			user.setEmail(rs.getString("email"));
			user.setRegDate(rs.getDate("reg_date"));
			
			Product product = new Product();
			product = new Product();
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(rs.getInt("price"));
			product.setFileName(rs.getString("image_file"));
			product.setRegDate(rs.getDate("reg_date"));
			product.setProTranCode(rs.getString("tran_status_code"));

			purchase = new Purchase();
			purchase.setTranNo(rs.getInt("tran_no"));
			purchase.setPurchaseProd(product);
			purchase.setBuyer(user);
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDlvyAddr(rs.getString("demailaddr"));// dlvy_addr
			purchase.setDlvyRequest(rs.getString("dlvy_request"));
			purchase.setTranCode(rs.getString("tran_status_code"));
			purchase.setDlvyDate((rs.getString("dlvy_date") != null) ? rs.getString("dlvy_date").substring(0, 10) : rs.getString("dlvy_date"));
			purchase.setOrderDate(rs.getDate("order_data"));
			list.add(purchase);
		}

		map.put("list", list);
		map.put("count", totalUnit);

		stmt.close();
		rs.close();
		con.close();

		return map;
	}

	// 판매목록 보기
	public HashMap<String, Object> getSaleList(Search search) throws Exception {

		return null;
	}

	// 구매
	public void insertPurchase(Purchase Purchase) throws Exception {
		System.out.println("::insertPurchase 시작");
		Connection con = DBUtil.getConnection();
		String sql = "INSERT INTO transaction VALUES(seq_transaction_tran_no.NEXTVAL, ?,?,?,?,?,?,?,?,sysdate,?)";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, Purchase.getPurchaseProd().getProdNo());
		stmt.setString(2, Purchase.getBuyer().getUserId());
		stmt.setString(3, Purchase.getPaymentOption());
		stmt.setString(4, Purchase.getReceiverName());
		stmt.setString(5, Purchase.getReceiverPhone());
		stmt.setString(6, Purchase.getDlvyAddr());
		stmt.setString(7, Purchase.getDlvyRequest());
		stmt.setString(8, Purchase.getTranCode());
		stmt.setString(9, Purchase.getDlvyDate());

		System.out.println("insertPurchase : " + stmt.executeUpdate());

		stmt.close();
		con.close();
	}

	// 구매 정보 수정
	public void updatePurchase(Purchase Purchase) throws Exception {
		System.out.println("::updatePurchase 시작");
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction" + " SET"
				+ " payment_option = ?, receiver_name = ?, receiver_phone = ?, demailaddr = ?,"
				+ " dlvy_request = ?, dlvy_date = ?" + " WHERE tran_no = ?";
		PreparedStatement stmt = con.prepareStatement(sql);

		stmt.setString(1, Purchase.getPaymentOption());
		stmt.setString(2, Purchase.getReceiverName());
		stmt.setString(3, Purchase.getReceiverPhone());
		stmt.setString(4, Purchase.getDlvyAddr());
		stmt.setString(5, Purchase.getDlvyRequest());
		stmt.setString(6, Purchase.getDlvyDate());
		stmt.setInt(7, Purchase.getTranNo());

		System.out.println("uptdate중 Purchase : " + Purchase);

		System.out.println("UpdatePurchase로 변경된 줄 수 : " + stmt.executeUpdate());

		stmt.close();
		con.close();
	}

	// 판매 상태 코드 수정
	public void updateTranCode(Purchase Purchase) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET tran_status_code = ? WHERE tran_no = ?";

		PreparedStatement stmt = con.prepareStatement(sql);

		stmt.setString(1, Purchase.getTranCode());
		stmt.setInt(2, Purchase.getTranNo());

		System.out.println("Update로 변경된 줄 수 : " + stmt.executeUpdate());

		stmt.close();
		con.close();
	}

	// 게시판 Page 처리를 위한 전체 Row(totalCount) return
	private int getTotalCount(String sql) throws Exception {

		sql = "SELECT COUNT(*) " + "FROM ( " + sql + ") countTable";

		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();

		int totalCount = 0;
		if (rs.next()) {
			totalCount = rs.getInt(1);
		}

		pStmt.close();
		con.close();
		rs.close();

		return totalCount;
	}

	// 게시판 currentPage Row 만 return
	private String makeCurrentPageSql(String sql, Search search) {
		sql = "SELECT * " + "FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " + " 	FROM (	" + sql
				+ " ) inner_table " + "	WHERE ROWNUM <=" + search.getCurrentPage() * search.getPageSize() + " ) "
				+ "WHERE row_seq BETWEEN " + ((search.getCurrentPage() - 1) * search.getPageSize() + 1) + " AND "
				+ search.getCurrentPage() * search.getPageSize();

		System.out.println("UserDAO :: make SQL :: " + sql);

		return sql;
	}
}
