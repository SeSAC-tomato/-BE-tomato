package com.exam.tomatoback.post.repository;

import com.exam.tomatoback.user.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAll();
}
