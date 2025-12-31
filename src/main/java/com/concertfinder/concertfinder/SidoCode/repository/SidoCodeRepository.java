package com.concertfinder.concertfinder.SidoCode.repository;

import com.concertfinder.concertfinder.SidoCode.domain.SidoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SidoCodeRepository extends JpaRepository<SidoCode, Byte> {

    public List<SidoCode> findAll();

    public SidoCode findBySidoCode(byte sidoCode);
}
