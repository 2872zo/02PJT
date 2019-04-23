package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.CommonUtil;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;

public class ProductDAO {

	public ProductDAO() {
	}

	public void insertProduct(Product product) throws Exception {
		// 1.DBUtil을 이용하여 DB연결
		Connection con = DBUtil.getConnection();

		// 2.DB에 보낼 DML 작성 및 값 설정
		String sql = "INSERT INTO Product VALUES(seq_product_prod_no.NEXTVAL,?,?,?,?,?,SYSDATE)";
		PreparedStatement stmt = con.prepareStatement(sql);
		System.out.println(product.getManuDate());
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setString(3, product.getManuDate().replaceAll("\\-", ""));
		stmt.setInt(4, product.getPrice());
		stmt.setString(5, product.getFileName());

		// 3.실행
		System.out.println("실행 : " + stmt.executeUpdate());

		stmt.close();
		con.close();
	}

	public Product findProduct(int prodNo) throws Exception {
		// 1.DBUtil을 이용하여 DB연결
		Connection con = DBUtil.getConnection();

		// 2.DB에 보낼 DML 작성 및 값 설정
		String sql = "SELECT"
				+ " p.prod_no, p.prod_name, p.prod_detail, p.manufacture_day, p.price, p.image_file, p.reg_date, t.tran_status_code"
				+ " FROM product p, transaction t" + " WHERE p.prod_no = t.prod_no(+)" + " AND p.prod_no = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);
		
		ResultSet rs = stmt.executeQuery();

		// 3. 결과값을 ProductVO에 담아서 전달
		Product product = null;
		if (rs.next()) {
			product = new Product();
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(rs.getInt("price"));
			product.setFileName(rs.getString("image_file"));
			product.setRegDate(rs.getDate("reg_date"));
			product.setProTranCode(rs.getString("tran_status_code"));
		}

		stmt.close();
		rs.close();
		con.close();

		return product;
	}

	public HashMap<String, Object> getProductList(Search search) throws Exception {
		System.out.println("getProductList 시작");
		// 1.DBUtil을 이용하여 DB연결
		Connection con = DBUtil.getConnection();

		// 2.DB에 보낼 DML 작성 및 값 설정
		String sql = "SELECT " + " p.*, t.tran_status_code" + " FROM product p, transaction t"
				+ " WHERE p.prod_no = t.prod_no(+)";
		if (!CommonUtil.null2str(search.getSearchKeyword()).equals("")) {
			if (search.getSearchCondition().equals("0")) {
				sql += " AND p.prod_no = '" + search.getSearchKeyword() + "'";
			} else if (search.getSearchCondition().equals("1")) {
				sql += " AND p.prod_name like '%" + search.getSearchKeyword() + "%'";
			} else if (search.getSearchCondition().equals("2")) {
				sql += " AND p.price = '" + search.getSearchKeyword() + "'";
			}
		}
		sql += " ORDER BY p.prod_no";
		
		System.out.println("search.getSearchKeyword() : " + CommonUtil.null2str(search.getSearchKeyword()));
		
		// 3. 총 Row수 구함
		int totalUnit = getTotalCount(sql);
		System.out.println("상품 총 줄수 : " + totalUnit);
		
		// 4. 반환할 map생성 및 총 Row수를 저장
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("count", new Integer(totalUnit));
		
		// 5. search의 값에 따라 가져올 정보의 수를 변경
		// 가져올 정보가 리스트의 최대값을 넘어가면 1페이지 출력
		if(((search.getCurrentPage()-1)*search.getPageSize()+1) > totalUnit) {
			search.setCurrentPage(1);
		}
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		
		
		// 6. ResultSet의 Product를 list에 저장
		List<Product> list = new ArrayList<Product>();
		while (rs.next()) {
			Product product = new Product();
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(rs.getInt("price"));
			product.setFileName(rs.getString("image_file"));
			product.setRegDate(rs.getDate("reg_date"));
			product.setProTranCode(rs.getString("tran_status_code"));
			list.add(product);
		}

		System.out.println("list.size() : " + list.size());
		map.put("list", list);
		System.out.println("map.size() : " + map.size());

		stmt.close();
		rs.close();
		con.close();

		return map;
	}

	public void updateProduct(Product product) throws Exception {
		// 1.DBUtil을 이용하여 DB연결
		Connection con = DBUtil.getConnection();

		System.out.println("update 중 CommonUtil.toDateStr(product.getManuDate()) : " + CommonUtil.toDateStr(product.getManuDate()));
		
		// 2.DB에 보낼 DML 작성 및 값 설정
		String sql = "UPDATE product set prod_name = ?, prod_detail = ?, manufacture_day =?, price = ?, image_file = ? WHERE prod_no = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setString(3, product.getManuDate().replaceAll("-", ""));
		stmt.setInt(4, product.getPrice());
		stmt.setString(5, product.getFileName());
		stmt.setInt(6, product.getProdNo());

		System.out.println("updateProduct로 변경된 줄 수 : " + stmt.executeUpdate());

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

	// 게시판 currentPage Row 만  return 
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
				"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
				" 	FROM (	"+sql+" ) inner_table "+
				"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
				"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();

		System.out.println("UserDAO :: make SQL :: "+ sql);	

		return sql;
	}
}
