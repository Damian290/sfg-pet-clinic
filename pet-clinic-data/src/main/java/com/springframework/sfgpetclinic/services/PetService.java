package com.springframework.sfgpetclinic.services;

import com.springframework.sfgpetclinic.model.Pet;

import java.util.Set;

public interface PetService {

    Pet findById(Long id);

    Pet save(Pet owner);

    Set<Pet> findAll();
}
