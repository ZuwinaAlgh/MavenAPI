import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class ReadAPI {
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
				
				
				
				try {

		            Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
		            DriverManager.registerDriver(driver);

		            con = DriverManager.getConnection(url, user, pass);

		            Statement st = con.createStatement();
		           
		            Scanner sa=new Scanner(System.in);
		        	System.out.println("How many Rows you want to read: ");
		            int read =sa.nextInt();
		            int count=0;
		            String sql="select * from ApiTable";
		            ResultSet rs=st.executeQuery(sql);
		            
		            while (rs.next()&&count<=read) {
		      
		            	 int id=rs.getInt("id");
		                 String webpages=rs.getString("web_pages");
		                 String stateprovince= rs.getString("state_province");
		                 String alphatwocode= rs.getString("alpha_two_code");
		                 String names=rs.getString("name");
		                 String countrys=rs.getString("country");
		                 String domains=rs.getString("domains");
		                 System.out.println(id +" "+webpage+" "+stateprovince+" "+ alphatwocode+" " +names+ " "+countrys+""+domains);
		                 count++;
		                 
		           
		            }}
			      
		        

		        catch (Exception ex) {
		            System.err.println(ex);
		        }	
		
			}}

}
