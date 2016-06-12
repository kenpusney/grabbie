package net.kimleo.grabbie.controller;

import jdk.nashorn.internal.ir.annotations.Reference;
import net.kimleo.grabbie.model.Client;
import net.kimleo.grabbie.model.Execution;
import net.kimleo.grabbie.repository.ClientRepo;
import net.kimleo.grabbie.repository.ExecRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private ExecRepo execRepo;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client existedClient = clientRepo.findByUrl(client.getUrl());
        if (existedClient != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .location(URI.create("/client/" + existedClient.getId()))
                    .body(existedClient);
        }
        client = clientRepo.save(client);
        return ResponseEntity.created(URI.create("/client/" + client.getId()))
                .body(client);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clientRepo.findOne(id));
    }

    @RequestMapping("/{id}/execution")
    public ResponseEntity<List<Execution>> getClientExecution(
            @PathVariable("id") Long id,
            @RequestParam(value = "executed", required = false) Boolean executed) {
        if (executed != null) {
            return ResponseEntity.ok(execRepo.findByClientIdAndExecuted(id, executed));
        }
        return ResponseEntity.ok(execRepo.findByClientId(id));
    }
}
