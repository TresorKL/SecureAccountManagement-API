package com.example.secureaccountapi.controller;


import com.example.secureaccountapi.dto.SecuritySettingRequest;
import com.example.secureaccountapi.dto.UpdateContactRequest;
import com.example.secureaccountapi.dto.UpdateDetailsRequest;
import com.example.secureaccountapi.entity.Contact;
import com.example.secureaccountapi.entity.Details;
import com.example.secureaccountapi.entity.SecuritySetting;
import com.example.secureaccountapi.entity.User;
import com.example.secureaccountapi.repository.ContactRepo;
import com.example.secureaccountapi.repository.DetailsRepo;
import com.example.secureaccountapi.repository.SecuritySettingRepo;
import com.example.secureaccountapi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    SecuritySettingRepo securitySettingRepo;
    @Autowired
    DetailsRepo detailsRepo;
    @Autowired
    ContactRepo contactRepo;

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello user");
    }

    @PostMapping(value="update/security", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateSecuritySetting(@RequestBody SecuritySettingRequest setting){

        SecuritySetting securitySetting =new SecuritySetting();

        User user=userRepo.findByEmail(setting.getEmail()).get();

        securitySetting.setAskConfirmationQuestionForMfa(setting.isAskConfirmationQuestionForMfa());
        securitySetting.setMfaEnabled(setting.isMfaEnabled());
        securitySetting.setSendCodeForMfa(setting.isSendCodeForMfa());
        securitySetting.setUserSecondaryEmailForMfa(setting.isUserSecondaryEmailForMfa());

        user.setSecuritySetting(securitySetting);
        securitySettingRepo.save(securitySetting);

        userRepo.save(user);

        return ResponseEntity.ok("Security Setting successfully updated");
    }

    @PostMapping("update/details")
    public ResponseEntity<String> updateUserDetails(@RequestBody UpdateDetailsRequest details){

        Details newDetails =new Details();
        User user=userRepo.findByEmail(details.getEmail()).get();

        if (details.getFirstName()!=null){
            newDetails.setFirstName(details.getFirstName());
        }else if(details.getLastName()!=null){
            newDetails.setLastName(details.getLastName());
        }else if(details.getDataOfBirth()!=null){

            newDetails.setDataOfBirth(details.getDataOfBirth());
        }else if(details.getPhysicalAddress()!=null){

            newDetails.setPhysicalAddress(details.getPhysicalAddress());
        }

        user.setDetails(newDetails);
        detailsRepo.save(newDetails);
        userRepo.save(user);

        return ResponseEntity.ok("Details successfully updated");
    }

    @PostMapping(value = "update/contact", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String updateUserContact(@RequestBody UpdateContactRequest contact){


        User user = userRepo.findByEmail(contact.getEmail()).orElse(null);



        if (user != null) {
            Contact userContact = user.getContact();

            if (userContact != null && userContact.getId() != null) {
                Contact contact1 = contactRepo.findById(userContact.getId()).orElse(null);

                if (contact1 != null) {
                    if (contact.getSecondEmail() != null) {
                        contact1.setSecondEmail(contact.getSecondEmail());
                    }
                    if (contact.getPhoneNumber() != null) {
                        contact1.setPhoneNumber(contact.getPhoneNumber());
                    }

                    user.setContact(contact1);

                }
            } else {
                Contact newContact = new Contact();
                if (contact.getSecondEmail() != null) {
                    newContact.setSecondEmail(contact.getSecondEmail());
                }
                if (contact.getPhoneNumber() != null) {
                    newContact.setPhoneNumber(contact.getPhoneNumber());
                }
                user.setContact(newContact);
                contactRepo.save(newContact);
            }

            userRepo.save(user);
        }





        return  "Contact successfully updated";
    }

    @GetMapping("get/{email}")
    public ResponseEntity<Object>getUser(@PathVariable("email") String email){

        User user=userRepo.findByEmail(email).get();

        return ResponseEntity.ok(user);
    }
}
