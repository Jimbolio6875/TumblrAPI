package org.example.tumblrapijavamaster;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

@Controller
public class TumblrController {

    private final TumblrService tumblrService;

    @Autowired
    public TumblrController(TumblrService tumblrService) {
        this.tumblrService = tumblrService;
    }

    @GetMapping("/start-oauth")
    public String startOAuthProcess(RedirectAttributes redirectAttributes) {
        String authorizationUrl = tumblrService.getAuthorizationUrl();
        return "redirect:" + authorizationUrl;
    }

    @GetMapping("/oauth-callback")
    public String oauthCallback(@RequestParam("oauth_verifier") String oauthVerifier, Model model) throws Exception {
        String userInfo = tumblrService.getUserInfo(oauthVerifier);
        model.addAttribute("userInfo", userInfo);
        return "create-post";
    }

    @PostMapping("/post-to-tumblr")
    public String postToTumblr(@RequestParam("postContent") String postContent, Model model) throws Exception {
        tumblrService.postToBlog(postContent);
        model.addAttribute("message", "Successfully posted to Tumblr!");
        return "post-success";
    }

    @GetMapping("/create-post")
    public String showPostCreationPage() {
        return "create-post";
    }

}
