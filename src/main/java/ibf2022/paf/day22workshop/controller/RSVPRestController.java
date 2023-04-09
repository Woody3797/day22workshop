package ibf2022.paf.day22workshop.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    @GetMapping(path = "/rsvp", produces = MediaType.APPLICATION_JSON_VALUE)
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

    // insert/update form data into mysql table
    @PostMapping(path = "/rsvp", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insertUpdateRSVPForm(@ModelAttribute RSVP rsvp) {        
        JsonObject jo = null;
        boolean isExist = repository.getRsvpByEmail(rsvp.getEmail()) != null ? true : false;
        try {
            RSVP result = repository.createRsvp(rsvp);
            String resp = isExist ? "RSVP entry " + rsvp.getId() + " is updated" : "RSVP entry " + rsvp.getId() + " is added into entries";
            jo = Json.createObjectBuilder()
            .add("rsvpId", result.getId())
            .add("response", resp)
            .build();
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(jo.toString());
        } catch (Exception e) {
            e.printStackTrace();
            jo = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return ResponseEntity.badRequest().body(jo.toString());
        }
    }

    // insert/update json data into mysql table
    @PostMapping(path = "/rsvp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insertUpdateRSVPJson(@RequestBody String json) {
        JsonObject jo = null;
        try {
        RSVP rsvp = RSVP.create(json); // converting json to java object
        RSVP result = repository.createRsvp(rsvp);
        jo = Json.createObjectBuilder()
        .add("rsvpID", result.getId())
        .build();
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(jo.toString());
        } catch (Exception e) {
         e.printStackTrace();
        jo = Json.createObjectBuilder().add("error", e.getMessage()).build();
        return ResponseEntity.badRequest().body(jo.toString());
        }
        
    }

    @PutMapping(path = "/rsvp/{email}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> putRSVP(@RequestBody String json, Model model, @ModelAttribute RSVP rsvp, @PathVariable String email){
        JsonObject resp;
        try {
            RSVP existingRsvp = repository.getRsvpByEmail(rsvp.getEmail());
            boolean rsvpResult = existingRsvp == null ? false : true;
            rsvp = RSVP.create(json);

            if (!rsvpResult) {
                resp = Json.createObjectBuilder()
                .add("error message", "RSVP entry with " + email + " not found")
                .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(resp.toString());
            }

            rsvpResult = repository.updateRSVP(rsvp);
            resp = Json.createObjectBuilder()
            .add("updated", rsvpResult)
            .build();
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(resp.toString());
        } catch (Exception e) {
            e.printStackTrace();
            resp = Json.createObjectBuilder()
            .add("error: ", e.getMessage())
            .build();
            return ResponseEntity.badRequest().body(resp.toString());
        }
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
