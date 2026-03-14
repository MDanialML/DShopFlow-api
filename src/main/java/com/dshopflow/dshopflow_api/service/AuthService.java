package com.dshopflow.dshopflow_api.service;

import com.dshopflow.dshopflow_api.dto.AuthResponse;
import com.dshopflow.dshopflow_api.dto.LoginRequest;
import com.dshopflow.dshopflow_api.dto.RegisterRequest;
import com.dshopflow.dshopflow_api.model.Shop;
import com.dshopflow.dshopflow_api.model.User;
import com.dshopflow.dshopflow_api.model.UserRole;
import com.dshopflow.dshopflow_api.repository.ShopRepository;
import com.dshopflow.dshopflow_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       ShopRepository shopRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // check email not already taken
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // create the shop first
        Shop shop = new Shop();
        shop.setName(request.getShopName());
        shop.setIsActive(true);
        shop.setCreatedAt(LocalDateTime.now());
        Shop savedShop = shopRepository.save(shop);

        // create the owner user
        User user = new User();
        user.setShop(savedShop);
        user.setEmail(request.getEmail());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.OWNER);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        // generate JWT token
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                savedShop.getId()
        );

        return new AuthResponse(
                token,
                user.getRole().name(),
                savedShop.getName(),
                user.getEmail()
        );
    }

    public AuthResponse login(LoginRequest request) {

        // find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        // verify password
        if (!passwordEncoder.matches(request.getPassword(),
                user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        // generate token
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getShop() != null ? user.getShop().getId() : null
        );

        return new AuthResponse(
                token,
                user.getRole().name(),
                user.getShop() != null ?
                        user.getShop().getName() : "Platform",
                user.getEmail()
        );
    }
}