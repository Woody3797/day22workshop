package ibf2022.paf.day22workshop.repository;

public class DBQueries {
    
    public static final String SELECT_ALL_RSVP = "SELECT id, name, email, phone, confirmation_date, comments FROM rsvp";
    public static final String SELECT_RSVP_BY_NAME = "SELECT id, name, email, phone, confirmation_date, comments FROM rsvp WHERE name LIKE CONCAT('%', ?, '%')";
    public static final String SELECT_RSVP_BY_EMAIL = "SELECT * FROM rsvp WHERE email = ?";
    public static final String INSERT_NEW_RSVP = "INSERT INTO rsvp (name, email, phone, confirmation_date, comments) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_RSVP_BY_EMAIL = "UPDATE rsvp SET name = ?, phone = ?, confirmation_date = ?, comments = ? WHERE email = ?";
    public static final String TOTAL_RSVP_COUNT = "SELECT count(*) AS total_count FROM rsvp";
    
}
