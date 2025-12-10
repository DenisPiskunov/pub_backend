package ru.mint.service

import ru.mint.dao.*
import ru.mint.service.converter.AccountConverter
import ru.mint.service.converter.AuthorityConverter
import ru.mint.service.dto.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.mint.dao.Databases
import ru.mint.dao.MasterAccountTable
import java.util.*

class AccountServiceImpl(
    private val databases: Databases,
    private val accountConverter: AccountConverter,
    private val authorityConverter: AuthorityConverter
) : AccountService {

    override fun findById(uuid: UUID): AccountDTO {
        val masterAccountLogin = transaction(databases.authServer) {
            MasterAccountEntity.findById(uuid)!!.login
        }
        return transaction {
            accountConverter.toDTO(AccountEntity.findById(uuid)!!, masterAccountLogin)
        }
    }

    override fun page(limit: Int, offset: Int): PageDTO<AccountDTO> {
        val count = transaction {
            AccountEntity.all().count()
        }
        val accountEntities = transaction {
            AccountEntity.all()
                .limit(limit, offset.toLong())
                .toList()
        }
        val accountIds = accountEntities.map { it.id.value }
        val idToMasterAccounts: Map<UUID, MasterAccountEntity> = transaction(databases.authServer) {
            MasterAccountEntity.find {
                MasterAccountTable.id inList accountIds
            }
                .associateBy { it.id.value }
        }
        val items = accountEntities.map {
            transaction {
                accountConverter.toDTO(it, idToMasterAccounts.getValue(it.id.value).login)
            }
        }
        return PageDTO(items, count)
    }

    override fun findAuthorities(uuid: UUID): List<AuthorityDTO> {
        return transaction {
            AccountEntity.findById(uuid)!!.roles
                .flatMap { it.authorities }
                .map { authorityConverter.toDTO(it)}
        }
    }

    override fun create(dto: AccountCreateDTO) {
        transaction {
            val account = transaction {
                AccountEntity.new(dto.uuid) {}
            }
            transaction {
                account.roles = RoleEntity.forIds(dto.rolesIds)
            }
        }
    }

    override fun update(dto: AccountUpdateDTO) {
        val account = transaction {
            AccountEntity.findById(dto.uuid)!!
        }
        transaction {
            account.roles = RoleEntity.forIds(dto.rolesIds)
        }
    }

}