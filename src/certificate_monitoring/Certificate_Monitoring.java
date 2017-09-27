/**
 *
 * @author Sanan Garibli
 * 
 * This software prepared for Certificate notification when certificate expired.
 * 
 */

package certificate_monitoring;

import com.mysql.jdbc.Connection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Certificate_Monitoring 
{
  
private final String USER_AGENT = "Mozilla/5.0";
  
 public static void main(String[] args) throws Exception  { 
    
      Certificate_Monitoring http = new Certificate_Monitoring();
      http.MySQL_connect();
      
}   
    
 
public int Compare_date(String str) throws ParseException
  {
    Date date  =  new Date();
    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(str);

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);

    if(cal.get(Calendar.YEAR) == cal1.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == cal1.get(Calendar.MONTH)
            && cal.get(Calendar.DAY_OF_MONTH) == cal1.get(Calendar.DAY_OF_MONTH)
            )
    {
        System.out.println("Dates are equal");
    return 1;
    }
    else
    {
        System.out.println("Dates not equal");
    return 0;
        }

    }
 
 
 
 public  void MySQL_connect() throws Exception
 {
    String url = "jdbc:mysql://172.16.10.246:3306/certificates_monitoring";
    String username = "seymur";
    String password = "seymur";

    System.out.println("Connecting database...");

try (Connection connection = (Connection) DriverManager.getConnection(url, username, password)) {
    System.out.println("Database connected!");
    Statement stmt=connection.createStatement();  
    try (ResultSet rs=stmt.executeQuery("SELECT  C.CN, C.EXPIRY_DATE, C.SERVER_NAME, C.IP FROM CERTS C"))
        { 
            while(rs.next()) 
            {
              System.out.println(rs.getString(1)+ " | " + rs.getString(2)+" | "+ rs.getString(3) +" | " + rs.getString(4)); 
              Compare_date(rs.getString(2));

        //sendGet("http://172.25.10.63:13002/cgi-bin/sendsms?username=ag&password=ag&smsc=otp&to=994702909137&text=cert_info");
            }
              connection.close(); 
        }
        catch (SQLException e) {
        throw new IllegalStateException("SQL ERROR: ", e);
            }
    }
catch (SQLException e) {
    throw new IllegalStateException("Cannot connect the database!", e);
}
 }
 


	// HTTP GET request
	public  void sendGet(String url) throws Exception {		

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

                StringBuffer response;
            try (BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
        }
    }

		//print result
		System.out.println(response.toString());
}

  
}




