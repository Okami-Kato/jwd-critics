package com.epam.jwd_critics.entity;

import java.util.Objects;

public class User extends AbstractBaseEntity {
    private static final String DEFAULT_PROFILE_IMAGE = "user-icons/default_profile.jpg";

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private Status status;

    @Column(name = "role")
    private Role role;

    @Column(name = "image_path")
    private String imagePath;

    private User() {

    }

    public static UserBuilder newBuilder() {
        return new User().new UserBuilder();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, login, password, status, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return firstName.equals(user.firstName) && lastName.equals(user.lastName) && email.equals(user.email) && login.equals(user.login) && password.equals(user.password) && status == user.status && role == user.role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", role=" + role +
                '}';
    }

    public class UserBuilder {


        private UserBuilder() {
            // private constructor
        }

        public UserBuilder setId(int id) {
            User.this.id = id;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            User.this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            User.this.lastName = lastName;
            return this;
        }

        public UserBuilder setLogin(String login) {
            User.this.login = login;
            return this;
        }

        public UserBuilder setPassword(String password) {
            User.this.password = password;
            return this;
        }

        public UserBuilder setEmail(String email) {
            User.this.email = email;
            return this;
        }

        public UserBuilder setRole(Role role) {
            User.this.role = role;
            return this;
        }

        public UserBuilder setStatus(Status status) {
            User.this.status = status;
            return this;
        }

        public UserBuilder setImagePath(String imagePath) {
            User.this.imagePath = imagePath;
            return this;
        }

        public User build() {
            User user = new User();
            user.id = User.this.id;
            user.firstName = User.this.firstName;
            user.lastName = User.this.lastName;
            user.email = User.this.email;
            user.login = User.this.login;
            user.password = User.this.password;
            user.status = User.this.status;
            user.role = User.this.role;
            user.imagePath = (User.this.imagePath == null || User.this.imagePath.equals("")) ? (DEFAULT_PROFILE_IMAGE) : (User.this.imagePath);
            return user;
        }
    }
}
