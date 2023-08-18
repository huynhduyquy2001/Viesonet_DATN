package com.viesonet.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.InteractionDao;
import com.viesonet.entity.Interaction;
import com.viesonet.entity.Users;

@Service
public class InteractionService {
	@Autowired
	InteractionDao interactionDao;

	public void plusInteraction(String interactingPerson, String interactedPerson) {
	    Interaction interaction = interactionDao.findUserInteraction(interactingPerson, interactedPerson);
	    Interaction interac = new Interaction();
	    Date currentDate = new Date();
	    
	    if (interaction == null) {
	        // Tạo đối tượng Users cho người được tương tác (interactedPerson)
	        Users interactedUser = new Users();
	        interactedUser.setUserId(interactedPerson);

	        // Tạo đối tượng Users cho người tương tác (interactingPerson)
	        Users interactingUser = new Users();
	        interactingUser.setUserId(interactingPerson);

	        interac.setInteractedPerson(interactedUser);
	        interac.setInteractingPerson(interactingUser);
	        interac.setInteractionCount(1);
	        interac.setMostRecentInteractionDate(currentDate);
	        
	        //lưu
	        interactionDao.saveAndFlush(interac);
	    } else {
	        interaction.setMostRecentInteractionDate(currentDate);
	        interaction.setInteractionCount(interaction.getInteractionCount() + 1);
	        //lưu
	        interactionDao.saveAndFlush(interaction);
	    }
	}
	
	public void minusInteraction(String interactingPerson, String interactedPerson) {
		Interaction interaction = interactionDao.findUserInteraction(interactingPerson, interactedPerson);
		Date currentDate = new Date();
			interaction.setMostRecentInteractionDate(currentDate);
			interaction.setInteractionCount(interaction.getInteractionCount() - 1);
			//lưu
			interactionDao.saveAndFlush(interaction);
	}
	
	public List<Interaction> findListInteraction(String id){
		return interactionDao.findListInteraction(id);
	}
}
