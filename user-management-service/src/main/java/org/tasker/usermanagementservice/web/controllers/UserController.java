package org.tasker.usermanagementservice.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.tasker.usermanagementservice.service.UserService;
import org.tasker.usermanagementservice.web.dto.user.GetUserInfoResponse;

@Slf4j
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetUserInfoResponse> getUserInfo(@PathVariable("id") String id) {
        log.info("Authentificated user: {}", ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaims());
        return ResponseEntity.ok(userService.getUserInfo(id));
    }
}
