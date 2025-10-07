package com.example.JOOQPROJECT.Controller;
import com.example.JOOQPROJECT.service.UserProfileService;
import com.example.JOOQPROJECT.UserProfileRequest;
import com.example.jooq.generated.tables.pojos.UserProfile;
import com.example.jooq.generated.tables.records.UserProfileRecord;
import com.example.jooq.generated.tables.records.UserProfileViewRecord;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jooq.Record;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user-profiles")
@Tag(name = "User Profile", description = "CRUD operations on user profiles")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }


@PostMapping
@Operation(summary = "Create a new user profile")
public ResponseEntity<UserProfile> create(@RequestBody UserProfileRequest request) {
    UserProfileRecord record = service.createUser(request);  
    UserProfile userProfile = record.into(UserProfile.class); 
    return ResponseEntity.ok(userProfile);
}



@GetMapping("/{id}")
@Operation(summary = "Get user profile by ID")
public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
    Optional<UserProfileRecord> record = service.getUserById(id);
    return record
            .map(r -> ResponseEntity.ok(r.intoMap()))
            .orElse(ResponseEntity.notFound().build());
}


@GetMapping
@Operation(summary = "Get all user profiles")
public ResponseEntity<List<Map<String, Object>>> getAll() {
    return ResponseEntity.ok(
        service.getUniqueEmails().stream()
               .map(Record::intoMap)   
               .toList()
    );
}



@PutMapping("/{id}")
@Operation(summary = "Update an existing user profile")
public ResponseEntity<String> update(@PathVariable Long id, @RequestBody UserProfileRequest request) {
    UserProfileRecord updatedRecord = service.updateUser(id, request);
    return updatedRecord != null ? ResponseEntity.ok("User updated") : ResponseEntity.notFound().build();
}


@DeleteMapping("/{id}")
@Operation(summary = "Delete a user profile by ID")
public ResponseEntity<String> delete(@PathVariable Long id) {
    int deleted = service.deleteUser(id);
    return deleted > 0 ? ResponseEntity.ok("User deleted") : ResponseEntity.notFound().build();
}


@GetMapping("/sorted")
@Operation(summary = "Get all user profiles sorted by username")
public ResponseEntity<List<Map<String, Object>>> getAllSortedUsers() {
    List<UserProfileViewRecord> records = service.getAllSortedUsersAsList();
    
    
    List<Map<String, Object>> result = records.stream()
            .map(UserProfileViewRecord::intoMap)
            .toList();

    return ResponseEntity.ok(result);
}


@GetMapping("/max-age")
@Operation(summary = "Get user profile with the biggest age")
public ResponseEntity<List<Map<String, Object>>> getBiggestAgeUser() {
    List<UserProfileViewRecord> records = service.getBiggestAgeUser();
    
    
    List<Map<String, Object>> result = records.stream()
            .map(UserProfileViewRecord::intoMap)
            .toList();

    return ResponseEntity.ok(result);
}


}