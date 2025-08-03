package com.nearli.backend.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nearli.backend.dto.FavouriteRequest;
import com.nearli.backend.model.Favourite;
import com.nearli.backend.service.FavouriteService;

@RestController
@RequestMapping("/api/favourite")
public class FavouriteController {

    @Autowired
    private FavouriteService service;

  @PostMapping("/save")
public Favourite save(@RequestBody FavouriteRequest request, Principal principal) {
    Favourite favourite = new Favourite(
        principal.getName(),          // email
        request.getName(),            // placeName
        request.getType()             // type
    );
    return service.saveFavourite(favourite);
}

    /**
     * Get all favourites of the logged-in user
     */
   @GetMapping("/get")
public List<Favourite> get(@RequestParam(required = false) String type, Principal principal) {
    if (type != null) {
        return service.getFavouritesByType(principal.getName(), type);
    }
    return service.getFavourites(principal.getName());
}

    /**
     * Delete a favourite by ID
     */
   @DeleteMapping("/delete")
public ResponseEntity<String> deleteFavourite(@RequestParam Long id, Principal principal) {
    boolean deleted = service.deleteFavouriteById(id, principal.getName());
    if (deleted) {
        return ResponseEntity.ok("Favourite deleted successfully");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favourite not found or unauthorized");
    }
}
}