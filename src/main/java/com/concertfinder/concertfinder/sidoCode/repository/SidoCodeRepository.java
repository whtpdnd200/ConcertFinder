package com.concertfinder.concertfinder.sidoCode.repository;

import com.concertfinder.concertfinder.sidoCode.domain.SidoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SidoCodeRepository extends JpaRepository<SidoCode, Byte> {

    SidoCode findBySidoCode(String sidoCode);
}
