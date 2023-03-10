package co.mvpmatch.backendtask1.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "buy_histories")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class BuyHistory(
    @Id
    @Column(name = "id", updatable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "product_id", nullable = false)
    var productId: Long,

    @Column(name = "buyer_id", nullable = false)
    var buyerId: Long,

    @get: Min(value = 0L)
    @Column(nullable = false)
    var totalCost: Int

) : AbstractAuditingEntity(), Serializable
