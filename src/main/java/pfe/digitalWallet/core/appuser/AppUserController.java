package pfe.digitalWallet.core.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/appuser")
public class AppUserController {
    @Autowired
    private AppUserService appUserService;

    @PostMapping
    public ResponseEntity<AppUser> createAppUser(AppUser appUser){
        return ResponseEntity.ok(appUserService.getByUsername(appUser.getUsername()).orElse(null));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> getAll(){
        List<AppUser> appUsers = appUserService.getAll().orElse(null);
        return ResponseEntity.ok(appUsers);
    }

}
