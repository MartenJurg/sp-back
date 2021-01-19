package ee.developest.studentpraxis.controller

import ee.developest.studentpraxis.model.Deal
import ee.developest.studentpraxis.model.DealStatus
import ee.developest.studentpraxis.model.UserRole
import ee.developest.studentpraxis.service.DealService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("deal")
class DealController(private val dealService: DealService) {

    @Secured(UserRole.Companion.SpringRole.ADMIN)
    @PutMapping
    fun update(@RequestParam dealId: Long, @RequestParam statusId: Long): Deal {
        return dealService.updateDeal(dealId, statusId)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN)
    @PostMapping
    fun add(@RequestParam cost: Int, @RequestParam duration: Int): Deal {
        return dealService.saveDeal(cost, duration)
    }

    @GetMapping("/active")
    fun getActiveDeals(): List<Deal> {
        return dealService.getAllActiveDeals()
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN)
    @GetMapping("/all")
    fun getAllDeals(): List<Deal> {
        return dealService.getAllDeals()
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN)
    @GetMapping("/status")
    fun getAllStatuses(): List<DealStatus> {
        return dealService.getAllStatuses()
    }
}