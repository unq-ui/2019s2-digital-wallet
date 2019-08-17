package wallet

import java.time.LocalDate

/**
 * Nombre
 * Tipo de Beneficio (descuento o regalo)
 * Porcentaje de descuento (en caso de ser descuento)
 * Importe de Regalo (en caso de ser regalo)
 * Cantidad de operaciones (*)
 * Importe de Cada Operación(**)
 * Vigencia Fecha Desde
 * Vigencia Fecha Hasta
 */
class LoyaltyGift(
    val name: String,
    val strategy: LoyaltyGiftStrategy,
    val minNumberOfTransactions: Int,
    val minAmountPerTransaction: Int,
    val validFrom: LocalDate,
    val validTo: LocalDate
)

interface LoyaltyGiftStrategy

class DiscountGiftStrategy : LoyaltyGiftStrategy
class FixedGiftStrategy : LoyaltyGiftStrategy