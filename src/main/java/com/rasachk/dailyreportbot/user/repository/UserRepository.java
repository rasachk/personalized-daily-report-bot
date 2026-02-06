package com.rasachk.dailyreportbot.user.repository;

import com.rasachk.dailyreportbot.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
