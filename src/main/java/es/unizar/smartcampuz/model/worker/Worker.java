package es.unizar.smartcampuz.model.worker;

import es.unizar.smartcampuz.model.report.Report;
import es.unizar.smartcampuz.model.report.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
