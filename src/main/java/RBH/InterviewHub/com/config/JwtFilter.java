package RBH.InterviewHub.com.config;

import RBH.InterviewHub.com.entity.User;
import RBH.InterviewHub.com.service.JwtService;
import RBH.InterviewHub.com.service.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailServiceImpl userDetailServiceImp;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader=request.getHeader("Authorization");
        String jwtToken=null;
        String username=null;


        if(authHeader!=null && authHeader.startsWith("bearer")){
            jwtToken=authHeader.substring(7);
            username=jwtService.extractUserName(jwtToken);
        }

        if(username!=null){
            UserDetails user=userDetailServiceImp.loadUserByUsername(username);
            if(jwtService.validateToken(jwtToken)){
                UsernamePasswordAuthenticationToken auth=
                        new UsernamePasswordAuthenticationToken(
                                user,null,user.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }else {
                filterChain.doFilter(request,response);
            }
        }else {
            filterChain.doFilter(request,response);
        }


    }
}
