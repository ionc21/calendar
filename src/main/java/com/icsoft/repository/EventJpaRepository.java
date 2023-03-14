package com.icsoft.repository;

import com.icsoft.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long> {

    /* Note these two methods do the same thing.  The @Repository annotation handles both*/


    /* This one uses a JPQL */
    List<Event> findByStartGreaterThanEqualAndFinishLessThanEqual(LocalDateTime start, LocalDateTime end);


    /* This one uses an @Query */
    @Query("select b from Event b where b.start >= ?1 and b.finish <= ?2")
    List<Event> findByDateBetween(LocalDateTime start, LocalDateTime end);

}
