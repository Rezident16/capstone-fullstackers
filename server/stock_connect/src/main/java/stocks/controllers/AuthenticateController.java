package stocks.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import stocks.domain.Result;
import stocks.models.AppUser;
import stocks.security.JwtConverter;
import stocks.security.SecurityConfig;
import stocks.security.UserService;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class AuthenticateController {
    private final JwtConverter jwtConverter;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SecurityConfig securityConfig;

    public AuthenticateController(JwtConverter jwtConverter, UserService userService, AuthenticationManager authenticationManager, SecurityConfig securityConfig) {
        this.jwtConverter = jwtConverter;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.securityConfig = securityConfig;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody Map<String, String> userInfo) {
        AppUser appUser = new AppUser();

        appUser.setFirstName(userInfo.get("firstName"));
        appUser.setLastName(userInfo.get("lastName"));
        appUser.setUsername(userInfo.get("username"));
        appUser.setPassword(userInfo.get("password"));
        appUser.setEmail(userInfo.get("email"));
        appUser.setRoleId(2);

        System.out.println(userInfo.get("password"));
        Result<AppUser> result = userService.add(appUser);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

//    login
//    @PostMapping("/authenticate")
//    public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> credentials) {
//
//        UsernamePasswordAuthenticationToken authToken =
//                new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password"));
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(authToken);
//
//            if (authentication.isAuthenticated()) {
////                String jwtToken = jwtConverter.getTokenFromUser((User) authentication.getPrincipal());
//
//                HashMap<String, String> map = new HashMap<>();
//                map.put("jwt_token", jwtToken);
//
//                return new ResponseEntity<>(map, HttpStatus.OK);
//            }
//
//        } catch (AuthenticationException ex) {
//            System.out.println(ex);
//        }
//
//        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//    }

    @GetMapping("/{userId}")
    public AppUser findById(@PathVariable int userId) {
        return userService.findByID(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("#userId == principal.id")
    public ResponseEntity<Object> update(@PathVariable int userId, @RequestBody AppUser appUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser currentUser = (AppUser) authentication.getPrincipal();

        if (currentUser.getUserId() == userId) {
            Result<AppUser> result = userService.update(appUser);
            if (result.isSuccess()) {
                return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
            }
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
