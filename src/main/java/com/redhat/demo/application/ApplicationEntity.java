package com.redhat.demo.application;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

@Entity(name = "Application")
@Table(name = "application")
public class ApplicationEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    public Long applicationId;

    @Column(name = "name")
    @NotEmpty(message = "{Application.name.required}")
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationEntity that = (ApplicationEntity) o;
        return Objects.equals(applicationId, that.applicationId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, name);
    }

    @Override
    public String toString() {
        return "ApplicationEntity{" +
                "applicationId=" + applicationId +
                ", name='" + name + '\'' +
                '}';
    }

}