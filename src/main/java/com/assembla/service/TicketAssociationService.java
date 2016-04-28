package com.assembla.service;

import static java.lang.String.format;
import static com.assembla.utils.ObjectUtils.notNull;
import java.util.List;

import com.assembla.Ticket;
import com.assembla.TicketAssociation;
import com.assembla.client.AssemblaClient;
import com.assembla.client.AssemblaConstants;
import com.assembla.client.AssemblaRequest;

public class TicketAssociationService extends AbstractBaseService {

	public TicketAssociationService(AssemblaClient assemblaClient, String spaceId) {
		super(assemblaClient, spaceId);
	}

	public List<TicketAssociation> getAssociations(Ticket ticket) {
		notNull(ticket, "ticket == null");
		String uri = format(AssemblaConstants.TICKET_ASSOCIATIONS, this.getSpaceId(), ticket.getNumber());
		AssemblaRequest request = new AssemblaRequest(uri, TicketAssociation[].class);
		return super.getList(request);
	}

	public TicketAssociation getAssociation(Ticket ticket, int id) {
		notNull(ticket, "ticket == null");
		String uri = format(AssemblaConstants.TICKET_ASSOCIATIONS_BY_ID, this.getSpaceId(), ticket.getNumber(), id);
		AssemblaRequest request = new AssemblaRequest(uri, TicketAssociation.class);
		return super.get(request, format("No association with id %d for ticket %d", ticket.getNumber(), id));
	}

	public TicketAssociation create(Ticket ticket, TicketAssociation association) {
		notNull(ticket, "ticket == null");
		notNull(association, "association == null");
		notNull(association.getTicket2Id(), "Association must have a ticket2 id");

		String uri = format(AssemblaConstants.TICKET_ASSOCIATIONS, super.getSpaceId(), ticket.getNumber(),
				association.getTicket2Id());
		AssemblaRequest request = new AssemblaRequest(uri, TicketAssociation.class);
		request.withBody(association);

		return super.post(request);
	}

	public void update(Ticket ticket, TicketAssociation association) {
		notNull(ticket, "ticket == null");
		notNull(association, "association == null");
		notNull(association.getId(), "Association must have an id");
		notNull(ticket.getNumber(), "ticket must have a number");

		String uri = format(AssemblaConstants.TICKET_ASSOCIATIONS_BY_ID, super.getSpaceId(), ticket.getNumber(),
				association.getId());
		AssemblaRequest request = new AssemblaRequest(uri);
		request.withBody(association);

		client.doPut(request);
	}

	public void delete(Ticket ticket, TicketAssociation association) {
		notNull(ticket, "ticket == null");
		notNull(association, "association == null");
		notNull(association.getId(), "Association must have an id");
		notNull(ticket.getNumber(), "ticket must have a number");
		
		String uri = format(AssemblaConstants.TICKET_ASSOCIATIONS_BY_ID, super.getSpaceId(), ticket.getNumber(),
				association.getId());
		AssemblaRequest request = new AssemblaRequest(uri);
		super.client.doDelete(request);
	}

}
