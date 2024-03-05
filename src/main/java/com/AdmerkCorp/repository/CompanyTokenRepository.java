package com.AdmerkCorp.repository;

import com.AdmerkCorp.model.token.CompanyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyTokenRepository extends JpaRepository<CompanyToken, Long> {

    @Query(value = """
      select t from CompanyToken t inner join Company c\s
      on t.company.id = c.id\s
      where c.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<CompanyToken> findAllValidTokenByCompany(Long id);

    Optional<CompanyToken> findByToken(String userToken);

}
