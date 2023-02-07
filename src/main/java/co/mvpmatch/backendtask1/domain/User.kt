package co.mvpmatch.backendtask1.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "users")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class User(

    @Id
    @Column(name = "id", updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(length = 320, unique = true, nullable = false)
    var username: String,

    @Column(nullable = false)
    var activated: Boolean = false,

    @Column(name = "full_name", length = 100)
    var fullName: String? = null,

    @Column(name = "lang_key", length = 10)
    var langKey: String? = null,

    @get: Min(value = 0)
    @Column(nullable = false)
    var credit: Int,

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_authorities",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "authority_name", referencedColumnName = "name")]
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    var authorities: MutableSet<Authority> = mutableSetOf(),

    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,

    @JsonIgnore
    @Column(name = "jwt_salt", nullable = false)
    var jwtSalt: String,

    ) : AbstractAuditingEntity(), Serializable
