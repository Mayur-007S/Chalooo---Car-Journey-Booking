package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.APILogs;

public interface APILogsRepository extends JpaRepository<APILogs, Long> {

}
