package net.kimleo.grabbie.repository;

import net.kimleo.grabbie.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepo extends CrudRepository<Client, Long> {
    public Client findByUrl(String url);
}
