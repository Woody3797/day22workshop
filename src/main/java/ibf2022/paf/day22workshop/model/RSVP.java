package ibf2022.paf.day22workshop.model;

import java.io.ByteArrayInputStream;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
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
    private DateTime confirmationDate;
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
    public DateTime getConfirmationDate() {
        return confirmationDate;
    }
    public void setConfirmDate(DateTime confirmDate) {
        this.confirmationDate = confirmDate;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    public RSVP() {
    }

    public RSVP(Integer id, String name, String email, String phone, DateTime confirmationDate, String comments) {
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
        rsvp.setConfirmDate(new DateTime(DateTimeFormat.forPattern("YYYY-MM-dd'T'hh:mm").parseDateTime(rs.getString("confirmation_date"))));
        rsvp.setComments(rs.getString("comments"));
        return rsvp;
    }

    public JsonValue toJson() {
        return Json.createObjectBuilder()
        .add("id", getId())
        .add("name", getName())
        .add("email", getEmail())
        .add("confirmation_date", getConfirmationDate().toString(DateTimeFormat.forPattern("dd-MM-YYYY")))
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
        rsvp.setConfirmDate(new DateTime(Instant.parse(jo.getString("confirmation_date"))));

        return rsvp;
    }

}
