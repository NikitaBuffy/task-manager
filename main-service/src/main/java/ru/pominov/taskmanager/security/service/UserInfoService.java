package ru.pominov.taskmanager.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pominov.taskmanager.model.User;
import ru.pominov.taskmanager.repository.UserRepository;
import ru.pominov.taskmanager.security.model.UserInfo;
import ru.pominov.taskmanager.security.config.UserInfoDetails;
import ru.pominov.taskmanager.security.repository.UserInfoRepository;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<UserInfo> userDetail = userInfoRepository.findByEmail(email);

        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with email=" + email + " was not found"));
    }

    public UserInfo addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        User user = new User();
        user.setUserInfo(userInfo);
        userInfoRepository.save(userInfo);
        userRepository.save(user);
        return userInfo;
    }

    public UserInfo getUserInfo(String email) {
        return userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email=" + email + " was not found"));
    }

    public Long getUserIdByEmail(String email) {
        UserInfo userDetail = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email=" + email + " was not found"));
        return userDetail.getId();
    }

    public UserInfo getUserInfoById(Long userId) {
        return userInfoRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with id=" + userId + " was not found"));
    }
}
