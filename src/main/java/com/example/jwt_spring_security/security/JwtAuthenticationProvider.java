package com.example.jwt_spring_security.security;

import com.example.jwt_spring_security.exception.JwtTokenException;
import com.example.jwt_spring_security.model.JwtAuthenticationToken;
import com.example.jwt_spring_security.model.JwtUser;
import com.example.jwt_spring_security.model.JwtUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        System.out.println("Inside retrieveUser");
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        String token = authenticationToken.getToken();
        JwtUser jwtUser = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey("HEARMESUBJECTSOFYMIRMYNAMEISERENJAEGERIAMADDRESSINGMYFELLOWSUBJECTSOFYMIRSPEAKINGTOYOUDIRECTLYTHROUGHTHEPOWEROFTHEFOUNDERALLTHEWALLSONTHEISLANDOFPARADISHAVECRUMBLEDTOTHEGROUNDANDTHELEGIONSOFTITANSBURIEDWITHINHAVEBEGUNTHEIRMARCH")
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            jwtUser = new JwtUser();
            System.out.println("sub "+body.getSubject()+" id "+body.get("userId")+" role"+body.get("role"));
            jwtUser.setUserName(body.getSubject());
            jwtUser.setId(Long.parseLong((String) body.get("userId")));
            jwtUser.setRole((String) body.get("role"));
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList(jwtUser.getRole());
            System.out.println("Granted Authorities: " + grantedAuthorities);
            return new JwtUserDetails(jwtUser.getUserName(), jwtUser.getId(),
                    token,
                    grantedAuthorities);
        }
        catch (Exception e) {
            System.out.println("Inside exception");
            throw new JwtTokenException("JWT Token is incorrect");
        }

    }
}
