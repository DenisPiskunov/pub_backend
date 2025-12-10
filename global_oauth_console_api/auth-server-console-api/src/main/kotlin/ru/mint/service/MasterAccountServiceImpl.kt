package ru.mint.service

import org.jetbrains.exposed.sql.and
import ru.mint.dao.Databases
import ru.mint.dao.MasterAccountEntity
import ru.mint.dao.MasterAccountTable
import ru.mint.service.PasswordGenerator.CharactersConfig
import ru.mint.service.converter.MasterAccountConverter
import ru.mint.service.dto.MasterAccountCreateDTO
import ru.mint.service.dto.MasterAccountDTO
import ru.mint.service.dto.MasterAccountUpdateDTO
import ru.mint.service.dto.PageDTO
import ru.mint.service.mail.MailService
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class MasterAccountServiceImpl(
    private val passwordGenerator: PasswordGenerator,
    private val passwordEncoder: PasswordEncoder,
    private val databases: Databases,
    private val masterAccountConverter: MasterAccountConverter,
    private val mailService: MailService
) : MasterAccountService {

    override fun findValid(): List<MasterAccountDTO> {
        return transaction(databases.authServer) {
            MasterAccountEntity.find { MasterAccountTable.isBlocked eq false and (MasterAccountTable.isDeleted eq false) }
                .map { masterAccountConverter.toDTO(it) }
        }
    }

    override fun findById(uuid: UUID): MasterAccountDTO {
        return transaction(databases.authServer) {
            masterAccountConverter.toDTO(MasterAccountEntity.findById(uuid)!!)
        }
    }

    override fun page(limit: Int, offset: Int): PageDTO<MasterAccountDTO> {
        val count = transaction(databases.authServer) {
            MasterAccountEntity.all().count()
        }
        val items = transaction(databases.authServer) {
            MasterAccountEntity.all().limit(limit, offset.toLong()).map {
                masterAccountConverter.toDTO(it)
            }
        }
        return PageDTO(items, count)
    }

    override fun create(dto: MasterAccountCreateDTO) {
        val rawPassword = passwordGenerator.generatePassword(CharactersConfig(2, 2, 3, 3))
        val passwordHash = passwordEncoder.encode(rawPassword)
        transaction(databases.authServer) {
            MasterAccountEntity.new(UUID.randomUUID()) {
                login = dto.login
                email = dto.email
                this.password = passwordHash
            }
        }
        mailService.sendNewMasterAccountEmail(dto.email, rawPassword)
    }

    override fun update(dto: MasterAccountUpdateDTO) {
//        TODO: check if account is blocked deleted etc
        val account = transaction(databases.authServer) { MasterAccountEntity.findById(dto.uuid)!! }
        transaction(databases.authServer) {
            account.login = dto.login
            account.email = dto.email
        }
    }

    override fun markAsDeleted(uuid: UUID) {
        val account = transaction(databases.authServer) { MasterAccountEntity.findById(uuid) }
        if (account != null && !account.isDeleted) {
            transaction(databases.authServer) {
                account.isDeleted = true
            }
        }
    }

    override fun block(uuid: UUID) {
        val account = transaction(databases.authServer) { MasterAccountEntity.findById(uuid) }
        if (account != null && !account.isDeleted && !account.isBlocked) {
            transaction(databases.authServer) {
                account.isBlocked = true
            }
        }
    }

}