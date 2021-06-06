package com.laurentiuspilca.liveproject.controllers;

import com.laurentiuspilca.liveproject.entities.HealthProfile;
import com.laurentiuspilca.liveproject.services.HealthProfileService;
// TODO import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
public class HealthProfileController {

  private final HealthProfileService healthProfileService;

  public HealthProfileController(HealthProfileService healthProfileService) {
    this.healthProfileService = healthProfileService;
  }

  @PostMapping
  @PreAuthorize("#healthProfile.username == authentication.getName()")
  public void addHealthProfile(@RequestBody HealthProfile healthProfile) {
    healthProfileService.addHealthProfile(healthProfile);
  }

  @GetMapping("/{username}")
  @PreAuthorize("hasAuthority('admin') || " +
          "(!hasAuthority('admin') && (#username == authentication.getName()))")
  public HealthProfile findHealthProfile(@PathVariable String username) {
    return healthProfileService.findHealthProfile(username);
  }

  @DeleteMapping("/{username}")
  @PreAuthorize("hasAuthority('admin')")
  public void deleteHealthProfile(@PathVariable String username ) {
    healthProfileService.deleteHealthProfile(username);
  }
}
