package org.mberhe.management.phone;

import lombok.RequiredArgsConstructor;
import org.mberhe.management.phone.dto.PhoneAvailability;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PhoneCustomRepositoryImpl implements PhoneCustomRepository {
  private final DatabaseClient databaseClient;

  @Override
  public Mono<PhoneAvailability> getPhoneAvailability(Integer phoneId) {
    String sql = """
      SELECT
      	p.id AS phoneId,
      	IF(
      	(
      	CASE
      	    WHEN u.id IS NULL AND pb.returned_date IS NULL THEN TRUE
      	    WHEN u.id  IS NOT NULL AND pb.returned_date IS NOT NULL THEN TRUE
      	    ELSE FALSE
      	 END
      	 ) = FALSE, CONCAT(u.first_name , ' ', u.last_name), NULL
      	) AS tester,
      	CASE
      	    WHEN u.id IS NULL AND pb.returned_date IS NULL THEN TRUE
      	    WHEN u.id  IS NOT NULL AND pb.returned_date IS NOT NULL THEN TRUE
      	    ELSE FALSE
      	 END AS available
      FROM phones AS p
      LEFT JOIN phone_borrowings AS pb ON pb.phone_id = p.id
      LEFT JOIN users AS u ON u.id = pb.tester_id
      WHERE p.id = :phoneId
      ORDER BY pb.borrowed_date DESC
      LIMIT 1;
      """;
    return getPhoneAvailability(sql, "phoneId", phoneId);
  }

  @Override
  public Mono<PhoneAvailability> getPhoneAvailability(String phoneAssignedId) {
    String sql = """
      SELECT
      	p.id AS phoneId,
      	IF(
      	(
      	CASE
      	    WHEN u.id IS NULL AND pb.returned_date IS NULL THEN TRUE
      	    WHEN u.id  IS NOT NULL AND pb.returned_date IS NOT NULL THEN TRUE
      	    ELSE FALSE
      	 END
      	 ) = FALSE, CONCAT(u.first_name , ' ', u.last_name), NULL
      	) AS tester,
      	CASE
      	    WHEN u.id IS NULL AND pb.returned_date IS NULL THEN TRUE
      	    WHEN u.id  IS NOT NULL AND pb.returned_date IS NOT NULL THEN TRUE
      	    ELSE FALSE
      	 END AS available
      FROM phones AS p
      LEFT JOIN phone_borrowings AS pb ON pb.phone_id = p.id
      LEFT JOIN users AS u ON u.id = pb.tester_id
      WHERE p.assigned_id = :phoneAssignedId
      ORDER BY pb.borrowed_date DESC
      LIMIT 1;
      """;

    return getPhoneAvailability(sql, "phoneAssignedId", phoneAssignedId);
  }

  public Mono<PhoneAvailability> getPhoneAvailability(String sql, String bindName, Object bindValue) {
    return databaseClient
      .sql(sql)
      .bind(bindName, bindValue)
      .map((row, rowMetadata) ->
        PhoneAvailability.builder()
          .phoneId(row.get("phoneId", Integer.class))
          .tester(row.get("tester", String.class))
          .available(row.get("available", Integer.class) == 0 ? false : true)
          .build()
      ).first();
  }

}
