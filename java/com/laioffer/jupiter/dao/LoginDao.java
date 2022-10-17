package com.laioffer.jupiter.dao;


import com.laioffer.jupiter.entity.db.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {
    //get session factory
    @Autowired
    private SessionFactory sessionFactory;

    // from service will passing the userId and password;
    // we have to verified them in db
    public String verifyLogin(String userId, String password){
        String name = "";
        Session session = null;

        try {
            session = sessionFactory.openSession();
            // no need session.begintransection here bc we don't need to add or delete
            User user = session.get(User.class, userId);
            // go to User class actually is checking the userId in User table
            if (user != null && user.getPassword().equals(password)){
                name = user.getFirstName();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            if(session != null){
                session.close();
            }
        }
        return name;
    }
}
