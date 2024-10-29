package com.url.shorten.repo;

import com.url.shorten.dto.Url;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UrlRepo extends JpaRepository<Url, Long> {
    @Query(value = "SELECT id FROM URL ORDER BY id DESC LIMIT 1;\n", nativeQuery = true)
    public Optional<Integer> getMaxId();

    @Query(value = "select * from url where short_url=:shortUrl", nativeQuery = true)
    Optional<Url> getUrlObj(@Param("shortUrl") String shortUrl);

    @Modifying
    @Transactional
    @Query(value = " delete from url where short_url=:alias\n", nativeQuery = true)
    public void deleteARecordByAlias(@Param("alias") String alias);
}
