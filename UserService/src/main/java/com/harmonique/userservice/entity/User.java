package com.harmonique.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name = "users")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    
//    @Column(name = "profile_picture")
//    private String profilePictureUrl;
//
//    @NotBlank(message = "Name is required")
//    private String name;
//
//    @Column(unique = true, nullable = false)
//    @Email(message = "Invalid email format")
//    @NotBlank(message = "Email is required")
//    private String email;
//
//    @Size(min = 6, message = "Password must be at least 6 characters")
//    private String password;
//
//    private String about;
//
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
//    )
//   
//    @Builder.Default
//    private Set<Role> roles = new HashSet<>();
//}

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_picture")
    private String profilePictureUrl;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String about;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "location")
    private String location;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}