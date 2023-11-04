package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.viesonet.entity.Interaction;

public interface InteractionDao extends JpaRepository<Interaction, Integer> {
	@Query("SELECT i FROM Interaction i WHERE i.interactingPerson.userId =:id and i.interactedPerson.userId =:id2")
	Interaction findUserInteraction(String id, String id2);

	@Query("SELECT i FROM Interaction i WHERE i.interactedPerson.userId =:id")
	List<Interaction> findListInteraction(String id);
}
