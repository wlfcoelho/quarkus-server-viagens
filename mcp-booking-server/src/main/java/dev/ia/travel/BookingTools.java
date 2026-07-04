package dev.ia.travel;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BookingTools {
    @Inject
    BookingService bookingService;
    //Cuidado com os nomes das Tools, pois, elas podem causar alucinação ma IA
    @Tool(name = "get_booking_details")
    public String getBookingDetails(
            @ToolArg(description = "O ID numérico único da reserva (ex: 12345)") long bookingId) {
        return bookingService.getBookingDetails(bookingId)
                .map(Booking::toString)
                .orElse("Reserva com ID " + bookingId + " não encontrada.");
    }

    @Tool(
            name = "cancel_booking")
    public String cancelBooking(
            @ToolArg(description = "ID da reserva a cancelar") long bookingId,
            @ToolArg(description = "Usuário que está tentando cancelar a reserva") String name) {
        return bookingService.cancelBooking(bookingId, name)
                .map(b -> "Reserva " + b.id() + " cancelada com sucesso.")
                .orElse("Não foi possível cancelar a reserva. Verifique se o ID está correto e se você tem permissão.");
    }

    @Tool(name = "list_packages_by_category")
    public String listPackagesByCategory(
            @ToolArg(description = "Categoria utilizada como filtro para pacotes") Category category) {
        List<Booking> packages = bookingService.findPackagesByCategory(category);
        if (packages.isEmpty()) {
            return "Nenhum pacote encontrado para a categoria: " + category;
        }
        return "Pacotes encontrados para a categoria '" + category + "': " + packages.stream()
                .map(Booking::destination)
                .toList().toString();
    }

}