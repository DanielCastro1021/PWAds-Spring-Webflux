package com.example.springangularadsapp.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface IHateoasController<T,S> {

    public CollectionModel<EntityModel<T>> all();

    public EntityModel<? extends T> one(@PathVariable String id);

    public ResponseEntity<?> save(@RequestBody S newEntity);

    public ResponseEntity<?> update(@RequestBody S newEntity, @PathVariable String id);

    public ResponseEntity<?> delete(@PathVariable String id);
}
