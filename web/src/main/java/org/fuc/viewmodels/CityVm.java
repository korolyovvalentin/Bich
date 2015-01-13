package org.fuc.viewmodels;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class CityVm {

    private Long id;
    @NotBlank(message = "Name should not be blank.")
    @Length(min = 3, max = 20, message = "Length should be between 3 and 20")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
