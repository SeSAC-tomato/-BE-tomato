package com.exam.tomatoback.mypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepository extends JpaRepository<User, Long> {

}
