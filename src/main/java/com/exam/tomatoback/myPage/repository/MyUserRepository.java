package com.exam.tomatoback.mypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.exam.tomatoback.user.model.*;

@Repository
public interface MyUserRepository extends JpaRepository<User, Long> {

}
