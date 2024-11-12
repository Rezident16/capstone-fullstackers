//package stocks.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import stocks.data.UserRepository;
//
//@Service
//public class UserService implements UserDetailsService {
//
//    @Autowired
//    private final UserRepository repository;
//
//    private final PasswordEncoder encoder;
//
//    public UserService(UserRepository repository, PasswordEncoder encoder) {
//        this.repository = repository;
//        this.encoder = encoder;
//    }
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = repository.findByUsername(u)
//
//
//    }
//
//
//}
