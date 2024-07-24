package com.redhat.demo.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    public ApplicationService(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
    }

    public List<Application> findAll() {
        return this.applicationRepository.findAll().stream()
                .map(applicationMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Application> findById(long applicationId) {
        return this.applicationRepository.findByIdOptional(applicationId)
                .map(applicationMapper::toDomain);
    }

    @Transactional
    public Application create(@Valid Application application) {
        ApplicationEntity entity = this.applicationMapper.toEntity(application);
        this.applicationRepository.persist(entity);
        return this.applicationMapper.toDomain(entity);
    }

    @Transactional
    public Application update(@Valid Application application) {
        ApplicationEntity entity = this.applicationRepository.findById(application.applicationId());
        entity.name = application.name();
        this.applicationRepository.persist(entity);
        return this.applicationMapper.toDomain(entity);
    }

    @Transactional
    public void delete(long applicationId) {
        this.applicationRepository.deleteById(applicationId);
    }

}