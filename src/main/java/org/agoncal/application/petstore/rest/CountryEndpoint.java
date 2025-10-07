package org.agoncal.application.petstore.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.agoncal.application.petstore.model.Country;
import org.agoncal.application.petstore.util.Loggable;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@RestController
@RequestMapping("/rest/countries")
@Loggable
@Tag(name = "Country")
@Transactional
public class CountryEndpoint
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @PersistenceContext
   private EntityManager em;

   // ======================================
   // =          Business methods          =
   // ======================================

   @PostMapping
   @Operation(summary = "Creates a country")
   public ResponseEntity<Country> create(@RequestBody Country entity)
   {
      em.persist(entity);
      return ResponseEntity.created(URI.create("/rest/countries/" + entity.getId())).body(entity);
   }

   @DeleteMapping("/{id}")
   @Operation(summary = "Deletes a country given an id")
   public ResponseEntity<Void> deleteById(@PathVariable("id") Long id)
   {
      Country entity = em.find(Country.class, id);
      if (entity == null)
      {
         return ResponseEntity.notFound().build();
      }
      em.remove(entity);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("/{id}")
   @Operation(summary = "Retrieves a country by its id")
   public ResponseEntity<Country> findById(@PathVariable("id") Long id)
   {
      TypedQuery<Country> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Country c WHERE c.id = :entityId ORDER BY c.id", Country.class);
      findByIdQuery.setParameter("entityId", id);
      Country entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(entity);
   }

   @GetMapping
   @Operation(summary = "Lists all the countries")
   public List<Country> listAll(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer max)
   {
      TypedQuery<Country> findAllQuery = em.createQuery("SELECT DISTINCT c FROM Country c ORDER BY c.id", Country.class);
      if (start != null)
      {
         findAllQuery.setFirstResult(start);
      }
      if (max != null)
      {
         findAllQuery.setMaxResults(max);
      }
      final List<Country> results = findAllQuery.getResultList();
      return results;
   }

   @PutMapping("/{id}")
   @Operation(summary = "Updates a country")
   public ResponseEntity<Country> update(@PathVariable("id") Long id, @RequestBody Country entity)
   {
      try
      {
         entity = em.merge(entity);
         return ResponseEntity.ok(entity);
      }
      catch (OptimisticLockException e)
      {
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
   }
}
