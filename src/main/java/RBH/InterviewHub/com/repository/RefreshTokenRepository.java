package RBH.InterviewHub.com.repository;

import RBH.InterviewHub.com.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken,String> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserName(String username);
}
