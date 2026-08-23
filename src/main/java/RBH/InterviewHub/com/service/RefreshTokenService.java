package RBH.InterviewHub.com.service;

import RBH.InterviewHub.com.entity.RefreshToken;
import RBH.InterviewHub.com.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(String userName){

        RefreshToken refreshToken=new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUsername(userName);
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshToken.setRevoked(false);

       return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyToken(RefreshToken refreshToken){

        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);

            throw new RuntimeException("refresh token expired");
        }

        if(refreshToken.isRevoked()){
            throw new RuntimeException("Refresh token has been revoked");
        }

        return refreshToken;
    }

    public void revokedToken(String token){
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
        });

    }

}
