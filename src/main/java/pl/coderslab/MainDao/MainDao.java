package pl.coderslab.MainDao;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import java.sql.SQLException;

public class MainDao {

    public static void main(String[] args) throws SQLException {

        User user = new User();
        user.setEmail("domino7@yahoo.com");
        user.setUsername("MatiiDomino");
        user.setPassword("Gyrosil");

        UserDao userDao = new UserDao();
        // Insert into user

       //userDao.create(user);
        // SHOW ALL USERS
       //userDao.printUsers();

        // read user

       // userDao.read(6);
        // pytanie, dlaczego musimy ustawiac statement.setInt(1,userId)

        //DELETE

        //userDao.delete(3);
       // userDao.printUsers();

        userDao.findAll();





    }
}
