package co.mvpmatch.backendtask1.mapper

import co.mvpmatch.backendtask1.vm.BuyProcessPayload
import co.mvpmatch.backendtask1.web.api.model.BuyResponseDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [])
abstract class BuyResponseMapper {
    abstract fun toResponseDTO(buyProcessPayload: BuyProcessPayload): BuyResponseDTO
}
