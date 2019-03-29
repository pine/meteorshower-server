package moe.pine.meteorshower.services.setting

import moe.pine.meteorshower.scoped.Authenticated
import moe.pine.meteorshower.setting.models.UserExcludedRepository
import moe.pine.meteorshower.setting.models.UserOption
import moe.pine.meteorshower.setting.repositories.UserExcludedRepositoryRepository
import moe.pine.meteorshower.setting.repositories.UserOptionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@Service
class SettingService(
    val authenticated: Authenticated,
    val userOptionRepository: UserOptionRepository,
    val userExcludedRepositoryRepository: UserExcludedRepositoryRepository,
    val transactionTemplate: TransactionTemplate
) {
    fun load(): Setting {
        val user = authenticated.requestUser()
        val userId = user.requestId()

        val userOption = userOptionRepository.findByUserId(userId)
            ?: UserOption(userId = userId)
        val userExcludedRepositories =
            userExcludedRepositoryRepository.filterByUserId(userId)

        return Setting(
            excludeRepositories = userExcludedRepositories
                .map { repository ->
                    SettingExcludeRepository(
                        owner = repository.owner,
                        name = repository.name
                    )
                },
            excludeForked = userOption.excludeForked,
            excludeGist = userOption.excludeGist
        )
    }

    fun save(setting: Setting) {
        val user = authenticated.requestUser()
        val userId = user.requestId()

        val userOption = UserOption(
            userId = userId,
            excludeForked = setting.excludeForked,
            excludeGist = setting.excludeGist
        )
        val userExcludedRepositories =
            setting.excludeRepositories
                .map { repository ->
                    UserExcludedRepository(
                        userId = userId,
                        owner = repository.owner,
                        name = repository.name
                    )
                }
                .toTypedArray()

        val oldUserExcludedRepositories =
            userExcludedRepositoryRepository
                .filterByUserId(userId)
                .toTypedArray()

        transactionTemplate.execute {
            val optionInsertedCount = userOptionRepository.insertOrUpdate(userOption)
            assert(optionInsertedCount > 0L)

            userExcludedRepositoryRepository.remove(*oldUserExcludedRepositories)
            val excludeInsertedCount = userExcludedRepositoryRepository.add(*userExcludedRepositories)
            assert(excludeInsertedCount == userExcludedRepositories.size.toLong())
        }
    }
}