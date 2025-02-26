package com.springAI.SupprtTicket;

import io.restassured.path.json.mapper.factory.Jackson1ObjectMapperFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Integer> {
}
