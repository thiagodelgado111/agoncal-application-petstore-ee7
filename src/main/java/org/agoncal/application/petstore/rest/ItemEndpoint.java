package org.agoncal.application.petstore.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.agoncal.application.petstore.model.Item;
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
@RequestMapping("/rest/items")
@Loggable
@Tag(name = "Item")
@Transactional
public class ItemEndpoint
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
   @Operation(summary = "Creates a new item")
   public ResponseEntity<Item> create(@RequestBody Item entity)
   {
      em.persist(entity);
      return ResponseEntity.created(URI.create("/rest/items/" + entity.getId())).body(entity);
   }

   @DeleteMapping("/{id}")
   @Operation(summary = "Deletes an item by its id")
   public ResponseEntity<Void> deleteById(@PathVariable("id") Long id)
   {
      Item entity = em.find(Item.class, id);
      if (entity == null)
      {
         return ResponseEntity.notFound().build();
      }
      em.remove(entity);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("/{id}")
   @Operation(summary = "Finds an item by its id")
   public ResponseEntity<Item> findById(@PathVariable("id") Long id)
   {
      TypedQuery<Item> findByIdQuery = em.createQuery("SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.product WHERE i.id = :entityId ORDER BY i.id", Item.class);
      findByIdQuery.setParameter("entityId", id);
      Item entity;
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
   @Operation(summary = "Lists all items")
   public List<Item> listAll(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer max)
   {
      TypedQuery<Item> findAllQuery = em.createQuery("SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.product ORDER BY i.id", Item.class);
      if (start != null)
      {
         findAllQuery.setFirstResult(start);
      }
      if (max != null)
      {
         findAllQuery.setMaxResults(max);
      }
      final List<Item> results = findAllQuery.getResultList();
      return results;
   }

   @PutMapping("/{id}")
   @Operation(summary = "Updates an item")
   public ResponseEntity<Item> update(@PathVariable("id") Long id, @RequestBody Item entity)
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
