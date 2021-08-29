package com.max.movierating.service.impl;

import com.max.movierating.entity.Basket;
import com.max.movierating.entity.EnumRole;
import com.max.movierating.entity.User;
import com.max.movierating.exception.FilmNotFoundException;
import com.max.movierating.exception.UserExistException;
import com.max.movierating.exception.UserNotFoundException;
import com.max.movierating.repository.BasketRepository;
import com.max.movierating.repository.RoleRepository;
import com.max.movierating.repository.UserRepository;
import com.max.movierating.service.DefaultService;
import com.max.movierating.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements DefaultService<User>, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BasketRepository basketRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException("User with id: " + id + " was not found"));
    }


    @Override
    @Transactional(rollbackFor = ConstraintViolationException.class)
    public User save(User user) {
        try {
            checkUsernameAndEmail(user);
            Basket basket = new Basket();
            basketRepository.save(basket);
            user.setRoles(Set.of(roleRepository.findByName(EnumRole.ROLE_USER)));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setBasket(basket);
            userRepository.save(user);
        } catch (ConstraintViolationException e) {
            log.error("Can't save object = " + user.toString());
        }

        return user;
    }

    @Override
    public User update(User user) {
        User existUser = findById(user.getId());
        user.setPassword(existUser.getPassword());

        if (!user.getUsername().equals(existUser.getUsername())) {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new UserExistException("Username are existed");
            }
        }
        if (!user.getEmail().equals(existUser.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new UserExistException("Email are existed");
            }
        }

        return userRepository.save(user);
    }

    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Override
    public User deleteById(Long id) {
        User user = findById(id);
        userRepository.delete(user);
        return user;
    }

    /**
     * Method that checks login and email of user.
     *
     * @param user {@link User}
     */
    private void checkUsernameAndEmail(User user) {

    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("User not found with username: " + username);
            throw new UserNotFoundException("User not found with username = " + username);
        }
        return user;
    }

}
