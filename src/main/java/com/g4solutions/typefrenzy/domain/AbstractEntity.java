package com.g4solutions.typefrenzy.domain;

import com.g4solutions.typefrenzy.util.IdUtil;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

  @Id
  @Column(name = "id", nullable = false)
  private final String id;

  /**
   * Creation date and time.
   */
  @CreatedDate
  @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime createdAt;

  /**
   * Updated date and time.
   */
  @LastModifiedDate
  @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime updatedAt;

  protected AbstractEntity() {
    this.id = IdUtil.generateId(this.getClass());
  }
}
