package es.unizar.smartcampuz.model.worker;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Value Object that represents User Data
 */
@Embeddable
public class UserData {
    private String name;

    //Used by Hibernate. Private is more readable. Protected will improve performance.
    protected UserData(){

    }

    public UserData(@NotNull String name){
        this.name = name;
    }

    //Used by Hibernate. Private is more readable. Protected will improve performance.
    protected void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
