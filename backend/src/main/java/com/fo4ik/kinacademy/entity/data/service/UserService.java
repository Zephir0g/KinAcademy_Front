package com.fo4ik.kinacademy.entity.data.service;

import com.fo4ik.kinacademy.configuration.PasswordConfig;
import com.fo4ik.kinacademy.configuration.UserAuthProvider;
import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.dto.user.CredentialDto;
import com.fo4ik.kinacademy.dto.user.SingUpUserDto;
import com.fo4ik.kinacademy.dto.user.UserDto;
import com.fo4ik.kinacademy.entity.data.mappers.UserMapper;
import com.fo4ik.kinacademy.entity.data.repository.UserRepository;
import com.fo4ik.kinacademy.entity.user.Role;
import com.fo4ik.kinacademy.entity.user.Status;
import com.fo4ik.kinacademy.entity.user.User;
import com.fo4ik.kinacademy.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final UserMapper userMapper;
    private final UserAuthProvider userAuthProvider;

    public UserDto login(CredentialDto credentialDTO) {
        User user = userRepository.findByLogin(credentialDTO.login())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));


        if (user.getPassword().equals(String.valueOf(credentialDTO.password()))) {
            return userMapper.userToUserDto(user);
        }

        if (passwordConfig.checkEncodedData(credentialDTO.password(), user.getPassword())) {
            return userMapper.userToUserDto(user);
        }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SingUpUserDto singUpDto) {
        Optional<User> oUser = userRepository.findByLogin(singUpDto.login());
        Optional<User> oUserEmail = userRepository.findByEmail(singUpDto.email());

        if (oUser.isPresent()) {
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        //Check if email is already taken
        if (oUserEmail.isPresent()) {
            throw new AppException("Email already taken", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.singUpDtoToUser(singUpDto);
        user.setPassword(passwordConfig.encodeData(singUpDto.password()));
        user.setUSER_TOKEN();
        user.setLanguage(singUpDto.language());
        user.setStatus(Status.INACTIVE);
        user.setRoles(new ArrayList<>(List.of(Role.STUDENT)));

        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public UserDto saveUser(UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        return userMapper.userToUserDto(userRepository.save(user));
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return userMapper.userToUserDto(user);
    }

    public boolean isUserSecureTokenValid(String SECURE_TOKEN, Long userId) {
        UserDto oUser = userMapper.userToUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND)));


        SECURE_TOKEN = SECURE_TOKEN.replace("Bearer ", "");
        Authentication authentication = userAuthProvider.validateToken(SECURE_TOKEN);

        if (authentication != null && authentication.getPrincipal() instanceof UserDto) {
            UserDto userDto = (UserDto) authentication.getPrincipal();
            return oUser.getLogin().equals(userDto.getLogin());
        }

        return false;
    }

    public Response isUserValid(String SECURE_TOKEN, Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("User not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        if (!isUserSecureTokenValid(SECURE_TOKEN, id)) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("Invalid SECURE_TOKEN")
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        if (!user.get().getStatus().equals(Status.ACTIVE)) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("User is not active")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        return new Response().builder()
                .isSuccess(true)
                .build();
    }

}
