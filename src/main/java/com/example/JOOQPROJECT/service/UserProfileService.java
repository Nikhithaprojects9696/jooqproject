package com.example.JOOQPROJECT.service;

import com.example.JOOQPROJECT.UserProfileRequest;
import com.example.jooq.generated.tables.records.UserProfileRecord;
import com.example.jooq.generated.tables.records.UserProfileViewRecord;
import com.example.jooq.generated.tables.UserProfile;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import com.example.jooq.generated.tables.UserProfileView;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;


@Service
public class UserProfileService {

    private final DSLContext dsl;

    public UserProfileService(DSLContext dsl) {
        this.dsl = dsl;
    }


     
    public UserProfileRecord createUser(UserProfileRequest request) {
        return dsl.insertInto(UserProfile.USER_PROFILE)
                .set(UserProfile.USER_PROFILE.USERNAME, request.username())
                .set(UserProfile.USER_PROFILE.EMAIL, request.email())
                .set(UserProfile.USER_PROFILE.DISPLAY_NAME, request.displayName())
                .set(UserProfile.USER_PROFILE.BIO, request.bio())
                .set(UserProfile.USER_PROFILE.AGE, request.age())
                .set(UserProfile.USER_PROFILE.GENDER, request.gender())
                .returning()
                .fetchOne();
    }

    
 public Optional<UserProfileRecord> getUserById(Long id) {
    return Optional.ofNullable(
        dsl.selectFrom(UserProfile.USER_PROFILE)
           .where(UserProfile.USER_PROFILE.ID.eq(id))
           .fetchOne()
    );
}



public List<Map<String, Object>> getAllUsersAge18AsMapList() {
    return dsl.selectFrom(UserProfileView.USER_PROFILE_VIEW)
              .fetch()
              .stream()
              .filter(user -> user.getAge() != null && user.getAge() > 18) 
              .map(UserProfileViewRecord::intoMap)  
              .toList();
}

public List<Map<String, Object>> getAllUsersAsMapList(int page, int size) {
    int offset = page * size;

    return dsl.selectFrom(UserProfileView.USER_PROFILE_VIEW)
              .limit(size)
              .offset(offset)
              .fetch()
              .map(UserProfileViewRecord::intoMap);
}



public List<UserProfileViewRecord> getAllSortedUsersAsList() {
    return dsl.selectFrom(UserProfileView.USER_PROFILE_VIEW)
              .fetch()
              .stream()
              .sorted(Comparator.comparing(UserProfileViewRecord::getUsername)) 
             .toList();
}

public List<UserProfileViewRecord> getAlltop5UsersAsList() {
    return dsl.selectFrom(UserProfileView.USER_PROFILE_VIEW)
              .fetch()
              .stream()
              .limit(5)
              .toList();
}
public List<UserProfileViewRecord> getremoveduplicatesUsersAsList() {
    return dsl.selectFrom(UserProfileView.USER_PROFILE_VIEW)
              .fetch()
              .stream()
              .collect(Collectors.collectingAndThen(
                  Collectors.toCollection(() ->
                      new TreeSet<>(Comparator.comparing(UserProfileViewRecord::getEmail))
                  ),
                  ArrayList::new
              ));
}

public List<UserProfileViewRecord> getBiggestAgeUser() {
    return dsl.selectFrom(UserProfileView.USER_PROFILE_VIEW)
              .fetch()
              .stream()
              .filter(u -> u.getAge() != null)
              .max(Comparator.comparing(UserProfileViewRecord::getAge))
              .map(List::of)         
              .orElseGet(List::of);  
}


public List<UserProfileViewRecord> getUniqueEmails() {
    return dsl.selectFrom(UserProfileView.USER_PROFILE_VIEW)
              .fetch()
              .stream()
              .filter(u -> u.getAge() != null && u.getAge() > 18)  
              .collect(Collectors.collectingAndThen(
                  Collectors.toMap(
                      UserProfileViewRecord::getEmail,  
                      record -> record,                
                      (r1, r2) -> r1                   
                  ),
                  map -> new ArrayList<>(map.values())   
              ));
}



public UserProfileRecord updateUser(Long id, UserProfileRequest request) {
    return dsl.update(UserProfile.USER_PROFILE)
            .set(UserProfile.USER_PROFILE.USERNAME, request.username())
            .set(UserProfile.USER_PROFILE.EMAIL, request.email())
            .set(UserProfile.USER_PROFILE.DISPLAY_NAME, request.displayName())
            .set(UserProfile.USER_PROFILE.BIO, request.bio())
            .set(UserProfile.USER_PROFILE.AGE, request.age())
            .set(UserProfile.USER_PROFILE.GENDER, request.gender())
            .where(UserProfile.USER_PROFILE.ID.eq(id)) 
            .returning() 
            .fetchOne();
}


    
     public int deleteUser(Long id) {
        return dsl.deleteFrom(UserProfile.USER_PROFILE)
                  .where(UserProfile.USER_PROFILE.ID.eq(id))
                  .execute();
    }
}
