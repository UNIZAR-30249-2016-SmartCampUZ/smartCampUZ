package es.unizar.smartcampuz.model.worker;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * This entity represents a Worker previously registered.
 */
@Entity
@Table(name = "worker")
public class Worker {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String email;

    @NotNull
    @Embedded
    private UserData userData;

    // ------------------------
    // CONSTRUCTORS
    // ------------------------

    public Worker(){ }

    public Worker(long id) {
        this.id = id;
    }

    public Worker(@NotNull String email, @NotNull UserData userData) {
        this.email = email;
        this.userData = userData;
    }

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

}
