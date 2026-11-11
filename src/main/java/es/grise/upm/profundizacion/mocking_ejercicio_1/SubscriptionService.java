package es.grise.upm.profundizacion.mocking_ejercicio_1;

import java.util.ArrayList;
import java.util.Collection;

public class SubscriptionService {
	
	// El comportamiento se prueba mediante la API pública del servicio.
	private final Collection<Client> subscribers = new ArrayList<>();
	
	public void addSubscriber(Client client) throws NullClientException, ExistingClientException {
		if(client == null)
			throw new NullClientException();
		if(subscribers.contains(client))
			throw new ExistingClientException();
		subscribers.add(client);
	}
	
	public void removeSubscriber(Client client) throws NullClientException, NonExistingClientException {
		if(client == null)
			throw new NullClientException();
		if(!subscribers.contains(client))
			throw new NonExistingClientException();
		subscribers.remove(client);
	}
	
	public void sendMessage(Message message) {
		for(Client client : subscribers) {
			if(client.hasEmail())
				client.receiveMessage(message);
		}
	}

}
