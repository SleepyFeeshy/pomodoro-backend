package com.pomodoro.backend.sync.repository;

import com.pomodoro.backend.sync.model.ProcessedMutation;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProcessedMutationRepository extends CrudRepository<ProcessedMutation, UUID> {

    @Modifying
    @Query("""
            INSERT INTO processed_mutations (mutation_id, user_id)
            VALUES (:mutationId, :userId)
            ON CONFLICT DO NOTHING
            """)
    int insertProcessedMutation(UUID mutationId, UUID userId);
}
