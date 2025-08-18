package com.joinai_support.repository;

import com.joinai_support.domain.TicketAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketAnalysisRepository extends
        MongoRepository<TicketAnalysis, String> {
}
