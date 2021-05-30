package com.angular.ecommerce.services;

import com.angular.ecommerce.dto.CartDTO;
import com.angular.ecommerce.dto.CartItemDTO;
import com.angular.ecommerce.dto.RegisterDTO;
import com.angular.ecommerce.entities.Cart;
import com.angular.ecommerce.entities.CartItem;
import com.angular.ecommerce.entities.Role;
import com.angular.ecommerce.entities.User;
import com.angular.ecommerce.repositories.ICartItemRepository;
import com.angular.ecommerce.repositories.ICartRepository;
import com.angular.ecommerce.repositories.IRoleRepository;
import com.angular.ecommerce.repositories.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Transactional
@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ICartItemRepository cartItemRepository;
    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public User addUser(RegisterDTO registerDTO) {

        User user = modelMapper.map(registerDTO,User.class);
        Cart cart = new Cart();
        Cart cart1 = cartRepository.save(cart);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().clear();
        for (String role : registerDTO.getRole()){
            Role role1 = roleRepository.findByRole(role);
            user.getRoles().add(role1);
        }
        User u =  userRepository.save(user);
        cart1.setUser(user);
        user.setCart(cart1);
        return u;
    }

    @Override
    public Boolean addToCart(Long cartId, CartItemDTO cartItemDTO) {
        Optional<Cart> cart=cartRepository.findById(cartId);
        AtomicReference<Boolean> added = new AtomicReference<>(false);
        CartItem cartItem = modelMapper.map(cartItemDTO,CartItem.class);
        if (cart.isPresent()) {
            if (cart.get().getCartItems().stream().anyMatch(cartItem1 -> cartItem1.getProduct().getId().equals(cartItem.getProduct().getId()))) {
               cart.get().getCartItems().stream().forEach(cartItem1 -> {
                   if (cartItem1.getProduct().getId().equals(cartItem.getProduct().getId())){
                       if (cartItem1.getProduct().getStock()>=(cartItem1.getQuantity()+cartItem.getQuantity())){
                           cartItem1.setQuantity(cartItem1.getQuantity()+cartItem.getQuantity());
                           cartRepository.save(cart.get());
                           added.set(true);
                       }
                   }
                 });
         }else{
                if (cartItem.getQuantity()<=cartItem.getProduct().getStock()){
                    cart.get().getCartItems().add(cartItem);
                    added.set(true);
                }
            }


        }

        return added.get();
    }

    @Override
    public Boolean emptyCart(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
           user.get().getCart().getCartItems().clear();
           userRepository.save(user.get());
           return true;
        }

        return false;
    }

    @Override
    public Boolean deleteCartItem(Long cartId, Long cartItemId) {
        Optional<Cart> cart=cartRepository.findById(cartId);
        if (cart.isPresent()){
            cart.get().getCartItems().removeIf(cartItem -> cartItem.getId().equals(cartItemId));
            cartRepository.save(cart.get());
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateCartItemQuantity(Long cartItemId, int quantity) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if(cartItem.isPresent()){
            if (cartItem.get().getProduct().getStock()>=quantity && quantity>0){
                cartItemRepository.save(cartItem.get());

            }if (quantity==0){
                cartItemRepository.delete(cartItem.get());
            }
        }
        return true;
    }

    @Override
    public CartDTO getCart(Long id) {
        Optional<User> user = userRepository.findById(id);
        CartDTO cartDTO = null;
        if (user.isPresent()){
            cartDTO = modelMapper.map(user.get().getCart(),CartDTO.class);
        }
        return cartDTO;
    }
}
