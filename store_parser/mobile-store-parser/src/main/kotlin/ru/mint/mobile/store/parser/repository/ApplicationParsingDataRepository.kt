package ru.mint.mobile.store.parser.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import ru.mint.mobile.store.parser.repository.entity.ApplicationParsingData


@Transactional(readOnly = true)
interface ApplicationParsingDataRepository : JpaRepository<ApplicationParsingData, Long>