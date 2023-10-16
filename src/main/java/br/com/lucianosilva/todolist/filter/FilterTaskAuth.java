package br.com.lucianosilva.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.lucianosilva.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    System.out.println("passing in filter");

    // TODO Auto-generated method stub

    // get auth (user and password)

    // check the user

    // check psw

    // next steps

    var servletPath = request.getServletPath();

    if (servletPath.startsWith("/tasks/")) {
      var authorization = request.getHeader("Authorization");

      var authEncoded = authorization.substring("Basic".length()).trim();
      byte[] authDecode = Base64.getDecoder().decode(authEncoded);

      var authString = new String(authDecode);
      System.out.println(authDecode);

      String[] credentials = authString.split(":");
      String username = credentials[0];
      String password = credentials[1];
      var user = this.userRepository.findByUsername(username);

      if (user == null) {
        response.sendError(401, "User is null");
        System.out.println("User is null");
      } else {

        var pswVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPsw());
        if (pswVerify.verified) {
          request.setAttribute("idUser", user.getId());
          filterChain.doFilter(request, response);

        } else {
          response.sendError(401, "this psw is'nt verified");
          System.out.println("pswVerify.verified == false");
        }
      }

    } else {
      filterChain.doFilter(request, response);

    }

  }

}
