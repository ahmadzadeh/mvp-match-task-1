package co.mvpmatch.backendtask1.domain

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "authorities")
data class Authority(
    @Id
    @Column(length = 50)
    var name: String

) : Serializable {}
