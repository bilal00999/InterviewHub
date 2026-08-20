package RBH.InterviewHub.com.controller;


import RBH.InterviewHub.com.entity.LoginResponse;
import RBH.InterviewHub.com.entity.RefeshTokenRequest;
import RBH.InterviewHub.com.entity.RefreshToken;
import RBH.InterviewHub.com.entity.User;
import RBH.InterviewHub.com.repository.RefreshTokenRepository;
import RBH.InterviewHub.com.service.JwtService;
import RBH.InterviewHub.com.service.RefreshTokenService;
import RBH.InterviewHub.com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        userService.SaveNewUser(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUserName(), user.getPassword()
                    )
            );

            String accessToken=jwtService.generateToken(authentication.getName());
            RefreshToken refreshToken= refreshTokenService.createRefreshToken(authentication.getName());

            LoginResponse response=new LoginResponse(accessToken,refreshToken.getToken());
            return new ResponseEntity<>(response,HttpStatus.OK);

        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefeshTokenRequest request){
        try{
            RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                    .orElseThrow(() -> new RuntimeException("refrsh token not found"));

            refreshTokenService.verifyToken(refreshToken);

            String newAccessToken = jwtService.generateToken(refreshToken.getUsername());

            return new ResponseEntity<>("access token : "+newAccessToken,HttpStatus.OK);
        }catch (RuntimeException e ){
            return new ResponseEntity<>("refresh token expire",HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestBody RefeshTokenRequest request
    ) {

        refreshTokenService.revokedToken(
                request.getRefreshToken()
        );

        return ResponseEntity.ok(
                "Logged out successfully"
        );
    }
}
