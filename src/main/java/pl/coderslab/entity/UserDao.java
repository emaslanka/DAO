package pl.coderslab.entity;


import java.sql.*;
import java.util.Arrays;


public class UserDao {

    //QURENDY
    private static final String CREATE_USER_QUERY ="INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email =?, username = ?, password = ? WHERE id = ?";
    private static final String SELECT_USER_QUERY = "SELECT name, email FROM users WHERE id = ?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id= ? ;";
    private static final String SELECT_ALL_USER_QUERY = "SELECT * FROM users ;";


    // for read function

    private static final String SELECT_USER_QUERY1 = "SELECT id FROM users WHERE id = ?;";
    private static final String SELECT_USER_QUERY2 = "SELECT * FROM users WHERE id = ?;";
    private static final String SELECT_USER_QUERY3 = "SELECT email FROM users WHERE id = ?;";
    private static final String SELECT_USER_QUERY4 = "SELECT password FROM users WHERE id = ?;";



    //PASSWORD HASH

    public String hashPassword(String password) {

        return BCrypt.hashpw(password, BCrypt.gensalt());

    }


    //Create new user

    public User create(User user) {

        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUsername());

            statement.setString(2, user.getEmail());

            statement.setString(3, hashPassword(user.getPassword()));

            statement.executeUpdate();

            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.

            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {

                user.setId(resultSet.getInt(1));

            }

            return user;

        } catch (SQLException e) {

            e.printStackTrace();

            return null;

        }

    }

// Display all users
    public static void printUsers()

    {
        String[] columnNames = {"id", "username", "email", "password"};
        try (Connection conn = DBUtil.getConnection()){

            PreparedStatement statement = conn.prepareStatement(SELECT_ALL_USER_QUERY);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                for (String param : columnNames) {

                    System.out.print(resultSet.getString(param) + "  ");


                }
                System.out.println();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }

    }


    // New User from database

    public User read(int userId){

        try (Connection conn = DBUtil.getConnection()){

            User user = new User();
            PreparedStatement statement = conn.prepareStatement(SELECT_USER_QUERY2);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
            user.setId(resultSet.getInt("id"));
            user.setEmail(resultSet.getString("email"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));

            System.out.println("User id = "+userId + " : "+ user.getUsername() + " " + user.getEmail() + " " + user.getPassword());

            return user;}

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }

        return null;
    }


    public static void delete(int userId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()){

            PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY.replace("tableName","users"));
            statement.setInt(1,userId);
            statement.executeUpdate();

        }

    }

    private User[] addToArray(User u, User[] users) {

        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.

        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.

        return tmpUsers; // Zwracamy nową tablicę.

    }


    public void findAll()

    {
       try (Connection conn = DBUtil.getConnection()){
            User[] users = {};

            // ile mamy rekordow w tablicy
           PreparedStatement st = conn.prepareStatement("SELECT COUNT(*) FROM users");
           ResultSet rs = st.executeQuery();
           rs.next();
           int count = rs.getInt(1);


           // tworzymy obiekt klasy user i wpisujemy do tablicy
           for(int id =1; id<=count; id++){

                User user = new User();
                PreparedStatement statement = conn.prepareStatement(SELECT_USER_QUERY2);
                statement.setInt(1, id);
                ResultSet resultSet = statement.executeQuery();

                if(resultSet.next()){
                    user.setId(resultSet.getInt("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));

                    users = addToArray(user,users);

                }}


                for (int i = 0; i < users.length; i++) {

                    System.out.println(users[i].getUsername());

                }

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }

    }

    public void update(User user) throws SQLException {

        try (Connection conn = DBUtil.getConnection()){

            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUsername());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();


        }


    }



}
