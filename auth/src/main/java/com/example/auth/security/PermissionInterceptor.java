package com.example.auth.security;

import com.example.auth.repository.UsersRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.naming.AuthenticationException;
import javax.naming.NoPermissionException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UsersRepository userRepository;

    @Value("${jwt.secret}")
    String secretKey ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            if (!(handler instanceof HandlerMethod hm)) {
                return true;
            }

            Method method = hm.getMethod();

            if (method.isAnnotationPresent(Permission.class)) {
                Permission permissionAnnotation = method.getAnnotation(Permission.class);
                PermissionsEnum[] requiredPermissions = permissionAnnotation.permissions();
                LogicEnum logicType = permissionAnnotation.type();

                String username = getUserNameFromRequest(request);

                List<PermissionsEnum> userPermissions = getUserPermissions(username);

                if (userPermissions == null) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }
                boolean hasPermission = checkPermissions(userPermissions, requiredPermissions, logicType);

                if (!hasPermission) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }
            }

            return true;
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (NoPermissionException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return false;
    }

    private String getUserNameFromRequest(HttpServletRequest request) throws AuthenticationException, NoPermissionException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                        .parseClaimsJws(jwtToken).getBody();
                 return claims.getSubject();

            } catch (ExpiredJwtException e) {
                throw new AuthenticationException("Token expired");
            } catch (SignatureException e) {
                throw new AuthenticationException("Invalid JWT signature");
            } catch (JwtException e) {
                throw new AuthenticationException("Invalid JWT token");
            }
        }
        throw new AuthenticationException("Missing or invalid Authorization header");
    }

    private List<PermissionsEnum> getUserPermissions(String username) {
        try {
            return userRepository.getUserPermissions(username);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    private boolean checkPermissions(List<PermissionsEnum> userPermissions, PermissionsEnum[] requiredPermissions, LogicEnum logicType) {

        if(logicType == LogicEnum.All){
            return Arrays.stream(requiredPermissions).allMatch(userPermissions::contains);
        }else if(logicType == LogicEnum.Any){
            return Arrays.stream(requiredPermissions).anyMatch(userPermissions::contains);
        }
        return false;
    }
}

