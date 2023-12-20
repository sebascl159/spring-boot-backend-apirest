package com.bolsadeideas.springboot.backend.apirest.controllers;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Client;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Date;

@CrossOrigin(origins = {"http://localhost:4200"})
//ANOTACION PARA PERMITIR PETICIONES CRUZADAS DE OTRAS APLICACIONES, ORIGINS: URL
@RestController //DEFINIMOS QUE ES UN REST
@RequestMapping("/api") //PARA INDICAR LA URL
public class ClientRestController {

    @Autowired
    private IClientService clientService;

    private final Logger log = LoggerFactory.getLogger(ClientRestController.class);

    @GetMapping("/clients")
    public List<Client> index() {
        return clientService.findAll();
    }

    @GetMapping("/clients/page/{page}")
    public Page<Client> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return clientService.findAll(pageable);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) { //PathVariable: PORQUE VIENE EN LA URL || ResponseEntity: Para indicar que devolveremos una entidad || <?>: Tipo de dato generic

        Optional<Client> client = null;
        Map<String, Object> response = new HashMap<>();

        //VALIDAMOS EN CASO DE UN ERROR DE BD
        try {
            client = clientService.findById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la BD");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //DEVOLVEMOS UN MENSAJE QUE NO EXISTE EL CLIENTE
        if (client.isEmpty()) {
            response.put("mensaje", "El cliente ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Optional<Client>>(client, HttpStatus.OK);
    }

    @PostMapping("/clients")
    public ResponseEntity<?> save(@Valid @RequestBody Client client, BindingResult result) { //RequestBody: PORQUE VIENE EN EL BODY DEL REQUEST || @Valid y BindingResult result: DEBE DECLARARSE PARA QUE COJA LAS VALIDACIONES DEL ENTITY

        Client clientNew = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            //METODO PARA JAVA 8
            /*List<String> errors = new ArrayList<>();
            for (FieldError err : result.getFieldErrors()) {
                errors.add("El campo '" + err.getField() + "'" + err.getDefaultMessage());
            }*/
            List<String> errors = result.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "'" + err.getDefaultMessage()).collect(Collectors.toList());

            response.put("mensaje", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            clientNew = clientService.save(client);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al guardar en la BD");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //client.setCreateAt(new Date()); LO QUITAMOS PORQUE LO INDICAMOS EN EL BEAN PERPERSIST

        response.put("Message", "El cliente ha sido creado con exito");
        response.put("Client", clientNew);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/clients/{id}")
    //@ResponseStatus(HttpStatus.CREATED) // PARA INDICAR EL TIPO DE RESPUESTA DEL STATUS, POR DEFECTO EL STATUS ES OK.
    public ResponseEntity<?> update(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id) {

        Optional<Client> clientAct = clientService.findById(id);
        Client clientNew = new Client();
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            //METODO PARA JAVA 8
            /*List<String> errors = new ArrayList<>();
            for (FieldError err : result.getFieldErrors()) {
                errors.add("El campo '" + err.getField() + "'" + err.getDefaultMessage());
            }*/
            List<String> errors = result.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "'" + err.getDefaultMessage()).collect(Collectors.toList());

            response.put("mensaje", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (clientAct.isEmpty()) {
            response.put("mensaje", "Error: no se pudo editar, el cliente ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {

            clientNew = clientAct.get();
            clientNew.setFirstname(client.getFirstname());
            clientNew.setLastname(client.getLastname());
            clientNew.setEmail(client.getEmail());
            clientService.save(clientNew);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la BD");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("Message", "El cliente ha sido actualizado con exito");
        response.put("Client", clientNew);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @DeleteMapping("/clients/{id}")
    //@ResponseStatus(HttpStatus.OK) // PARA INDICAR EL TIPO DE RESPUESTA DEL STATUS, NOT CONTENT PORQUE NO RETORNA NADA
    public ResponseEntity<?> delete(@PathVariable Long id) { //PathVariable: PORQUE VIENE EN LA URL
        Map<String, Object> response = new HashMap<>();
        //Optional<Client> clientDeleted = clientService.delete(id);

        Optional<Client> client = clientService.findById(id);
        Client clientEntity = new Client();

        try {

            /* RECUPERAMOS EL NOMBRE DE LA FOTO PARA ELIMINARLA */

            String lastNameFile = clientEntity.getFoto();
            if(lastNameFile != null && !lastNameFile.isEmpty()){
                Path lastPathFile = Paths.get("upload").resolve(lastNameFile).toAbsolutePath();
                File lastFile = lastPathFile.toFile();
                if(lastFile.exists() && lastFile.canRead()){
                    lastFile.delete();
                }
            }

            clientService.delete(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar el registro de la BD");
            response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("Message", "El cliente ha sido eliminado con exito");
        //response.put("Client Id:", id);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }


    @PostMapping("/clients/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {

        Map<String, Object> response = new HashMap<>();

        Optional<Client> client = clientService.findById(id);
        Client clientEntity = new Client();

        if (!archivo.isEmpty()) {
            String nameFile = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
            Path routeFile = Paths.get("upload").resolve(nameFile).toAbsolutePath();
            log.info(routeFile.toString());

            try {
                Files.copy(archivo.getInputStream(), routeFile);
            } catch (IOException e) {
                response.put("mensaje", "Error al subir la imagenD");
                response.put("error", e.getMessage().concat(":").concat(String.valueOf(e.getCause())));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (!client.isEmpty()) {
                try {
                    clientEntity = client.get();

                    /* RECUPERAMOS EL NOMBRE DE LA ANTIGUA FOTO PARA ELIMINARAL Y SOLO DEJAR LA ULTIMA */

                    String lastNameFile = clientEntity.getFoto();
                    if(lastNameFile != null && !lastNameFile.isEmpty()){
                        Path lastPathFile = Paths.get("upload").resolve(lastNameFile).toAbsolutePath();
                        File lastFile = lastPathFile.toFile();
                        if(lastFile.exists() && lastFile.canRead()){
                            lastFile.delete();
                        }
                    }

                    /* GUARDAMOS LA NUEVA INFORMACION DE LA FOTO */

                    clientEntity.setFoto(nameFile);
                    clientEntity.setCreateAt(new Date());
                    clientService.save(clientEntity);

                } catch (DataAccessException e) {
                    response.put("clientNew", clientEntity);
                    response.put("mensaje", "Error al realizar la consulta en la BD");
                    response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
                    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                response.put("Cliente", clientEntity);
                response.put("Mensaje", "Has subido correctamente correctamente la imagen " + nameFile);
            }
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/uploads/img/{nameFile:.+}") //{nameFile:.+} EXPRESION REGULAR QUE CONTENDRA UN PUNTO
    public ResponseEntity<Resource> viewFile(@PathVariable String nameFile){

        Path routeFile = Paths.get("upload").resolve(nameFile).toAbsolutePath();
        Resource recurso = null;
        try {
            recurso = new UrlResource(routeFile.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        if(!recurso.exists() && !recurso.isReadable()){
            throw new RuntimeException("No se pudo cargar la imagen; " + nameFile);
        }

        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");


        return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);

    }


}
