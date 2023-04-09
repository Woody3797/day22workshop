package ibf2022.paf.day22workshop.model;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

public class RSVP {
    
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime confirmationDate;
    private String comments;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public LocalDateTime getConfirmationDate() {
        return confirmationDate;
    }
    public void setConfirmationDate(LocalDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    public RSVP() {
    }

    public RSVP(Integer id, String name, String email, String phone, LocalDateTime confirmationDate, String comments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.confirmationDate = confirmationDate;
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "RSVP [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", confirmationDate="
                + confirmationDate + ", comments=" + comments + "]";
    }

    public static RSVP create(SqlRowSet rs) {
        RSVP rsvp = new RSVP();
        rsvp.setId(rs.getInt("id"));
        rsvp.setName(rs.getString("name"));
        rsvp.setEmail(rs.getString("email"));
        rsvp.setPhone(rs.getString("phone"));
        // DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:[..]");
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .toFormatter();
      
        rsvp.setConfirmationDate(LocalDateTime.parse(rs.getString("confirmation_date"), formatter));
        rsvp.setComments(rs.getString("comments"));
        return rsvp;
    }

    public JsonValue toJson() {
        return Json.createObjectBuilder()
        .add("id", getId())
        .add("name", getName())
        .add("email", getEmail())
        .add("confirmation_date", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(getConfirmationDate()))
        .add("comments", getComments())
        .build();
    }

    public static RSVP create(String json) {
        RSVP rsvp = new RSVP();
        JsonReader jr = Json.createReader(new ByteArrayInputStream(json.getBytes()));
        JsonObject jo = jr.readObject();
        rsvp.setName(jo.getString("name"));
        rsvp.setEmail(jo.getString("email"));
        rsvp.setPhone(jo.getString("phone"));
        rsvp.setComments(jo.getString("comments"));
        rsvp.setConfirmationDate(LocalDateTime.parse(jo.getString("confirmation_date")));

        return rsvp;
    }

}
