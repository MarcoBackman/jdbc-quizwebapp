package com.example.week3day13project.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @TODO: It is best to have credential check path before redirection instead of '/*' path checking every cases
 */
@WebFilter(urlPatterns = "/*")
public class MainFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        //when user session is valid
        if (session != null && session.getAttribute("userObject") != null) {
            //add when valid user enters wrong address
            response.sendRedirect("/home");
        } else {
            System.out.println("Invalid user session");
            // redirect back to the login page if user is not logged in
            response.sendRedirect("/login");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        //Consider changing this to switch case; if-else will decrease speed
        if ("/login".equals(path)) {
            return true;
        } else if ("/register".equals(path)) {
            return true;
        } else if ("/contact-us".equals(path)) {
            return true;
        } else if ("/home".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/feedback".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/question-editor".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/user-profile".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/suspend_user".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/activate_user".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/add_multiple_question".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/add_short_question".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/submit_multiple_change".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/delete_question".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/take-quiz".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/previous".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/next".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/disable_question".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else if ("/activate_question".equals(path)) {
            HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("userObject") != null;
        } else {
            //Take quiz topic choose matcher
            final Pattern pattern
                    = Pattern.compile("^(take-quiz)$|(take-quiz\\?+topic-info=[0-9]+\\/[0-9]+)");
            final Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                System.out.println("Redirecting to take-quiz page");
                HttpSession session = request.getSession(false);
                return session != null && session.getAttribute("userObject") != null;
            }

            //Take quiz question numbering matcher
            final Pattern pattern2
                    = Pattern.compile("^(take-quiz)$|(take-quiz\\/[0-9]+\\/[0-9]+)|(take-quiz\\/[0-9]+)");
            final Matcher matcher2 = pattern2.matcher(path);
            if (matcher2.find()) {
                System.out.println("Redirecting to take-quiz page");
                HttpSession session = request.getSession(false);
                return session != null && session.getAttribute("userObject") != null;
            }

            //Submit quiz matcher
            final Pattern pattern3
                    = Pattern.compile("^\\/.*(submit_quiz)$|\\/.*(submit_quiz\\/[0-9]+)");
            final Matcher matcher3 = pattern3.matcher(path);
            if (matcher3.find()) {
                System.out.println("Redirecting to submit_quiz page");
                HttpSession session = request.getSession(false);
                return session != null && session.getAttribute("userObject") != null;
            }

            //Admin page matcher
            final Pattern pattern4
                    = Pattern.compile("^\\/(admin-page)\\/(log-detail){1}\\/([0-9]+)" +
                    "|^\\/(admin-page)\\/{0,1}$" +
                    "|\\/(admin-page)\\/(by-category){1}" +
                    "|\\/(admin-page)\\/(by-user){1}");
            final Matcher matcher4 = pattern4.matcher(path);
            if (matcher4.find()) {
                System.out.println("Redirecting to admin page");
                HttpSession session = request.getSession(false);
                return session != null && session.getAttribute("userObject") != null;
            }

        }


        System.out.println("redirecting to login or home....");
        return false;
    }
}
