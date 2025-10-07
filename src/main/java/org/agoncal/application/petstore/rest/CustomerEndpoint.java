package org.agoncal.application.petstore.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.agoncal.application.petstore.model.Customer;
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
@RequestMapping("/rest/customers")
@Loggable
@Tag(name = "Customer")
@Transactional
public class CustomerEndpoint
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
   @Operation(summary = "Creates a customer")
   public ResponseEntity<Customer> create(@RequestBody Customer entity)
   {
      em.persist(entity);
      return ResponseEntity.created(URI.create("/rest/customers/" + entity.getId())).body(entity);
   }

   @DeleteMapping("/{id}")
   @Operation(summary = "Deletes a customer by id")
   public ResponseEntity<Void> deleteById(@PathVariable("id") Long id)
   {
      Customer entity = em.find(Customer.class, id);
      if (entity == null)
      {
         return ResponseEntity.notFound().build();
      }
      em.remove(entity);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("/{id}")
   @Operation(summary = "Finds a customer by it identifier")
   public ResponseEntity<Customer> findById(@PathVariable("id") Long id)
   {
      TypedQuery<Customer> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.homeAddress.country WHERE c.id = :entityId ORDER BY c.id", Customer.class);
      findByIdQuery.setParameter("entityId", id);
      Customer entity;
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
   @Operation(summary = "Lists all the customers")
   public List<Customer> listAll(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer max)
   {
      TypedQuery<Customer> findAllQuery = em.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.homeAddress.country ORDER BY c.id", Customer.class);
      if (start != null)
      {
         findAllQuery.setFirstResult(start);
      }
      if (max != null)
      {
         findAllQuery.setMaxResults(max);
      }
      final List<Customer> results = findAllQuery.getResultList();
      return results;
   }

   @PutMapping("/{id}")
   @Operation(summary = "Updates a customer")
   public ResponseEntity<Customer> update(@PathVariable("id") Long id, @RequestBody Customer entity)
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
