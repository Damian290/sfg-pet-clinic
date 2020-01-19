package com.springframework.sfgpetclinic.services;

import com.springframework.sfgpetclinic.model.Owner;

import java.util.Set;

public interface OwnerService  {

    Owner findByLastName(String lasName);

    Owner findById(Long id);

    Owner save(Owner owner);

    Set<Owner> findAll();
}
