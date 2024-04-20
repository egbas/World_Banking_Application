package com.egbas.World.Banking.infrastructure.controller;

import com.egbas.World.Banking.payload.response.BankResponse;
import com.egbas.World.Banking.service.GeneralUserService;
import com.egbas.World.Banking.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class GeneralUserController {
    private final GeneralUserService generalUserService;

    @PutMapping("profile-picture")
    public ResponseEntity<BankResponse<String>> profilePicUpload(@RequestParam MultipartFile profilePics){
        if (profilePics.getSize() > AppConstant.MAX_FILE_SIZE){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BankResponse<>("File size exceeds the normal limit"));
        }
        return generalUserService.uploadProfilePics(profilePics);
    }
}
