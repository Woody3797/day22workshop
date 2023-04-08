package ibf2022.paf.day22workshop.repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ibf2022.paf.day22workshop.model.RSVP;

@Repository
public class RSVPRepository {
    
    @Autowired
    JdbcTemplate template;

    public List<RSVP> getAllRSVP() {
        List<RSVP> rsvps = new ArrayList<>();
        SqlRowSet rs = null;
        rs = template.queryForRowSet(DBQueries.SELECT_ALL_RSVP);
        while(rs.next()) {
            rsvps.add(RSVP.create(rs));
        }
        return rsvps;
    }

    public List<RSVP> getRSVPByName(String name) {
        List<RSVP> rsvps = new ArrayList<>();
        SqlRowSet rs = null;
        rs = template.queryForRowSet(DBQueries.SELECT_RSVP_BY_NAME, name);
        while(rs.next()) {
            rsvps.add(RSVP.create(rs));
        }
        return rsvps;
    }

    public RSVP getRsvpByEmail(String email) {
        List<RSVP> rsvpList = new ArrayList<>();
        SqlRowSet rs = template.queryForRowSet(DBQueries.SELECT_RSVP_BY_EMAIL, email);
        while (rs.next()) {
            rsvpList.add(RSVP.create(rs));
        }
        if (rsvpList.size() == 0) {
            return null;
        }
        return rsvpList.get(0);
    }

    public RSVP createRsvp(RSVP rsvp) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        RSVP existingRSVP = getRsvpByEmail(rsvp.getEmail());

        if (Objects.isNull(existingRSVP)) {
            template.update(conn -> {
                PreparedStatement statement = conn.prepareStatement(DBQueries.INSERT_NEW_RSVP, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, rsvp.getName());
                statement.setString(2, rsvp.getEmail());
                statement.setString(3, rsvp.getPhone());
                statement.setTimestamp(4, Timestamp.valueOf(rsvp.getConfirmationDate()));
                statement.setString(5, rsvp.getComments());
                return statement;
            }, keyHolder);

            BigInteger primaryKey = (BigInteger) keyHolder.getKey();
            rsvp.setId(primaryKey.intValue());
        } else {
            existingRSVP.setName(rsvp.getName());
            existingRSVP.setPhone(rsvp.getPhone());
            existingRSVP.setConfirmationDate(rsvp.getConfirmationDate());
            existingRSVP.setComments(rsvp.getComments());

            boolean isUpdated = updateRSVP(existingRSVP);
            if (isUpdated) {
                rsvp.setId(existingRSVP.getId());
            }
        }

        return rsvp;
    }

    public boolean updateRSVP(RSVP existingRSVP) {
        return template.update(DBQueries.UPDATE_RSVP_BY_EMAIL, 
        existingRSVP.getName(),
        existingRSVP.getPhone(), 
        existingRSVP.getConfirmationDate(), 
        existingRSVP.getComments()) > 0;
    }

    public Long getTotalRSVPCount() {
        List<Map<String, Object>> rows = template.queryForList(DBQueries.TOTAL_RSVP_COUNT);
        return (Long) rows.get(0).get("total_count");
    }
}
