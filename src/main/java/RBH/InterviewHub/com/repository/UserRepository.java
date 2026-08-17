package RBH.InterviewHub.com.repository;

import org.bson.types.ObjectId;
import RBH.InterviewHub.com.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface UserRepository extends MongoRepository<User ,ObjectId>{

    User findByUserName(String username);
}