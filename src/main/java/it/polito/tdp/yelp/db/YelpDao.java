package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {
	
	TreeMap<String, User> idMap;

	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	public List<User> getAllUsers(){
//		String sql = "SELECT * FROM Users";
//		List<User> result = new ArrayList<User>();
//		Connection conn = DBConnect.getConnection();
//
//		try {
//			PreparedStatement st = conn.prepareStatement(sql);
//			ResultSet res = st.executeQuery();
//			while (res.next()) {
//
//				User user = new User(res.getString("user_id"),
//						res.getInt("votes_funny"),
//						res.getInt("votes_useful"),
//						res.getInt("votes_cool"),
//						res.getString("name"),
//						res.getDouble("average_stars"),
//						res.getInt("review_count"));
//				
//				result.add(user);
//			}
//			res.close();
//			st.close();
//			conn.close();
//			return result;
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	public List<User> getUsers(int n){
		idMap = new TreeMap<>();
		String sql = "SELECT *"
				+ "FROM reviews r, (SELECT u.user_id, COUNT(r.review_id) AS valide "
				+ "						FROM reviews r, users u "
				+ "						WHERE u.user_id = r.user_id  "
				+ "						GROUP BY u.user_id "
				+ "						HAVING COUNT(r.review_id) >= ?) us, users u "
				+ "WHERE us.user_id = r.user_id AND u.user_id = r.user_id";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, n);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				if(idMap.get(res.getString("user_id")) == null) {
					User user = new User(res.getString("user_id"),
							res.getInt("votes_funny"),
							res.getInt("votes_useful"),
							res.getInt("votes_cool"),
							res.getString("name"),
							res.getDouble("average_stars"),
							res.getInt("review_count"), review);
					idMap.put(user.getUserId(), user);
					result.add(user);
				}else {
					idMap.get(res.getString("user_id")).setReviews(review);
				}
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
