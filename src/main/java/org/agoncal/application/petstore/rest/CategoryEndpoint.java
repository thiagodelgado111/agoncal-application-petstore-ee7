package org.agoncal.application.petstore.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.agoncal.application.petstore.model.Category;
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
@RequestMapping("/rest/categories")
@Loggable
@Tag(name = "Category")
@Transactional
public class CategoryEndpoint
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
   @Operation(summary = "Creates a category")
   public ResponseEntity<Category> create(@RequestBody Category entity)
   {
      em.persist(entity);
      return ResponseEntity.created(URI.create("/rest/categories/" + entity.getId())).body(entity);
   }

   @DeleteMapping("/{id}")
   @Operation(summary = "Deletes a category by id")
   public ResponseEntity<Void> deleteById(@PathVariable("id") Long id)
   {
      Category entity = em.find(Category.class, id);
      if (entity == null)
      {
         return ResponseEntity.notFound().build();
      }
      em.remove(entity);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("/{id}")
   @Operation(summary = "Finds a category given an identifier")
   public ResponseEntity<Category> findById(@PathVariable("id") Long id)
   {
      TypedQuery<Category> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Category c WHERE c.id = :entityId ORDER BY c.id", Category.class);
      findByIdQuery.setParameter("entityId", id);
      Category entity;
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
   @Operation(summary = "Lists all the categories")
   public List<Category> listAll(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer max)
   {
      TypedQuery<Category> findAllQuery = em.createQuery("SELECT DISTINCT c FROM Category c ORDER BY c.id", Category.class);
      if (start != null)
      {
         findAllQuery.setFirstResult(start);
      }
      if (max != null)
      {
         findAllQuery.setMaxResults(max);
      }
      final List<Category> results = findAllQuery.getResultList();
      return results;
   }

   @PutMapping("/{id}")
   @Operation(summary = "Updates a category")
   public ResponseEntity<Category> update(@PathVariable("id") Long id, @RequestBody Category entity)
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
