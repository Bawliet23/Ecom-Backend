package com.angular.ecommerce.controllers;

import com.angular.ecommerce.dto.CartDTO;
import com.angular.ecommerce.dto.CartItemDTO;
import com.angular.ecommerce.dto.ChangePasswordDTO;
import com.angular.ecommerce.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private IUserService userService;

    @PostMapping("/{id}/addCart")
    public ResponseEntity<?> addToCart(@PathVariable("id") Long id, @RequestBody CartItemDTO item){
        Boolean added = userService.addToCart(id,item);
        if(!added)
            return ResponseEntity.badRequest().body("Not Added");

        return ResponseEntity.ok().body("Added successful");
    }
    @GetMapping("/{id}/cart")
    public  ResponseEntity<?> getCart(@PathVariable("id") Long id){
        CartDTO cartDTO = userService.getCart(id);
        if(cartDTO == null)
            return ResponseEntity.badRequest().body("No Cart Found");
        return ResponseEntity.ok(cartDTO);
    }
    @DeleteMapping("/{id}/emptyCart")
    public ResponseEntity<?> emptyCart(@PathVariable("id") Long id){
        Boolean isEmpty = userService.emptyCart(id);
        if(!isEmpty)
            return ResponseEntity.badRequest().body("Cart Not Empty");
        return ResponseEntity.ok("Cart Empty");
    }
    @DeleteMapping("/cart/{itemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("itemId") Long cartItemId){
        Boolean deleted = userService.deleteCartItem( cartItemId);
        if(!deleted)
            return ResponseEntity.badRequest().body("Item Not Deleted");
        return ResponseEntity.ok("Item Deleted");
    }
    @PutMapping("/cart/{itemId}")
    public ResponseEntity<?> updateQuantity(@PathVariable("itemId") Long cartItemId,@RequestParam("quantity") int quantity){
        Boolean modified = userService.updateCartItemQuantity(cartItemId,quantity);
        if(!modified)
            return ResponseEntity.badRequest().body("Quantity Not Modified");
        return ResponseEntity.ok("Quantity  Modified");
    }
    @PostMapping("/makeOrder")
    public ResponseEntity<?> makeOrder(@RequestParam("id") String id){
        Long id1 = Long.valueOf(id);
        Boolean added = userService.makeOrder(id1);
        if(!added)
            return ResponseEntity.badRequest().body("Order Failed");

        return ResponseEntity.ok().body("Order successful");
    }
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePassword){
        Boolean changed = userService.changePassword(changePassword);
        if(!changed)
            return ResponseEntity.badRequest().body("Failed To Change Password");

        return ResponseEntity.ok().body("Password Changed");
    }
}
