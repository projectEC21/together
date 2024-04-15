package net.kdigital.ec21.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.kdigital.ec21.entity.BlacklistEntity;

public interface BlacklistRepository extends JpaRepository<BlacklistEntity, Long> {

}
