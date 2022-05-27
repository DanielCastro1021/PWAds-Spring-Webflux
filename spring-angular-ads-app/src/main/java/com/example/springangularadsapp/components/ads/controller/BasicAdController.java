package com.example.springangularadsapp.components.ads.controller;

import com.example.springangularadsapp.components.ads.assembler.AdModelAssembler;
import com.example.springangularadsapp.components.ads.repository.AdRepository;
import com.example.springangularadsapp.components.ads.model.basic_ad.BasicAd;
import com.example.springangularadsapp.components.ads.model.basic_ad.BasicAdDto;
import com.example.springangularadsapp.components.ads.mapper.AdMapper;
import com.example.springangularadsapp.security.authorization.annotation.UserAccess;
import com.example.springangularadsapp.exceptions.AdNotFoundException;
import com.example.springangularadsapp.exceptions.UserNotFoundException;
import com.example.springangularadsapp.security.models.User;
import com.example.springangularadsapp.security.repository.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/ads/basic")
public class BasicAdController extends AdHateoasController<BasicAd, BasicAdDto> {
    private final AdMapper mapper;

    public BasicAdController(AdRepository<BasicAd> repository, UserRepository userRepository, AdModelAssembler<BasicAd> assembler, AdMapper mapper) {
        super(repository, assembler, userRepository);
        this.mapper = mapper;
    }

    /**
     * Returns all BasicAds in repository, with HATEOAS links.
     *
     * @return CollectionModel of the BasicAd EntityModel.
     */
    @GetMapping("/")
    public CollectionModel<EntityModel<BasicAd>> all() {
        List<EntityModel<BasicAd>> ads = super.getRepository().findAll().stream().map(super.getAssembler()::toModel).collect(Collectors.toList());
        return CollectionModel.of(ads, linkTo(methodOn(BasicAdController.class).all()).withSelfRel());
    }

    /**
     * Returns a specific BasicAds in repository, with HATEOAS links.
     *
     * @param id of an existing BasicAd
     * @return BasicAd EntityModel.
     */
    @GetMapping("/{id}")
    public EntityModel<BasicAd> one(@PathVariable String id) {
        BasicAd basicAd = super.getRepository().findById(id).orElseThrow(() -> new AdNotFoundException(id));
        return super.getAssembler().toModel(basicAd);
    }

    /**
     * Saves a BasicAd using as argument a BasicAdDto.
     *
     * @param newEntity a BasicAdDto instance.
     * @return EntityModel of BasicAd with HATEOAS links.
     * @throws UserNotFoundException If username of the user that made the POST request is not found.
     */
    @UserAccess
    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody BasicAdDto newEntity) throws UserNotFoundException {
        EntityModel<BasicAd> entityModel = super.getAssembler().toModel(super.getRepository().save(this.mapper.basicAdDtoToBasicAd(newEntity)));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }


    /**
     * Updates a BasicAd with specific id with values of a BasicAdDto instance.
     *
     * @param newEntity BasicAdDto instance.
     * @param id        of an existing BasicAd
     * @return EntityModel of BasicAd with HATEOAS links.
     * @throws UserNotFoundException If username of the user that made the POST request is not found.
     */
    @UserAccess
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody BasicAdDto newEntity, @PathVariable String id) throws UserNotFoundException {
        BasicAd newAd = this.mapper.basicAdDtoToBasicAd(newEntity);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        if (!newAd.checkOwner(userDetails.getUsername()) || !request.isUserInRole("ROLE_ADMIN"))
            throw new AdNotFoundException(userDetails.getUsername(), "doesn't own this ad:" + newAd);
        else {
            BasicAd updatedAd = super.getRepository().findById(id).map(basicAd -> {
                basicAd.setTitle(newAd.getTitle());
                basicAd.setDescription(newAd.getDescription());
                basicAd.setOwner(newAd.getOwner());
                basicAd.setImageList(newAd.getImageList());
                return super.getRepository().save(basicAd);
            }).orElseGet(() -> {
                newAd.setId(id);
                return super.getRepository().save(newAd);
            });

            EntityModel<BasicAd> entityModel = super.getAssembler().toModel(updatedAd);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        }
    }


    /* *
     * Deletes BasicAd if the user owns the ad or possesses the Admin access.
     *
     * @param id of an existing
     * @return
     */
    @UserAccess
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        BasicAd ad = super.getRepository().findById(id).orElseThrow(() -> new AdNotFoundException(id));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        if (ad.checkOwner(userDetails.getUsername()) || request.isUserInRole("ROLE_ADMIN")) {
            super.getRepository().deleteById(ad.getId());
            return ResponseEntity.noContent().build();
        } else throw new AdNotFoundException(userDetails.getUsername(), "doesn't own this ad:" + ad);
    }


    /**
     * Return all BasicAds that the user, that made the GET request, owns.
     *
     * @return CollectionModel of the  BasicAd EntityModel.
     */
    @UserAccess
    @GetMapping("/personal")
    public CollectionModel<EntityModel<BasicAd>> getMyBasicAds() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = super.getUserRepository().findByUsername(userDetails.getUsername());

        List<EntityModel<BasicAd>> ads = super.getRepository().findByOwner(user.get()).stream().map(super.getAssembler()::toModel).collect(Collectors.toList());
        return CollectionModel.of(ads, linkTo(methodOn(BasicAdController.class).all()).withSelfRel());
    }
}

