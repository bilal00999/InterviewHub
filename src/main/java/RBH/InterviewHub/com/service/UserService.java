package RBH.InterviewHub.com.service;

import RBH.InterviewHub.com.entity.User;
import RBH.InterviewHub.com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public boolean SaveNewUser(User user){
        try{
            user.setPassword(Objects.requireNonNull(passwordEncoder.encode(user.getPassword())));
            userRepository.save(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
