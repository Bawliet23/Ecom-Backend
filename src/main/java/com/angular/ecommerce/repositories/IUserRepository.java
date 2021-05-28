package com.angular.ecommerce.repositories;

import com.angular.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    User  findUserByUsername(String username);
}
