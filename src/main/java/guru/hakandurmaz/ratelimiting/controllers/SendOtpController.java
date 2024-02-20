package guru.hakandurmaz.ratelimiting.controllers;

import guru.hakandurmaz.ratelimiting.dto.SendOtpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/send-otp")
public class SendOtpController {
    @PostMapping
    public ResponseEntity<String> sendOtp(@RequestBody SendOtpRequest sendOtpRequest) {
        return ResponseEntity.ok("Otp sent to " + sendOtpRequest.phoneNumber());
    }
}
