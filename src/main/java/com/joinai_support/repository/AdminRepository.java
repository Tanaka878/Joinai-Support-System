package com.joinai_support.repository;

import com.joinai_support.domain.Admin;
import com.joinai_support.utils.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByEmail(String email);
    List<Admin> findAllByRole(Role role);

}
