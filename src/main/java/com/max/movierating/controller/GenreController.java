package com.max.movierating.controller;

import com.max.movierating.entity.Genre;
import com.max.movierating.service.impl.GenreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/api/v1/genres/")
public class GenreController {

    private final GenreServiceImpl genreService;

    @Autowired
    public GenreController(GenreServiceImpl genreService) {
        this.genreService = genreService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Genre>> findAll() {
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Genre> findById(@PathVariable Long id) {
        return new ResponseEntity<>(genreService.findById(id), HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Genre> create(@Valid @RequestBody Genre genre) {
        return new ResponseEntity<>(genreService.save(genre), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Genre> update(@PathVariable Long id, @Valid @RequestBody Genre genre) {
        return new ResponseEntity<>(genreService.update(genre, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Genre> delete(@PathVariable Long id) {
        return new ResponseEntity<>(genreService.deleteById(id), HttpStatus.OK);
    }

}