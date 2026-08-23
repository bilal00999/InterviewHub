package RBH.InterviewHub.com.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Healthcheck {

    @GetMapping("/health-check")
    public String healthcheck(){
        return "ok";
    }
}
