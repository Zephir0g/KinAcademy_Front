package com.fo4ik.kinacademy.entity.data.service;

import com.fo4ik.kinacademy.core.PasswordConfig;
import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.core.UserAuthProvider;
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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final UserMapper userMapper;
    private final UserAuthProvider userAuthProvider;

    public UserDto login(CredentialDto credentialDTO) {
        User user = userRepository.findByUsername(credentialDTO.username())
                .orElseThrow(() -> new AppException("Incorrect Login or Password", HttpStatus.NOT_FOUND));


        if (user.getPassword().equals(String.valueOf(credentialDTO.password()))) {
            return userMapper.userToUserDto(user);
        }

        if (passwordConfig.checkEncodedData(credentialDTO.password(), user.getPassword())) {
            return userMapper.userToUserDto(user);
        }

        throw new AppException("Incorrect Login or Password", HttpStatus.BAD_REQUEST);
    }

    public User register(SingUpUserDto singUpDto) {
        Optional<User> oUser = userRepository.findByUsername(singUpDto.username());
        Optional<User> oUserEmail = userRepository.findByEmail(singUpDto.email());

        if (oUser.isPresent()) {
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        //Check if email is already taken
        if (oUserEmail.isPresent()) {
            throw new AppException("Email is invalid or already taken", HttpStatus.BAD_REQUEST);
        }


        User user = userMapper.singUpDtoToUser(singUpDto);
        user.setPassword(passwordConfig.encodeData(singUpDto.password()));
        user.setLanguage(singUpDto.language());
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.STUDENT);

        return save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public UserDto save(UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        return userMapper.userToUserDto(userRepository.save(user));
    }


    public UserDto getUserByUsernameDto(String username) {
        return userMapper.userToUserDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND)));
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }

    public boolean isUserDataValid(String username, CredentialDto credentialDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (user.getPassword().equals(String.valueOf(credentialDTO.password()))) {
            return true;
        }

        if (passwordConfig.checkEncodedData(credentialDTO.password(), user.getPassword())) {
            return true;
        }

        return false;
    }

    public Response isUserActive(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("User not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        if (!user.get().getStatus().equals(Status.ACTIVE)) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("User is not active")
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .build();
        }

        return new Response().builder()
                .isSuccess(true)
                .build();
    }

    public User updateUser(User user) {
        Optional<User> oUser = userRepository.findById(user.getId());
        if (oUser.isEmpty()) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }

        return save(user);
    }

    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND))
                .getId();
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> oUser = userRepository.findByUsername(username);
        if (oUser.isEmpty()) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }

        return oUser;
    }
}
