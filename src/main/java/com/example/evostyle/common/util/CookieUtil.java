package com.example.evostyle.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class CookieUtil {

    public static Cookie getOrCreateCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        String value = UUID.randomUUID().toString().substring(0, 8);

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            Optional<Cookie> cookie = Arrays.stream(cookies)
                    .filter(c -> cookieName.equals(c.getName()))
                    .findFirst();
            if (cookie.isPresent()) {
                return cookie.get();
            }
        }

        Cookie newCookie = new Cookie(cookieName, value);
        newCookie.setMaxAge(86400); // 1일
        newCookie.setPath("/");
        response.addCookie(newCookie);
        return newCookie;
    }

}
