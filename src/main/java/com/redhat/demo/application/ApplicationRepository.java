package com.redhat.demo.application;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApplicationRepository implements PanacheRepositoryBase<ApplicationEntity, Long> {
}