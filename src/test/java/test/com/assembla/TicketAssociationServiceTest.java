package test.com.assembla;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.assembla.Ticket;
import com.assembla.TicketAssociation;
import com.assembla.TicketAssociation.TicketRelationship;
import com.assembla.client.AssemblaRequest;
import com.assembla.client.AssemblaResponse;
import com.assembla.service.TicketAssociationResource;
import com.assembla.service.TicketAssociationService;

@RunWith(MockitoJUnitRunner.class)
public class TicketAssociationServiceTest extends ServiceTest {

	private TicketAssociationResource ticketAssociationService;

	@Before
	public void setup() {
		ticketAssociationService = new TicketAssociationService(super.assemblaClient, TEST_SPACE_ID);
	}

	@Test
	public void ticketAssociationTest() {
		when(assemblaClient.get(any(AssemblaRequest.class))).thenReturn(
				new AssemblaResponse(new TicketAssociation[10], TicketAssociation[].class));
		AssemblaRequest request = new AssemblaRequest("/spaces/test_space_id/tickets/100/ticket_associations.json",
				TicketAssociation[].class);

		Ticket ticket = new Ticket();
		ticket.setNumber(100);

		List<TicketAssociation> associations = ticketAssociationService.get(ticket);

		verify(assemblaClient).get(request);
		assertFalse("List of associations should not be empty", associations.isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void ticketAssociationByIdBadArgsTest() {
		ticketAssociationService.get(null);
	}

	@Test
	public void getTicketAssociationByIdTest() {
		when(assemblaClient.get(any(AssemblaRequest.class))).thenReturn(
				new AssemblaResponse(new TicketAssociation(), TicketAssociation.class));
		AssemblaRequest request = new AssemblaRequest("/spaces/test_space_id/tickets/100/ticket_associations/999.json",
				TicketAssociation.class);

		Ticket ticket = new Ticket();
		ticket.setNumber(100);
		TicketAssociation association = ticketAssociationService.getById(ticket, 999);

		verify(assemblaClient).get(request);
		assertNotNull(association);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getTIcketAssociationByIdBadArgTest() {
		ticketAssociationService.getById(null, 100);
	}

	@Test
	public void createAssociationTest() {
		when(assemblaClient.post(any(AssemblaRequest.class))).thenReturn(
				new AssemblaResponse(new TicketAssociation(), TicketAssociation.class));
		AssemblaRequest request = new AssemblaRequest("/spaces/test_space_id/tickets/200/ticket_associations.json",
				TicketAssociation.class);

		Ticket ticket = new Ticket();
		ticket.setNumber(200);

		TicketAssociation ta1 = new TicketAssociation();
		ta1.setTicket2Id(100);
		request.withBody(ta1);

		TicketAssociation newAssociation = ticketAssociationService.create(ticket, ta1);

		assertNotNull(newAssociation);
		verify(assemblaClient).post(request);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createAssociationBadArgs() {
		ticketAssociationService.create(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createAssociationNoIdTest() {
		ticketAssociationService.create(new Ticket(), new TicketAssociation());
	}

	@Test
	public void updateAssociationTest() {
		when(assemblaClient.put(any(AssemblaRequest.class))).thenReturn(new AssemblaResponse());
		AssemblaRequest request = new AssemblaRequest("/spaces/test_space_id/tickets/200/ticket_associations/123456.json");

		Ticket ticket = new Ticket();
		ticket.setNumber(200);

		TicketAssociation association = new TicketAssociation();
		association.setId(123456);
		association.setTicket2Id(100);
		association.setRelationship(TicketRelationship.CHILD);
		request.withBody(association);

		ticketAssociationService.update(ticket, association);

		verify(assemblaClient).put(request);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAssociationArgsTest() {
		ticketAssociationService.update(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAssociationCheckTicketTest() {
		Ticket ticket = new Ticket(); // no number

		TicketAssociation association = new TicketAssociation();
		association.setTicket2Id(100);
		association.setRelationship(TicketRelationship.CHILD);

		ticketAssociationService.update(ticket, association);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAssociationCheckAssociationTest() {
		Ticket ticket = new Ticket();
		ticket.setNumber(100);

		// No ticket 2 id
		TicketAssociation association = new TicketAssociation();
		association.setRelationship(TicketRelationship.BLOCK);

		ticketAssociationService.update(ticket, association);
	}

	@Test
	public void deleteTicketAssociationTest() throws Exception {
		when(assemblaClient.delete(any(AssemblaRequest.class))).thenReturn(new AssemblaResponse());
		AssemblaRequest request = new AssemblaRequest("/spaces/test_space_id/tickets/200/ticket_associations/100.json");

		Ticket ticket = new Ticket();
		ticket.setNumber(200);

		TicketAssociation association = new TicketAssociation();
		association.setId(100);

		ticketAssociationService.delete(ticket, association);
		verify(assemblaClient).delete(request);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteAssociationBadArgTest() {
		ticketAssociationService.delete(null, null);
	}

}
