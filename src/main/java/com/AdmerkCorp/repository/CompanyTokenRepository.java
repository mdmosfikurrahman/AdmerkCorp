package com.AdmerkCorp.repository;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.token.CompanyToken;
import com.AdmerkCorp.model.token.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyTokenRepository extends JpaRepository<CompanyToken, Long> {

    @Query(value = """
      select t from CompanyToken t inner join Company c\s
      on t.company.id = c.id\s
      where c.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<CompanyToken> findAllValidTokenByCompany(Long id);

    Optional<CompanyToken> findByToken(String userToken);
}
