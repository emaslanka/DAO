package pl.coderslab.entity;


import java.sql.*;
import java.util.Arrays;


public class UserDao {

    //-----------------------------------QUERIES---------------------------------------------------------------//


    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email =?, username = ?, password = ? WHERE id = ?";
    private static final String SELECT_USER_QUERY2 = "SELECT * FROM users WHERE id = ?;";
    private static final String SELECT_ALL_USER_QUERY = "SELECT * FROM users ;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id= ? ;";


    //--------------------------------AUXILIARY METHODS --------------------------------------------------------------//


    // 1) PASSWORD HASH  --- > http://www.mindrot.org/projects/jBCrypt/  -----------------------------------------------

    public String hashPassword(String password) {

        return BCrypt.hashpw(password, BCrypt.gensalt());

    }

    // 2) CHECK AMOUNT OF RECORDS ----------------------------------------------------------------------------------
    public static int getCount(Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT COUNT(*) FROM users");
        ResultSet rs = st.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        return count;
    }


    // 3) ADD USER TO USERS[] ------------------------------------------------------------------------------------------
    private User[] addToArray(User u, User[] users) {

        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);                                             // New array with length increased by 1
        tmpUsers[users.length] = u;                                                                                     // Add user on last position
        return tmpUsers;
    }

    //--------------------------------MAJOR  METHODS -----------------------------------------------------------------//
    // 1) CREATE NEW USER ----------------------------------------------------------------------------------------------

    public User create(User user) {

        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());                                                     // first data in query -> username
            statement.setString(2, user.getEmail());                                                        // second data in query -> email
            statement.setString(3, hashPassword(user.getPassword()));                                       // third data in query --> password - additionally we hash password

            statement.executeUpdate();                                                                                   // executing query  --> new user in 'users'

            ResultSet resultSet = statement.getGeneratedKeys();                                                          // getting generated keys - id for user from column 1
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }

            return user;

        } catch (SQLException e) {

            e.printStackTrace();

            return null;

        }
    }

    // 2) PRINT ALL USERS  - additional method to display all users-----------------------------------------------------
    public static void printUsers() throws SQLException {
        String[] columnNames = {"id", "username", "email", "password"};
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(SELECT_ALL_USER_QUERY);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                for (String param : columnNames) {

                    System.out.print(resultSet.getString(param) + "  ");
                }
                System.out.println();
            }
        }
    }


    // 3) READ USER WITH GIVEN ID --------------------------------------------------------------------------------------

    public User read(int userId) throws SQLException {

        try (Connection conn = DBUtil.getConnection()) {


                User user = new User();
                PreparedStatement statement = conn.prepareStatement(SELECT_USER_QUERY2);
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    user.setId(resultSet.getInt("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));

                    System.out.println("User id = " + userId + " : " + user.getUsername() + " " + user.getEmail() + " " + user.getPassword());
                    return user;
                }
                else {
                    return null;
                }


            }
        }


    // 4) DELETE USER WITH GIVEN ID --------------------------------------------------------------------------------------
    public static void delete(int userId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {

            if (userId <= getCount(conn)) {

                PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY.replace("tableName", "users"));
                statement.setInt(1, userId);
                statement.executeUpdate();
            } else {
                System.out.println("No indeks in tale users");
            }
        }
    }


    // 5) FIND ALL USERS -----------------------------------------------------------------------------------------------
    public void findAll() throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            User[] users = {};
            int count = getCount(conn);

            for (int id = 1; id <= count; id++) {

                User user = new User();
                PreparedStatement statement = conn.prepareStatement(SELECT_USER_QUERY2);
                statement.setInt(1, id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    user.setId(resultSet.getInt("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));

                    users = addToArray(user, users);

                }
            }

            for (int i = 0; i < users.length; i++) {

                System.out.println(users[i].getUsername());                                                              //Print all users from table --> checking if table is set and filled
            }
        }
    }

    // 6) UPDATE USER WITH GIVEN CHANGES -------------------------------------------------------------------------------
    public void update(User user) throws SQLException {

        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUsername());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        }
    }
}


