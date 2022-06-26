package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Coppia;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectorsYear(int year){
		String sql = "SELECT DISTINCT d.* "
				+ "FROM movies m, movies_directors md, directors d "
				+ "WHERE m.id = md.movie_id "
				+ "AND md.director_id = d.id "
				+ "AND m.year = ?";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Coppia> listCoppieAnno(int year, Map<Integer, Director> idMap) {
		
		String sql = "SELECT md1.director_id AS d1, md2.director_id AS d2, COUNT(DISTINCT r1.actor_id) AS peso "
				+ "FROM movies_directors md1, movies_directors md2, roles r1, roles r2, movies m1, movies m2 "
				+ "WHERE md1.director_id > md2.director_id "
				+ "AND m1.year = ? "
				+ "AND m2.year = m1.year "
				+ "AND r1.actor_id = r2.actor_id "
				+ "AND r1.movie_id = m1.id "
				+ "AND r2.movie_id = m2.id "
				+ "AND md1.movie_id = m1.id "
				+ "AND md2.movie_id = m2.id "
				+ "GROUP BY md1.director_id, md2.director_id";
		List<Coppia> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Director d1 = idMap.get(res.getInt("d1"));
				Director d2 = idMap.get(res.getInt("d2"));
				if(d1!=null && d2!=null)
					result.add(new Coppia(d1, d2, res.getInt("peso")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
	
	
	
	
}
