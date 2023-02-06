package co.mvpmatch.backendtask1.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
data class AbstractAuditingEntity(

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @JsonIgnore
    var createdAt: ZonedDateTime? = ZonedDateTime.now(),

    @LastModifiedDate
    @Column(name = "last_modified_at")
    @JsonIgnore
    var lastModifiedAt: ZonedDateTime? = ZonedDateTime.now()

) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
}

