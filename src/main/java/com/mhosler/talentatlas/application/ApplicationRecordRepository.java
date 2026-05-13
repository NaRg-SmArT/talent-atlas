package com.mhosler.TalentAtlas.application;

import com.mhosler.TalentAtlas.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRecordRepository extends JpaRepository<ApplicationRecord, Long> {
    List<ApplicationRecord> findByUser(User user);
    Optional<ApplicationRecord> findByIdAndUser(Long id, User user);
}
