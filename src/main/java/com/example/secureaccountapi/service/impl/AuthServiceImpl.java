package com.example.secureaccountapi.service.impl;

import com.example.secureaccountapi.dto.*;
import com.example.secureaccountapi.entity.EmailVerification;
import com.example.secureaccountapi.entity.MfaVerificationCode;
import com.example.secureaccountapi.entity.Role;
import com.example.secureaccountapi.entity.User;
import com.example.secureaccountapi.repository.MfaVerificationCodeRepo;
import com.example.secureaccountapi.repository.UserRepo;
import com.example.secureaccountapi.service.AuthService;
import com.example.secureaccountapi.service.EmailService;
import com.example.secureaccountapi.service.EmailVerificationService;
import com.example.secureaccountapi.service.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Autowired
    MfaVerificationCodeRepo mfaVerificationCodeRepo;

    @Autowired
    EmailVerificationService emailVerificationService;

    @Autowired
    EmailService emailService;

    //------------------------------------------------------------------------------------------------------------------------------
    // SIGN UP
    //------------------------------------------------------------------------------------------------------------------------------

    public User signUp(SignUpRequest signUpRequest) throws MessagingException {
        User user = new User();

        user.setEmail(signUpRequest.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        user.setCreationDate(new Date());
        user.setUsername(signUpRequest.getUsername());

        User newUser= userRepository.save(user);

        emailVerificationService.sendVerificationCode(user.getEmail(),1);

        return newUser;

    }


    private final JwtService jwtService;


    //------------------------------------------------------------------------------------------------------------------------------
    // SIGN IN (IMPLEMENTING MFA)
    //------------------------------------------------------------------------------------------------------------------------------

    public JwtAuthResponse signIn(SignInRequest signInRequest) throws MessagingException {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword()));

        var user =userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));

        // check if MFA is enabled
        if(user.getSecuritySetting().isMfaEnabled()){
            // generate verification code
            int mfaCode = generateMfaCode();

            MfaVerificationCode mfaVerificationCode=new MfaVerificationCode();
            mfaVerificationCode.setCode(mfaCode);
            mfaVerificationCode.setEmail(user.getEmail());
            mfaVerificationCode.setVerificationToken(null);
            mfaVerificationCode.setIssuedAt(new Date());
            mfaVerificationCode.setExpireAt(determineExpirationDate());
            mfaVerificationCodeRepo.save(mfaVerificationCode);

            String to = user.getContact().getSecondEmail();
            String subject= "YOUR CODE - "+mfaCode;
            String content= "<html>"
                    + "<body style='font-family: Arial, sans-serif;'>"
                    + "<h1 style='color: navy;'>2 Factor Authentication</h1>"
                    + "<p>Hello <strong> UserName </strong>,</p>"
                    + "<p>Your MFA code is: <span style='color: green; font-weight: bold;'>" + mfaCode + "</span></p>"
                    + "<p>This code will expire in 15 minutes.</p>"
                    + "<p>If you did not request this code, please ignore this email.</p>"
                    + "<br>"
                    + "<p>Best regards,<br>Security</p>"
                    + "</body>"
                    + "</html>";

            emailService.sendHtmlMail(  to,  subject,   content,true);


            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setToken(null);
            jwtAuthResponse.setRefreshToken(null);
            jwtAuthResponse.setMessage("Mfa successfully sent to the user");
            return jwtAuthResponse;

        }else {

            var jwt =jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);

            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setToken(jwt);
            jwtAuthResponse.setRefreshToken(refreshToken);
            jwtAuthResponse.setMessage("User successfully Authenticated");
            return jwtAuthResponse;
        }



    }


    //------------------------------------------------------------------------------------------------------------------------------
    // VERIFY MFA CODE AND GENERATE JWT if MFA is correct
    //------------------------------------------------------------------------------------------------------------------------------
    public JwtAuthResponse verifyMfaCode(MfaVerificationRequest mfaVerificationRequest) {
        User user = userRepository.findByEmail(mfaVerificationRequest.getEmail()).get();

        MfaVerificationCode mfaVerificationCode=mfaVerificationCodeRepo.findByEmail(mfaVerificationRequest.getEmail()).get();

        if (mfaVerificationCode.getCode()==mfaVerificationRequest.getCode()){
            if (!isMoreThanFifteenMinutesPassed(mfaVerificationCode.getExpireAt())){

                var jwt =jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);

                JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
                jwtAuthResponse.setToken(jwt);
                jwtAuthResponse.setRefreshToken(refreshToken);
                jwtAuthResponse.setMessage("Mfa successfully sent to the user");

                mfaVerificationCodeRepo.delete(mfaVerificationCode);

                return jwtAuthResponse;
            }
        }

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setToken(null);
        jwtAuthResponse.setRefreshToken(null);
        jwtAuthResponse.setMessage("Invalid Mfa Code");

        mfaVerificationCodeRepo.delete(mfaVerificationCode);

        return jwtAuthResponse;
    }


    //------------------------------------------------------------------------------------------------------------------------------
    // REFRESH JWT TOKEN
    //------------------------------------------------------------------------------------------------------------------------------

    public JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail= jwtService.extractUsername(refreshTokenRequest.getToken());

        User user =userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(),user)){
            var jwt =jwtService.generateToken(user);


            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setToken(jwt);
            jwtAuthResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthResponse;

        }

        return null;
    }

    //------------------------------------------------------------------------------------------------------------------------------
    // REQUEST PASSWORD RESET
    //------------------------------------------------------------------------------------------------------------------------------

    public ResponseEntity<Object> requestPasswordRecovery(@RequestBody ResetPasswordRequest resetPasswordRequest) throws MessagingException {

        Optional<User> user=userRepository.findByEmail(resetPasswordRequest.getEmail());

        Map<String, Object> map = new HashMap<String, Object>();

        int responseCode;
        String message;

        if (user.isPresent()){
            responseCode=200;
            int subRecoveryEmail = user.get().getContact().getSecondEmail().length() -9 ;
            message="We have sent a verification code to your recovery email address. Ending with with ****"+user.get().getContact().getSecondEmail().substring(subRecoveryEmail);
            emailVerificationService.sendVerificationCode(user.get().getEmail(),2);
        }else {
            responseCode=403;
            message="Invalid email address";
        }

        map.put("status", HttpStatusCode.valueOf(responseCode).value());
        map.put("message",message);

        return new ResponseEntity<Object>(map, HttpStatusCode.valueOf(responseCode));

    }


    //------------------------------------------------------------------------------------------------------------------------------
    // RESET PASSWORD
    //------------------------------------------------------------------------------------------------------------------------------

    public ResponseEntity<Object> resetPassword(ResetPasswordRequest resetPasswordRequest){

        EmailVerificationRequest emailVerificationRequest =new EmailVerificationRequest();
        emailVerificationRequest.setEmail(resetPasswordRequest.getEmail());
        emailVerificationRequest.setCode(resetPasswordRequest.getCode());

        String verifyCode = emailVerificationService.verifyCode(emailVerificationRequest);


        String[] responseStr=verifyCode.split("#");

        int responseCode;
        String message;

        Map<String, Object> map = new HashMap<String, Object>();

        if (responseStr[0].equals("0")){
            responseCode=403;
            message=responseStr[1];
        } else {
            responseCode=200;
            message="The password was successfully rest";
            User user=userRepository.findByEmail(resetPasswordRequest.getEmail()).get();
            user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
            userRepository.save(user);

            // Send Alert Email (About password rest)

        }

        map.put("status",HttpStatusCode.valueOf(responseCode).value());
        map.put("message",message);

        return new ResponseEntity<Object>(map, HttpStatusCode.valueOf(responseCode));
    }

    private int generateMfaCode(){
        Random random = new Random();
        return 10000 + random.nextInt(90000); // Range: 10000 to 99999
    }



    private Date determineExpirationDate() {
        Calendar calendar = Calendar.getInstance(); // Gets a calendar using the default time zone and locale.
        calendar.add(Calendar.MINUTE, 15); // Adds 15 minutes to the current time.
        return calendar.getTime(); // Returns a Date representing the new time.
    }


    private boolean isMoreThanFifteenMinutesPassed(Date expirationDate) {
        LocalDateTime expirationDateTime = convertToLocalDateTime(expirationDate);
        LocalDateTime currentDateTime = LocalDateTime.now();

        return ChronoUnit.MINUTES.between(expirationDateTime, currentDateTime) > 15;
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
