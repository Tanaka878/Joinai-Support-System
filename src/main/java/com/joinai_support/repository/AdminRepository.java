package com.joinai_support.repository;

import com.joinai_support.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByEmail(String email);

}
