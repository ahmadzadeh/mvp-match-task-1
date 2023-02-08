package co.mvpmatch.backendtask1.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "products")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class Product(
    @Id
    @Column(name = "id", updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "product_name", length = 50, unique = true, nullable = false)
    var productName: String,

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    //@JoinColumn(name="seller_id", nullable=false)
    var seller: User,

    @get: Min(value = 0)
    @Column(name = "amount_available", nullable = false)
    var amountAvailable: Int,

    @get: Min(value = 0L)
    @Column(nullable = false)
    var cost: Int
) : AbstractAuditingEntity()
