package pl.coderslab.MainDao;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import java.sql.SQLException;

public class MainDao {

    public static void main(String[] args) throws SQLException {

        User user = new User();
        user.setEmail("EWelina@yahoo.com");
        user.setUsername("zabka");
        user.setPassword("Hyjkiu");

        UserDao userDao = new UserDao();


        // Insert into user
        System.out.println("--------------CREATE NEW USER------------------------");
        userDao.create(user);

        // SHOW ALL USERS
       userDao.printUsers();

        // read user

        System.out.println("--------------READ USER------------------------");

       userDao.read(5);

       //DELETE
       System.out.println("--------------AFTER DELETE USER------------------------");
       userDao.delete(1);
       userDao.printUsers();

       // userDao.findAll();

        user.setEmail("alamakora@kotmaale.pl");
        user.setUsername("majasiebie");
        user.setPassword("wlzalKOtek");
        user.setId(6);

        userDao.update(user);
        System.out.println("-----------after update-----------");
        userDao.printUsers();

    }
}
