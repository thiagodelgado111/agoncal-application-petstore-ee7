package org.agoncal.application.petstore.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.agoncal.application.petstore.model.Product;
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
@RequestMapping("/rest/products")
@Loggable
@Tag(name = "Product")
@Transactional
public class ProductEndpoint
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
   @Operation(summary = "Creates new product")
   public ResponseEntity<Product> create(@RequestBody Product entity)
   {
      em.persist(entity);
      return ResponseEntity.created(URI.create("/rest/products/" + entity.getId())).body(entity);
   }

   @DeleteMapping("/{id}")
   @Operation(summary = "Deletes a product by id")
   public ResponseEntity<Void> deleteById(@PathVariable("id") Long id)
   {
      Product entity = em.find(Product.class, id);
      if (entity == null)
      {
         return ResponseEntity.notFound().build();
      }
      em.remove(entity);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("/{id}")
   @Operation(summary = "Finds a product by id")
   public ResponseEntity<Product> findById(@PathVariable("id") Long id)
   {
      TypedQuery<Product> findByIdQuery = em.createQuery("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :entityId ORDER BY p.id", Product.class);
      findByIdQuery.setParameter("entityId", id);
      Product entity;
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
   @Operation(summary = "Lists all products")
   public List<Product> listAll(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer max)
   {
      TypedQuery<Product> findAllQuery = em.createQuery("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.id", Product.class);
      if (start != null)
      {
         findAllQuery.setFirstResult(start);
      }
      if (max != null)
      {
         findAllQuery.setMaxResults(max);
      }
      final List<Product> results = findAllQuery.getResultList();
      return results;
   }

   @PutMapping("/{id}")
   @Operation(summary = "Updates a product")
   public ResponseEntity<Product> update(@PathVariable("id") Long id, @RequestBody Product entity)
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
