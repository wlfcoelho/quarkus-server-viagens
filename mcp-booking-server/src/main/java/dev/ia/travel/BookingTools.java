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

    @Tool(name = "Obtém os detalhes completos de uma reserva com base em seu número de identificação (bookingId).")
    public String getBookingDetails(
            @ToolArg(description = "O ID numérico único da reserva (ex: 12345)") long bookingId) {
        return bookingService.getBookingDetails(bookingId)
                .map(Booking::toString)
                .orElse("Reserva com ID " + bookingId + " não encontrada.");
    }

    @Tool(name = """
            Cancela uma reserva existente com base no seu ID (bookingId).
            O usuário deve estar autenticado.
            """)
    public String cancelBooking(
            @ToolArg(description = "ID da reserva a cancelar") long bookingId,
            @ToolArg(description = "Usuário que está tentando cancelar a reserva") String name) {
        return bookingService.cancelBooking(bookingId, name)
                .map(b -> "Reserva " + b.id() + " cancelada com sucesso.")
                .orElse("Não foi possível cancelar a reserva. Verifique se o ID está correto e se você tem permissão.");
    }

    @Tool(name = "Lista os pacotes de viagem disponíveis para uma determinada categoria (ex: ADVENTURE, TREASURES).")
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