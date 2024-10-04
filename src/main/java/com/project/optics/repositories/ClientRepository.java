package com.project.optics.repositories;

import com.project.optics.models.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    @Query("SELECT c FROM Client c WHERE LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))")
    Page<Client> searchByPhoneNumber(@Param("phoneNumber") String phoneNumber, Pageable pageable);

    @Query("SELECT c FROM Client c WHERE c.id = :id")
    Page<Client> searchById(@Param("id") Long id, Pageable pageable);

    @Query("SELECT c FROM Client c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Client> searchByFirstNameOrLastName(@Param("name") String name, Pageable pageable);

    @Query("SELECT c FROM Client c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) " +
            "AND LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    Page<Client> searchByFirstAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName, Pageable pageable);

    @Query("SELECT c FROM Client c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) " +
            "AND LOWER(c.secondName) LIKE LOWER(CONCAT('%', :secondName, '%')) " +
            "AND LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    Page<Client> searchByFirstSecondAndLastName(@Param("firstName") String firstName, @Param("secondName") String secondName, @Param("lastName") String lastName, Pageable pageable);

    @Query("SELECT c FROM Client c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) " +
            "AND LOWER(c.secondName) LIKE LOWER(CONCAT('%', :secondName, '%')) " +
            "AND LOWER(c.thirdName) LIKE LOWER(CONCAT('%', :thirdName, '%')) " +
            "AND LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    Page<Client> searchByFullName(@Param("firstName") String firstName, @Param("secondName") String secondName, @Param("thirdName") String thirdName, @Param("lastName") String lastName, Pageable pageable);

    @Query("SELECT c FROM Client c WHERE " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.secondName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.thirdName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "c.phoneNumber LIKE CONCAT('%', :query, '%') OR " +
            "CAST(c.id AS string) LIKE CONCAT('%', :query, '%')")
    Page<Client> defaultSearch(@Param("query") String query, Pageable pageable);

    @Query("SELECT c FROM Client c WHERE " +
            "(:firstName IS NULL OR LOWER(c.firstName) = LOWER(:firstName)) AND " +
            "(:secondName IS NULL OR LOWER(c.secondName) = LOWER(:secondName)) AND " +
            "(:thirdName IS NULL OR LOWER(c.thirdName) = LOWER(:thirdName)) AND " +
            "(:lastName IS NULL OR LOWER(c.lastName) = LOWER(:lastName))")
    List<Client> findSimilarClients(@Param("firstName") String firstName,
                                    @Param("secondName") String secondName,
                                    @Param("thirdName") String thirdName,
                                    @Param("lastName") String lastName);
}
