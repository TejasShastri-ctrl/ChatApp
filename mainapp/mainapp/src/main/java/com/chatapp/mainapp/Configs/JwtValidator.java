package com.chatapp.mainapp.Configs;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;


import org.hibernate.grammars.hql.HqlParser.SecondContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        // System.out.println("JWT VALIDATOR TOKEN : " + jwt);
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        } //! for some fucking reason this was not working

        if (jwt != null) {
            try {

                // Bearer removal from token - removed
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes()); // Ensure it's long enough
 // ! could be parseClaimsJwt
                Claims claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String username = String.valueOf(claim.get("email"));
                String authorities = String.valueOf(claim.get("authorities"));

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, auths);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                System.err.println("JWT parsing error: " + e.getMessage());
                throw new BadCredentialsException("Invalid token recieved...and this is the authorization : " + jwt + " err message: " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}