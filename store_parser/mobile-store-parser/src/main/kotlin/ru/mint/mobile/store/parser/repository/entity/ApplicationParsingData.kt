package ru.mint.mobile.store.parser.repository.entity

import java.time.LocalDateTime
import javax.persistence.*


@Table(name = "application_parsing_data")
@Entity
class ApplicationParsingData(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
        @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "app_id", updatable = false) val application: Application,
        @Column(name = "parsing_result") val parsingResult: String,
        @Column(name = "creation_date") val creationDate: LocalDateTime) {

    constructor(application: Application, parsingResult: String) : this(null, application, parsingResult, LocalDateTime.now())

}