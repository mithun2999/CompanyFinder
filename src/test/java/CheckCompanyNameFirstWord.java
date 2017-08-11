import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;


import com.hirrr.companyfinder.utitlities.GetDbConnection;

public class CheckCompanyNameFirstWord {

	
	public void checkCompnayNameInUrl() throws SQLException {
		boolean hasFirstWordMatch = false;
		Connection connection = null;
		Integer count = 0;
		String companyName = null;
		ResultSet resultSet = null;
		String companyUrl = null;
		PreparedStatement statement = null;
		connection = GetDbConnection.getDbConnection();
		final String getQuery = "SELECT company_name,company_url FROM results_provider_db.Wrapped limit 1000";
		statement = connection.prepareStatement(getQuery);
		resultSet = statement.executeQuery();
		while(resultSet.next()) {
	  
		companyName = resultSet.getString("company_name");
		companyUrl = resultSet.getString("company_url");
		
		String[] companyNameSplitArray ;
		companyNameSplitArray = companyName.split(" ");
		
		for(String companyNameWord : companyNameSplitArray) {
			if(count==0) {
				if(companyUrl.toUpperCase(Locale.ENGLISH)
						.contains(companyNameWord.toUpperCase(Locale.ENGLISH))) {
					hasFirstWordMatch = true;
					
				}
				count++;
				continue;
			}
			
		}
		if(!hasFirstWordMatch) {
			
			
		System.out.println("Company Name :"+companyName + "\t" + "Company URL :"+companyUrl + "\t" + "FirstWord Match :"+hasFirstWordMatch + "\t" );
		}
		hasFirstWordMatch = false;
		count=0;
		}
		
	}
	
	
	
	public static void main(String[] args) throws SQLException {
		CheckCompanyNameFirstWord ch = new CheckCompanyNameFirstWord();
		ch.checkCompnayNameInUrl();
	}
}
