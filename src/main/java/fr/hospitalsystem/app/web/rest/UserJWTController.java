package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.Session;
import fr.hospitalsystem.app.domain.User;
import fr.hospitalsystem.app.repository.SessionRepository;
import fr.hospitalsystem.app.repository.UserRepository;
import fr.hospitalsystem.app.security.AuthoritiesConstants;
import fr.hospitalsystem.app.security.jwt.JWTFilter;
import fr.hospitalsystem.app.security.jwt.TokenProvider;
import fr.hospitalsystem.app.web.rest.vm.LoginVM;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        this.createSession(loginVM.getUsername().toLowerCase());

        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public void logout() {
        System.out.println("Logout");
        HttpSession httpSession = httpSessionFactory.getObject();
        httpSession.invalidate();
    }

    private void createSession(String login){
        Optional<User> user = userRepository.findOneWithAuthoritiesByLogin(login);

        List<GrantedAuthority> grantedAuthorities = user.get().getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());

        // Create new session if is Cassier user
        boolean isNeedSession = false;
        for (GrantedAuthority auth : grantedAuthorities){
            if (auth.getAuthority().equals(AuthoritiesConstants.CASSIER) || auth.getAuthority().equals(AuthoritiesConstants.ADMIN)){
                isNeedSession = true;
                break;
            }
        }


        HttpSession httpSession = httpSessionFactory.getObject();
        Session curentSession = (Session) httpSession.getAttribute("SessionUser");

        if (isNeedSession && curentSession == null){
            Session session = new Session();
            session.setTotal(0.0);
            session.setTotalCash(0.0);
            session.setTotalCheck(0.0);
            session.setTotalPC(0.0);
            session.setJhi_user(user.get());
            session.setCreated_by(user.get().getLogin());
            session.setCreated_date(Instant.now());

            sessionRepository.saveAndFlush(session);
            httpSession.setAttribute("SessionUser", session);
        }
    }
    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
