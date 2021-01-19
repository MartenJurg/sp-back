package ee.developest.studentpraxis.model

import javax.persistence.*

@Entity
@Table(schema = "sp", name = "contact_type")
data class ContactType (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "contact_type_id")
        val id: Long = -1,

        @Column(name = "type", nullable = false)
        val name: String
){
    companion object {
        val EMAIL = ContactType(1, "EMAIL")
        val PHONE = ContactType(2, "PHONE")
        val LINKED_IN = ContactType(3, "LINKED_IN")
    }
}