package org.example.imdbclone.repository;

import org.example.imdbclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
