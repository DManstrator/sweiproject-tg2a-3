package ly.muasica.base;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ly.muasica.base.auth.AuthRepository;
import ly.muasica.base.auth.User;

@Controller // so framework can recognize this as a controller class
@RequestMapping("/")
public class HomeController {
	
    @Autowired
    private AuthRepository authRepository;
    
	@RequestMapping()
    public String index(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null)  {
			for (Cookie cookie : cookies)  {
				if (cookie.getName().equals("MUASiCaly"))  {
					Iterable<User> users = authRepository.findAll();
					for (User user : users)  {
						if (user.getCookie().getValue().equals(cookie.getValue()))  {
						    System.out.println("Cookie matches");
							return "activities.html";
						}
					}
				}
			}
		}
		System.out.println("returning index.html");
		return "index.html";
    }

}