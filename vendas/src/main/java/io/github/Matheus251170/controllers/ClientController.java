package io.github.Matheus251170.controllers;

import io.github.Matheus251170.model.Client;
import io.github.Matheus251170.repositories.ClientRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Api("Client API")
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

//    @RequestMapping(value = "/busca/{id}", method = RequestMethod.GET)
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado!"),
            @ApiResponse(code = 404, message = "Cliente não encontrado")
    })
    @GetMapping("{id}")
    public Client getClientById(@PathVariable Integer id){
        Optional<Client> client = clientRepository.findById(id);

        if(client.isPresent()){

            return client.get();

        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
    }

    @ApiOperation("Salvar um novo cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo!"),
            @ApiResponse(code = 404, message = "Erro de validação")
    })
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Client saveClient(@RequestBody @Valid Client client){
        return clientRepository.save(client);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Integer id){
        clientRepository.findById(id)
                .map(clientBd -> {
                    clientRepository.delete(clientBd);
                    return HttpStatus.NO_CONTENT;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateClient(@PathVariable Integer id, @RequestBody @Valid Client client) {
       return clientRepository.findById(id).map(clientBd -> {
            client.setId(clientBd.getId());
            clientRepository.save(client);
            return ResponseEntity.noContent().build();
        }).orElseGet( () -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity find(Client filtro) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        List<Client> clientsList = clientRepository.findAll(example);
        return ResponseEntity.ok(clientsList);
    }
}
