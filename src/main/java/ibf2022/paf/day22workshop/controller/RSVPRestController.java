package ibf2022.paf.day22workshop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.paf.day22workshop.model.RSVP;
import ibf2022.paf.day22workshop.repository.RSVPRepository;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RSVPRestController {
    
    RSVPRepository repository;

    RSVPRestController(RSVPRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/rsvps")
    public ResponseEntity<String> getAllRsvps() {
        List<RSVP> rsvps = repository.getAllRSVP();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (RSVP r : rsvps) {
            arrayBuilder.add(r.toJson());
        }
        JsonArray result = arrayBuilder.build();

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }

    @GetMapping("/rsvp")
    public ResponseEntity<String> getRSVPByName(@RequestParam String name) {
        List<RSVP> rsvps = repository.getRSVPByName(name);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (RSVP r : rsvps) {
            arrayBuilder.add(r.toJson());
        }
        JsonArray result = arrayBuilder.build();

        if (rsvps.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body("{'error_code' : " + HttpStatus.NOT_FOUND + "}");
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }

    @PostMapping(path = "/rsvp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insertUpdateRSVP(@RequestBody String json) {
        RSVP rsvp = null;
        JsonObject jo = null;
        try {
            rsvp = RSVP.create(json);
        } catch (Exception e) {
            e.printStackTrace();
            jo = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return ResponseEntity.badRequest().body(jo.toString());
        }
        
        RSVP result = repository.createRsvp(rsvp);
        jo = Json.createObjectBuilder()
        .add("rsvpId", result.getId())
        .build();

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(jo.toString());
    }


    @PutMapping(path = "/rsvp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putRSVP(@RequestBody String json, Model model, @ModelAttribute RSVP rsvp){
        rsvp = null;
        boolean rsvpResult = false;
        JsonObject resp;
        try {
            rsvp = RSVP.create(json);
        } catch (Exception e) {
            e.printStackTrace();
            resp = Json.createObjectBuilder()
            .add("error: ", e.getMessage())
            .build();
            return ResponseEntity.badRequest().body(resp.toString());
        }
        rsvpResult = repository.updateRSVP(rsvp);
        resp = Json.createObjectBuilder()
        .add("updated", rsvpResult)
        .build();

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(resp.toString());
    }

    @GetMapping(path = "/rsvps/count")
    public ResponseEntity<String> getTotalRSVPCount() {
        Long rsvp_count = repository.getTotalRSVPCount();
        JsonObject jo;
        jo = Json.createObjectBuilder()
        .add("total_count", rsvp_count)
        .build();

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(jo.toString());
    }

}
