package pl.coderslab.entity;

public class User {

    private int id;
    private String email;
    private String username;
    private String password;

//Settery
    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setPassword(String password){
        this.password=password;
    }

    // Gettery


    public int getId() {
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
