// package com.harmonique.userservice.entity;

// import java.util.List;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import com.harmonique.userservice.repository.RoleRepository;

// /**
//  * Component that seeds the database with default roles when the application starts.
//  * Implements CommandLineRunner so it runs after the Spring context is initialized.
//  */
// @Component
// public class RoleSeeder implements CommandLineRunner {

//     private static final Logger logger = LoggerFactory.getLogger(RoleSeeder.class); // Logger instance

//     private final RoleRepository roleRepository;

//     // Constructor injection for RoleRepository
//     public RoleSeeder(RoleRepository roleRepository) {
//         this.roleRepository = roleRepository;
//     }

//     /**
//      * This method is executed on application startup. 
//      * It checks if the role table is empty, and if so, seeds it with default roles.
//      *
//      * @param args application arguments
//      */
//     @Override
//     public void run(String... args) {
//         logger.info("Checking if roles need to be seeded...");

//         long count = roleRepository.count();

//         if (count == 0) {
//             logger.info("No roles found. Seeding default roles: ROLE_USER, ROLE_ARTIST, ROLE_ADMIN");
            
//             roleRepository.saveAll(List.of(
//                 new Role(1L, "ROLE_USER"),
//                 new Role(2L, "ROLE_ARTIST"),
//                 new Role(3L, "ROLE_ADMIN")
//             ));

//             logger.info("Roles seeded successfully.");
//         } else {
//             logger.info("Roles already exist. Skipping seeding.");
//         }
//     }
// }

