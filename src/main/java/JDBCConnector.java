import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class JDBCConnector {

    public static Connection connect(String url){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

}


