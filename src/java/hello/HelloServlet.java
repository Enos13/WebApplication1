package hello;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
    public static void main(String[]args){}
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/sci_sonas";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public HelloServlet(){
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        Connection connection = null;
        PreparedStatement preparedStatement= null ;

        try {
            // 1. Load the JDBC driver (optional in newer JDBC versions)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establish the database connection
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // 3. Prepare the SQL INSERT statement
            String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);

            // 4. Execute the SQL statement
            int rowsAffected = preparedStatement.executeUpdate();

            // 5. Provide feedback to the user
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Data Saved</title>");
            out.println("</head>");
            out.println("<body>");
            if (rowsAffected > 0) {
                out.println("<h1>Credentials saved successfully!</h1>");
            } else {
                out.println("<h1>Error saving credentials. Please try again.</h1>");
            }
            out.println("</body>");
            out.println("</html>");

        } catch (ClassNotFoundException e) {
            out.println("<h1>Error: JDBC driver not found.</h1>");
            e.printStackTrace();
        } catch (SQLException e) {
            out.println("<h1>Error connecting to the database or saving data.</h1>");
            e.printStackTrace();
        } finally {
            // 6. Close resources in the finally block
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // Or handle GET requests differently
    }
}