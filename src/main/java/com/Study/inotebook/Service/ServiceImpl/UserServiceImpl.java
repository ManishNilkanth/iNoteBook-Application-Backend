package com.Study.inotebook.Service.ServiceImpl;

import com.Study.inotebook.DTO.UserDTO;
import com.Study.inotebook.Entities.User;
import com.Study.inotebook.Exceptions.UserNotFoundException;
import com.Study.inotebook.Repository.UserRepository;
import com.Study.inotebook.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userRepository.save(modelMapper.map(userDTO,User.class));
        return modelMapper.map(user,UserDTO.class);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("user not found with this userId "));
        return modelMapper.map(user,UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user,UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("user not found with this userId"));
        updateUserData(user,userDTO);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser,UserDTO.class);
    }

    private void updateUserData(User user, UserDTO userDTO) {
         if(StringUtils.isNotBlank(userDTO.getFirstName()))
         {
             user.setFirstName(userDTO.getFirstName());
         }
        if(StringUtils.isNotBlank(userDTO.getLastName()))
        {
            user.setLastName(userDTO.getLastName());
        }
        if(StringUtils.isNotBlank(userDTO.getEmail()))
        {
            user.setEmail(userDTO.getEmail());
        }
        user.setIsActive(userDTO.getIsActive());
    }

    @Override
    public void deleteUser(Long id) {
        if(userRepository.existsById(id))
        {
            userRepository.deleteById(id);
        }
        throw new UserNotFoundException("user not found with this userId");

    }
}
