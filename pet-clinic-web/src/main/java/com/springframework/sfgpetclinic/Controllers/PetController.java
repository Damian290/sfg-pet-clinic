package com.springframework.sfgpetclinic.Controllers;

import com.springframework.sfgpetclinic.model.Owner;
import com.springframework.sfgpetclinic.model.Pet;
import com.springframework.sfgpetclinic.model.PetType;
import com.springframework.sfgpetclinic.services.OwnerService;
import com.springframework.sfgpetclinic.services.PetService;
import com.springframework.sfgpetclinic.services.PetTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("owners/{ownerId}")
public class PetController {

    private static final String VIEWS_PETS_CREATE_OR_UPDATE = "pets/createOrUpdatePetForm";
    private final PetService petService;
    private final OwnerService ownerService;
    private final PetTypeService petTypeService;

    public PetController(PetService petService, OwnerService ownerService, PetTypeService petTypeService) {
        this.petService = petService;
        this.ownerService = ownerService;
        this.petTypeService = petTypeService;
    }

    @InitBinder("owner")
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable Long ownerId){
        return ownerService.findById(ownerId);
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes(){
        return petTypeService.findAll();
    }

    @GetMapping("/pets/new")
    public String initCreateForm(Owner owner, Model model){
        Pet pet = new Pet();
        owner.getPets().add(pet);
        pet.setOwner(owner);
        model.addAttribute("pet",pet);
        return VIEWS_PETS_CREATE_OR_UPDATE;
    }

    @PostMapping("/pets/new")
    public String processCreateForm(Owner owner, @Valid Pet pet, BindingResult result, Model model){
        if(StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(),true) != null){
            result.rejectValue("name", "duplicate", "alreadyExist");
        }
        owner.getPets().add(pet);
        if(result.hasErrors()){
            model.addAttribute("pet",pet);
            return VIEWS_PETS_CREATE_OR_UPDATE;
        } else{
            petService.save(pet);
            return "redirect:/owners/" + owner.getId();
        }
    }

    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(Model model, @PathVariable Long petId){
        Pet pet = petService.findById(petId);
        model.addAttribute("pet",pet);
        return VIEWS_PETS_CREATE_OR_UPDATE;
    }

    @PostMapping("/pets/{petId}/edit")
    public String processUpdateForm(Owner owner, BindingResult result, @Valid Pet pet, Model model){
        if(result.hasErrors()){
            pet.setOwner(owner);
            model.addAttribute("pet",pet);
            return VIEWS_PETS_CREATE_OR_UPDATE;
        } else{
            owner.getPets().add(pet);
            petService.save(pet);
            return "redirect:/owners/" + owner.getId();
        }

    }
}
