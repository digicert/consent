package com.digicert.consent.controller;

import com.digicert.consent.service.IdProviderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class IdentityProviderController {

    private IdProviderService idProviderService;

    @PostMapping("identity-provider/status")
    public ResponseEntity<Boolean> updateStatus(@RequestBody String message) {
        return ResponseEntity.ok(idProviderService.updateIdProviderStatus(message));
    }
}
