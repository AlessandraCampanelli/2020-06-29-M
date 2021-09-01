package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
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
	public List<Adiacenza> getAdiacenze(Map <Integer,Director> idMap,Integer anno){
		String sql = 
				 "SELECT md1.director_id,md2.director_id,COUNT(DISTINCT(r1.actor_id)) AS peso "
				+ "FROM roles  r1,roles r2, movies  m1,movies m2,movies_directors  md1,movies_directors md2 "
				+ "WHERE m1.year=? AND m2.year=? AND  md1.director_id<> md2.director_id AND m1.id=md1.movie_id "
				+ "AND m2.id=md2.movie_id AND m1.id=r1.movie_id "
				+ "AND m2.id=r2.movie_id AND r2.actor_id=r1.actor_id AND md1.director_id> md2.director_id "
				+ "GROUP BY md1.director_id,md2.director_id";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
                Director d1= idMap.get(res.getInt("md1.director_id"));
                Director d2= idMap.get(res.getInt("md2.director_id"));
				if(d1!=null && d2!=null)
				result.add(new Adiacenza(d1,d2,res.getInt("peso")));
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listAllDirectors(Map <Integer,Director>idMap,Integer anno){
		String sql = "SELECT d.id,d.first_name,d.last_name "
				+ "FROM movies AS m,directors AS d, movies_directors AS md "
				+ "WHERE  m.year=? AND m.id=md.movie_id AND d.id=md.director_id "
				+ "GROUP BY d.id,d.first_name,d.last_name "
				+ "HAVING COUNT(*)>=1";
		//List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
 if(!idMap.containsKey(res.getInt("d.id"))) {
				Director director = new Director(res.getInt("d.id"), res.getString("d.first_name"), res.getString("d.last_name"));
				idMap.put(res.getInt("d.id"), director);
 }
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return ;
		}
	}
	
	
	
	
	
	
}
