import java.beans.Statement;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;


public class ReadingAPI {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		
		String url = "jdbc:sqlserver://localhost:1433;databaseName=API;encrypt=true;trustServerCertificate=true";
		String user = "sa";
		String pass = "root";
		Connection con ;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://universities.hipolabs.com/search?country=United+States")).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		String uglyJsonString = response.body();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			
			ImportAPI[] M = gson.fromJson(uglyJsonString, ImportAPI[].class);
			for (ImportAPI x : M) {
				String webpage = x.getWeb_pages()[0];
				String name = x.getName();
				String domian = x.getDomains()[0];
				String state_province = x.getState_province();
				String alpha_two_code = x.getAlpha_two_code();
				String country = x.getCountry();
				String SQLqueryForInserting = "insert into  ApiTable(web_pages,state_province, alpha_two_code,name, country,domains)"
						+ " values('" + webpage + "' ,'" + state_province + "', '" + alpha_two_code
						+ "','" + name + "' ,' " +domian  +  "','" +country+"')";
				

				
				try {
					Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
					DriverManager.registerDriver(driver);
					con = DriverManager.getConnection(url, user, pass);
					java.sql.Statement st = con.createStatement();
					// Executing query
					
					int m = st.executeUpdate(SQLqueryForInserting );
					if (m >= 0)
						System.out.println("inserted successfully : " + SQLqueryForInserting);
					else
						System.out.println("insertion failed");
					// Closing the connections
					con.close();
				} catch (Exception ex) {
					// Display message when exceptions occurs
					System.err.println(ex);
				}
		
			}}}












