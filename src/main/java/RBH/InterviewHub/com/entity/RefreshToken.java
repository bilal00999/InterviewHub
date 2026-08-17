package RBH.InterviewHub.com.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "refresh_tokens")
@Data
public class RefreshToken {

    @Id
    private String id;

    private String token;

    private String username;

    private Instant expiryDate;

    private boolean revoked;

}