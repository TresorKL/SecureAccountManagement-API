package com.example.secureaccountapi.service.impl;

import com.example.secureaccountapi.dto.EmailVerificationRequest;

import com.example.secureaccountapi.entity.EmailVerification;
import com.example.secureaccountapi.entity.User;
import com.example.secureaccountapi.repository.EmailVerificationRepo;
import com.example.secureaccountapi.repository.UserRepo;
import com.example.secureaccountapi.service.EmailService;
import com.example.secureaccountapi.service.EmailVerificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationImpl implements EmailVerificationService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    EmailVerificationRepo emailVerificationRepo;

    @Autowired
    EmailService emailService;







    @Override
    public void sendVerificationCode(String email,int reason) throws MessagingException {

        /* REASON

        * 1: email verification
        * 2: reset password request
        * 3: Mfa
        * */

        String subject;

        if(reason==1){
            subject ="SECURE ACCOUNT: EMAIL VERIFICATION";
        }else if(reason==2){
            subject= "RESET PASSWORD REQUEST: Verification Code";
        }else{
            subject="MFA: Verification code";
        }

        Optional<User> user =userRepo.findByEmail(email);

        if (user.isPresent()){
            String username=user.get().getUsername();

            int code= generateCode();

            // save Email verification
            EmailVerification emailVerification=new EmailVerification();


            emailVerification.setEmail(email);
            emailVerification.setCode(code);
            emailVerification.setIssuedAt(new Date());
            emailVerification.setExpireAt(determineExpirationDate());

            emailVerificationRepo.save(emailVerification);

            String sendTo;

            if(reason==1) {
                sendTo=email;
            }else {
                sendTo=user.get().getContact().getSecondEmail();
            }

            // Send Code
            String htmlContent=createHtmlVerificationEmail( username,  code);
            emailService.sendHtmlMail(sendTo,subject,htmlContent,true);

        }

    }

    @Override
    public void resendVerificationCode(String email) throws MessagingException {

    }

    @Override
    public String verifyCode(EmailVerificationRequest emailVerificationRequest) {
        User user = userRepo.findByEmail(emailVerificationRequest.getEmail()).get();

        EmailVerification emailVerification=emailVerificationRepo.findByEmail(emailVerificationRequest.getEmail());

        if (emailVerification.getCode()==emailVerificationRequest.getCode()){
            if (!isMoreThanFifteenMinutesPassed(emailVerification.getExpireAt())){

                user.setEnabled(true);
                userRepo.save(user);
                emailVerificationRepo.delete(emailVerification);

                return "1#Account Successfully Activated";
            }
        }
        emailVerificationRepo.delete(emailVerification);
        return "0#Invalid Code Or It Has Expired";
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


//    public static boolean isMoreThanFifteenMinutesPassed(LocalDateTime expirationDateTime) {
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        return ChronoUnit.MINUTES.between( expirationDateTime, currentDateTime) > 15;
//    }




    private int generateCode(){
        Random random = new Random();
        return 10000 + random.nextInt(90000); // Range: 10000 to 99999

    }


    private Date determineExpirationDate() {
        Calendar calendar = Calendar.getInstance(); // Gets a calendar using the default time zone and locale.
        calendar.add(Calendar.MINUTE, 15); // Adds 15 minutes to the current time.
        return calendar.getTime(); // Returns a Date representing the new time.
    }

    private String createHtmlVerificationEmail(String username, int verificationCode) {

        String htmlContent = "<html>"
                + "<body style='font-family: Arial, sans-serif;'>"
                + "<h1 style='color: navy;'>Email Verification</h1>"
                + "<p>Hello <strong>" + username + "</strong>,</p>"
                + "<p>Your verification code is: <span style='color: green; font-weight: bold;'>" + verificationCode + "</span></p>"
                + "<p>This code will expire in 15 minutes.</p>"
                + "<p>If you did not request this code, please ignore this email.</p>"
                + "<br>"
                + "<p>Best regards,<br>Your Team</p>"
                + "</body>"
                + "</html>";

        return htmlContent;
    }
}
